package simpledb.remote

import java.sql.*
import java.rmi.registry.*
import java.util.Properties

/**
 * The SimpleDB database driver.
 * @author Edward Sciore
 */
class SimpleDriver : DriverAdapter() {

    /**
     * Connects to the SimpleDB server on the specified host.
     * The method retrieves the RemoteDriver stub from
     * the RMI registry on the specified host.
     * It then calls the connect method on that stub,
     * which in turn creates a new connection and
     * returns the RemoteConnection stub for it.
     * This stub is wrapped in a SimpleConnection object
     * and is returned.
     * <P>
     * The current implementation of this method ignores the
     * properties argument.
     * @see java.sql.Driver.connect
    </P> */
    @Throws(SQLException::class)
    override fun connect(url: String, info: Properties): Connection {
        try {
            val host = url.replace("jdbc:simpledb://", "")  //assumes no port specified
            val reg = LocateRegistry.getRegistry(host)
            val rdvr = reg.lookup("simpledb") as RemoteDriver
            val rconn = rdvr.connect()
            return SimpleConnection(rconn)
        } catch (e: Exception) {
            throw SQLException(e)
        }

    }
}
