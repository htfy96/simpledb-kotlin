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
open class TableScanBase
/**
 * Creates a new table scan,
 * and opens its corresponding record file.
 * @param ti the table's metadata
 * @param tx the calling transaction
 */
(ti: TableInfo, tx: Transaction) : Scan {
    val rf: RecordFile = RecordFile(ti, tx)
    val sch: Schema = ti.schema()

    // Scan methods

    override fun beforeFirst() {
        rf.beforeFirst()
    }

    override fun next(): Boolean {
        return rf.next()
    }

    override fun close() {
        rf.close()
    }

    override fun getVal(fldname: String): Constant {
        return if (sch.type(fldname) == Types.INTEGER)
            IntConstant(rf.nonBlockingGetInt(fldname))
        else
            StringConstant(rf.nonBlockingGetString(fldname))
    }

    override fun getInt(fldname: String): Int {
        return rf.nonBlockingGetInt(fldname)
    }

    override fun getString(fldname: String): String {
        return rf.nonBlockingGetString(fldname)
    }

    override fun hasField(fldname: String): Boolean {
        return sch.hasField(fldname)
    }
}
