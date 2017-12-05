package simpledb.parse

/**
 * The parser for the *create index* statement.
 * @author Edward Sciore
 */
class CreateIndexData
/**
 * Saves the table and field names of the specified index.
 */
(private val idxname: String, private val tblname: String, private val fldname: String) {

    /**
     * Returns the name of the index.
     * @return the name of the index
     */
    fun indexName(): String {
        return idxname
    }

    /**
     * Returns the name of the indexed table.
     * @return the name of the indexed table
     */
    fun tableName(): String {
        return tblname
    }

    /**
     * Returns the name of the indexed field.
     * @return the name of the indexed field
     */
    fun fieldName(): String {
        return fldname
    }
}

