package simpledb.planner

import simpledb.tx.Transaction
import simpledb.query.*
import simpledb.parse.*
import simpledb.server.SimpleDB
import java.util.*

/**
 * The simplest, most naive query planner possible.
 * @author Edward Sciore
 */
class BasicQueryPlanner : QueryPlanner {

    /**
     * Creates a query plan as follows.  It first takes
     * the product of all tables and views; it then selects on the predicate;
     * and finally it projects on the field list.
     */
    override fun createPlan(data: QueryData, tx: Transaction): Plan {
        //Step 1: Create a plan for each mentioned table or view
        val plans = ArrayList<Plan>()
        for (tblname in data.tables()) {
            val viewdef = SimpleDB.mdMgr().getViewDef(tblname, tx)
            if (viewdef != null)
                plans.add(SimpleDB.planner().createQueryPlan(viewdef, tx))
            else
                plans.add(TablePlan(tblname, tx))
        }

        //Step 2: Create the product of all table plans
        var p = plans.removeAt(0)
        for (nextplan in plans)
            p = ProductPlan(p, nextplan)

        //Step 3: Add a selection plan for the predicate
        p = SelectPlan(p, data.pred())

        //Step 4: Project on the field names
        p = ProjectPlan(p, data.fields())
        return p
    }
}
