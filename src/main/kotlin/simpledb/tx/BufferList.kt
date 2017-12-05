package simpledb.tx

import simpledb.server.SimpleDB
import simpledb.file.Block
import simpledb.buffer.*
import java.util.*

/**
 * Manages the transaction's currently-pinned buffers.
 * @author Edward Sciore
 */
internal class BufferList {
    private val buffers = HashMap<Block, Buffer>()
    private val pins = ArrayList<Block>()
    private val bufferMgr = SimpleDB.bufferMgr()

    /**
     * Returns the buffer pinned to the specified block.
     * The method returns null if the transaction has not
     * pinned the block.
     * @param blk a reference to the disk block
     * @return the buffer pinned to that block
     */
    fun getBuffer(blk: Block): Buffer? {
        return buffers[blk]
    }

    /**
     * Pins the block and keeps track of the buffer internally.
     * @param blk a reference to the disk block
     */
    fun pin(blk: Block) {
        val buff = bufferMgr.pin(blk)
        buffers.put(blk, buff)
        pins.add(blk)
    }

    /**
     * Appends a new block to the specified file
     * and pins it.
     * @param filename the name of the file
     * @param fmtr the formatter used to initialize the new page
     * @return a reference to the newly-created block
     */
    fun pinNew(filename: String, fmtr: PageFormatter): Block {
        val buff = bufferMgr.pinNew(filename, fmtr)
        val blk = buff.block()!!
        buffers.put(blk, buff)
        pins.add(blk)
        return blk
    }

    /**
     * Unpins the specified block.
     * @param blk a reference to the disk block
     */
    fun unpin(blk: Block) {
        val buff = buffers[blk]!!
        bufferMgr.unpin(buff)
        pins.remove(blk)
        if (!pins.contains(blk))
            buffers.remove(blk)
    }

    /**
     * Unpins any buffers still pinned by this transaction.
     */
    fun unpinAll() {
        for (blk in pins) {
            val buff = buffers[blk]!!
            bufferMgr.unpin(buff)
        }
        buffers.clear()
        pins.clear()
    }
}