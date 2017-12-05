package simpledb.buffer

import simpledb.file.*

/**
 * The publicly-accessible buffer manager.
 * A buffer manager wraps a basic buffer manager, and
 * provides the same methods. The difference is that
 * the methods [pin][.pin] and
 * [pinNew][.pinNew]
 * will never return null.
 * If no buffers are currently available, then the
 * calling thread will be placed on a waiting list.
 * The waiting threads are removed from the list when
 * a buffer becomes available.
 * If a thread has been waiting for a buffer for an
 * excessive amount of time (currently, 10 seconds)
 * then a [BufferAbortException] is thrown.
 * @author Edward Sciore
 */
class BufferMgr
/**
 * Creates a new buffer manager having the specified
 * number of buffers.
 * This constructor depends on both the [FileMgr] and
 * [LogMgr][simpledb.log.LogMgr] objects
 * that it gets from the class
 * [simpledb.server.SimpleDB].
 * Those objects are created during system initialization.
 * Thus this constructor cannot be called until
 * [simpledb.server.SimpleDB.initFileAndLogMgr] or
 * is called first.
 * @param numbuffers the number of buffer slots to allocate
 */
(numbuffers: Int) {
    private val bufferMgr: BasicBufferMgr
    private val lock = java.lang.Object()

    init {
        bufferMgr = BasicBufferMgr(numbuffers)
    }

    /**
     * Pins a buffer to the specified block, potentially
     * waiting until a buffer becomes available.
     * If no buffer becomes available within a fixed
     * time period, then a [BufferAbortException] is thrown.
     * @param blk a reference to a disk block
     * @return the buffer pinned to that block
     */
    fun pin(blk: Block): Buffer {
        synchronized(lock) {
            try {
                val timestamp = System.currentTimeMillis()
                var buff = bufferMgr.pin(blk)
                while (buff == null && !waitingTooLong(timestamp)) {
                    lock.wait(MAX_TIME)
                    buff = bufferMgr.pin(blk)
                }
                if (buff == null)
                    throw BufferAbortException()
                return buff
            } catch (e: InterruptedException) {
                throw BufferAbortException()
            }
        }

    }

    /**
     * Pins a buffer to a new block in the specified file,
     * potentially waiting until a buffer becomes available.
     * If no buffer becomes available within a fixed
     * time period, then a [BufferAbortException] is thrown.
     * @param filename the name of the file
     * @param fmtr the formatter used to initialize the page
     * @return the buffer pinned to that block
     */
    fun pinNew(filename: String, fmtr: PageFormatter): Buffer {
        synchronized(lock) {
            try {
                val timestamp = System.currentTimeMillis()
                var buff = bufferMgr.pinNew(filename, fmtr)
                while (buff == null && !waitingTooLong(timestamp)) {
                    lock.wait(MAX_TIME)
                    buff = bufferMgr.pinNew(filename, fmtr)
                }
                if (buff == null)
                    throw BufferAbortException()
                return buff
            } catch (e: InterruptedException) {
                throw BufferAbortException()
            }
        }

    }

    /**
     * Unpins the specified buffer.
     * If the buffer's pin count becomes 0,
     * then the threads on the wait list are notified.
     * @param buff the buffer to be unpinned
     */
    fun unpin(buff: Buffer) {
        synchronized(lock) {
            bufferMgr.unpin(buff)
            if (!buff.isPinned)
                lock.notifyAll()
        }
    }

    /**
     * Flushes the dirty buffers modified by the specified transaction.
     * @param txnum the transaction's id number
     */
    fun flushAll(txnum: Int) {
        bufferMgr.flushAll(txnum)
    }

    /**
     * Returns the number of available (ie unpinned) buffers.
     * @return the number of available buffers
     */
    fun available(): Int {
        return bufferMgr.available()
    }

    private fun waitingTooLong(starttime: Long): Boolean {
        return System.currentTimeMillis() - starttime > MAX_TIME
    }

    companion object {
        private val MAX_TIME: Long = 10000 // 10 seconds
    }
}
