package simpledb.index.query

import simpledb.query.*
import simpledb.index.Index

/**
 * The scan class corresponding to the indexjoin relational
 * algebra operator.
 * The code is very similar to that of ProductScan,
 * which makes sense because an index join is essentially
 * the product of each LHS record with the matching RHS index records.
 * @author Edward Sciore
 */
class IndexJoinScan
/**
 * Creates an index join scan for the specified LHS scan and
 * RHS index.
 * @param s the LHS scan
 * @param idx the RHS index
 * @param joinfield the LHS field used for joining
 */
(private val s: Scan, private val idx: Index, private val joinfield: String, private val ts: TableScan  // the data table
) : Scan {

    init {
        beforeFirst()
    }

    /**
     * Positions the scan before the first record.
     * That is, the LHS scan will be positioned at its
     * first record, and the index will be positioned
     * before the first record for the join value.
     * @see simpledb.query.Scan.beforeFirst
     */
    override fun beforeFirst() {
        s.beforeFirst()
        s.next()
        resetIndex()
    }

    /**
     * Moves the scan to the next record.
     * The method moves to the next index record, if possible.
     * Otherwise, it moves to the next LHS record and the
     * first index record.
     * If there are no more LHS records, the method returns false.
     * @see simpledb.query.Scan.next
     */
    override fun next(): Boolean {
        while (true) {
            if (idx.next()) {
                ts.moveToRid(idx.dataRid)
                return true
            }
            if (!s.next())
                return false
            resetIndex()
        }
    }

    /**
     * Closes the scan by closing its LHS scan and its RHS index.
     * @see simpledb.query.Scan.close
     */
    override fun close() {
        s.close()
        idx.close()
        ts.close()
    }

    /**
     * Returns the Constant value of the specified field.
     * @see simpledb.query.Scan.getVal
     */
    override fun getVal(fldname: String): Constant {
        return if (ts.hasField(fldname))
            ts.getVal(fldname)
        else
            s.getVal(fldname)
    }

    /**
     * Returns the integer value of the specified field.
     * @see simpledb.query.Scan.getVal
     */
    override fun getInt(fldname: String): Int {
        return if (ts.hasField(fldname))
            ts.getInt(fldname)
        else
            s.getInt(fldname)
    }

    /**
     * Returns the string value of the specified field.
     * @see simpledb.query.Scan.getVal
     */
    override fun getString(fldname: String): String {
        return if (ts.hasField(fldname))
            ts.getString(fldname)
        else
            s.getString(fldname)
    }

    /** Returns true if the field is in the schema.
     * @see simpledb.query.Scan.hasField
     */
    override fun hasField(fldname: String): Boolean {
        return ts.hasField(fldname) || s.hasField(fldname)
    }

    private fun resetIndex() {
        val searchkey = s.getVal(joinfield)
        idx.beforeFirst(searchkey)
    }
}
