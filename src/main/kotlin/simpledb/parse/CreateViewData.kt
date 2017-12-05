package simpledb.parse

/**
 * Data for the SQL *create view* statement.
 * @author Edward Sciore
 */
class CreateViewData
/**
 * Saves the view name and its definition.
 */
(private val viewname: String, private val qrydata: QueryData) {

    /**
     * Returns the name of the new view.
     * @return the name of the new view
     */
    fun viewName(): String {
        return viewname
    }

    /**
     * Returns the definition of the new view.
     * @return the definition of the new view
     */
    fun viewDef(): String {
        return qrydata.toString()
    }
}
