package simpledb.remote

import java.sql.*

/**
 * An adapter class that wraps RemoteResultSet.
 * Its methods do nothing except transform RemoteExceptions
 * into SQLExceptions.
 * @author Edward Sciore
 */
class SimpleResultSet(private val rrs: RemoteResultSet) : ResultSetAdapter() {

    @Throws(SQLException::class)
    override fun next(): Boolean {
        try {
            return rrs.next()
        } catch (e: Exception) {
            throw SQLException(e)
        }

    }

    @Throws(SQLException::class)
    override fun getInt(fldname: String): Int {
        try {
            return rrs.getInt(fldname)
        } catch (e: Exception) {
            throw SQLException(e)
        }

    }

    @Throws(SQLException::class)
    override fun getString(fldname: String): String {
        try {
            return rrs.getString(fldname)
        } catch (e: Exception) {
            throw SQLException(e)
        }

    }

    @Throws(SQLException::class)
    override fun getMetaData(): ResultSetMetaData {
        try {
            val rmd = rrs.getMetaData()
            return SimpleMetaData(rmd)
        } catch (e: Exception) {
            throw SQLException(e)
        }

    }

    @Throws(SQLException::class)
    override fun close() {
        try {
            rrs.close()
        } catch (e: Exception) {
            throw SQLException(e)
        }

    }
}

