package simpledb.opt

import simpledb.tx.Transaction
import simpledb.record.Schema
import simpledb.query.*
import simpledb.index.query.*
import simpledb.metadata.IndexInfo
import simpledb.multibuffer.MultiBufferProductPlan
import simpledb.server.SimpleDB

/**
 * This class contains methods for planning a single table.
 * @author Edward Sciore
 */
internal class TablePlanner
/**
 * Creates a new table planner.
 * The specified predicate applies to the entire query.
 * The table planner is responsible for determining
 * which portion of the predicate is useful to the table,
 * and when indexes are useful.
 * @param tblname the name of the table
 * @param mypred the query predicate
 * @param tx the calling transaction
 */
(tblname: String, private val mypred: Predicate, private val tx: Transaction) {
    private val myplan: TablePlan
    private val myschema: Schema?
    private val indexes: Map<String, IndexInfo>

    init {
        myplan = TablePlan(tblname, tx)
        myschema = myplan.schema()
        indexes = SimpleDB.mdMgr().getIndexInfo(tblname, tx)
    }

    /**
     * Constructs a select plan for the table.
     * The plan will use an indexselect, if possible.
     * @return a select plan for the table.
     */
    fun makeSelectPlan(): Plan {
        var p = makeIndexSelect()
        if (p == null)
            p = myplan
        return addSelectPred(p)
    }

    /**
     * Constructs a join plan of the specified plan
     * and the table.  The plan will use an indexjoin, if possible.
     * (Which means that if an indexselect is also possible,
     * the indexjoin operator takes precedence.)
     * The method returns null if no join is possible.
     * @param current the specified plan
     * @return a join plan of the plan and this table
     */
    fun makeJoinPlan(current: Plan): Plan? {
        val currsch = current.schema()
        val joinpred = mypred.joinPred(myschema!!, currsch) ?: return null
        var p = makeIndexJoin(current, currsch)
        if (p == null)
            p = makeProductJoin(current, currsch)
        return p
    }

    /**
     * Constructs a product plan of the specified plan and
     * this table.
     * @param current the specified plan
     * @return a product plan of the specified plan and this table
     */
    fun makeProductPlan(current: Plan): Plan {
        val p = addSelectPred(myplan)
        return MultiBufferProductPlan(current, p, tx)
    }

    private fun makeIndexSelect(): Plan? {
        for (fldname in indexes.keys) {
            val `val` = mypred.equatesWithConstant(fldname)
            if (`val` != null) {
                val ii = indexes[fldname]
                return IndexSelectPlan(myplan, ii!!, `val`, tx)
            }
        }
        return null
    }

    private fun makeIndexJoin(current: Plan, currsch: Schema): Plan? {
        for (fldname in indexes.keys) {
            val outerfield = mypred.equatesWithField(fldname)
            if (outerfield != null && currsch.hasField(outerfield)) {
                val ii = indexes[fldname]
                var p: Plan = IndexJoinPlan(current, myplan, ii!!, outerfield, tx)
                p = addSelectPred(p)
                return addJoinPred(p, currsch)
            }
        }
        return null
    }

    private fun makeProductJoin(current: Plan, currsch: Schema): Plan {
        val p = makeProductPlan(current)
        return addJoinPred(p, currsch)
    }

    private fun addSelectPred(p: Plan): Plan {
        val selectpred = mypred.selectPred(myschema!!)
        return if (selectpred != null)
            SelectPlan(p, selectpred)
        else
            p
    }

    private fun addJoinPred(p: Plan, currsch: Schema): Plan {
        val joinpred = mypred.joinPred(currsch, myschema!!)
        return if (joinpred != null)
            SelectPlan(p, joinpred)
        else
            p
    }
}
