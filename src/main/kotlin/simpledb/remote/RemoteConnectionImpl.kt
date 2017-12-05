package simpledb.remote

import simpledb.tx.Transaction
import java.rmi.RemoteException
import java.rmi.server.UnicastRemoteObject

/**
 * The RMI server-side implementation of RemoteConnection.
 * @author Edward Sciore
 */
internal class RemoteConnectionImpl
/**
 * Creates a remote connection
 * and begins a new transaction for it.
 * @throws RemoteException
 */
@Throws(RemoteException::class)
constructor() : UnicastRemoteObject(), RemoteConnection {
    // The following methods are used by the server-side classes.

    /**
     * Returns the transaction currently associated with
     * this connection.
     * @return the transaction associated with this connection
     */
    var transaction: Transaction? = null
        private set

    init {
        transaction = Transaction()
    }

    /**
     * Creates a new RemoteStatement for this connection.
     * @see simpledb.remote.RemoteConnection.createStatement
     */
    @Throws(RemoteException::class)
    override fun createStatement(): RemoteStatement {
        return RemoteStatementImpl(this)
    }

    /**
     * Closes the connection.
     * The current transaction is committed.
     * @see simpledb.remote.RemoteConnection.close
     */
    @Throws(RemoteException::class)
    override fun close() {
        transaction!!.commit()
    }

    /**
     * Commits the current transaction,
     * and begins a new one.
     */
    fun commit() {
        transaction!!.commit()
        transaction = Transaction()
    }

    /**
     * Rolls back the current transaction,
     * and begins a new one.
     */
    fun rollback() {
        transaction!!.rollback()
        transaction = Transaction()
    }
}

