package simpledb.record

import simpledb.file.Page.Companion.BLOCK_SIZE
import simpledb.file.Page.Companion.INT_SIZE
import simpledb.file.Block
import simpledb.tx.Transaction

/**
 * Manages the placement and access of records in a block.
 * @author Edward Sciore
 */
class RecordPage
/** Creates the record manager for the specified block.
 * The current record is set to be prior to the first one.
 * @param blk a reference to the disk block
 * @param ti the table's metadata
 * @param tx the transaction performing the operations
 */
(blk: Block, private val ti: TableInfo, private val tx: Transaction) {
    private val slotsize: Int
    private var currentslot = -1
    private var blk: Block?

    private val isValidSlot: Boolean
        get() = currentpos() + slotsize <= BLOCK_SIZE

    init {
        this.blk = blk
        slotsize = ti.recordLength() + INT_SIZE
        tx.pin(blk)
    }

    /**
     * Closes the manager, by unpinning the block.
     */
    fun close() {
        if (blk != null) {
            tx.unpin(blk!!)
            blk = null
        }
    }

    /**
     * Moves to the next record in the block.
     * @return false if there is no next record.
     */
    operator fun next(): Boolean {
        return searchFor(INUSE)
    }

    /**
     * Returns the integer value stored for the
     * specified field of the current record.
     * @param fldname the name of the field.
     * @return the integer stored in that field
     */
    fun getInt(fldname: String): Int {
        val position = fieldpos(fldname)
        return tx.getInt(blk!!, position)
    }

    fun nonBlockingGetInt(fldname: String): Int {
        val position = fieldpos(fldname)
        val txnumpos = fieldpos("_txnum")
        return tx.nonblockingGetInt(blk!!, position, txnumpos)!!
    }

    /**
     * Returns the string value stored for the
     * specified field of the current record.
     * @param fldname the name of the field.
     * @return the string stored in that field
     */

    fun getString(fldname: String): String {
        val position = fieldpos(fldname)
        return tx.getString(blk!!, position)
    }

    fun nonBlockingGetString(fldname: String): String {
        val position = fieldpos(fldname)
        val txnumpos = fieldpos("_txnum")
        return tx.nonblockingGetString(blk!!, position, txnumpos)!!
    }

    /**
     * Stores an integer at the specified field
     * of the current record.
     * @param fldname the name of the field
     * @param val the integer value stored in that field
     */
    fun setInt(fldname: String, `val`: Int) {
        val position = fieldpos(fldname)
        val txnumpos = fieldpos("_txnum")
        tx.mvccSetInt(blk!!, position, `val`, txnumpos)
    }

    /**
     * Stores a string at the specified field
     * of the current record.
     * @param fldname the name of the field
     * @param val the string value stored in that field
     */
    fun setString(fldname: String, `val`: String) {
        val position = fieldpos(fldname)
        val txnumpos = fieldpos("_txnum")
        tx.mvccSetString(blk!!, position, `val`, txnumpos)
    }

    /**
     * Deletes the current record.
     * Deletion is performed by just marking the record
     * as "deleted"; the current record does not change.
     * To get to the next record, call next().
     */
    fun delete() {
        val position = currentpos()
        tx.setInt(blk!!, position, EMPTY)
    }

    /**
     * Inserts a new, blank record somewhere in the page.
     * Return false if there were no available slots.
     * @return false if the insertion was not possible
     */
    fun insert(): Boolean {
        currentslot = -1
        val found = searchFor(EMPTY)
        if (found) {
            val position = currentpos()
            tx.setInt(blk!!, position, INUSE)
        }
        return found
    }

    /**
     * Sets the current record to be the record having the
     * specified ID.
     * @param id the ID of the record within the page.
     */
    fun moveToId(id: Int) {
        currentslot = id
    }

    /**
     * Returns the ID of the current record.
     * @return the ID of the current record
     */
    fun currentId(): Int {
        return currentslot
    }

    private fun currentpos(): Int {
        return currentslot * slotsize
    }

    private fun fieldpos(fldname: String): Int {
        val offset = INT_SIZE + ti.offset(fldname)
        return currentpos() + offset
    }

    private fun getFlagAtPos(blk: Block, position: Int, tx: Transaction): Int {
        if (ti.schema().fields().contains("_txnum"))
            return tx.nonblockingGetInt(blk, position, fieldpos("_txnum")) ?: 0
        else {
            println(" warning: _txnum has not been created. Query may be inaccurate")
            return tx.getInt(blk, offset = position)
        }
    }

    private fun searchFor(flag: Int): Boolean {
        currentslot++
        while (isValidSlot) {
            val position = currentpos()
            if (getFlagAtPos(blk!!, position, tx) == flag)
                return true
            currentslot++
        }
        return false
    }

    companion object {
        val EMPTY = 0
        val INUSE = 1
    }
}
