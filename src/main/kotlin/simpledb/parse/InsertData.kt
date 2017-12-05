package simpledb.parse

import simpledb.query.Constant
import java.util.*

/**
 * Data for the SQL *insert* statement.
 * @author Edward Sciore
 */
class InsertData
/**
 * Saves the table name and the field and value lists.
 */
(private val tblname: String, private val flds: List<String>, private val vals: List<Constant>) {

    /**
     * Returns the name of the affected table.
     * @return the name of the affected table
     */
    fun tableName(): String {
        return tblname
    }

    /**
     * Returns a list of fields for which
     * values will be specified in the new record.
     * @return a list of field names
     */
    fun fields(): List<String> {
        return flds
    }

    /**
     * Returns a list of values for the specified fields.
     * There is a one-one correspondence between this
     * list of values and the list of fields.
     * @return a list of Constant values.
     */
    fun vals(): List<Constant> {
        return vals
    }
}

