package simpledb.materialize

import simpledb.tx.Transaction
import simpledb.record.*
import simpledb.query.*
import java.util.*

/**
 * The Plan class for the *mergejoin* operator.
 * @author Edward Sciore
 */
class MergeJoinPlan
/**
 * Creates a mergejoin plan for the two specified queries.
 * The RHS must be materialized after it is sorted,
 * in order to deal with possible duplicates.
 * @param p1 the LHS query plan
 * @param p2 the RHS query plan
 * @param fldname1 the LHS join field
 * @param fldname2 the RHS join field
 * @param tx the calling transaction
 */
(p1: Plan, p2: Plan, private val fldname1: String, private val fldname2: String, tx: Transaction) : Plan {
    private val p1: Plan
    private val p2: Plan
    private val sch = Schema()

    init {
        val sortlist1 = Arrays.asList(fldname1)
        this.p1 = SortPlan(p1, sortlist1, tx)
        val sortlist2 = Arrays.asList(fldname2)
        this.p2 = SortPlan(p2, sortlist2, tx)

        sch.addAll(p1.schema())
        sch.addAll(p2.schema())
    }

    /** The method first sorts its two underlying scans
     * on their join field. It then returns a mergejoin scan
     * of the two sorted table scans.
     * @see simpledb.query.Plan.open
     */
    override fun open(): Scan {
        val s1 = p1.open()
        val s2 = p2.open() as SortScan
        return MergeJoinScan(s1, s2, fldname1, fldname2)
    }

    /**
     * Returns the number of block acceses required to
     * mergejoin the sorted tables.
     * Since a mergejoin can be preformed with a single
     * pass through each table, the method returns
     * the sum of the block accesses of the
     * materialized sorted tables.
     * It does *not* include the one-time cost
     * of materializing and sorting the records.
     * @see simpledb.query.Plan.blocksAccessed
     */
    override fun blocksAccessed(): Int {
        return p1.blocksAccessed() + p2.blocksAccessed()
    }

    /**
     * Returns the number of records in the join.
     * Assuming uniform distribution, the formula is:
     * <pre> R(join(p1,p2)) = R(p1)*R(p2)/max{V(p1,F1),V(p2,F2)}</pre>
     * @see simpledb.query.Plan.recordsOutput
     */
    override fun recordsOutput(): Int {
        val maxvals = Math.max(p1.distinctValues(fldname1),
                p2.distinctValues(fldname2))
        return p1.recordsOutput() * p2.recordsOutput() / maxvals
    }

    /**
     * Estimates the distinct number of field values in the join.
     * Since the join does not increase or decrease field values,
     * the estimate is the same as in the appropriate underlying query.
     * @see simpledb.query.Plan.distinctValues
     */
    override fun distinctValues(fldname: String): Int {
        return if (p1.schema().hasField(fldname))
            p1.distinctValues(fldname)
        else
            p2.distinctValues(fldname)
    }

    /**
     * Returns the schema of the join,
     * which is the union of the schemas of the underlying queries.
     * @see simpledb.query.Plan.schema
     */
    override fun schema(): Schema {
        return sch
    }
}

