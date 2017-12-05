package simpledb.remote

import java.rmi.*

/**
 * The RMI remote interface corresponding to Connection.
 * The methods are identical to those of Connection,
 * except that they throw RemoteExceptions instead of SQLExceptions.
 * @author Edward Sciore
 */
interface RemoteConnection : Remote {
    @Throws(RemoteException::class)
    fun createStatement(): RemoteStatement

    @Throws(RemoteException::class)
    fun close()
}

