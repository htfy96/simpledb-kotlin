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
    override fun executeQuery(qry: String): ResultSet {
        try {
            val rrs = rstmt.executeQuery(qry)
            return SimpleResultSet(rrs)
        } catch (e: Exception) {
            throw SQLException(e)
        }

    }

    @Throws(SQLException::class)
    override fun executeUpdate(cmd: String): Int {
        try {
            return rstmt.executeUpdate(cmd)
        } catch (e: Exception) {
            throw SQLException(e)
        }

    }
}

