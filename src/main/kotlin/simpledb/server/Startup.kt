package simpledb.server

import simpledb.remote.*
import java.rmi.registry.*

object Startup {
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        // configure and initialize the database
        SimpleDB.init(args[0])

        // create a registry specific for the server on the default port
        val reg = LocateRegistry.createRegistry(1099)

        // and post the server entry in it
        val d = RemoteDriverImpl()
        reg.rebind("simpledb", d)

        println("database server ready")
    }
}
