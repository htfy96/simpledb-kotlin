package simpledb.index.query

import simpledb.tx.Transaction
import simpledb.record.Schema
import simpledb.metadata.IndexInfo
import simpledb.query.*
import simpledb.index.Index

/** The Plan class corresponding to the *indexselect*
 * relational algebra operator.
 * @author Edward Sciore
 */
class IndexSelectPlan
/**
 * Creates a new indexselect node in the query tree
 * for the specified index and selection constant.
 * @param p the input table
 * @param ii information about the index
 * @param val the selection constant
 * @param tx the calling transaction
 */
(private val p: Plan, private val ii: IndexInfo, private val `val`: Constant, tx: Transaction) : Plan {

    /**
     * Creates a new indexselect scan for this query
     * @see simpledb.query.Plan.open
     */
    override fun open(): Scan {
        // throws an exception if p is not a tableplan.
        val ts = p.open() as TableScan
        val idx = ii.open()
        return IndexSelectScan(idx, `val`, ts)
    }

    /**
     * Estimates the number of block accesses to compute the
     * index selection, which is the same as the
     * index traversal cost plus the number of matching data records.
     * @see simpledb.query.Plan.blocksAccessed
     */
    override fun blocksAccessed(): Int {
        return ii.blocksAccessed() + recordsOutput()
    }

    /**
     * Estimates the number of output records in the index selection,
     * which is the same as the number of search key values
     * for the index.
     * @see simpledb.query.Plan.recordsOutput
     */
    override fun recordsOutput(): Int {
        return ii.recordsOutput()
    }

    /**
     * Returns the distinct values as defined by the index.
     * @see simpledb.query.Plan.distinctValues
     */
    override fun distinctValues(fldname: String): Int {
        return ii.distinctValues(fldname)
    }

    /**
     * Returns the schema of the data table.
     * @see simpledb.query.Plan.schema
     */
    override fun schema(): Schema {
        return p.schema()
    }
}
