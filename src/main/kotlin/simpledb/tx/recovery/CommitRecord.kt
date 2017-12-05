package simpledb.tx.recovery

import simpledb.log.BasicLogRecord

/**
 * The COMMIT log record
 * @author Edward Sciore
 */
internal class CommitRecord : LogRecord {
    private var txnum: Int = 0

    /**
     * Creates a new commit log record for the specified transaction.
     * @param txnum the ID of the specified transaction
     */
    constructor(txnum: Int) {
        this.txnum = txnum
    }

    /**
     * Creates a log record by reading one other value from the log.
     * @param rec the basic log record
     */
    constructor(rec: BasicLogRecord) {
        txnum = rec.nextInt()
    }

    /**
     * Writes a commit record to the log.
     * This log record contains the COMMIT operator,
     * followed by the transaction id.
     * @return the LSN of the last log value
     */
    override fun writeToLog(): Int {
        val rec = arrayOf<Any>(LogRecord.COMMIT, txnum)
        return LogRecord.logMgr.append(rec)
    }

    override fun op(): Int {
        return LogRecord.COMMIT
    }

    override fun txNumber(): Int {
        return txnum
    }

    /**
     * Does nothing, because a commit record
     * contains no undo information.
     */
    override fun undo(txnum: Int) {}

    override fun toString(): String {
        return "<COMMIT $txnum>"
    }
}
