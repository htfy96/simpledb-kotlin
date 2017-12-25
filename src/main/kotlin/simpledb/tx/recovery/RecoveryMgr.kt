package simpledb.tx.recovery

import simpledb.tx.recovery.LogRecord.*
import simpledb.file.Block
import simpledb.buffer.Buffer
import simpledb.server.SimpleDB
import simpledb.tx.Transaction
import simpledb.tx.recovery.LogRecord.Companion.CHECKPOINT
import simpledb.tx.recovery.LogRecord.Companion.COMMIT
import simpledb.tx.recovery.LogRecord.Companion.ROLLBACK
import simpledb.tx.recovery.LogRecord.Companion.START
import java.util.*

/**
 * The recovery manager.  Each transaction has its own recovery manager.
 * @author Edward Sciore
 */
class RecoveryMgr
/**
 * Creates a recovery manager for the specified transaction.
 * @param txnum the ID of the specified transaction
 */
(private val txnum: Int) {

    init {
        StartRecord(txnum).writeToLog()
    }

    /**
     * Writes a commit record to the log, and flushes it to disk.
     */
    fun commit() {
        SimpleDB.bufferMgr().flushAll(txnum)
        val lsn = CommitRecord(txnum).writeToLog()
        SimpleDB.logMgr().flush(lsn)
    }

    /**
     * Writes a rollback record to the log, and flushes it to disk.
     */
    fun rollback() {
        doRollback()
        SimpleDB.bufferMgr().flushAll(txnum)
        val lsn = RollbackRecord(txnum).writeToLog()
        SimpleDB.logMgr().flush(lsn)
    }

    /**
     * Recovers uncompleted transactions from the log,
     * then writes a quiescent checkpoint record to the log and flushes it.
     */
    fun recover() {
        val maxTxNum = doRecover()
        Transaction.Companion.nextTxNum = maxTxNum
        SimpleDB.bufferMgr().flushAll(txnum)
        val lsn = CheckpointRecord().writeToLog()
        SimpleDB.logMgr().flush(lsn)

    }

    /**
     * Writes a setint record to the log, and returns its lsn.
     * Updates to temporary files are not logged; instead, a
     * "dummy" negative lsn is returned.
     * @param buff the buffer containing the page
     * @param offset the offset of the value in the page
     * @param newval the value to be written
     */
    fun setInt(buff: Buffer, offset: Int,
               @Suppress("UNUSED_PARAMETER")
               newval: Int): Int {
        val oldval = buff.getInt(offset)
        val blk = buff.block()!!
        return if (isTempBlock(blk))
            -1
        else
            SetIntRecord(txnum, blk, offset, oldval).writeToLog()
    }

    /**
     * Writes a setstring record to the log, and returns its lsn.
     * Updates to temporary files are not logged; instead, a
     * "dummy" negative lsn is returned.
     * @param buff the buffer containing the page
     * @param offset the offset of the value in the page
     * @param newval the value to be written
     */
    fun setString(
            buff: Buffer,
            offset: Int,
            @Suppress("UNUSED_PARAMETER")
            newval: String
    ): Int {
        val oldval = buff.getString(offset)
        val blk = buff.block()!!
        return if (isTempBlock(blk))
            -1
        else
            SetStringRecord(txnum, blk, offset, oldval).writeToLog()
    }

    fun getValueFromRecovery(blk: Block, offset: Int, readViewBefore: Int, curTxNum: Int): Any? {
        val finishedTxs = ArrayList<Int>()
        for (rec in LogRecordIterator()) {
            if (rec.op() == COMMIT)
                finishedTxs.add(rec.txNumber())
            if (finishedTxs.contains(rec.txNumber()) && rec is LogSetRecord) {
                if (rec.getBlock() == blk && rec.getOffset() == offset &&
                        (rec.txNumber() < readViewBefore || rec.txNumber() == curTxNum)) {
                    return rec.getValue()
                }
            }
        }
        return null
    }

    /**
     * Rolls back the transaction.
     * The method iterates through the log records,
     * calling undo() for each log record it finds
     * for the transaction,
     * until it finds the transaction's START record.
     */
    private fun doRollback() {
        val iter = LogRecordIterator()
        while (iter.hasNext()) {
            val rec = iter.next()
            if (rec.txNumber() == txnum) {
                if (rec.op() == START)
                    return
                rec.undo(txnum)
            }
        }
    }

    /**
     * Does a complete database recovery.
     * The method iterates through the log records.
     * Whenever it finds a log record for an unfinished
     * transaction, it calls undo() on that record.
     * The method stops when it encounters a CHECKPOINT record
     * or the end of the log.
     */
    private fun doRecover(): Int {
        val finishedTxs = ArrayList<Int>()
        val iter = LogRecordIterator()
        while (iter.hasNext()) {
            val rec = iter.next()
            if (rec.op() == CHECKPOINT)
                break
            if (rec.op() == COMMIT || rec.op() == ROLLBACK)
                finishedTxs.add(rec.txNumber())
            else if (!finishedTxs.contains(rec.txNumber()))
                rec.undo(txnum)
        }
        return finishedTxs.max() ?: 0
    }

    /**
     * Determines whether a block comes from a temporary file or not.
     */
    private fun isTempBlock(blk: Block?): Boolean {
        return blk!!.fileName().startsWith("temp")
    }
}
