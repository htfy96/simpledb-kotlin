package simpledb.query

import simpledb.record.RID

class NonblockingSelectScan
/**
 * Creates a select scan having the specified underlying
 * scan and predicate.
 * @param s the scan of the underlying query
 * @param pred the selection predicate
 */
(private val s: Scan, private val pred: Predicate) : Scan {

    // Scan methods

    override fun beforeFirst() {
        s.beforeFirst()
    }

    /**
     * Move to the next record satisfying the predicate.
     * The method repeatedly calls next on the underlying scan
     * until a suitable record is found, or the underlying scan
     * contains no more records.
     * @see simpledb.query.Scan.next
     */
    override fun next(): Boolean {
        while (s.next())
            if (pred.isSatisfied(s))
                return true
        return false
    }

    override fun close() {
        s.close()
    }

    override fun getVal(fldname: String): Constant {
        return s.getVal(fldname)
    }

    override fun getInt(fldname: String): Int {
        return s.getInt(fldname)
    }

    override fun getString(fldname: String): String {
        return s.getString(fldname)
    }

    override fun hasField(fldname: String): Boolean {
        return s.hasField(fldname)
    }
}
