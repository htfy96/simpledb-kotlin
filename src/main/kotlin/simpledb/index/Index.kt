package simpledb.index

import simpledb.record.RID
import simpledb.query.Constant

/**
 * This interface contains methods to traverse an index.
 * @author Edward Sciore
 */
interface Index {

    /**
     * Returns the dataRID value stored in the current index record.
     * @return the dataRID stored in the current index record.
     */
    val dataRid: RID

    /**
     * Positions the index before the first record
     * having the specified search key.
     * @param searchkey the search key value.
     */
    fun beforeFirst(searchkey: Constant)

    /**
     * Moves the index to the next record having the
     * search key specified in the beforeFirst method.
     * Returns false if there are no more such index records.
     * @return false if no other index records have the search key.
     */
    operator fun next(): Boolean

    /**
     * Inserts an index record having the specified
     * dataval and dataRID values.
     * @param dataval the dataval in the new index record.
     * @param datarid the dataRID in the new index record.
     */
    fun insert(dataval: Constant, datarid: RID)

    /**
     * Deletes the index record having the specified
     * dataval and dataRID values.
     * @param dataval the dataval of the deleted index record
     * @param datarid the dataRID of the deleted index record
     */
    fun delete(dataval: Constant, datarid: RID)

    /**
     * Closes the index.
     */
    fun close()
}
