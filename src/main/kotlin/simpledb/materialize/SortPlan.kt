package simpledb.materialize

import simpledb.tx.Transaction
import simpledb.record.*
import simpledb.query.*

import java.util.*

/**
 * The Plan class for the *sort* operator.
 * @author Edward Sciore
 */
class SortPlan
/**
 * Creates a sort plan for the specified query.
 * @param p the plan for the underlying query
 * @param sortfields the fields to sort by
 * @param tx the calling transaction
 */
(private val p: Plan, sortfields: List<String>, private val tx: Transaction) : Plan {
    private val sch: Schema
    private val comp: RecordComparator

    init {
        sch = p.schema()
        comp = RecordComparator(sortfields)
    }

    /**
     * This method is where most of the action is.
     * Up to 2 sorted temporary tables are created,
     * and are passed into SortScan for final merging.
     * @see simpledb.query.Plan.open
     */
    override fun open(): Scan {
        val src = p.open()
        var runs: MutableList<TempTable> = splitIntoRuns(src)
        src.close()
        while (runs.size > 2)
            runs = doAMergeIteration(runs)
        return SortScan(runs, comp)
    }

    /**
     * Returns the number of blocks in the sorted table,
     * which is the same as it would be in a
     * materialized table.
     * It does *not* include the one-time cost
     * of materializing and sorting the records.
     * @see simpledb.query.Plan.blocksAccessed
     */
    override fun blocksAccessed(): Int {
        // does not include the one-time cost of sorting
        val mp = MaterializePlan(p, tx) // not opened; just for analysis
        return mp.blocksAccessed()
    }

    /**
     * Returns the number of records in the sorted table,
     * which is the same as in the underlying query.
     * @see simpledb.query.Plan.recordsOutput
     */
    override fun recordsOutput(): Int {
        return p.recordsOutput()
    }

    /**
     * Returns the number of distinct field values in
     * the sorted table, which is the same as in
     * the underlying query.
     * @see simpledb.query.Plan.distinctValues
     */
    override fun distinctValues(fldname: String): Int {
        return p.distinctValues(fldname)
    }

    /**
     * Returns the schema of the sorted table, which
     * is the same as in the underlying query.
     * @see simpledb.query.Plan.schema
     */
    override fun schema(): Schema {
        return sch
    }

    private fun splitIntoRuns(src: Scan): MutableList<TempTable> {
        val temps = ArrayList<TempTable>()
        src.beforeFirst()
        if (!src.next())
            return temps
        var currenttemp = TempTable(sch, tx)
        temps.add(currenttemp)
        var currentscan = currenttemp.open()
        while (copy(src, currentscan))
            if (comp.compare(src, currentscan) < 0) {
                // start a new run
                currentscan.close()
                currenttemp = TempTable(sch, tx)
                temps.add(currenttemp)
                currentscan = currenttemp.open()
            }
        currentscan.close()
        return temps
    }

    private fun doAMergeIteration(runs: MutableList<TempTable>): MutableList<TempTable> {
        val result = ArrayList<TempTable>()
        while (runs.size > 1) {
            val p1 = runs.removeAt(0)
            val p2 = runs.removeAt(0)
            result.add(mergeTwoRuns(p1, p2))
        }
        if (runs.size == 1)
            result.add(runs[0])
        return result
    }

    private fun mergeTwoRuns(p1: TempTable, p2: TempTable): TempTable {
        val src1 = p1.open()
        val src2 = p2.open()
        val result = TempTable(sch, tx)
        val dest = result.open()

        var hasmore1 = src1.next()
        var hasmore2 = src2.next()
        while (hasmore1 && hasmore2)
            if (comp.compare(src1, src2) < 0)
                hasmore1 = copy(src1, dest)
            else
                hasmore2 = copy(src2, dest)

        if (hasmore1)
            while (hasmore1)
                hasmore1 = copy(src1, dest)
        else
            while (hasmore2)
                hasmore2 = copy(src2, dest)
        src1.close()
        src2.close()
        dest.close()
        return result
    }

    private fun copy(src: Scan, dest: UpdateScan): Boolean {
        dest.insert()
        for (fldname in sch.fields())
            dest.setVal(fldname, src.getVal(fldname))
        return src.next()
    }
}
