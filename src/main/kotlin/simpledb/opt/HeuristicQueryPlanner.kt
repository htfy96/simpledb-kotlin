package simpledb.opt

import simpledb.tx.Transaction
import simpledb.query.*
import simpledb.opt.TablePlanner
import simpledb.parse.QueryData
import simpledb.planner.QueryPlanner
import java.util.*

/**
 * A query planner that optimizes using a heuristic-based algorithm.
 * @author Edward Sciore
 */
class HeuristicQueryPlanner : QueryPlanner {
    private val tableplanners = ArrayList<TablePlanner>()

    private val lowestSelectPlan: Plan?
        get() {
            var besttp: TablePlanner? = null
            var bestplan: Plan? = null
            for (tp in tableplanners) {
                val plan = tp.makeSelectPlan()
                if (bestplan == null || plan.recordsOutput() < bestplan.recordsOutput()) {
                    besttp = tp
                    bestplan = plan
                }
            }
            tableplanners.remove(besttp)
            return bestplan
        }

    /**
     * Creates an optimized left-deep query plan using the following
     * heuristics.
     * H1. Choose the smallest table (considering selection predicates)
     * to be first in the join order.
     * H2. Add the table to the join order which
     * results in the smallest output.
     */
    override fun createPlan(data: QueryData, tx: Transaction): Plan {

        // Step 1:  Create a TablePlanner object for each mentioned table
        for (tblname in data.tables()) {
            val tp = TablePlanner(tblname, data.pred(), tx)
            tableplanners.add(tp)
        }

        // Step 2:  Choose the lowest-size plan to begin the join order
        var currentplan = lowestSelectPlan

        // Step 3:  Repeatedly add a plan to the join order
        while (!tableplanners.isEmpty()) {
            val p = getLowestJoinPlan(currentplan!!)
            if (p != null)
                currentplan = p
            else
            // no applicable join
                currentplan = getLowestProductPlan(currentplan)
        }

        // Step 4.  Project on the field names and return
        return ProjectPlan(currentplan!!, data.fields())
    }

    private fun getLowestJoinPlan(current: Plan): Plan? {
        var besttp: TablePlanner? = null
        var bestplan: Plan? = null
        for (tp in tableplanners) {
            val plan = tp.makeJoinPlan(current)
            if (plan != null && (bestplan == null || plan.recordsOutput() < bestplan.recordsOutput())) {
                besttp = tp
                bestplan = plan
            }
        }
        if (bestplan != null)
            tableplanners.remove(besttp)
        return bestplan
    }

    private fun getLowestProductPlan(current: Plan): Plan? {
        var besttp: TablePlanner? = null
        var bestplan: Plan? = null
        for (tp in tableplanners) {
            val plan = tp.makeProductPlan(current)
            if (bestplan == null || plan.recordsOutput() < bestplan.recordsOutput()) {
                besttp = tp
                bestplan = plan
            }
        }
        tableplanners.remove(besttp)
        return bestplan
    }
}
