package simpledb.remote

import java.sql.*

/**
 * An adapter class that wraps RemoteStatement.
 * Its methods do nothing except transform RemoteExceptions
 * into SQLExceptions.
 * @author Edward Sciore
 */
class SimpleStatement(private val rstmt: RemoteStatement) : StatementAdapter() {

    @Throws(SQLException::class)
    override fun executeQuery(sql: String): ResultSet {
        try {
            val rrs = rstmt.executeQuery(sql)
            return SimpleResultSet(rrs)
        } catch (e: Exception) {
            throw SQLException(e)
        }

    }

    @Throws(SQLException::class)
    override fun executeUpdate(sql: String): Int {
        try {
            return rstmt.executeUpdate(sql)
        } catch (e: Exception) {
            throw SQLException(e)
        }

    }
}

