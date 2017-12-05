package simpledb.planner

import simpledb.tx.Transaction
import simpledb.parse.*
import simpledb.query.*

/**
 * The object that executes SQL statements.
 * @author sciore
 */
class Planner(private val qplanner: QueryPlanner, private val uplanner: UpdatePlanner) {

    /**
     * Creates a plan for an SQL select statement, using the supplied planner.
     * @param qry the SQL query string
     * @param tx the transaction
     * @return the scan corresponding to the query plan
     */
    fun createQueryPlan(qry: String, tx: Transaction): Plan {
        val parser = Parser(qry)
        val data = parser.query()
        return qplanner.createPlan(data, tx)
    }

    /**
     * Executes an SQL insert, delete, modify, or
     * create statement.
     * The method dispatches to the appropriate method of the
     * supplied update planner,
     * depending on what the parser returns.
     * @param cmd the SQL update string
     * @param tx the transaction
     * @return an integer denoting the number of affected records
     */
    fun executeUpdate(cmd: String, tx: Transaction): Int {
        val parser = Parser(cmd)
        val obj = parser.updateCmd()
        return if (obj is InsertData)
            uplanner.executeInsert(obj, tx)
        else if (obj is DeleteData)
            uplanner.executeDelete(obj, tx)
        else if (obj is ModifyData)
            uplanner.executeModify(obj, tx)
        else if (obj is CreateTableData)
            uplanner.executeCreateTable(obj, tx)
        else if (obj is CreateViewData)
            uplanner.executeCreateView(obj, tx)
        else if (obj is CreateIndexData)
            uplanner.executeCreateIndex(obj, tx)
        else
            0
    }
}
