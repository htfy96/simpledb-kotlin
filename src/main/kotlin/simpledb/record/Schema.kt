package simpledb.record

import java.sql.Types.*
import java.util.*

/**
 * The record schema of a table.
 * A schema contains the name and type of
 * each field of the table, as well as the length
 * of each varchar field.
 * @author Edward Sciore
 */
/**
 * Creates an empty schema.
 * Field information can be added to a schema
 * via the five addXXX methods.
 */
class Schema {
    private val info = HashMap<String, FieldInfo>()

    /**
     * Adds a field to the schema having a specified
     * name, type, and length.
     * If the field type is "integer", then the length
     * value is irrelevant.
     * @param fldname the name of the field
     * @param type the type of the field, according to the constants in simpledb.sql.types
     * @param length the conceptual length of a string field.
     */
    fun addField(fldname: String, type: Int, length: Int) {
        info.put(fldname, FieldInfo(type, length))
    }

    /**
     * Adds an integer field to the schema.
     * @param fldname the name of the field
     */
    fun addIntField(fldname: String) {
        addField(fldname, INTEGER, 0)
    }

    /**
     * Adds a string field to the schema.
     * The length is the conceptual length of the field.
     * For example, if the field is defined as varchar(8),
     * then its length is 8.
     * @param fldname the name of the field
     * @param length the number of chars in the varchar definition
     */
    fun addStringField(fldname: String, length: Int) {
        addField(fldname, VARCHAR, length)
    }

    /**
     * Adds a field to the schema having the same
     * type and length as the corresponding field
     * in another schema.
     * @param fldname the name of the field
     * @param sch the other schema
     */
    fun add(fldname: String, sch: Schema) {
        val type = sch.type(fldname)
        val length = sch.length(fldname)
        addField(fldname, type, length)
    }

    /**
     * Adds all of the fields in the specified schema
     * to the current schema.
     * @param sch the other schema
     */
    fun addAll(sch: Schema) {
        info.putAll(sch.info)
    }

    /**
     * Returns a collection containing the name of
     * each field in the schema.
     * @return the collection of the schema's field names
     */
    fun fields(): Collection<String> {
        return info.keys
    }

    /**
     * Returns true if the specified field
     * is in the schema
     * @param fldname the name of the field
     * @return true if the field is in the schema
     */
    fun hasField(fldname: String): Boolean {
        return fields().contains(fldname)
    }

    /**
     * Returns the type of the specified field, using the
     * constants in [java.sql.Types].
     * @param fldname the name of the field
     * @return the integer type of the field
     */
    fun type(fldname: String): Int {
        return info[fldname]!!.type
    }

    /**
     * Returns the conceptual length of the specified field.
     * If the field is not a string field, then
     * the return value is undefined.
     * @param fldname the name of the field
     * @return the conceptual length of the field
     */
    fun length(fldname: String): Int {
        return info[fldname]!!.length
    }

    internal inner class FieldInfo(var type: Int, var length: Int)
}
