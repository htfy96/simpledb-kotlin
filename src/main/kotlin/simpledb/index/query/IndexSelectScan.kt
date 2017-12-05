package simpledb.index.query

import simpledb.record.RID
import simpledb.query.*
import simpledb.index.Index

/**
 * The scan class corresponding to the select relational
 * algebra operator.
 * @author Edward Sciore
 */
class IndexSelectScan
/**
 * Creates an index select scan for the specified
 * index and selection constant.
 * @param idx the index
 * @param val the selection constant
 */
(private val idx: Index, private val `val`: Constant, private val ts: TableScan) : Scan {

    init {
        beforeFirst()
    }

    /**
     * Positions the scan before the first record,
     * which in this case means positioning the index
     * before the first instance of the selection constant.
     * @see simpledb.query.Scan.beforeFirst
     */
    override fun beforeFirst() {
        idx.beforeFirst(`val`)
    }

    /**
     * Moves to the next record, which in this case means
     * moving the index to the next record satisfying the
     * selection constant, and returning false if there are
     * no more such index records.
     * If there is a next record, the method moves the
     * tablescan to the corresponding data record.
     * @see simpledb.query.Scan.next
     */
    override fun next(): Boolean {
        val ok = idx.next()
        if (ok) {
            val rid = idx.dataRid
            ts.moveToRid(rid)
        }
        return ok
    }

    /**
     * Closes the scan by closing the index and the tablescan.
     * @see simpledb.query.Scan.close
     */
    override fun close() {
        idx.close()
        ts.close()
    }

    /**
     * Returns the value of the field of the current data record.
     * @see simpledb.query.Scan.getVal
     */
    override fun getVal(fldname: String): Constant {
        return ts.getVal(fldname)
    }

    /**
     * Returns the value of the field of the current data record.
     * @see simpledb.query.Scan.getInt
     */
    override fun getInt(fldname: String): Int {
        return ts.getInt(fldname)
    }

    /**
     * Returns the value of the field of the current data record.
     * @see simpledb.query.Scan.getString
     */
    override fun getString(fldname: String): String {
        return ts.getString(fldname)
    }

    /**
     * Returns whether the data record has the specified field.
     * @see simpledb.query.Scan.hasField
     */
    override fun hasField(fldname: String): Boolean {
        return ts.hasField(fldname)
    }
}
