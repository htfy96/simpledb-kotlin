package simpledb.parse

import simpledb.query.*

/**
 * Data for the SQL *delete* statement.
 * @author Edward Sciore
 */
class DeleteData
/**
 * Saves the table name and predicate.
 */
(private val tblname: String, private val pred: Predicate) {

    /**
     * Returns the name of the affected table.
     * @return the name of the affected table
     */
    fun tableName(): String {
        return tblname
    }

    /**
     * Returns the predicate that describes which
     * records should be deleted.
     * @return the deletion predicate
     */
    fun pred(): Predicate {
        return pred
    }
}

