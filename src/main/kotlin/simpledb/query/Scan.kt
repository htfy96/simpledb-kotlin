package simpledb.query

/**
 * The interface will be implemented by each query scan.
 * There is a Scan class for each relational
 * algebra operator.
 * @author Edward Sciore
 */
interface Scan {

    /**
     * Positions the scan before its first record.
     */
    fun beforeFirst()

    /**
     * Moves the scan to the next record.
     * @return false if there is no next record
     */
    operator fun next(): Boolean

    /**
     * Closes the scan and its subscans, if any.
     */
    fun close()

    /**
     * Returns the value of the specified field in the current record.
     * The value is expressed as a Constant.
     * @param fldname the name of the field
     * @return the value of that field, expressed as a Constant.
     */
    fun getVal(fldname: String): Constant

    /**
     * Returns the value of the specified integer field
     * in the current record.
     * @param fldname the name of the field
     * @return the field's integer value in the current record
     */
    fun getInt(fldname: String): Int

    /**
     * Returns the value of the specified string field
     * in the current record.
     * @param fldname the name of the field
     * @return the field's string value in the current record
     */
    fun getString(fldname: String): String

    /**
     * Returns true if the scan has the specified field.
     * @param fldname the name of the field
     * @return true if the scan has that field
     */
    fun hasField(fldname: String): Boolean
}
