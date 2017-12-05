package simpledb.remote

import simpledb.record.Schema
import java.sql.Types.INTEGER
import java.rmi.RemoteException
import java.rmi.server.UnicastRemoteObject
import java.util.*

/**
 * The RMI server-side implementation of RemoteMetaData.
 * @author Edward Sciore
 */
class RemoteMetaDataImpl
/**
 * Creates a metadata object that wraps the specified schema.
 * The method also creates a list to hold the schema's
 * collection of field names,
 * so that the fields can be accessed by position.
 * @param sch the schema
 * @throws RemoteException
 */
@Throws(RemoteException::class)
constructor(private val sch: Schema) : UnicastRemoteObject(), RemoteMetaData {
    private val fields = ArrayList<String>()

    /**
     * Returns the size of the field list.
     * @see simpledb.remote.RemoteMetaData.getColumnCount
     */
    override val columnCount: Int
        @Throws(RemoteException::class)
        get() = fields.size

    init {
        fields.addAll(sch.fields())
    }

    /**
     * Returns the field name for the specified column number.
     * In JDBC, column numbers start with 1, so the field
     * is taken from position (column-1) in the list.
     * @see simpledb.remote.RemoteMetaData.getColumnName
     */
    @Throws(RemoteException::class)
    override fun getColumnName(column: Int): String {
        return fields[column - 1]
    }

    /**
     * Returns the type of the specified column.
     * The method first finds the name of the field in that column,
     * and then looks up its type in the schema.
     * @see simpledb.remote.RemoteMetaData.getColumnType
     */
    @Throws(RemoteException::class)
    override fun getColumnType(column: Int): Int {
        val fldname = getColumnName(column)
        return sch.type(fldname)
    }

    /**
     * Returns the number of characters required to display the
     * specified column.
     * For a string-type field, the method simply looks up the
     * field's length in the schema and returns that.
     * For an int-type field, the method needs to decide how
     * large integers can be.
     * Here, the method arbitrarily chooses 6 characters,
     * which means that integers over 999,999 will
     * probably get displayed improperly.
     * @see simpledb.remote.RemoteMetaData.getColumnDisplaySize
     */
    @Throws(RemoteException::class)
    override fun getColumnDisplaySize(column: Int): Int {
        val fldname = getColumnName(column)
        val fldtype = sch.type(fldname)
        val fldlength = sch.length(fldname)
        return if (fldtype == INTEGER)
            6  // accommodate 6-digit integers
        else
            fldlength
    }
}
