package simpledb.metadata

import simpledb.metadata.TableMgr.Companion.MAX_NAME
import simpledb.tx.Transaction
import simpledb.record.*
import java.util.*

/**
 * The index manager.
 * The index manager has similar functionalty to the table manager.
 * @author Edward Sciore
 */
class IndexMgr
/**
 * Creates the index manager.
 * This constructor is called during system startup.
 * If the database is new, then the *idxcat* table is created.
 * @param isnew indicates whether this is a new database
 * @param tx the system startup transaction
 */
(isnew: Boolean, tblmgr: TableMgr, tx: Transaction) {
    private val ti: TableInfo

    init {
        if (isnew) {
            val sch = Schema()
            sch.addStringField("indexname", MAX_NAME)
            sch.addStringField("tablename", MAX_NAME)
            sch.addStringField("fieldname", MAX_NAME)
            tblmgr.createTable("idxcat", sch, tx)
        }
        ti = tblmgr.getTableInfo("idxcat", tx)
    }

    /**
     * Creates an index of the specified type for the specified field.
     * A unique ID is assigned to this index, and its information
     * is stored in the idxcat table.
     * @param idxname the name of the index
     * @param tblname the name of the indexed table
     * @param fldname the name of the indexed field
     * @param tx the calling transaction
     */
    fun createIndex(idxname: String, tblname: String, fldname: String, tx: Transaction) {
        val rf = RecordFile(ti, tx)
        rf.insert()
        rf.setString("indexname", idxname)
        rf.setString("tablename", tblname)
        rf.setString("fieldname", fldname)
        rf.close()
    }

    /**
     * Returns a map containing the index info for all indexes
     * on the specified table.
     * @param tblname the name of the table
     * @param tx the calling transaction
     * @return a map of IndexInfo objects, keyed by their field names
     */
    fun getIndexInfo(tblname: String, tx: Transaction): Map<String, IndexInfo> {
        val result = HashMap<String, IndexInfo>()
        val rf = RecordFile(ti, tx)
        while (rf.next())
            if (rf.getString("tablename") == tblname) {
                val idxname = rf.getString("indexname")
                val fldname = rf.getString("fieldname")
                val ii = IndexInfo(idxname, tblname, fldname, tx)
                result.put(fldname, ii)
            }
        rf.close()
        return result
    }
}
