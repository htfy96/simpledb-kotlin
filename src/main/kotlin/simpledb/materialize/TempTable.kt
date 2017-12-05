package simpledb.materialize

import simpledb.tx.Transaction
import simpledb.record.*
import simpledb.query.*

/**
 * A class that creates temporary tables.
 * A temporary table is not registered in the catalog.
 * The class therefore has a method getTableInfo to return the
 * table's metadata.
 * @author Edward Sciore
 */
class TempTable
/**
 * Allocates a name for for a new temporary table
 * having the specified schema.
 * @param sch the new table's schema
 * @param tx the calling transaction
 */
(sch: Schema, private val tx: Transaction) {
    /**
     * Return the table's metadata.
     * @return the table's metadata
     */
    val tableInfo: TableInfo

    init {
        val tblname = nextTableName()
        tableInfo = TableInfo(tblname, sch)
    }

    /**
     * Opens a table scan for the temporary table.
     */
    fun open(): UpdateScan {
        return TableScan(tableInfo, tx)
    }

    companion object {
        private var nextTableNum = 0

        @Synchronized private fun nextTableName(): String {
            nextTableNum++
            return "temp" + nextTableNum
        }
    }
}