package simpledb.index.query

import simpledb.tx.Transaction
import simpledb.record.Schema
import simpledb.metadata.IndexInfo
import simpledb.query.*
import simpledb.index.Index

/** The Plan class corresponding to the *indexjoin*
 * relational algebra operator.
 * @author Edward Sciore
 */
class IndexJoinPlan
/**
 * Implements the join operator,
 * using the specified LHS and RHS plans.
 * @param p1 the left-hand plan
 * @param p2 the right-hand plan
 * @param ii information about the right-hand index
 * @param joinfield the left-hand field used for joining
 * @param tx the calling transaction
 */
(
        private val p1: Plan,
        private val p2: Plan,
        private val ii: IndexInfo,
        private val joinfield: String,
        @Suppress("UNUSED_PARAMETER")
        tx: Transaction
) : Plan {
    private val sch = Schema()

    init {
        sch.addAll(p1.schema())
        sch.addAll(p2.schema())
    }

    /**
     * Opens an indexjoin scan for this query
     * @see simpledb.query.Plan.open
     */
    override fun open(): Scan {
        val s = p1.open()
        // throws an exception if p2 is not a tableplan
        val ts = p2.open() as TableScan
        val idx = ii.open()
        return IndexJoinScan(s, idx, joinfield, ts)
    }

    /**
     * Estimates the number of block accesses to compute the join.
     * The formula is:
     * <pre> B(indexjoin(p1,p2,idx)) = B(p1) + R(p1)*B(idx)
     * + R(indexjoin(p1,p2,idx) </pre>
     * @see simpledb.query.Plan.blocksAccessed
     */
    override fun blocksAccessed(): Int {
        return (p1.blocksAccessed()
                + p1.recordsOutput() * ii.blocksAccessed()
                + recordsOutput())
    }

    /**
     * Estimates the number of output records in the join.
     * The formula is:
     * <pre> R(indexjoin(p1,p2,idx)) = R(p1)*R(idx) </pre>
     * @see simpledb.query.Plan.recordsOutput
     */
    override fun recordsOutput(): Int {
        return p1.recordsOutput() * ii.recordsOutput()
    }

    /**
     * Estimates the number of distinct values for the
     * specified field.
     * @see simpledb.query.Plan.distinctValues
     */
    override fun distinctValues(fldname: String): Int {
        return if (p1.schema().hasField(fldname))
            p1.distinctValues(fldname)
        else
            p2.distinctValues(fldname)
    }

    /**
     * Returns the schema of the index join.
     * @see simpledb.query.Plan.schema
     */
    override fun schema(): Schema {
        return sch
    }
}
