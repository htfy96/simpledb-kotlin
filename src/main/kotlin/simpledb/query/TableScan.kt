package simpledb.query

import java.sql.Types.INTEGER
import simpledb.tx.Transaction
import simpledb.record.*

/**
 * The Scan class corresponding to a table.
 * A table scan is just a wrapper for a RecordFile object;
 * most methods just delegate to the corresponding
 * RecordFile methods.
 * @author Edward Sciore
 */
class TableScan
/**
 * Creates a new table scan,
 * and opens its corresponding record file.
 * @param ti the table's metadata
 * @param tx the calling transaction
 */
(ti: TableInfo, tx: Transaction) : TableScanBase(ti, tx), UpdateScan {
    override val rid: RID
        get() = rf.currentRid()

    // UpdateScan methods

    /**
     * Sets the value of the specified field, as a Constant.
     * The schema is examined to determine the field's type.
     * If INTEGER, then the record file's setInt method is called;
     * otherwise, the setString method is called.
     * @see simpledb.query.UpdateScan.setVal
     */
    override fun setVal(fldname: String, `val`: Constant) {
        if (sch.type(fldname) == INTEGER)
            rf.setInt(fldname, `val`.asJavaVal() as Int)
        else
            rf.setString(fldname, `val`.asJavaVal() as String)
    }

    override fun setInt(fldname: String, `val`: Int) {
        rf.setInt(fldname, `val`)
    }

    override fun setString(fldname: String, `val`: String) {
        rf.setString(fldname, `val`)
    }

    override fun delete() {
        rf.delete()
    }

    override fun insert() {
        rf.insert()
    }

    override fun moveToRid(rid: RID) {
        rf.moveToRid(rid)
    }
}
