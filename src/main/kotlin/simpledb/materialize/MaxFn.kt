package simpledb.materialize

import simpledb.query.*

/**
 * The *max* aggregation function.
 * @author Edward Sciore
 */
class MaxFn
/**
 * Creates a max aggregation function for the specified field.
 * @param fldname the name of the aggregated field
 */
(private val fldname: String) : AggregationFn {
    private var `val`: Constant? = null

    /**
     * Starts a new maximum to be the
     * field value in the current record.
     * @see simpledb.materialize.AggregationFn.processFirst
     */
    override fun processFirst(s: Scan) {
        `val` = s.getVal(fldname)
    }

    /**
     * Replaces the current maximum by the field value
     * in the current record, if it is higher.
     * @see simpledb.materialize.AggregationFn.processNext
     */
    override fun processNext(s: Scan) {
        val newval = s.getVal(fldname)
        if (newval.compareTo(`val`!!) > 0)
            `val` = newval
    }

    /**
     * Returns the field's name, prepended by "maxof".
     * @see simpledb.materialize.AggregationFn.fieldName
     */
    override fun fieldName(): String {
        return "maxof" + fldname
    }

    /**
     * Returns the current maximum.
     * @see simpledb.materialize.AggregationFn.value
     */
    override fun value(): Constant? {
        return `val`
    }
}
