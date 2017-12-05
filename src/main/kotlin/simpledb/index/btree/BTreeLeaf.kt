package simpledb.index.btree

import simpledb.file.Block
import simpledb.tx.Transaction
import simpledb.record.*
import simpledb.query.Constant

/**
 * An object that holds the contents of a B-tree leaf block.
 * @author Edward Sciore
 */
class BTreeLeaf
/**
 * Opens a page to hold the specified leaf block.
 * The page is positioned immediately before the first record
 * having the specified search key (if any).
 * @param blk a reference to the disk block
 * @param ti the metadata of the B-tree leaf file
 * @param searchkey the search key value
 * @param tx the calling transaction
 */
(blk: Block, private val ti: TableInfo, private val searchkey: Constant, private val tx: Transaction) {
    private var contents: BTreePage? = null
    private var currentslot: Int = 0

    /**
     * Returns the dataRID value of the current leaf record.
     * @return the dataRID of the current record
     */
    val dataRid: RID
        get() = contents!!.getDataRid(currentslot)

    init {
        contents = BTreePage(blk, ti, tx)
        currentslot = contents!!.findSlotBefore(searchkey)
    }

    /**
     * Closes the leaf page.
     */
    fun close() {
        contents!!.close()
    }

    /**
     * Moves to the next leaf record having the
     * previously-specified search key.
     * Returns false if there is no more such records.
     * @return false if there are no more leaf records for the search key
     */
    operator fun next(): Boolean {
        currentslot++
        return if (currentslot >= contents!!.numRecs)
            tryOverflow()
        else if (contents!!.getDataVal(currentslot) == searchkey)
            true
        else
            tryOverflow()
    }

    /**
     * Deletes the leaf record having the specified dataRID
     * @param datarid the dataRId whose record is to be deleted
     */
    fun delete(datarid: RID) {
        while (next())
            if (dataRid == datarid) {
                contents!!.delete(currentslot)
                return
            }
    }

    /**
     * Inserts a new leaf record having the specified dataRID
     * and the previously-specified search key.
     * If the record does not fit in the page, then
     * the page splits and the method returns the
     * directory entry for the new page;
     * otherwise, the method returns null.
     * If all of the records in the page have the same dataval,
     * then the block does not split; instead, all but one of the
     * records are placed into an overflow block.
     * @param datarid the dataRID value of the new record
     * @return the directory entry of the newly-split page, if one exists.
     */
    fun insert(datarid: RID): DirEntry? {
        // bug fix:  If the page has an overflow page
        // and the searchkey of the new record would be lowest in its page,
        // we need to first move the entire contents of that page to a new block
        // and then insert the new record in the now-empty current page.
        if (contents!!.flag >= 0 && contents!!.getDataVal(0).compareTo(searchkey) > 0) {
            val firstval = contents!!.getDataVal(0)
            val newblk = contents!!.split(0, contents!!.flag)
            currentslot = 0
            contents!!.flag = -1
            contents!!.insertLeaf(currentslot, searchkey, datarid)
            return DirEntry(firstval, newblk!!.number())
        }

        currentslot++
        contents!!.insertLeaf(currentslot, searchkey, datarid)
        if (!contents!!.isFull)
            return null
        // else page is full, so split it
        val firstkey = contents!!.getDataVal(0)
        val lastkey = contents!!.getDataVal(contents!!.numRecs - 1)
        if (lastkey == firstkey) {
            // create an overflow block to hold all but the first record
            val newblk = contents!!.split(1, contents!!.flag)
            contents!!.flag = newblk!!.number()
            return null
        } else {
            var splitpos = contents!!.numRecs / 2
            var splitkey = contents!!.getDataVal(splitpos)
            if (splitkey == firstkey) {
                // move right, looking for the next key
                while (contents!!.getDataVal(splitpos) == splitkey)
                    splitpos++
                splitkey = contents!!.getDataVal(splitpos)
            } else {
                // move left, looking for first entry having that key
                while (contents!!.getDataVal(splitpos - 1) == splitkey)
                    splitpos--
            }
            val newblk = contents!!.split(splitpos, -1)
            return DirEntry(splitkey, newblk!!.number())
        }
    }

    private fun tryOverflow(): Boolean {
        val firstkey = contents!!.getDataVal(0)
        val flag = contents!!.flag
        if (searchkey != firstkey || flag < 0)
            return false
        contents!!.close()
        val nextblk = Block(ti.fileName(), flag)
        contents = BTreePage(nextblk, ti, tx)
        currentslot = 0
        return true
    }
}
