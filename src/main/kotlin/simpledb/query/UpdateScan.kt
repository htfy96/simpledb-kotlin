package simpledb.query

import simpledb.record.RID

/**
 * The interface implemented by all updateable scans.
 * @author Edward Sciore
 */
interface UpdateScan : Scan {

    /**
     * Returns the RID of the current record.
     * @return the RID of the current record
     */
    val rid: RID

    /**
     * Modifies the field value of the current record.
     * @param fldname the name of the field
     * @param val the new value, expressed as a Constant
     */
    fun setVal(fldname: String, `val`: Constant)

    /**
     * Modifies the field value of the current record.
     * @param fldname the name of the field
     * @param val the new integer value
     */
    fun setInt(fldname: String, `val`: Int)

    /**
     * Modifies the field value of the current record.
     * @param fldname the name of the field
     * @param val the new string value
     */
    fun setString(fldname: String, `val`: String)

    /**
     * Inserts a new record somewhere in the scan.
     */
    fun insert()

    /**
     * Deletes the current record from the scan.
     */
    fun delete()

    /**
     * Positions the scan so that the current record has
     * the specified RID.
     * @param rid the RID of the desired record
     */
    fun moveToRid(rid: RID)
}
