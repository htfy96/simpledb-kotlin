package simpledb.parse

import simpledb.query.*

/**
 * Data for the SQL *update* statement.
 * @author Edward Sciore
 */
class ModifyData
/**
 * Saves the table name, the modified field and its new value, and the predicate.
 */
(private val tblname: String, private val fldname: String, private val newval: Expression, private val pred: Predicate) {

    /**
     * Returns the name of the affected table.
     * @return the name of the affected table
     */
    fun tableName(): String {
        return tblname
    }

    /**
     * Returns the field whose values will be modified
     * @return the name of the target field
     */
    fun targetField(): String {
        return fldname
    }

    /**
     * Returns an expression.
     * Evaluating this expression for a record produces
     * the value that will be stored in the record's target field.
     * @return the target expression
     */
    fun newValue(): Expression {
        return newval
    }

    /**
     * Returns the predicate that describes which
     * records should be modified.
     * @return the modification predicate
     */
    fun pred(): Predicate {
        return pred
    }
}