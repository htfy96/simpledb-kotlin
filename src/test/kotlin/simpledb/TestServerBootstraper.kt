package simpledb

import simpledb.remote.RemoteDriverImpl
import simpledb.server.SimpleDB
import java.rmi.registry.LocateRegistry
import java.rmi.registry.Registry
import java.rmi.server.UnicastRemoteObject

object TestServerBootstraper {
    var reg: Registry? = null
    fun startupDBServer() {
        SimpleDB.init("studentdb-test")
        reg = LocateRegistry.createRegistry(1099)

        // and post the server entry in it
        val d = RemoteDriverImpl()
        reg!!.rebind("simpledb", d)

        println("database server ready")
    }

    fun shutdownDBServer() {
        UnicastRemoteObject.unexportObject(reg, true)
    }
}