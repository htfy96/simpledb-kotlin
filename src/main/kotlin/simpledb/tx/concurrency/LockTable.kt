package simpledb.tx.concurrency

import simpledb.file.Block
import java.util.*

/**
 * The lock table, which provides methods to lock and unlock blocks.
 * If a transaction requests a lock that causes a conflict with an
 * existing lock, then that transaction is placed on a wait list.
 * There is only one wait list for all blocks.
 * When the last lock on a block is unlocked, then all transactions
 * are removed from the wait list and rescheduled.
 * If one of those transactions discovers that the lock it is waiting for
 * is still locked, it will place itself back on the wait list.
 * @author Edward Sciore
 */
internal class LockTable {

    private val locks = HashMap<Block, Int>()

    private val lock = java.lang.Object()

    /**
     * Grants an SLock on the specified block.
     * If an XLock exists when the method is called,
     * then the calling thread will be placed on a wait list
     * until the lock is released.
     * If the thread remains on the wait list for a certain
     * amount of time (currently 10 seconds),
     * then an exception is thrown.
     * @param blk a reference to the disk block
     */
    @Synchronized
    fun sLock(blk: Block) {
        try {
            val timestamp = System.currentTimeMillis()
            while (hasXlock(blk) && !waitingTooLong(timestamp))
                lock.wait(MAX_TIME)
            if (hasXlock(blk))
                throw LockAbortException()
            val `val` = getLockVal(blk)  // will not be negative
            locks.put(blk, `val` + 1)
        } catch (e: InterruptedException) {
            throw LockAbortException()
        }

    }

    /**
     * Grants an XLock on the specified block.
     * If a lock of any type exists when the method is called,
     * then the calling thread will be placed on a wait list
     * until the locks are released.
     * If the thread remains on the wait list for a certain
     * amount of time (currently 10 seconds),
     * then an exception is thrown.
     * @param blk a reference to the disk block
     */
    @Synchronized
    fun xLock(blk: Block) {
        try {
            val timestamp = System.currentTimeMillis()
            while (hasOtherSLocks(blk) && !waitingTooLong(timestamp))
                lock.wait(MAX_TIME)
            if (hasOtherSLocks(blk))
                throw LockAbortException()
            locks.put(blk, -1)
        } catch (e: InterruptedException) {
            throw LockAbortException()
        }

    }

    /**
     * Releases a lock on the specified block.
     * If this lock is the last lock on that block,
     * then the waiting transactions are notified.
     * @param blk a reference to the disk block
     */
    fun unlock(blk: Block) {
        synchronized(lock) {
            val `val` = getLockVal(blk)
            if (`val` > 1)
                locks.put(blk, `val` - 1)
            else {
                locks.remove(blk)
                lock.notifyAll()
            }
        }
    }

    private fun hasXlock(blk: Block): Boolean {
        return getLockVal(blk) < 0
    }

    private fun hasOtherSLocks(blk: Block): Boolean {
        return getLockVal(blk) > 1
    }

    private fun waitingTooLong(starttime: Long): Boolean {
        return System.currentTimeMillis() - starttime > MAX_TIME
    }

    private fun getLockVal(blk: Block): Int {
        val ival = locks[blk]
        return ival?.toInt() ?: 0
    }

    companion object {
        private val MAX_TIME: Long = 10000 // 10 seconds
    }
}
