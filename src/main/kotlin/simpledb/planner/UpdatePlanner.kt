package simpledb.planner

import simpledb.tx.Transaction
import simpledb.parse.*

/**
 * The interface implemented by the planners
 * for SQL insert, delete, and modify statements.
 * @author Edward Sciore
 */
interface UpdatePlanner {

    /**
     * Executes the specified insert statement, and
     * returns the number of affected records.
     * @param data the parsed representation of the insert statement
     * @param tx the calling transaction
     * @return the number of affected records
     */
    fun executeInsert(data: InsertData, tx: Transaction): Int

    /**
     * Executes the specified delete statement, and
     * returns the number of affected records.
     * @param data the parsed representation of the delete statement
     * @param tx the calling transaction
     * @return the number of affected records
     */
    fun executeDelete(data: DeleteData, tx: Transaction): Int

    /**
     * Executes the specified modify statement, and
     * returns the number of affected records.
     * @param data the parsed representation of the modify statement
     * @param tx the calling transaction
     * @return the number of affected records
     */
    fun executeModify(data: ModifyData, tx: Transaction): Int

    /**
     * Executes the specified create table statement, and
     * returns the number of affected records.
     * @param data the parsed representation of the create table statement
     * @param tx the calling transaction
     * @return the number of affected records
     */
    fun executeCreateTable(data: CreateTableData, tx: Transaction): Int

    /**
     * Executes the specified create view statement, and
     * returns the number of affected records.
     * @param data the parsed representation of the create view statement
     * @param tx the calling transaction
     * @return the number of affected records
     */
    fun executeCreateView(data: CreateViewData, tx: Transaction): Int

    /**
     * Executes the specified create index statement, and
     * returns the number of affected records.
     * @param data the parsed representation of the create index statement
     * @param tx the calling transaction
     * @return the number of affected records
     */
    fun executeCreateIndex(data: CreateIndexData, tx: Transaction): Int
}
