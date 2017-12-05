package simpledb.query

import simpledb.server.SimpleDB
import simpledb.tx.Transaction
import simpledb.metadata.*
import simpledb.record.*

/** The Plan class corresponding to a table.
 * @author Edward Sciore
 */
class TablePlan
/**
 * Creates a leaf node in the query tree corresponding
 * to the specified table.
 * @param tblname the name of the table
 * @param tx the calling transaction
 */
(tblname: String, private val tx: Transaction) : Plan {
    private val ti: TableInfo
    private val si: StatInfo

    init {
        ti = SimpleDB.mdMgr().getTableInfo(tblname, tx)
        si = SimpleDB.mdMgr().getStatInfo(tblname, ti, tx)
    }

    /**
     * Creates a table scan for this query.
     * @see simpledb.query.Plan.open
     */
    override fun open(): Scan {
        return TableScan(ti, tx)
    }

    /**
     * Estimates the number of block accesses for the table,
     * which is obtainable from the statistics manager.
     * @see simpledb.query.Plan.blocksAccessed
     */
    override fun blocksAccessed(): Int {
        return si.blocksAccessed()
    }

    /**
     * Estimates the number of records in the table,
     * which is obtainable from the statistics manager.
     * @see simpledb.query.Plan.recordsOutput
     */
    override fun recordsOutput(): Int {
        return si.recordsOutput()
    }

    /**
     * Estimates the number of distinct field values in the table,
     * which is obtainable from the statistics manager.
     * @see simpledb.query.Plan.distinctValues
     */
    override fun distinctValues(fldname: String): Int {
        return si.distinctValues(fldname)
    }

    /**
     * Determines the schema of the table,
     * which is obtainable from the catalog manager.
     * @see simpledb.query.Plan.schema
     */
    override fun schema(): Schema {
        return ti.schema()
    }
}
