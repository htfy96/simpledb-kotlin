package simpledb.query

import simpledb.tx.Transaction

/** The Plan class corresponding to a table.
 * @author Edward Sciore
 */
class NonblockingTablePlan
/**
 * Creates a leaf node in the query tree corresponding
 * to the specified table.
 * @param tblname the name of the table
 * @param tx the calling transaction
 */
(tblname: String, private val tx: Transaction) : Plan, TablePlanBase(tblname, tx) {
    /**
     * Creates a table scan for this query.
     * @see simpledb.query.Plan.open
     */
    override fun open(): Scan {
        return NonBlockingTableScan(ti, tx)
    }
}
