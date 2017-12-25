package simpledb.tx.recovery

import simpledb.server.SimpleDB
import simpledb.buffer.*
import simpledb.file.Block
import simpledb.log.BasicLogRecord

internal class SetStringRecord : LogRecord, LogSetRecord {
    private var txnum: Int = 0
    private var offset: Int = 0
    private var `val`: String? = null
    private var blk: Block? = null

    /**
     * Creates a new setstring log record.
     * @param txnum the ID of the specified transaction
     * @param blk the block containing the value
     * @param offset the offset of the value in the block
     * @param val the new value
     */
    constructor(txnum: Int, blk: Block, offset: Int, `val`: String) {
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
        `val` = rec.nextString()
    }

    /**
     * Writes a setString record to the log.
     * This log record contains the SETSTRING operator,
     * followed by the transaction id, the filename, number,
     * and offset of the modified block, and the previous
     * string value at that offset.
     * @return the LSN of the last log value
     */
    override fun writeToLog(): Int {
        val rec = arrayOf<Any>(LogRecord.SETSTRING, txnum, blk!!.fileName(), blk!!.number(), offset, `val`!!)
        return LogRecord.logMgr.append(rec)
    }

    override fun op(): Int {
        return LogRecord.SETSTRING
    }

    override fun txNumber(): Int {
        return txnum
    }

    override fun toString(): String {
        return "<SETSTRING $txnum $blk $offset $`val`>"
    }

    override fun getBlock(): Block {
        return blk!!
    }

    override fun getOffset(): Int {
        return offset
    }

    override fun getValue(): Any {
        return `val`!!
    }

    /**
     * Replaces the specified data value with the value saved in the log record.
     * The method pins a buffer to the specified block,
     * calls setString to restore the saved value
     * (using a dummy LSN), and unpins the buffer.
     * @see simpledb.tx.recovery.LogRecord.undo
     */
    override fun undo(txnum: Int) {
        val buffMgr = SimpleDB.bufferMgr()
        val buff = buffMgr.pin(blk!!)
        buff.setString(offset, `val`!!, txnum, -1)
        buffMgr.unpin(buff)
    }
}
