package simpledb.parse

import simpledb.query.*
import java.util.*

/**
 * Data for the SQL *select* statement.
 * @author Edward Sciore
 */
class QueryData
/**
 * Saves the field and table list and predicate.
 */
(private val fields: Collection<String>, private val tables: Collection<String>, private val pred: Predicate) {

    /**
     * Returns the fields mentioned in the select clause.
     * @return a collection of field names
     */
    fun fields(): Collection<String> {
        return fields
    }

    /**
     * Returns the tables mentioned in the from clause.
     * @return a collection of table names
     */
    fun tables(): Collection<String> {
        return tables
    }

    /**
     * Returns the predicate that describes which
     * records should be in the output table.
     * @return the query predicate
     */
    fun pred(): Predicate {
        return pred
    }

    override fun toString(): String {
        var result = "select "
        for (fldname in fields)
            result += fldname + ", "
        result = result.substring(0, result.length - 2) //remove final comma
        result += " from "
        for (tblname in tables)
            result += tblname + ", "
        result = result.substring(0, result.length - 2) //remove final comma
        val predstring = pred.toString()
        if (predstring != "")
            result += " where " + predstring
        return result
    }
}
