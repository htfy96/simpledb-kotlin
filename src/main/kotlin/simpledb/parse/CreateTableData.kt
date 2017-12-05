package simpledb.parse

import simpledb.record.Schema

/**
 * Data for the SQL *create table* statement.
 * @author Edward Sciore
 */
class CreateTableData
/**
 * Saves the table name and schema.
 */
(private val tblname: String, private val sch: Schema) {

    /**
     * Returns the name of the new table.
     * @return the name of the new table
     */
    fun tableName(): String {
        return tblname
    }

    /**
     * Returns the schema of the new table.
     * @return the schema of the new table
     */
    fun newSchema(): Schema {
        return sch
    }
}

