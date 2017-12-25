package simpledb.query

import simpledb.record.RecordFile
import simpledb.record.Schema
import simpledb.record.TableInfo
import simpledb.tx.Transaction
import java.sql.Types


/**
 * The Scan class corresponding to a table.
 * A table scan is just a wrapper for a RecordFile object;
 * most methods just delegate to the corresponding
 * RecordFile methods.
 * @author Zheng Luo
 */
class NonBlockingTableScan
/**
 * Creates a new table scan,
 * and opens its corresponding record file.
 * @param ti the table's metadata
 * @param tx the calling transaction
 */
(ti: TableInfo, tx: Transaction) : TableScanBase(ti, tx) {
}
