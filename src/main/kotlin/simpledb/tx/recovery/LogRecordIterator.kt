package simpledb.tx.recovery

import simpledb.tx.recovery.LogRecord.*
import simpledb.log.BasicLogRecord
import simpledb.server.SimpleDB
import simpledb.tx.recovery.LogRecord.Companion.CHECKPOINT
import simpledb.tx.recovery.LogRecord.Companion.COMMIT
import simpledb.tx.recovery.LogRecord.Companion.ROLLBACK
import simpledb.tx.recovery.LogRecord.Companion.SETINT
import simpledb.tx.recovery.LogRecord.Companion.SETSTRING
import simpledb.tx.recovery.LogRecord.Companion.START

/**
 * A class that provides the ability to read records
 * from the log in reverse order.
 * Unlike the similar class
 * [LogIterator][simpledb.log.LogIterator],
 * this class understands the meaning of the log records.
 * @author Edward Sciore
 */
internal class LogRecordIterator : Iterator<LogRecord> {
    private val iter = SimpleDB.logMgr().iterator()

    override fun hasNext(): Boolean {
        return iter.hasNext()
    }

    /**
     * Constructs a log record from the values in the
     * current basic log record.
     * The method first reads an integer, which denotes
     * the type of the log record.  Based on that type,
     * the method calls the appropriate LogRecord constructor
     * to read the remaining values.
     * @return the next log record, or null if no more records
     */
    override fun next(): LogRecord {
        val rec = iter.next()
        val op = rec.nextInt()
        when (op) {
            CHECKPOINT -> return CheckpointRecord(rec)
            START -> return StartRecord(rec)
            COMMIT -> return CommitRecord(rec)
            ROLLBACK -> return RollbackRecord(rec)
            SETINT -> return SetIntRecord(rec)
            SETSTRING -> return SetStringRecord(rec)
            else -> throw IllegalStateException("invalid op=$op")
        }
    }
}
