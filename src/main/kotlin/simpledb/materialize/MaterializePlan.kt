package simpledb.materialize

import simpledb.file.Page
import simpledb.tx.Transaction
import simpledb.record.*
import simpledb.query.*

/**
 * The Plan class for the *materialize* operator.
 * @author Edward Sciore
 */
class MaterializePlan
/**
 * Creates a materialize plan for the specified query.
 * @param srcplan the plan of the underlying query
 * @param tx the calling transaction
 */
(private val srcplan: Plan, private val tx: Transaction) : Plan {

    /**
     * This method loops through the underlying query,
     * copying its output records into a temporary table.
     * It then returns a table scan for that table.
     * @see simpledb.query.Plan.open
     */
    override fun open(): Scan {
        val sch = srcplan.schema()
        val temp = TempTable(sch, tx)
        val src = srcplan.open()
        val dest = temp.open()
        while (src.next()) {
            dest.insert()
            for (fldname in sch.fields())
                dest.setVal(fldname, src.getVal(fldname))
        }
        src.close()
        dest.beforeFirst()
        return dest
    }

    /**
     * Returns the estimated number of blocks in the
     * materialized table.
     * It does *not* include the one-time cost
     * of materializing the records.
     * @see simpledb.query.Plan.blocksAccessed
     */
    override fun blocksAccessed(): Int {
        // create a dummy TableInfo object to calculate record length
        val ti = TableInfo("", srcplan.schema())
        val rpb = (Page.BLOCK_SIZE / ti.recordLength()).toDouble()
        return Math.ceil(srcplan.recordsOutput() / rpb).toInt()
    }

    /**
     * Returns the number of records in the materialized table,
     * which is the same as in the underlying plan.
     * @see simpledb.query.Plan.recordsOutput
     */
    override fun recordsOutput(): Int {
        return srcplan.recordsOutput()
    }

    /**
     * Returns the number of distinct field values,
     * which is the same as in the underlying plan.
     * @see simpledb.query.Plan.distinctValues
     */
    override fun distinctValues(fldname: String): Int {
        return srcplan.distinctValues(fldname)
    }

    /**
     * Returns the schema of the materialized table,
     * which is the same as in the underlying plan.
     * @see simpledb.query.Plan.schema
     */
    override fun schema(): Schema {
        return srcplan.schema()
    }
}
