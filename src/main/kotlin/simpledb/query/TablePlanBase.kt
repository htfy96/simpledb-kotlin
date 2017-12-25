package simpledb.query

import simpledb.metadata.StatInfo
import simpledb.record.Schema
import simpledb.record.TableInfo
import simpledb.server.SimpleDB
import simpledb.tx.Transaction

/** The Plan class corresponding to a table.
 * @author Edward Sciore
 */
open class TablePlanBase
/**
 * Creates a leaf node in the query tree corresponding
 * to the specified table.
 * @param tblname the name of the table
 * @param tx the calling transaction
 */
(tblname: String, private val tx: Transaction) {
    val ti: TableInfo
    private val si: StatInfo

    init {
        ti = SimpleDB.mdMgr().getTableInfo(tblname, tx)
        si = SimpleDB.mdMgr().getStatInfo(tblname, ti, tx)
    }

    /**
     * Estimates the number of block accesses for the table,
     * which is obtainable from the statistics manager.
     * @see simpledb.query.Plan.blocksAccessed
     */
    fun blocksAccessed(): Int {
        return si.blocksAccessed()
    }

    /**
     * Estimates the number of records in the table,
     * which is obtainable from the statistics manager.
     * @see simpledb.query.Plan.recordsOutput
     */
    fun recordsOutput(): Int {
        return si.recordsOutput()
    }

    /**
     * Estimates the number of distinct field values in the table,
     * which is obtainable from the statistics manager.
     * @see simpledb.query.Plan.distinctValues
     */
    fun distinctValues(fldname: String): Int {
        return si.distinctValues(fldname)
    }

    /**
     * Determines the schema of the table,
     * which is obtainable from the catalog manager.
     * @see simpledb.query.Plan.schema
     */
    fun schema(): Schema {
        return ti.schema()
    }
}
