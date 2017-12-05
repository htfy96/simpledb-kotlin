package simpledb.remote

import java.rmi.*

/**
 * The RMI remote interface corresponding to Statement.
 * The methods are identical to those of Statement,
 * except that they throw RemoteExceptions instead of SQLExceptions.
 * @author Edward Sciore
 */
interface RemoteStatement : Remote {
    @Throws(RemoteException::class)
    fun executeQuery(qry: String): RemoteResultSet

    @Throws(RemoteException::class)
    fun executeUpdate(cmd: String): Int
}

