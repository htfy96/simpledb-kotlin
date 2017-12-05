package simpledb.index.btree

import java.sql.Types.INTEGER
import simpledb.file.Page.*
import simpledb.file.Block
import simpledb.file.Page
import simpledb.record.*
import simpledb.query.*
import simpledb.tx.Transaction

/**
 * B-tree directory and leaf pages have many commonalities:
 * in particular, their records are stored in sorted order,
 * and pages split when full.
 * A BTreePage object contains this common functionality.
 * @author Edward Sciore
 */
class BTreePage
/**
 * Opens a page for the specified B-tree block.
 * @param currentblk a reference to the B-tree block
 * @param ti the metadata for the particular B-tree file
 * @param tx the calling transaction
 */
(curblk: Block?, private val ti: TableInfo, private val tx: Transaction) {
    private val slotsize: Int
    private var currentblk: Block?

    /**
     * Returns true if the block is full.
     * @return true if the block is full
     */
    val isFull: Boolean
        get() = slotpos(numRecs + 1) >= Page.BLOCK_SIZE

    /**
     * Returns the value of the page's flag field
     * @return the value of the page's flag field
     */
    /**
     * Sets the page's flag field to the specified value
     * @param val the new value of the page flag
     */
    var flag: Int
        get() = tx.getInt(currentblk!!, 0)
        set(`val`) = tx.setInt(currentblk!!, 0, `val`)

    /**
     * Returns the number of index records in this page.
     * @return the number of index records in this page
     */
    var numRecs: Int
        get() = tx.getInt(currentblk!!, Page.INT_SIZE)
        private set(n) = tx.setInt(currentblk!!, Page.INT_SIZE, n)

    init {
        slotsize = ti.recordLength()
        currentblk = curblk
        tx.pin(currentblk!!)
    }

    /**
     * Calculates the position where the first record having
     * the specified search key should be, then returns
     * the position before it.
     * @param searchkey the search key
     * @return the position before where the search key goes
     */
    fun findSlotBefore(searchkey: Constant): Int {
        var slot = 0
        while (slot < numRecs && getDataVal(slot).compareTo(searchkey) < 0)
            slot++
        return slot - 1
    }

    /**
     * Closes the page by unpinning its buffer.
     */
    fun close() {
        if (currentblk != null)
            tx.unpin(currentblk!!)
        currentblk = null
    }

    /**
     * Splits the page at the specified position.
     * A new page is created, and the records of the page
     * starting at the split position are transferred to the new page.
     * @param splitpos the split position
     * @param flag the initial value of the flag field
     * @return the reference to the new block
     */
    fun split(splitpos: Int, flag: Int): Block? {
        val newblk = appendNew(flag)
        val newpage = BTreePage(newblk, ti, tx)
        transferRecs(splitpos, newpage)
        newpage.flag = flag
        newpage.close()
        return newblk
    }

    /**
     * Returns the dataval of the record at the specified slot.
     * @param slot the integer slot of an index record
     * @return the dataval of the record at that slot
     */
    fun getDataVal(slot: Int): Constant {
        return getVal(slot, "dataval")
    }

    /**
     * Appends a new block to the end of the specified B-tree file,
     * having the specified flag value.
     * @param flag the initial value of the flag
     * @return a reference to the newly-created block
     */
    fun appendNew(flag: Int): Block? {
        return tx.append(ti.fileName(), BTPageFormatter(ti, flag))
    }

    // Methods called only by BTreeDir

    /**
     * Returns the block number stored in the index record
     * at the specified slot.
     * @param slot the slot of an index record
     * @return the block number stored in that record
     */
    fun getChildNum(slot: Int): Int {
        return getInt(slot, "block")
    }

    /**
     * Inserts a directory entry at the specified slot.
     * @param slot the slot of an index record
     * @param val the dataval to be stored
     * @param blknum the block number to be stored
     */
    fun insertDir(slot: Int, `val`: Constant, blknum: Int) {
        insert(slot)
        setVal(slot, "dataval", `val`)
        setInt(slot, "block", blknum)
    }

    // Methods called only by BTreeLeaf

    /**
     * Returns the dataRID value stored in the specified leaf index record.
     * @param slot the slot of the desired index record
     * @return the dataRID value store at that slot
     */
    fun getDataRid(slot: Int): RID {
        return RID(getInt(slot, "block"), getInt(slot, "id"))
    }

    /**
     * Inserts a leaf index record at the specified slot.
     * @param slot the slot of the desired index record
     * @param val the new dataval
     * @param rid the new dataRID
     */
    fun insertLeaf(slot: Int, `val`: Constant, rid: RID) {
        insert(slot)
        setVal(slot, "dataval", `val`)
        setInt(slot, "block", rid.blockNumber())
        setInt(slot, "id", rid.id())
    }

    /**
     * Deletes the index record at the specified slot.
     * @param slot the slot of the deleted index record
     */
    fun delete(slot: Int) {
        for (i in slot + 1 until numRecs)
            copyRecord(i, i - 1)
        numRecs = numRecs - 1
        return
    }

    // Private methods

    private fun getInt(slot: Int, fldname: String): Int {
        val pos = fldpos(slot, fldname)
        return tx.getInt(currentblk!!, pos)
    }

    private fun getString(slot: Int, fldname: String): String {
        val pos = fldpos(slot, fldname)
        return tx.getString(currentblk!!, pos)
    }

    private fun getVal(slot: Int, fldname: String): Constant {
        val type = ti.schema().type(fldname)
        return if (type == INTEGER)
            IntConstant(getInt(slot, fldname))
        else
            StringConstant(getString(slot, fldname))
    }

    private fun setInt(slot: Int, fldname: String, `val`: Int) {
        val pos = fldpos(slot, fldname)
        tx.setInt(currentblk!!, pos, `val`)
    }

    private fun setString(slot: Int, fldname: String, `val`: String) {
        val pos = fldpos(slot, fldname)
        tx.setString(currentblk!!, pos, `val`)
    }

    private fun setVal(slot: Int, fldname: String, `val`: Constant) {
        val type = ti.schema().type(fldname)
        if (type == INTEGER)
            setInt(slot, fldname, `val`.asJavaVal() as Int)
        else
            setString(slot, fldname, `val`.asJavaVal() as String)
    }

    private fun insert(slot: Int) {
        for (i in numRecs downTo slot + 1)
            copyRecord(i - 1, i)
        numRecs = numRecs + 1
    }

    private fun copyRecord(from: Int, to: Int) {
        val sch = ti.schema()
        for (fldname in sch.fields())
            setVal(to, fldname, getVal(from, fldname))
    }

    private fun transferRecs(slot: Int, dest: BTreePage) {
        var destslot = 0
        while (slot < numRecs) {
            dest.insert(destslot)
            val sch = ti.schema()
            for (fldname in sch.fields())
                dest.setVal(destslot, fldname, getVal(slot, fldname))
            delete(slot)
            destslot++
        }
    }

    private fun fldpos(slot: Int, fldname: String): Int {
        val offset = ti.offset(fldname)
        return slotpos(slot) + offset
    }

    private fun slotpos(slot: Int): Int {
        return Page.INT_SIZE + Page.INT_SIZE + slot * slotsize
    }
}
