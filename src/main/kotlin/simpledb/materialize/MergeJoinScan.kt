package simpledb.materialize

import simpledb.query.*

/**
 * The Scan class for the *mergejoin* operator.
 * @author Edward Sciore
 */
class MergeJoinScan
/**
 * Creates a mergejoin scan for the two underlying sorted scans.
 * @param s1 the LHS sorted scan
 * @param s2 the RHS sorted scan
 * @param fldname1 the LHS join field
 * @param fldname2 the RHS join field
 */
(private val s1: Scan, private val s2: SortScan, private val fldname1: String, private val fldname2: String) : Scan {
    private var joinval: Constant? = null

    init {
        beforeFirst()
    }

    /**
     * Positions the scan before the first record,
     * by positioning each underlying scan before
     * their first records.
     * @see simpledb.query.Scan.beforeFirst
     */
    override fun beforeFirst() {
        s1.beforeFirst()
        s2.beforeFirst()
    }

    /**
     * Closes the scan by closing the two underlying scans.
     * @see simpledb.query.Scan.close
     */
    override fun close() {
        s1.close()
        s2.close()
    }

    /**
     * Moves to the next record.  This is where the action is.
     * <P>
     * If the next RHS record has the same join value,
     * then move to it.
     * Otherwise, if the next LHS record has the same join value,
     * then reposition the RHS scan back to the first record
     * having that join value.
     * Otherwise, repeatedly move the scan having the smallest
     * value until a common join value is found.
     * When one of the scans runs out of records, return false.
     * @see simpledb.query.Scan.next
    </P> */
    override fun next(): Boolean {
        var hasmore2 = s2.next()
        if (hasmore2 && s2.getVal(fldname2) == joinval)
            return true

        var hasmore1 = s1.next()
        if (hasmore1 && s1.getVal(fldname1) == joinval) {
            s2.restorePosition()
            return true
        }

        while (hasmore1 && hasmore2) {
            val v1 = s1.getVal(fldname1)
            val v2 = s2.getVal(fldname2)
            if (v1.compareTo(v2) < 0)
                hasmore1 = s1.next()
            else if (v1.compareTo(v2) > 0)
                hasmore2 = s2.next()
            else {
                s2.savePosition()
                joinval = s2.getVal(fldname2)
                return true
            }
        }
        return false
    }

    /**
     * Returns the value of the specified field.
     * The value is obtained from whichever scan
     * contains the field.
     * @see simpledb.query.Scan.getVal
     */
    override fun getVal(fldname: String): Constant {
        return if (s1.hasField(fldname))
            s1.getVal(fldname)
        else
            s2.getVal(fldname)
    }

    /**
     * Returns the integer value of the specified field.
     * The value is obtained from whichever scan
     * contains the field.
     * @see simpledb.query.Scan.getInt
     */
    override fun getInt(fldname: String): Int {
        return if (s1.hasField(fldname))
            s1.getInt(fldname)
        else
            s2.getInt(fldname)
    }

    /**
     * Returns the string value of the specified field.
     * The value is obtained from whichever scan
     * contains the field.
     * @see simpledb.query.Scan.getString
     */
    override fun getString(fldname: String): String {
        return if (s1.hasField(fldname))
            s1.getString(fldname)
        else
            s2.getString(fldname)
    }

    /**
     * Returns true if the specified field is in
     * either of the underlying scans.
     * @see simpledb.query.Scan.hasField
     */
    override fun hasField(fldname: String): Boolean {
        return s1.hasField(fldname) || s2.hasField(fldname)
    }
}

