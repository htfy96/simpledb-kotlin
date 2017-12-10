package simpledb.remote

import simpledb.record.Schema
import simpledb.query.*
import java.rmi.RemoteException
import java.rmi.server.UnicastRemoteObject

/**
 * The RMI server-side implementation of RemoteResultSet.
 * @author Edward Sciore
 */
internal class RemoteResultSetImpl
/**
 * Creates a RemoteResultSet object.
 * The specified plan is opened, and the scan is saved.
 * @param plan the query plan
 * @param rconn TODO
 * @throws RemoteException
 */
@Throws(RemoteException::class)
constructor(plan: Plan, private val rconn: RemoteConnectionImpl) : UnicastRemoteObject(), RemoteResultSet {
    private val s: Scan
    private val sch: Schema

    /**
     * Returns the result set's metadata,
     * by passing its schema into the RemoteMetaData constructor.
     * @see simpledb.remote.RemoteResultSet.getMetaData
     */
    override fun getMetaData(): RemoteMetaData = RemoteMetaDataImpl(sch)


    init {
        s = plan.open()
        sch = plan.schema()
    }

    /**
     * Moves to the next record in the result set,
     * by moving to the next record in the saved scan.
     * @see simpledb.remote.RemoteResultSet.next
     */
    @Throws(RemoteException::class)
    override fun next(): Boolean {
        try {
            return s.next()
        } catch (e: RuntimeException) {
            rconn.rollback()
            throw e
        }

    }

    /**
     * Returns the integer value of the specified field,
     * by returning the corresponding value on the saved scan.
     * @see simpledb.remote.RemoteResultSet.getInt
     */
    @Throws(RemoteException::class)
    override fun getInt(fldname: String): Int {
        var fldname = fldname
        try {
            fldname = fldname.toLowerCase() // to ensure case-insensitivity
            return s.getInt(fldname)
        } catch (e: RuntimeException) {
            rconn.rollback()
            throw e
        }

    }

    /**
     * Returns the integer value of the specified field,
     * by returning the corresponding value on the saved scan.
     * @see simpledb.remote.RemoteResultSet.getInt
     */
    @Throws(RemoteException::class)
    override fun getString(fldname: String): String {
        var fldname = fldname
        try {
            fldname = fldname.toLowerCase() // to ensure case-insensitivity
            return s.getString(fldname)
        } catch (e: RuntimeException) {
            rconn.rollback()
            throw e
        }

    }

    /**
     * Closes the result set by closing its scan.
     * @see simpledb.remote.RemoteResultSet.close
     */
    @Throws(RemoteException::class)
    override fun close() {
        s.close()
        rconn.commit()
    }
}

