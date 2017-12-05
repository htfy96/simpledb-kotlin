package simpledb.planner

import simpledb.server.SimpleDB
import simpledb.tx.Transaction
import simpledb.parse.*
import simpledb.query.*

/**
 * The basic planner for SQL update statements.
 * @author sciore
 */
class BasicUpdatePlanner : UpdatePlanner {

    override fun executeDelete(data: DeleteData, tx: Transaction): Int {
        var p: Plan = TablePlan(data.tableName(), tx)
        p = SelectPlan(p, data.pred())
        val us = p.open() as UpdateScan
        var count = 0
        while (us.next()) {
            us.delete()
            count++
        }
        us.close()
        return count
    }

    override fun executeModify(data: ModifyData, tx: Transaction): Int {
        var p: Plan = TablePlan(data.tableName(), tx)
        p = SelectPlan(p, data.pred())
        val us = p.open() as UpdateScan
        var count = 0
        while (us.next()) {
            val `val` = data.newValue().evaluate(us)
            us.setVal(data.targetField(), `val`)
            count++
        }
        us.close()
        return count
    }

    override fun executeInsert(data: InsertData, tx: Transaction): Int {
        val p = TablePlan(data.tableName(), tx)
        val us = p.open() as UpdateScan
        us.insert()
        val iter = data.vals().iterator()
        for (fldname in data.fields()) {
            val `val` = iter.next()
            us.setVal(fldname, `val`)
        }
        us.close()
        return 1
    }

    override fun executeCreateTable(data: CreateTableData, tx: Transaction): Int {
        SimpleDB.mdMgr().createTable(data.tableName(), data.newSchema(), tx)
        return 0
    }

    override fun executeCreateView(data: CreateViewData, tx: Transaction): Int {
        SimpleDB.mdMgr().createView(data.viewName(), data.viewDef(), tx)
        return 0
    }

    override fun executeCreateIndex(data: CreateIndexData, tx: Transaction): Int {
        SimpleDB.mdMgr().createIndex(data.indexName(), data.tableName(), data.fieldName(), tx)
        return 0
    }
}
