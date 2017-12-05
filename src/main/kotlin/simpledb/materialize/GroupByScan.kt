package simpledb.materialize

import simpledb.query.*
import java.util.*

/**
 * The Scan class for the *groupby* operator.
 * @author Edward Sciore
 */
class GroupByScan
/**
 * Creates a groupby scan, given a grouped table scan.
 * @param s the grouped scan
 * @param groupfields the group fields
 * @param aggfns the aggregation functions
 */
(private val s: Scan, private val groupfields: Collection<String>, private val aggfns: Collection<AggregationFn>) : Scan {
    private var groupval: GroupValue? = null
    private var moregroups: Boolean = false

    init {
        beforeFirst()
    }

    /**
     * Positions the scan before the first group.
     * Internally, the underlying scan is always
     * positioned at the first record of a group, which
     * means that this method moves to the
     * first underlying record.
     * @see simpledb.query.Scan.beforeFirst
     */
    override fun beforeFirst() {
        s.beforeFirst()
        moregroups = s.next()
    }

    /**
     * Moves to the next group.
     * The key of the group is determined by the
     * group values at the current record.
     * The method repeatedly reads underlying records until
     * it encounters a record having a different key.
     * The aggregation functions are called for each record
     * in the group.
     * The values of the grouping fields for the group are saved.
     * @see simpledb.query.Scan.next
     */
    override fun next(): Boolean {
        if (!moregroups)
            return false
        for (fn in aggfns)
            fn.processFirst(s)
        groupval = GroupValue(s, groupfields)
        while ({moregroups = s.next(); moregroups}()) {
            val gv = GroupValue(s, groupfields)
            if (groupval != gv)
                break
            for (fn in aggfns)
                fn.processNext(s)
        }
        return true
    }

    /**
     * Closes the scan by closing the underlying scan.
     * @see simpledb.query.Scan.close
     */
    override fun close() {
        s.close()
    }

    /**
     * Gets the Constant value of the specified field.
     * If the field is a group field, then its value can
     * be obtained from the saved group value.
     * Otherwise, the value is obtained from the
     * appropriate aggregation function.
     * @see simpledb.query.Scan.getVal
     */
    override fun getVal(fldname: String): Constant {
        if (groupfields.contains(fldname))
            return groupval!!.getVal(fldname)
        for (fn in aggfns)
            if (fn.fieldName() == fldname)
                return fn.value()!!
        throw RuntimeException("field $fldname not found.")
    }

    /**
     * Gets the integer value of the specified field.
     * If the field is a group field, then its value can
     * be obtained from the saved group value.
     * Otherwise, the value is obtained from the
     * appropriate aggregation function.
     * @see simpledb.query.Scan.getVal
     */
    override fun getInt(fldname: String): Int {
        return getVal(fldname).asJavaVal() as Int
    }

    /**
     * Gets the string value of the specified field.
     * If the field is a group field, then its value can
     * be obtained from the saved group value.
     * Otherwise, the value is obtained from the
     * appropriate aggregation function.
     * @see simpledb.query.Scan.getVal
     */
    override fun getString(fldname: String): String {
        return getVal(fldname).asJavaVal() as String
    }

    /* Returns true if the specified field is either a
    * grouping field or created by an aggregation function.
    * @see simpledb.query.Scan#hasField(java.lang.String)
    */
    override fun hasField(fldname: String): Boolean {
        if (groupfields.contains(fldname))
            return true
        for (fn in aggfns)
            if (fn.fieldName() == fldname)
                return true
        return false
    }
}

