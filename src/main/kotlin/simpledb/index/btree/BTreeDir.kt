package simpledb.index.btree

import simpledb.file.Block
import simpledb.tx.Transaction
import simpledb.record.TableInfo
import simpledb.query.Constant

/**
 * A B-tree directory block.
 * @author Edward Sciore
 */
class BTreeDir
/**
 * Creates an object to hold the contents of the specified
 * B-tree block.
 * @param blk a reference to the specified B-tree block
 * @param ti the metadata of the B-tree directory file
 * @param tx the calling transaction
 */
internal constructor(blk: Block, private val ti: TableInfo, private val tx: Transaction) {
    private val filename: String
    private var contents: BTreePage? = null

    init {
        filename = blk.fileName()
        contents = BTreePage(blk, ti, tx)
    }

    /**
     * Closes the directory page.
     */
    fun close() {
        contents!!.close()
    }

    /**
     * Returns the block number of the B-tree leaf block
     * that contains the specified search key.
     * @param searchkey the search key value
     * @return the block number of the leaf block containing that search key
     */
    fun search(searchkey: Constant): Int {
        var childblk = findChildBlock(searchkey)
        while (contents!!.flag > 0) {
            contents!!.close()
            contents = BTreePage(childblk, ti, tx)
            childblk = findChildBlock(searchkey)
        }
        return childblk.number()
    }

    /**
     * Creates a new root block for the B-tree.
     * The new root will have two children:
     * the old root, and the specified block.
     * Since the root must always be in block 0 of the file,
     * the contents of the old root will get transferred to a new block.
     * @param e the directory entry to be added as a child of the new root
     */
    fun makeNewRoot(e: DirEntry) {
        val firstval = contents!!.getDataVal(0)
        val level = contents!!.flag
        val newblk = contents!!.split(0, level) //ie, transfer all the records
        val oldroot = DirEntry(firstval, newblk!!.number())
        insertEntry(oldroot)
        insertEntry(e)
        contents!!.flag = level + 1
    }

    /**
     * Inserts a new directory entry into the B-tree block.
     * If the block is at level 0, then the entry is inserted there.
     * Otherwise, the entry is inserted into the appropriate
     * child node, and the return value is examined.
     * A non-null return value indicates that the child node
     * split, and so the returned entry is inserted into
     * this block.
     * If this block splits, then the method similarly returns
     * the entry information of the new block to its caller;
     * otherwise, the method returns null.
     * @param e the directory entry to be inserted
     * @return the directory entry of the newly-split block, if one exists; otherwise, null
     */
    fun insert(e: DirEntry): DirEntry? {
        if (contents!!.flag == 0)
            return insertEntry(e)
        val childblk = findChildBlock(e.dataVal())
        val child = BTreeDir(childblk, ti, tx)
        val myentry = child.insert(e)
        child.close()
        return if (myentry != null) insertEntry(myentry) else null
    }

    private fun insertEntry(e: DirEntry): DirEntry? {
        val newslot = 1 + contents!!.findSlotBefore(e.dataVal())
        contents!!.insertDir(newslot, e.dataVal(), e.blockNumber())
        if (!contents!!.isFull)
            return null
        // else page is full, so split it
        val level = contents!!.flag
        val splitpos = contents!!.numRecs / 2
        val splitval = contents!!.getDataVal(splitpos)
        val newblk = contents!!.split(splitpos, level)
        return DirEntry(splitval, newblk!!.number())
    }

    private fun findChildBlock(searchkey: Constant): Block {
        var slot = contents!!.findSlotBefore(searchkey)
        if (contents!!.getDataVal(slot + 1) == searchkey)
            slot++
        val blknum = contents!!.getChildNum(slot)
        return Block(filename, blknum)
    }
}
