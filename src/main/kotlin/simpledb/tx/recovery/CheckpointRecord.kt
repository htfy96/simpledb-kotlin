package simpledb.tx.recovery

import simpledb.log.BasicLogRecord

/**
 * The CHECKPOINT log record.
 * @author Edward Sciore
 */
internal class CheckpointRecord : LogRecord {

    /**
     * Creates a quiescent checkpoint record.
     */
    constructor()

    /**
     * Creates a log record by reading no other values
     * from the basic log record.
     * @param rec the basic log record
     */
    constructor(@Suppress("UNUSED_PARAMETER") rec: BasicLogRecord)

    /**
     * Writes a checkpoint record to the log.
     * This log record contains the CHECKPOINT operator,
     * and nothing else.
     * @return the LSN of the last log value
     */
    override fun writeToLog(): Int {
        val rec = arrayOf<Any>(LogRecord.CHECKPOINT)
        return LogRecord.logMgr.append(rec)
    }

    override fun op(): Int {
        return LogRecord.CHECKPOINT
    }

    /**
     * Checkpoint records have no associated transaction,
     * and so the method returns a "dummy", negative txid.
     */
    override fun txNumber(): Int {
        return -1 // dummy value
    }

    /**
     * Does nothing, because a checkpoint record
     * contains no undo information.
     */
    override fun undo(txnum: Int) {}

    override fun toString(): String {
        return "<CHECKPOINT>"
    }
}
