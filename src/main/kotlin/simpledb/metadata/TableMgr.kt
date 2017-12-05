package simpledb.metadata

import simpledb.tx.Transaction
import simpledb.record.*
import java.util.*

/**
 * The table manager.
 * There are methods to create a table, save the metadata
 * in the catalog, and obtain the metadata of a
 * previously-created table.
 * @author Edward Sciore
 */
class TableMgr
/**
 * Creates a new catalog manager for the database system.
 * If the database is new, then the two catalog tables
 * are created.
 * @param isNew has the value true if the database is new
 * @param tx the startup transaction
 */
(isNew: Boolean, tx: Transaction) {

    private val tcatInfo: TableInfo
    private val fcatInfo: TableInfo

    init {
        val tcatSchema = Schema()
        tcatSchema.addStringField("tblname", MAX_NAME)
        tcatSchema.addIntField("reclength")
        tcatInfo = TableInfo("tblcat", tcatSchema)

        val fcatSchema = Schema()
        fcatSchema.addStringField("tblname", MAX_NAME)
        fcatSchema.addStringField("fldname", MAX_NAME)
        fcatSchema.addIntField("type")
        fcatSchema.addIntField("length")
        fcatSchema.addIntField("offset")
        fcatInfo = TableInfo("fldcat", fcatSchema)

        if (isNew) {
            createTable("tblcat", tcatSchema, tx)
            createTable("fldcat", fcatSchema, tx)
        }
    }

    /**
     * Creates a new table having the specified name and schema.
     * @param tblname the name of the new table
     * @param sch the table's schema
     * @param tx the transaction creating the table
     */
    fun createTable(tblname: String, sch: Schema, tx: Transaction) {
        val ti = TableInfo(tblname, sch)
        // insert one record into tblcat
        val tcatfile = RecordFile(tcatInfo, tx)
        tcatfile.insert()
        tcatfile.setString("tblname", tblname)
        tcatfile.setInt("reclength", ti.recordLength())
        tcatfile.close()

        // insert a record into fldcat for each field
        val fcatfile = RecordFile(fcatInfo, tx)
        for (fldname in sch.fields()) {
            fcatfile.insert()
            fcatfile.setString("tblname", tblname)
            fcatfile.setString("fldname", fldname)
            fcatfile.setInt("type", sch.type(fldname))
            fcatfile.setInt("length", sch.length(fldname))
            fcatfile.setInt("offset", ti.offset(fldname))
        }
        fcatfile.close()
    }

    /**
     * Retrieves the metadata for the specified table
     * out of the catalog.
     * @param tblname the name of the table
     * @param tx the transaction
     * @return the table's stored metadata
     */
    fun getTableInfo(tblname: String, tx: Transaction): TableInfo {
        val tcatfile = RecordFile(tcatInfo, tx)
        var reclen = -1
        while (tcatfile.next())
            if (tcatfile.getString("tblname") == tblname) {
                reclen = tcatfile.getInt("reclength")
                break
            }
        tcatfile.close()

        val fcatfile = RecordFile(fcatInfo, tx)
        val sch = Schema()
        val offsets = HashMap<String, Int>()
        while (fcatfile.next())
            if (fcatfile.getString("tblname") == tblname) {
                val fldname = fcatfile.getString("fldname")
                val fldtype = fcatfile.getInt("type")
                val fldlen = fcatfile.getInt("length")
                val offset = fcatfile.getInt("offset")
                offsets.put(fldname, offset)
                sch.addField(fldname, fldtype, fldlen)
            }
        fcatfile.close()
        return TableInfo(tblname, sch, offsets, reclen)
    }

    companion object {
        /**
         * The maximum number of characters in any
         * tablename or fieldname.
         * Currently, this value is 16.
         */
        val MAX_NAME = 16
    }
}