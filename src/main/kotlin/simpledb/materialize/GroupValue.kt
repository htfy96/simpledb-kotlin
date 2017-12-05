package simpledb.materialize

import simpledb.query.*

import java.util.*

/**
 * An object that holds the values of the grouping fields
 * for the current record of a scan.
 * @author Edward Sciore
 */
class GroupValue
/**
 * Creates a new group value, given the specified scan
 * and list of fields.
 * The values in the current record of each field are
 * stored.
 * @param s a scan
 * @param fields the list of fields
 */
(s: Scan, fields: Collection<String>) {
    private val vals: MutableMap<String, Constant>

    init {
        vals = HashMap()
        for (fldname in fields)
            vals.put(fldname, s.getVal(fldname))
    }

    /**
     * Returns the Constant value of the specified field in the group.
     * @param fldname the name of a field
     * @return the value of the field in the group
     */
    fun getVal(fldname: String): Constant {
        return vals[fldname]!!
    }

    /**
     * Two GroupValue objects are equal if they have the same values
     * for their grouping fields.
     * @see java.lang.Object.equals
     */
    override fun equals(obj: Any?): Boolean {
        val gv = obj as GroupValue?
        for (fldname in vals.keys) {
            val v1 = vals[fldname]
            val v2 = gv!!.getVal(fldname)
            if (v1 != v2)
                return false
        }
        return true
    }

    /**
     * The hashcode of a GroupValue object is the sum of the
     * hashcodes of its field values.
     * @see java.lang.Object.hashCode
     */
    override fun hashCode(): Int {
        var hashval = 0
        for (c in vals.values)
            hashval += c.hashCode()
        return hashval
    }
}