package simpledb.multibuffer

import simpledb.tx.Transaction
import simpledb.record.TableInfo
import simpledb.query.*

/**
 * The Scan class for the muti-buffer version of the
 * *product* operator.
 * @author Edward Sciore
 */
class MultiBufferProductScan
/**
 * Creates the scan class for the product of the LHS scan and a table.
 * @param lhsscan the LHS scan
 * @param ti the metadata for the RHS table
 * @param tx the current transaction
 */
(private val lhsscan: Scan, private val ti: TableInfo, private val tx: Transaction) : Scan {
    private var rhsscan: Scan? = null
    private var prodscan: Scan? = null
    private val chunksize: Int
    private var nextblknum: Int = 0
    private val filesize: Int


    init {
        filesize = tx.size(ti.fileName())
        chunksize = BufferNeeds.bestFactor(filesize)
        beforeFirst()
    }

    /**
     * Positions the scan before the first record.
     * That is, the LHS scan is positioned at its first record,
     * and the RHS scan is positioned before the first record of the first chunk.
     * @see simpledb.query.Scan.beforeFirst
     */
    override fun beforeFirst() {
        nextblknum = 0
        useNextChunk()
    }

    /**
     * Moves to the next record in the current scan.
     * If there are no more records in the current chunk,
     * then move to the next LHS record and the beginning of that chunk.
     * If there are no more LHS records, then move to the next chunk
     * and begin again.
     * @see simpledb.query.Scan.next
     */
    override fun next(): Boolean {
        while (!prodscan!!.next())
            if (!useNextChunk())
                return false
        return true
    }

    /**
     * Closes the current scans.
     * @see simpledb.query.Scan.close
     */
    override fun close() {
        prodscan!!.close()
    }

    /**
     * Returns the value of the specified field.
     * The value is obtained from whichever scan
     * contains the field.
     * @see simpledb.query.Scan.getVal
     */
    override fun getVal(fldname: String): Constant {
        return prodscan!!.getVal(fldname)
    }

    /**
     * Returns the integer value of the specified field.
     * The value is obtained from whichever scan
     * contains the field.
     * @see simpledb.query.Scan.getInt
     */
    override fun getInt(fldname: String): Int {
        return prodscan!!.getInt(fldname)
    }

    /**
     * Returns the string value of the specified field.
     * The value is obtained from whichever scan
     * contains the field.
     * @see simpledb.query.Scan.getString
     */
    override fun getString(fldname: String): String {
        return prodscan!!.getString(fldname)
    }

    /**
     * Returns true if the specified field is in
     * either of the underlying scans.
     * @see simpledb.query.Scan.hasField
     */
    override fun hasField(fldname: String): Boolean {
        return prodscan!!.hasField(fldname)
    }

    private fun useNextChunk(): Boolean {
        if (rhsscan != null)
            rhsscan!!.close()
        if (nextblknum >= filesize)
            return false
        var end = nextblknum + chunksize - 1
        if (end >= filesize)
            end = filesize - 1
        rhsscan = ChunkScan(ti, nextblknum, end, tx)
        lhsscan.beforeFirst()
        prodscan = ProductScan(lhsscan, rhsscan!!)
        nextblknum = end + 1
        return true
    }
}

