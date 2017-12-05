package simpledb.materialize

import simpledb.query.*

/**
 * The *count* aggregation function.
 * @author Edward Sciore
 */
class CountFn
/**
 * Creates a count aggregation function for the specified field.
 * @param fldname the name of the aggregated field
 */
(private val fldname: String) : AggregationFn {
    private var count: Int = 0

    /**
     * Starts a new count.
     * Since SimpleDB does not support null values,
     * every record will be counted,
     * regardless of the field.
     * The current count is thus set to 1.
     * @see simpledb.materialize.AggregationFn.processFirst
     */
    override fun processFirst(s: Scan) {
        count = 1
    }

    /**
     * Since SimpleDB does not support null values,
     * this method always increments the count,
     * regardless of the field.
     * @see simpledb.materialize.AggregationFn.processNext
     */
    override fun processNext(s: Scan) {
        count++
    }

    /**
     * Returns the field's name, prepended by "countof".
     * @see simpledb.materialize.AggregationFn.fieldName
     */
    override fun fieldName(): String {
        return "countof" + fldname
    }

    /**
     * Returns the current count.
     * @see simpledb.materialize.AggregationFn.value
     */
    override fun value(): Constant {
        return IntConstant(count)
    }
}
