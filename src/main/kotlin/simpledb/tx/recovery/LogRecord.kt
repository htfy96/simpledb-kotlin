package simpledb.tx.recovery

import simpledb.log.LogMgr
import simpledb.server.SimpleDB

/**
 * The interface implemented by each type of log record.
 * @author Edward Sciore
 */
interface LogRecord {

    /**
     * Writes the record to the log and returns its LSN.
     * @return the LSN of the record in the log
     */
    fun writeToLog(): Int

    /**
     * Returns the log record's type.
     * @return the log record's type
     */
    fun op(): Int

    /**
     * Returns the transaction id stored with
     * the log record.
     * @return the log record's transaction id
     */
    fun txNumber(): Int

    /**
     * Undoes the operation encoded by this log record.
     * The only log record types for which this method
     * does anything interesting are SETINT and SETSTRING.
     * @param txnum the id of the transaction that is performing the undo.
     */
    fun undo(txnum: Int)

    companion object {
        /**
         * The six different types of log record
         */
        val CHECKPOINT = 0
        val START = 1
        val COMMIT = 2
        val ROLLBACK = 3
        val SETINT = 4
        val SETSTRING = 5

        val logMgr = SimpleDB.logMgr()
    }
}