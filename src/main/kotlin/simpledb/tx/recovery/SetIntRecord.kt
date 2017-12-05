package simpledb.tx.recovery

import simpledb.server.SimpleDB
import simpledb.buffer.*
import simpledb.file.Block
import simpledb.log.BasicLogRecord

internal class SetIntRecord : LogRecord {
    private var txnum: Int = 0
    private var offset: Int = 0
    private var `val`: Int = 0
    private var blk: Block? = null

    /**
     * Creates a new setint log record.
     * @param txnum the ID of the specified transaction
     * @param blk the block containing the value
     * @param offset the offset of the value in the block
     * @param val the new value
     */
    constructor(txnum: Int, blk: Block, offset: Int, `val`: Int) {
        this.txnum = txnum
        this.blk = blk
        this.offset = offset
        this.`val` = `val`
    }

    /**
     * Creates a log record by reading five other values from the log.
     * @param rec the basic log record
     */
    constructor(rec: BasicLogRecord) {
        txnum = rec.nextInt()
        val filename = rec.nextString()
        val blknum = rec.nextInt()
        blk = Block(filename, blknum)
        offset = rec.nextInt()
        `val` = rec.nextInt()
    }

    /**
     * Writes a setInt record to the log.
     * This log record contains the SETINT operator,
     * followed by the transaction id, the filename, number,
     * and offset of the modified block, and the previous
     * integer value at that offset.
     * @return the LSN of the last log value
     */
    override fun writeToLog(): Int {
        val rec = arrayOf(LogRecord.SETINT, txnum, blk!!.fileName(), blk!!.number(), offset, `val`)
        return LogRecord.logMgr.append(rec)
    }

    override fun op(): Int {
        return LogRecord.SETINT
    }

    override fun txNumber(): Int {
        return txnum
    }

    override fun toString(): String {
        return "<SETINT $txnum $blk $offset $`val`>"
    }

    /**
     * Replaces the specified data value with the value saved in the log record.
     * The method pins a buffer to the specified block,
     * calls setInt to restore the saved value
     * (using a dummy LSN), and unpins the buffer.
     * @see simpledb.tx.recovery.LogRecord.undo
     */
    override fun undo(txnum: Int) {
        val buffMgr = SimpleDB.bufferMgr()
        val buff = buffMgr.pin(blk!!)
        buff.setInt(offset, `val`, txnum, -1)
        buffMgr.unpin(buff)
    }
}
