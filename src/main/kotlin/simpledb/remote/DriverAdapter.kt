package simpledb.remote

import java.sql.*
import java.util.*
import java.util.logging.Logger

/**
 * This class implements all of the methods of the Driver interface,
 * by throwing an exception for each one.
 * Subclasses (such as SimpleDriver) can override those methods that
 * it want to implement.
 * @author Edward Sciore
 */
abstract class DriverAdapter : Driver {
    @Throws(SQLException::class)
    override fun acceptsURL(url: String): Boolean {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun connect(url: String, info: Properties): Connection {
        throw SQLException("operation not implemented")
    }

    override fun getMajorVersion(): Int {
        return 0
    }

    override fun getMinorVersion(): Int {
        return 0
    }

    override fun getPropertyInfo(url: String, info: Properties): Array<DriverPropertyInfo>? {
        return null
    }

    override fun jdbcCompliant(): Boolean {
        return false
    }

    @Throws(SQLFeatureNotSupportedException::class)
    override fun getParentLogger(): Logger {
        throw SQLFeatureNotSupportedException("operation not implemented")
    }
}