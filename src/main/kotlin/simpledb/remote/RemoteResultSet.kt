package simpledb.remote

import java.rmi.*

/**
 * The RMI remote interface corresponding to ResultSet.
 * The methods are identical to those of ResultSet,
 * except that they throw RemoteExceptions instead of SQLExceptions.
 * @author Edward Sciore
 */
interface RemoteResultSet : Remote {


    @Throws(RemoteException::class)
    fun getMetaData(): RemoteMetaData

    @Throws(RemoteException::class)
    operator fun next(): Boolean

    @Throws(RemoteException::class)
    fun getInt(fldname: String): Int

    @Throws(RemoteException::class)
    fun getString(fldname: String): String

    @Throws(RemoteException::class)
    fun close()

}

