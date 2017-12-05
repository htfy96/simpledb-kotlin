package simpledb.multibuffer

import java.sql.Types.INTEGER
import simpledb.tx.Transaction
import simpledb.record.*
import simpledb.file.Block
import simpledb.query.*

import java.util.ArrayList

/**
 * The class for the *chunk* operator.
 * @author Edward Sciore
 */
class ChunkScan
/**
 * Creates a chunk consisting of the specified pages.
 * @param ti the metadata for the chunked table
 * @param startbnum the starting block number
 * @param endbnum  the ending block number
 * @param tx the current transaction
 */
(ti: TableInfo, private val startbnum: Int, private val endbnum: Int, tx: Transaction) : Scan {
    private val pages: MutableList<RecordPage>
    private var current: Int = 0
    private val sch: Schema?
    private var rp: RecordPage? = null

    init {
        pages = ArrayList()
        this.sch = ti.schema()
        val filename = ti.fileName()
        for (i in startbnum..endbnum) {
            val blk = Block(filename, i)
            pages.add(RecordPage(blk, ti, tx))
        }
        beforeFirst()
    }


    /**
     * @see simpledb.query.Scan.beforeFirst
     */
    override fun beforeFirst() {
        moveToBlock(startbnum)
    }

    /**
     * Moves to the next record in the current block of the chunk.
     * If there are no more records, then make
     * the next block be current.
     * If there are no more blocks in the chunk, return false.
     * @see simpledb.query.Scan.next
     */
    override fun next(): Boolean {
        while (true) {
            if (rp!!.next())
                return true
            if (current == endbnum)
                return false
            moveToBlock(current + 1)
        }
    }

    /**
     * @see simpledb.query.Scan.close
     */
    override fun close() {
        for (r in pages)
            r.close()
    }

    /**
     * @see simpledb.query.Scan.getVal
     */
    override fun getVal(fldname: String): Constant {
        return if (sch!!.type(fldname) == INTEGER)
            IntConstant(rp!!.getInt(fldname))
        else
            StringConstant(rp!!.getString(fldname))
    }

    /**
     * @see simpledb.query.Scan.getInt
     */
    override fun getInt(fldname: String): Int {
        return rp!!.getInt(fldname)
    }

    /**
     * @see simpledb.query.Scan.getString
     */
    override fun getString(fldname: String): String {
        return rp!!.getString(fldname)
    }

    /**
     * @see simpledb.query.Scan.hasField
     */
    override fun hasField(fldname: String): Boolean {
        return sch!!.hasField(fldname)
    }

    private fun moveToBlock(blknum: Int) {
        current = blknum
        rp = pages[current - startbnum]
        rp!!.moveToId(-1)
    }
}