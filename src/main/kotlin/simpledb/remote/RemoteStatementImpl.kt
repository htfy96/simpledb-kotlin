package simpledb.remote

import simpledb.tx.Transaction
import simpledb.query.Plan
import simpledb.server.SimpleDB
import java.rmi.RemoteException
import java.rmi.server.UnicastRemoteObject

/**
 * The RMI server-side implementation of RemoteStatement.
 * @author Edward Sciore
 */
internal class RemoteStatementImpl @Throws(RemoteException::class)
constructor(private val rconn: RemoteConnectionImpl) : UnicastRemoteObject(), RemoteStatement {

    /**
     * Executes the specified SQL query string.
     * The method calls the query planner to create a plan
     * for the query. It then sends the plan to the
     * RemoteResultSetImpl constructor for processing.
     * @see simpledb.remote.RemoteStatement.executeQuery
     */
    @Throws(RemoteException::class)
    override fun executeQuery(qry: String): RemoteResultSet {
        try {
            val tx = rconn.transaction
            val pln = SimpleDB.planner().createQueryPlan(qry, tx!!)
            return RemoteResultSetImpl(pln, rconn)
        } catch (e: RuntimeException) {
            rconn.rollback()
            throw e
        }

    }

    /**
     * Executes the specified SQL update command.
     * The method sends the command to the update planner,
     * which executes it.
     * @see simpledb.remote.RemoteStatement.executeUpdate
     */
    @Throws(RemoteException::class)
    override fun executeUpdate(cmd: String): Int {
        try {
            val tx = rconn.transaction
            val result = SimpleDB.planner().executeUpdate(cmd, tx!!)
            rconn.commit()
            return result
        } catch (e: RuntimeException) {
            rconn.rollback()
            throw e
        }

    }
}
