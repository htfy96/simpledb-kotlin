package simpledb.buffer

import simpledb.file.*

/**
 * Manages the pinning and unpinning of buffers to blocks.
 * @author Edward Sciore
 */
internal class BasicBufferMgr
/**
 * Creates a buffer manager having the specified number
 * of buffer slots.
 * This constructor depends on both the [FileMgr] and
 * [LogMgr][simpledb.log.LogMgr] objects
 * that it gets from the class
 * [simpledb.server.SimpleDB].
 * Those objects are created during system initialization.
 * Thus this constructor cannot be called until
 * [simpledb.server.SimpleDB.initFileAndLogMgr] or
 * is called first.
 * @param numbuffs the number of buffer slots to allocate
 */
(private var numAvailable: Int) {
    private val bufferpool: Array<Buffer>

    init {
        this.bufferpool = Array(numAvailable, {Buffer()})
    }

    /**
     * Flushes the dirty buffers modified by the specified transaction.
     * @param txnum the transaction's id number
     */
    @Synchronized
    fun flushAll(txnum: Int) {
        for (buff in bufferpool)
            if (buff.isModifiedBy(txnum))
                buff.flush()
    }

    /**
     * Pins a buffer to the specified block.
     * If there is already a buffer assigned to that block
     * then that buffer is used;
     * otherwise, an unpinned buffer from the pool is chosen.
     * Returns a null value if there are no available buffers.
     * @param blk a reference to a disk block
     * @return the pinned buffer
     */
    @Synchronized
    fun pin(blk: Block): Buffer? {
        var buff = findExistingBuffer(blk)
        if (buff == null) {
            buff = chooseUnpinnedBuffer()
            if (buff == null)
                return null
            buff.assignToBlock(blk)
        }
        if (!buff.isPinned)
            numAvailable--
        buff.pin()
        return buff
    }

    /**
     * Allocates a new block in the specified file, and
     * pins a buffer to it.
     * Returns null (without allocating the block) if
     * there are no available buffers.
     * @param filename the name of the file
     * @param fmtr a pageformatter object, used to format the new block
     * @return the pinned buffer
     */
    @Synchronized
    fun pinNew(filename: String, fmtr: PageFormatter): Buffer? {
        val buff = chooseUnpinnedBuffer() ?: return null
        buff.assignToNew(filename, fmtr)
        numAvailable--
        buff.pin()
        return buff
    }

    /**
     * Unpins the specified buffer.
     * @param buff the buffer to be unpinned
     */
    @Synchronized
    fun unpin(buff: Buffer) {
        buff.unpin()
        if (!buff.isPinned)
            numAvailable++
    }

    /**
     * Returns the number of available (i.e. unpinned) buffers.
     * @return the number of available buffers
     */
    fun available(): Int {
        return numAvailable
    }

    private fun findExistingBuffer(blk: Block): Buffer? {
        for (buff in bufferpool) {
            val b = buff.block()
            if (b != null && b == blk)
                return buff
        }
        return null
    }

    private fun chooseUnpinnedBuffer(): Buffer? {
        for (buff in bufferpool)
            if (!buff.isPinned)
                return buff
        return null
    }
}
