package simpledb.metadata

import java.sql.Types.INTEGER
import simpledb.file.Page.Companion.BLOCK_SIZE
import simpledb.server.SimpleDB
import simpledb.tx.Transaction
import simpledb.record.*
import simpledb.index.Index
import simpledb.index.hash.HashIndex
import simpledb.index.btree.BTreeIndex //in case we change to btree indexing


/**
 * The information about an index.
 * This information is used by the query planner in order to
 * estimate the costs of using the index,
 * and to obtain the schema of the index records.
 * Its methods are essentially the same as those of Plan.
 * @author Edward Sciore
 */
class IndexInfo
/**
 * Creates an IndexInfo object for the specified index.
 * @param idxname the name of the index
 * @param tblname the name of the table
 * @param fldname the name of the indexed field
 * @param tx the calling transaction
 */
(private val idxname: String, tblname: String, private val fldname: String,
 private val tx: Transaction) {
    private val ti: TableInfo
    private val si: StatInfo

    init {
        ti = SimpleDB.mdMgr().getTableInfo(tblname, tx)
        si = SimpleDB.mdMgr().getStatInfo(tblname, ti, tx)
    }

    /**
     * Opens the index described by this object.
     * @return the Index object associated with this information
     */
    fun open(): Index {
        val sch = schema()
        // Create new HashIndex for hash indexing
        return HashIndex(idxname, sch, tx)
    }

    /**
     * Estimates the number of block accesses required to
     * find all index records having a particular search key.
     * The method uses the table's metadata to estimate the
     * size of the index file and the number of index records
     * per block.
     * It then passes this information to the traversalCost
     * method of the appropriate index type,
     * which provides the estimate.
     * @return the number of block accesses required to traverse the index
     */
    fun blocksAccessed(): Int {
        val idxti = TableInfo("", schema())
        val rpb = BLOCK_SIZE / idxti.recordLength()
        val numblocks = si.recordsOutput() / rpb
        // Call HashIndex.searchCost for hash indexing
        return HashIndex.searchCost(numblocks, rpb)
    }

    /**
     * Returns the estimated number of records having a
     * search key.  This value is the same as doing a select
     * query; that is, it is the number of records in the table
     * divided by the number of distinct values of the indexed field.
     * @return the estimated number of records having a search key
     */
    fun recordsOutput(): Int {
        return si.recordsOutput() / si.distinctValues(fldname)
    }

    /**
     * Returns the distinct values for a specified field
     * in the underlying table, or 1 for the indexed field.
     * @param fname the specified field
     */
    fun distinctValues(fname: String): Int {
        return if (fldname == fname)
            1
        else
            Math.min(si.distinctValues(fldname), recordsOutput())
    }

    /**
     * Returns the schema of the index records.
     * The schema consists of the dataRID (which is
     * represented as two integers, the block number and the
     * record ID) and the dataval (which is the indexed field).
     * Schema information about the indexed field is obtained
     * via the table's metadata.
     * @return the schema of the index records
     */
    private fun schema(): Schema {
        val sch = Schema()
        sch.addIntField("block")
        sch.addIntField("id")
        if (ti.schema().type(fldname) == INTEGER)
            sch.addIntField("dataval")
        else {
            val fldlen = ti.schema().length(fldname)
            sch.addStringField("dataval", fldlen)
        }
        return sch
    }
}
