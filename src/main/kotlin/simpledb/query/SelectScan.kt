package simpledb.query

import simpledb.record.*

/**
 * The scan class corresponding to the *select* relational
 * algebra operator.
 * All methods except next delegate their work to the
 * underlying scan.
 * @author Edward Sciore
 */
class SelectScan
/**
 * Creates a select scan having the specified underlying
 * scan and predicate.
 * @param s the scan of the underlying query
 * @param pred the selection predicate
 */
(private val s: Scan, private val pred: Predicate) : UpdateScan {

    override val rid: RID
        get() {
            val us = s as UpdateScan
            return us.rid
        }

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

    // UpdateScan methods

    override fun setVal(fldname: String, `val`: Constant) {
        val us = s as UpdateScan
        us.setVal(fldname, `val`)
    }

    override fun setInt(fldname: String, `val`: Int) {
        val us = s as UpdateScan
        us.setInt(fldname, `val`)
    }

    override fun setString(fldname: String, `val`: String) {
        val us = s as UpdateScan
        us.setString(fldname, `val`)
    }

    override fun delete() {
        val us = s as UpdateScan
        us.delete()
    }

    override fun insert() {
        val us = s as UpdateScan
        us.insert()
    }

    override fun moveToRid(rid: RID) {
        val us = s as UpdateScan
        us.moveToRid(rid)
    }
}
