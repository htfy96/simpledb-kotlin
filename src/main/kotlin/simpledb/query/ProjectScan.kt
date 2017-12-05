package simpledb.query

import java.util.*

/**
 * The scan class corresponding to the *project* relational
 * algebra operator.
 * All methods except hasField delegate their work to the
 * underlying scan.
 * @author Edward Sciore
 */
class ProjectScan
/**
 * Creates a project scan having the specified
 * underlying scan and field list.
 * @param s the underlying scan
 * @param fieldlist the list of field names
 */
(private val s: Scan, private val fieldlist: Collection<String>) : Scan {

    override fun beforeFirst() {
        s.beforeFirst()
    }

    override fun next(): Boolean {
        return s.next()
    }

    override fun close() {
        s.close()
    }

    override fun getVal(fldname: String): Constant {
        return if (hasField(fldname))
            s.getVal(fldname)
        else
            throw RuntimeException("field $fldname not found.")
    }

    override fun getInt(fldname: String): Int {
        return if (hasField(fldname))
            s.getInt(fldname)
        else
            throw RuntimeException("field $fldname not found.")
    }

    override fun getString(fldname: String): String {
        return if (hasField(fldname))
            s.getString(fldname)
        else
            throw RuntimeException("field $fldname not found.")
    }

    /**
     * Returns true if the specified field
     * is in the projection list.
     * @see simpledb.query.Scan.hasField
     */
    override fun hasField(fldname: String): Boolean {
        return fieldlist.contains(fldname)
    }
}
