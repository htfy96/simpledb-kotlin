package simpledb.index.btree

import java.sql.Types.INTEGER
import simpledb.file.Block
import simpledb.tx.Transaction
import simpledb.record.*
import simpledb.query.*
import simpledb.index.Index

/**
 * A B-tree implementation of the Index interface.
 * @author Edward Sciore
 */
class BTreeIndex
/**
 * Opens a B-tree index for the specified index.
 * The method determines the appropriate files
 * for the leaf and directory records,
 * creating them if they did not exist.
 * @param idxname the name of the index
 * @param leafsch the schema of the leaf index records
 * @param tx the calling transaction
 */
(idxname: String, leafsch: Schema, private val tx: Transaction) : Index {
    private val dirTi: TableInfo
    private val leafTi: TableInfo
    private var leaf: BTreeLeaf? = null
    private val rootblk: Block

    /**
     * Returns the dataRID value from the current leaf record.
     * @see simpledb.index.Index.getDataRid
     */
    override val dataRid: RID
        get() = leaf!!.dataRid

    init {
        // deal with the leaves
        val leaftbl = idxname + "leaf"
        leafTi = TableInfo(leaftbl, leafsch)
        if (tx.size(leafTi.fileName()) == 0)
            tx.append(leafTi.fileName(), BTPageFormatter(leafTi, -1))

        // deal with the directory
        val dirsch = Schema()
        dirsch.add("block", leafsch)
        dirsch.add("dataval", leafsch)
        val dirtbl = idxname + "dir"
        dirTi = TableInfo(dirtbl, dirsch)
        rootblk = Block(dirTi.fileName(), 0)
        if (tx.size(dirTi.fileName()) == 0)
        // create new root block
            tx.append(dirTi.fileName(), BTPageFormatter(dirTi, 0))
        val page = BTreePage(rootblk, dirTi, tx)
        if (page.numRecs == 0) {
            // insert initial directory entry
            val fldtype = dirsch.type("dataval")
            val minval = if (fldtype == INTEGER)
                IntConstant(Integer.MIN_VALUE)
            else
                StringConstant("")
            page.insertDir(0, minval, 0)
        }
        page.close()
    }

    /**
     * Traverses the directory to find the leaf block corresponding
     * to the specified search key.
     * The method then opens a page for that leaf block, and
     * positions the page before the first record (if any)
     * having that search key.
     * The leaf page is kept open, for use by the methods next
     * and getDataRid.
     * @see simpledb.index.Index.beforeFirst
     */
    override fun beforeFirst(searchkey: Constant) {
        close()
        val root = BTreeDir(rootblk, dirTi, tx)
        val blknum = root.search(searchkey)
        root.close()
        val leafblk = Block(leafTi.fileName(), blknum)
        leaf = BTreeLeaf(leafblk, leafTi, searchkey, tx)
    }

    /**
     * Moves to the next leaf record having the
     * previously-specified search key.
     * Returns false if there are no more such leaf records.
     * @see simpledb.index.Index.next
     */
    override fun next(): Boolean {
        return leaf!!.next()
    }

    /**
     * Inserts the specified record into the index.
     * The method first traverses the directory to find
     * the appropriate leaf page; then it inserts
     * the record into the leaf.
     * If the insertion causes the leaf to split, then
     * the method calls insert on the root,
     * passing it the directory entry of the new leaf page.
     * If the root node splits, then makeNewRoot is called.
     * @see simpledb.index.Index.insert
     */
    override fun insert(dataval: Constant, datarid: RID) {
        beforeFirst(dataval)
        val e = leaf!!.insert(datarid)
        leaf!!.close()
        if (e == null)
            return
        val root = BTreeDir(rootblk, dirTi, tx)
        val e2 = root.insert(e)
        if (e2 != null)
            root.makeNewRoot(e2)
        root.close()
    }

    /**
     * Deletes the specified index record.
     * The method first traverses the directory to find
     * the leaf page containing that record; then it
     * deletes the record from the page.
     * @see simpledb.index.Index.delete
     */
    override fun delete(dataval: Constant, datarid: RID) {
        beforeFirst(dataval)
        leaf!!.delete(datarid)
        leaf!!.close()
    }

    /**
     * Closes the index by closing its open leaf page,
     * if necessary.
     * @see simpledb.index.Index.close
     */
    override fun close() {
        if (leaf != null)
            leaf!!.close()
    }

    companion object {

        /**
         * Estimates the number of block accesses
         * required to find all index records having
         * a particular search key.
         * @param numblocks the number of blocks in the B-tree directory
         * @param rpb the number of index entries per block
         * @return the estimated traversal cost
         */
        fun searchCost(numblocks: Int, rpb: Int): Int {
            return 1 + (Math.log(numblocks.toDouble()) / Math.log(rpb.toDouble())).toInt()
        }
    }
}
