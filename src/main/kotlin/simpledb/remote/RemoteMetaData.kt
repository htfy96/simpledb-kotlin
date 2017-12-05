package simpledb.remote

import java.rmi.*

/**
 * The RMI remote interface corresponding to ResultSetMetaData.
 * The methods are identical to those of ResultSetMetaData,
 * except that they throw RemoteExceptions instead of SQLExceptions.
 * @author Edward Sciore
 */
interface RemoteMetaData : Remote {
    val columnCount: Int
    @Throws(RemoteException::class)
    fun getColumnName(column: Int): String

    @Throws(RemoteException::class)
    fun getColumnType(column: Int): Int

    @Throws(RemoteException::class)
    fun getColumnDisplaySize(column: Int): Int
}

