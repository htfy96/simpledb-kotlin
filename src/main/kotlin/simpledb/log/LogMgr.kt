package simpledb.log

import simpledb.server.SimpleDB
import simpledb.file.*
import simpledb.file.Page.*
import java.util.*

/**
 * The low-level log manager.
 * This log manager is responsible for writing log records
 * into a log file.
 * A log record can be any sequence of integer and string values.
 * The log manager does not understand the meaning of these
 * values, which are written and read by the
 * [recovery manager][simpledb.tx.recovery.RecoveryMgr].
 * @author Edward Sciore
 */
class LogMgr
/**
 * Creates the manager for the specified log file.
 * If the log file does not yet exist, it is created
 * with an empty first block.
 * This constructor depends on a [FileMgr] object
 * that it gets from the method
 * [simpledb.server.SimpleDB.fileMgr].
 * That object is created during system initialization.
 * Thus this constructor cannot be called until
 * [simpledb.server.SimpleDB.initFileMgr]
 * is called first.
 * @param logfile the name of the log file
 */
(private val logfile: String) : Iterable<BasicLogRecord> {
    private val mypage = Page()
    private var currentblk: Block? = null
    private var currentpos: Int = 0

    private var lastRecordPosition: Int
        get() = mypage.getInt(LAST_POS)
        set(pos) = mypage.setInt(LAST_POS, pos)

    init {
        val logsize = SimpleDB.fileMgr().size(logfile)
        if (logsize == 0)
            appendNewBlock()
        else {
            currentblk = Block(logfile, logsize - 1)
            mypage.read(currentblk!!)
            currentpos = lastRecordPosition + Page.INT_SIZE
        }
    }

    /**
     * Ensures that the log records corresponding to the
     * specified LSN has been written to disk.
     * All earlier log records will also be written to disk.
     * @param lsn the LSN of a log record
     */
    fun flush(lsn: Int) {
        if (lsn >= currentLSN())
            flush()
    }

    /**
     * Returns an iterator for the log records,
     * which will be returned in reverse order starting with the most recent.
     * @see java.lang.Iterable.iterator
     */
    @Synchronized override fun iterator(): Iterator<BasicLogRecord> {
        flush()
        return LogIterator(currentblk!!)
    }

    /**
     * Appends a log record to the file.
     * The record contains an arbitrary array of strings and integers.
     * The method also writes an integer to the end of each log record whose value
     * is the offset of the corresponding integer for the previous log record.
     * These integers allow log records to be read in reverse order.
     * @param rec the list of values
     * @return the LSN of the final value
     */
    @Synchronized
    fun append(rec: Array<Any>): Int {
        var recsize = Page.INT_SIZE  // 4 bytes for the integer that points to the previous log record
        for (obj in rec)
            recsize += size(obj)
        if (currentpos + recsize >= Page.BLOCK_SIZE) { // the log record doesn't fit,
            flush()        // so move to the next block.
            appendNewBlock()
        }
        for (obj in rec)
            appendVal(obj)
        finalizeRecord()
        return currentLSN()
    }

    /**
     * Adds the specified value to the page at the position denoted by
     * currentpos.  Then increments currentpos by the size of the value.
     * @param val the integer or string to be added to the page
     */
    private fun appendVal(`val`: Any) {
        if (`val` is String)
            mypage.setString(currentpos, `val`)
        else
            mypage.setInt(currentpos, `val` as Int)
        currentpos += size(`val`)
    }

    /**
     * Calculates the size of the specified integer or string.
     * @param val the value
     * @return the size of the value, in bytes
     */
    private fun size(`val`: Any): Int {
        return if (`val` is String) {
            Page.STR_SIZE(`val`.length)
        } else
            Page.INT_SIZE
    }

    /**
     * Returns the LSN of the most recent log record.
     * As implemented, the LSN is the block number where the record is stored.
     * Thus every log record in a block has the same LSN.
     * @return the LSN of the most recent log record
     */
    private fun currentLSN(): Int {
        return currentblk!!.number()
    }

    /**
     * Writes the current page to the log file.
     */
    private fun flush() {
        mypage.write(currentblk!!)
    }

    /**
     * Clear the current page, and append it to the log file.
     */
    private fun appendNewBlock() {
        lastRecordPosition = 0
        currentpos = Page.INT_SIZE
        currentblk = mypage.append(logfile)
    }

    /**
     * Sets up a circular chain of pointers to the records in the page.
     * There is an integer added to the end of each log record
     * whose value is the offset of the previous log record.
     * The first four bytes of the page contain an integer whose value
     * is the offset of the integer for the last log record in the page.
     */
    private fun finalizeRecord() {
        mypage.setInt(currentpos, lastRecordPosition)
        lastRecordPosition = currentpos
        currentpos += Page.INT_SIZE
    }

    companion object {
        /**
         * The location where the pointer to the last integer in the page is.
         * A value of 0 means that the pointer is the first value in the page.
         */
        val LAST_POS = 0
    }
}
