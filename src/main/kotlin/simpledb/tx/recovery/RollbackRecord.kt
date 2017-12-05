package simpledb.tx.recovery

import simpledb.log.BasicLogRecord

/**
 * The ROLLBACK log record.
 * @author Edward Sciore
 */
internal class RollbackRecord : LogRecord {
    private var txnum: Int = 0

    /**
     * Creates a new rollback log record for the specified transaction.
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
     * Writes a rollback record to the log.
     * This log record contains the ROLLBACK operator,
     * followed by the transaction id.
     * @return the LSN of the last log value
     */
    override fun writeToLog(): Int {
        val rec = arrayOf<Any>(LogRecord.ROLLBACK, txnum)
        return LogRecord.logMgr.append(rec)
    }

    override fun op(): Int {
        return LogRecord.ROLLBACK
    }

    override fun txNumber(): Int {
        return txnum
    }

    /**
     * Does nothing, because a rollback record
     * contains no undo information.
     */
    override fun undo(txnum: Int) {}

    override fun toString(): String {
        return "<ROLLBACK $txnum>"
    }
}
