package simpledb.index.hash

import simpledb.tx.Transaction
import simpledb.record.*
import simpledb.query.*
import simpledb.index.Index

/**
 * A static hash implementation of the Index interface.
 * A fixed number of buckets is allocated (currently, 100),
 * and each bucket is implemented as a file of index records.
 * @author Edward Sciore
 */
class HashIndex
/**
 * Opens a hash index for the specified index.
 * @param idxname the name of the index
 * @param sch the schema of the index records
 * @param tx the calling transaction
 */
(private val idxname: String, private val sch: Schema, private val tx: Transaction) : Index {
    private var searchkey: Constant? = null
    private var ts: TableScan? = null

    /**
     * Retrieves the dataRID from the current record
     * in the table scan for the bucket.
     * @see simpledb.index.Index.getDataRid
     */
    override val dataRid: RID
        get() {
            val blknum = ts!!.getInt("block")
            val id = ts!!.getInt("id")
            return RID(blknum, id)
        }

    /**
     * Positions the index before the first index record
     * having the specified search key.
     * The method hashes the search key to determine the bucket,
     * and then opens a table scan on the file
     * corresponding to the bucket.
     * The table scan for the previous bucket (if any) is closed.
     * @see simpledb.index.Index.beforeFirst
     */
    override fun beforeFirst(searchkey: Constant) {
        close()
        this.searchkey = searchkey
        val bucket = searchkey.hashCode() % NUM_BUCKETS
        val tblname = idxname + bucket
        val ti = TableInfo(tblname, sch)
        ts = TableScan(ti, tx)
    }

    /**
     * Moves to the next record having the search key.
     * The method loops through the table scan for the bucket,
     * looking for a matching record, and returning false
     * if there are no more such records.
     * @see simpledb.index.Index.next
     */
    override fun next(): Boolean {
        while (ts!!.next())
            if (ts!!.getVal("dataval") == searchkey)
                return true
        return false
    }

    /**
     * Inserts a new record into the table scan for the bucket.
     * @see simpledb.index.Index.insert
     */
    override fun insert(dataval: Constant, datarid: RID) {
        beforeFirst(dataval)
        ts!!.insert()
        ts!!.setInt("block", datarid.blockNumber())
        ts!!.setInt("id", datarid.id())
        ts!!.setVal("dataval", dataval)
    }

    /**
     * Deletes the specified record from the table scan for
     * the bucket.  The method starts at the beginning of the
     * scan, and loops through the records until the
     * specified record is found.
     * @see simpledb.index.Index.delete
     */
    override fun delete(dataval: Constant, datarid: RID) {
        beforeFirst(dataval)
        while (next())
            if (dataRid == datarid) {
                ts!!.delete()
                return
            }
    }

    /**
     * Closes the index by closing the current table scan.
     * @see simpledb.index.Index.close
     */
    override fun close() {
        if (ts != null)
            ts!!.close()
    }

    companion object {
        var NUM_BUCKETS = 100

        /**
         * Returns the cost of searching an index file having the
         * specified number of blocks.
         * The method assumes that all buckets are about the
         * same size, and so the cost is simply the size of
         * the bucket.
         * @param numblocks the number of blocks of index records
         * @param rpb the number of records per block (not used here)
         * @return the cost of traversing the index
         */
        fun searchCost(numblocks: Int,
                       @Suppress("UNUSED_PARAMETER")
                       rpb: Int): Int {
            return numblocks / HashIndex.NUM_BUCKETS
        }
    }
}
