package simpledb.query

/**
 * The scan class corresponding to the *product* relational
 * algebra operator.
 * @author Edward Sciore
 */
class ProductScan
/**
 * Creates a product scan having the two underlying scans.
 * @param s1 the LHS scan
 * @param s2 the RHS scan
 */
(private val s1: Scan, private val s2: Scan) : Scan {

    init {
        s1.next()
    }

    /**
     * Positions the scan before its first record.
     * In other words, the LHS scan is positioned at
     * its first record, and the RHS scan
     * is positioned before its first record.
     * @see simpledb.query.Scan.beforeFirst
     */
    override fun beforeFirst() {
        s1.beforeFirst()
        s1.next()
        s2.beforeFirst()
    }

    /**
     * Moves the scan to the next record.
     * The method moves to the next RHS record, if possible.
     * Otherwise, it moves to the next LHS record and the
     * first RHS record.
     * If there are no more LHS records, the method returns false.
     * @see simpledb.query.Scan.next
     */
    override fun next(): Boolean {
        if (s2.next())
            return true
        else {
            s2.beforeFirst()
            return s2.next() && s1.next()
        }
    }

    /**
     * Closes both underlying scans.
     * @see simpledb.query.Scan.close
     */
    override fun close() {
        s1.close()
        s2.close()
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
