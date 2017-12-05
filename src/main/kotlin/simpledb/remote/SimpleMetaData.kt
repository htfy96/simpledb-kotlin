package simpledb.remote

import java.sql.*

/**
 * An adapter class that wraps RemoteMetaData.
 * Its methods do nothing except transform RemoteExceptions
 * into SQLExceptions.
 * @author Edward Sciore
 */
class SimpleMetaData(private val rmd: RemoteMetaData) : ResultSetMetaDataAdapter() {

    @Throws(SQLException::class)
    override fun getColumnCount(): Int {
        try {
            return rmd.columnCount
        } catch (e: Exception) {
            throw SQLException(e)
        }

    }

    @Throws(SQLException::class)
    override fun getColumnName(column: Int): String {
        try {
            return rmd.getColumnName(column)
        } catch (e: Exception) {
            throw SQLException(e)
        }

    }

    @Throws(SQLException::class)
    override fun getColumnType(column: Int): Int {
        try {
            return rmd.getColumnType(column)
        } catch (e: Exception) {
            throw SQLException(e)
        }

    }

    @Throws(SQLException::class)
    override fun getColumnDisplaySize(column: Int): Int {
        try {
            return rmd.getColumnDisplaySize(column)
        } catch (e: Exception) {
            throw SQLException(e)
        }

    }
}

