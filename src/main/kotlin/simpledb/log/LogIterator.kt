package simpledb.log

import simpledb.file.Page
import simpledb.file.*

/**
 * A class that provides the ability to move through the
 * records of the log file in reverse order.
 *
 * @author Edward Sciore
 */
internal class LogIterator
/**
 * Creates an iterator for the records in the log file,
 * positioned after the last log record.
 * This constructor is called exclusively by
 * [LogMgr.iterator].
 */
(private var blk: Block) : Iterator<BasicLogRecord> {
    private val pg = Page()
    private var currentrec: Int = 0

    init {
        pg.read(blk)
        currentrec = pg.getInt(LogMgr.LAST_POS)
    }

    /**
     * Determines if the current log record
     * is the earliest record in the log file.
     * @return true if there is an earlier record
     */
    override fun hasNext(): Boolean {
        return currentrec > 0 || blk.number() > 0
    }

    /**
     * Moves to the next log record in reverse order.
     * If the current log record is the earliest in its block,
     * then the method moves to the next oldest block,
     * and returns the log record from there.
     * @return the next earliest log record
     */
    override fun next(): BasicLogRecord {
        if (currentrec == 0)
            moveToNextBlock()
        currentrec = pg.getInt(currentrec)
        return BasicLogRecord(pg, currentrec + Page.INT_SIZE)
    }

    fun remove() {
        throw UnsupportedOperationException()
    }

    /**
     * Moves to the next log block in reverse order,
     * and positions it after the last record in that block.
     */
    private fun moveToNextBlock() {
        blk = Block(blk.fileName(), blk.number() - 1)
        pg.read(blk)
        currentrec = pg.getInt(LogMgr.LAST_POS)
    }
}
