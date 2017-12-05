package simpledb.query

import simpledb.record.Schema

/** The Plan class corresponding to the *select*
 * relational algebra operator.
 * @author Edward Sciore
 */
class SelectPlan
/**
 * Creates a new select node in the query tree,
 * having the specified subquery and predicate.
 * @param p the subquery
 * @param pred the predicate
 */
(private val p: Plan, private val pred: Predicate) : Plan {

    /**
     * Creates a select scan for this query.
     * @see simpledb.query.Plan.open
     */
    override fun open(): Scan {
        val s = p.open()
        return SelectScan(s, pred)
    }

    /**
     * Estimates the number of block accesses in the selection,
     * which is the same as in the underlying query.
     * @see simpledb.query.Plan.blocksAccessed
     */
    override fun blocksAccessed(): Int {
        return p.blocksAccessed()
    }

    /**
     * Estimates the number of output records in the selection,
     * which is determined by the
     * reduction factor of the predicate.
     * @see simpledb.query.Plan.recordsOutput
     */
    override fun recordsOutput(): Int {
        return p.recordsOutput() / pred.reductionFactor(p)
    }

    /**
     * Estimates the number of distinct field values
     * in the projection.
     * If the predicate contains a term equating the specified
     * field to a constant, then this value will be 1.
     * Otherwise, it will be the number of the distinct values
     * in the underlying query
     * (but not more than the size of the output table).
     * @see simpledb.query.Plan.distinctValues
     */
    override fun distinctValues(fldname: String): Int {
        if (pred.equatesWithConstant(fldname) != null)
            return 1
        else {
            val fldname2 = pred.equatesWithField(fldname)
            return if (fldname2 != null)
                Math.min(p.distinctValues(fldname),
                        p.distinctValues(fldname2))
            else
                Math.min(p.distinctValues(fldname),
                        recordsOutput())
        }
    }

    /**
     * Returns the schema of the selection,
     * which is the same as in the underlying query.
     * @see simpledb.query.Plan.schema
     */
    override fun schema(): Schema {
        return p.schema()
    }
}
