package simpledb.record

import java.sql.Types.INTEGER
import simpledb.file.Page.*
import simpledb.file.Page.Companion.INT_SIZE
import simpledb.file.Page.Companion.STR_SIZE
import java.util.*

/**
 * The metadata about a table and its records.
 * @author Edward Sciore
 */
class TableInfo {
    private var schema: Schema? = null
    private var offsets: MutableMap<String, Int>? = null
    private var recordlen: Int = 0
    private var tblname: String? = null

    /**
     * Creates a TableInfo object, given a table name
     * and schema. The constructor calculates the
     * physical offset of each field.
     * This constructor is used when a table is created.
     * @param tblname the name of the table
     * @param schema the schema of the table's records
     */
    constructor(tblname: String, schema: Schema) {
        this.schema = schema
        this.tblname = tblname
        offsets = HashMap()
        var pos = 0
        for (fldname in schema.fields()) {
            offsets!!.put(fldname, pos)
            pos += lengthInBytes(fldname)
        }
        recordlen = pos
    }

    /**
     * Creates a TableInfo object from the
     * specified metadata.
     * This constructor is used when the metadata
     * is retrieved from the catalog.
     * @param tblname the name of the table
     * @param schema the schema of the table's records
     * @param offsets the already-calculated offsets of the fields within a record
     * @param recordlen the already-calculated length of each record
     */
    constructor(tblname: String, schema: Schema, offsets: MutableMap<String, Int>, recordlen: Int) {
        this.tblname = tblname
        this.schema = schema
        this.offsets = offsets
        this.recordlen = recordlen
    }

    /**
     * Returns the filename assigned to this table.
     * Currently, the filename is the table name
     * followed by ".tbl".
     * @return the name of the file assigned to the table
     */
    fun fileName(): String {
        return tblname!! + ".tbl"
    }

    /**
     * Returns the schema of the table's records
     * @return the table's record schema
     */
    fun schema(): Schema {
        return schema!!
    }

    /**
     * Returns the offset of a specified field within a record
     * @param fldname the name of the field
     * @return the offset of that field within a record
     */
    fun offset(fldname: String): Int {
        return offsets!![fldname]!!
    }

    /**
     * Returns the length of a record, in bytes.
     * @return the length in bytes of a record
     */
    fun recordLength(): Int {
        return recordlen
    }

    private fun lengthInBytes(fldname: String): Int {
        val fldtype = schema!!.type(fldname)
        return if (fldtype == INTEGER)
            INT_SIZE
        else
            STR_SIZE(schema!!.length(fldname))
    }
}