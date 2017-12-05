package simpledb.materialize

import simpledb.query.*

/**
 * The interface implemented by aggregation functions.
 * Aggregation functions are used by the *groupby* operator.
 * @author Edward Sciore
 */
interface AggregationFn {

    /**
     * Uses the current record of the specified scan
     * to be the first record in the group.
     * @param s the scan to aggregate over.
     */
    fun processFirst(s: Scan)

    /**
     * Uses the current record of the specified scan
     * to be the next record in the group.
     * @param s the scan to aggregate over.
     */
    fun processNext(s: Scan)

    /**
     * Returns the name of the new aggregation field.
     * @return the name of the new aggregation field
     */
    fun fieldName(): String

    /**
     * Returns the computed aggregation value.
     * @return the computed aggregation value
     */
    fun value(): Constant?
}
