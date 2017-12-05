package simpledb.remote

import java.sql.*

/**
 * An adapter class that wraps RemoteConnection.
 * Its methods do nothing except transform RemoteExceptions
 * into SQLExceptions.
 * @author Edward Sciore
 */
class SimpleConnection(private val rconn: RemoteConnection) : ConnectionAdapter() {

    @Throws(SQLException::class)
    override fun createStatement(): Statement {
        try {
            val rstmt = rconn.createStatement()
            return SimpleStatement(rstmt)
        } catch (e: Exception) {
            throw SQLException(e)
        }

    }

    @Throws(SQLException::class)
    override fun close() {
        try {
            rconn.close()
        } catch (e: Exception) {
            throw SQLException(e)
        }

    }
}

