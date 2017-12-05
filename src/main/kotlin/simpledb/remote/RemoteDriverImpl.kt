package simpledb.remote

import java.rmi.RemoteException
import java.rmi.server.UnicastRemoteObject

/**
 * The RMI server-side implementation of RemoteDriver.
 * @author Edward Sciore
 */
class RemoteDriverImpl @Throws(RemoteException::class)
constructor() : UnicastRemoteObject(), RemoteDriver {

    /**
     * Creates a new RemoteConnectionImpl object and
     * returns it.
     * @see simpledb.remote.RemoteDriver.connect
     */
    @Throws(RemoteException::class)
    override fun connect(): RemoteConnection {
        return RemoteConnectionImpl()
    }
}

