package simpledb.materialize

import simpledb.tx.Transaction
import simpledb.record.Schema
import simpledb.query.*
import java.util.*

/**
 * The Plan class for the *groupby* operator.
 * @author Edward Sciore
 */
class GroupByPlan
/**
 * Creates a groupby plan for the underlying query.
 * The grouping is determined by the specified
 * collection of group fields,
 * and the aggregation is computed by the
 * specified collection of aggregation functions.
 * @param p a plan for the underlying query
 * @param groupfields the group fields
 * @param aggfns the aggregation functions
 * @param tx the calling transaction
 */
(p: Plan, private val groupfields: Collection<String>, private val aggfns: Collection<AggregationFn>, tx: Transaction) : Plan {
    private val p: Plan
    private val sch = Schema()

    init {
        val grouplist = ArrayList<String>()
        grouplist.addAll(groupfields)
        this.p = SortPlan(p, grouplist, tx)
        for (fldname in groupfields)
            sch.add(fldname, p.schema())
        for (fn in aggfns)
            sch.addIntField(fn.fieldName())
    }

    /**
     * This method opens a sort plan for the specified plan.
     * The sort plan ensures that the underlying records
     * will be appropriately grouped.
     * @see simpledb.query.Plan.open
     */
    override fun open(): Scan {
        val s = p.open()
        return GroupByScan(s, groupfields, aggfns)
    }

    /**
     * Returns the number of blocks required to
     * compute the aggregation,
     * which is one pass through the sorted table.
     * It does *not* include the one-time cost
     * of materializing and sorting the records.
     * @see simpledb.query.Plan.blocksAccessed
     */
    override fun blocksAccessed(): Int {
        return p.blocksAccessed()
    }

    /**
     * Returns the number of groups.  Assuming equal distribution,
     * this is the product of the distinct values
     * for each grouping field.
     * @see simpledb.query.Plan.recordsOutput
     */
    override fun recordsOutput(): Int {
        var numgroups = 1
        for (fldname in groupfields)
            numgroups *= p.distinctValues(fldname)
        return numgroups
    }

    /**
     * Returns the number of distinct values for the
     * specified field.  If the field is a grouping field,
     * then the number of distinct values is the same
     * as in the underlying query.
     * If the field is an aggregate field, then we
     * assume that all values are distinct.
     * @see simpledb.query.Plan.distinctValues
     */
    override fun distinctValues(fldname: String): Int {
        return if (p.schema().hasField(fldname))
            p.distinctValues(fldname)
        else
            recordsOutput()
    }

    /**
     * Returns the schema of the output table.
     * The schema consists of the group fields,
     * plus one field for each aggregation function.
     * @see simpledb.query.Plan.schema
     */
    override fun schema(): Schema {
        return sch
    }
}
