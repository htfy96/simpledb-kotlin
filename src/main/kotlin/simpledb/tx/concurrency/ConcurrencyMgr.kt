package simpledb.tx.concurrency

import simpledb.file.Block
import java.util.*

/**
 * The concurrency manager for the transaction.
 * Each transaction has its own concurrency manager.
 * The concurrency manager keeps track of which locks the
 * transaction currently has, and interacts with the
 * global lock table as needed.
 * @author Edward Sciore
 */
class ConcurrencyMgr {
    private val locks = HashMap<Block, String>()

    /**
     * Obtains an SLock on the block, if necessary.
     * The method will ask the lock table for an SLock
     * if the transaction currently has no locks on that block.
     * @param blk a reference to the disk block
     */
    fun sLock(blk: Block) {
        if (locks[blk] == null) {
            locktbl.sLock(blk)
            locks.put(blk, "S")
        }
    }

    /**
     * Obtains an XLock on the block, if necessary.
     * If the transaction does not have an XLock on that block,
     * then the method first gets an SLock on that block
     * (if necessary), and then upgrades it to an XLock.
     * @param blk a refrence to the disk block
     */
    fun xLock(blk: Block) {
        if (!hasXLock(blk)) {
            sLock(blk)
            locktbl.xLock(blk)
            locks.put(blk, "X")
        }
    }

    /**
     * Releases all locks by asking the lock table to
     * unlock each one.
     */
    fun release() {
        for (blk in locks.keys)
            locktbl.unlock(blk)
        locks.clear()
    }

    private fun hasXLock(blk: Block): Boolean {
        val locktype = locks[blk]
        return locktype != null && locktype == "X"
    }

    companion object {

        /**
         * The global lock table.  This variable is static because all transactions
         * share the same table.
         */
        private val locktbl = LockTable()
    }
}
