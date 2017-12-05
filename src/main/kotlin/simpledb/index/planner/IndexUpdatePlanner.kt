package simpledb.index.planner

import simpledb.record.RID
import simpledb.server.SimpleDB
import simpledb.tx.Transaction
import simpledb.index.Index
import simpledb.metadata.IndexInfo
import simpledb.parse.*
import simpledb.planner.*
import simpledb.query.*

/**
 * A modification of the basic update planner.
 * It dispatches each update statement to the corresponding
 * index planner.
 * @author Edward Sciore
 */
class IndexUpdatePlanner : UpdatePlanner {

    override fun executeInsert(data: InsertData, tx: Transaction): Int {
        val tblname = data.tableName()
        val p = TablePlan(tblname, tx)

        // first, insert the record
        val s = p.open() as UpdateScan
        s.insert()
        val rid = s.rid

        // then modify each field, inserting an index record if appropriate
        val indexes = SimpleDB.mdMgr().getIndexInfo(tblname, tx)
        val valIter = data.vals().iterator()
        for (fldname in data.fields()) {
            val `val` = valIter.next()
            println("Modify field $fldname to val $`val`")
            s.setVal(fldname, `val`)

            val ii = indexes[fldname]
            if (ii != null) {
                val idx = ii.open()
                idx.insert(`val`, rid)
                idx.close()
            }
        }
        s.close()
        return 1
    }

    override fun executeDelete(data: DeleteData, tx: Transaction): Int {
        val tblname = data.tableName()
        var p: Plan = TablePlan(tblname, tx)
        p = SelectPlan(p, data.pred())
        val indexes = SimpleDB.mdMgr().getIndexInfo(tblname, tx)

        val s = p.open() as UpdateScan
        var count = 0
        while (s.next()) {
            // first, delete the record's RID from every index
            val rid = s.rid
            for (fldname in indexes.keys) {
                val `val` = s.getVal(fldname)
                val idx = indexes[fldname]!!.open()
                idx.delete(`val`, rid)
                idx.close()
            }
            // then delete the record
            s.delete()
            count++
        }
        s.close()
        return count
    }

    override fun executeModify(data: ModifyData, tx: Transaction): Int {
        val tblname = data.tableName()
        val fldname = data.targetField()
        var p: Plan = TablePlan(tblname, tx)
        p = SelectPlan(p, data.pred())

        val ii = SimpleDB.mdMgr().getIndexInfo(tblname, tx)[fldname]
        val idx = ii?.open()

        val s = p.open() as UpdateScan
        var count = 0
        while (s.next()) {
            // first, update the record
            val newval = data.newValue().evaluate(s)
            val oldval = s.getVal(fldname)
            s.setVal(data.targetField(), newval)

            // then update the appropriate index, if it exists
            if (idx != null) {
                val rid = s.rid
                idx.delete(oldval, rid)
                idx.insert(newval, rid)
            }
            count++
        }
        idx?.close()
        s.close()
        return count
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
