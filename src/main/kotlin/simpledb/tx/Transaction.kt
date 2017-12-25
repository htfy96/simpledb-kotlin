package simpledb.tx

import simpledb.server.SimpleDB
import simpledb.file.Block
import simpledb.buffer.*
import simpledb.tx.recovery.RecoveryMgr
import simpledb.tx.concurrency.ConcurrencyMgr

/**
 * Provides transaction management for clients,
 * ensuring that all transactions are serializable, recoverable,
 * and in general satisfy the ACID properties.
 * @author Edward Sciore
 */
class Transaction: AutoCloseable {
    private val recoveryMgr: RecoveryMgr
    private val concurMgr: ConcurrencyMgr
    private val txnum: Int
    private val readViewBeforeTxNum: Int
    private val myBuffers = BufferList()

    /**
     * Creates a new transaction and its associated
     * recovery and concurrency managers.
     * This constructor depends on the file, log, and buffer
     * managers that it gets from the class
     * [simpledb.server.SimpleDB].
     * Those objects are created during system initialization.
     * Thus this constructor cannot be called until either
     * [simpledb.server.SimpleDB.init] or
     * [simpledb.server.SimpleDB.initFileLogAndBufferMgr] or
     * is called first.
     */
    init {
        val newTxResult = registerNewTansaction()
        txnum = newTxResult.txNum
        readViewBeforeTxNum = newTxResult.readViewBeforeTxNum
        recoveryMgr = RecoveryMgr(txnum)
        concurMgr = ConcurrencyMgr()
    }

    /**
     * Commits the current transaction.
     * Flushes all modified buffers (and their log records),
     * writes and flushes a commit record to the log,
     * releases all locks, and unpins any pinned buffers.
     */
    fun commit() {
        recoveryMgr.commit()
        concurMgr.release()
        myBuffers.unpinAll()
        println("transaction $txnum committed")
    }

    /**
     * Rolls back the current transaction.
     * Undoes any modified values,
     * flushes those buffers,
     * writes and flushes a rollback record to the log,
     * releases all locks, and unpins any pinned buffers.
     */
    fun rollback() {
        recoveryMgr.rollback()
        concurMgr.release()
        myBuffers.unpinAll()
        println("transaction $txnum rolled back")
    }

    /**
     * Flushes all modified buffers.
     * Then goes through the log, rolling back all
     * uncommitted transactions.  Finally,
     * writes a quiescent checkpoint record to the log.
     * This method is called only during system startup,
     * before user transactions begin.
     */
    fun recover() {
        SimpleDB.bufferMgr().flushAll(txnum)
        recoveryMgr.recover()
    }

    /**
     * Pins the specified block.
     * The transaction manages the buffer for the client.
     * @param blk a reference to the disk block
     */
    fun pin(blk: Block) {
        myBuffers.pin(blk)
    }

    /**
     * Unpins the specified block.
     * The transaction looks up the buffer pinned to this block,
     * and unpins it.
     * @param blk a reference to the disk block
     */
    fun unpin(blk: Block) {
        myBuffers.unpin(blk)
    }

    /**
     * Returns the integer value stored at the
     * specified offset of the specified block.
     * The method first obtains an SLock on the block,
     * then it calls the buffer to retrieve the value.
     * @param blk a reference to a disk block
     * @param offset the byte offset within the block
     * @return the integer stored at that offset
     */
    fun getInt(blk: Block, offset: Int): Int {
        concurMgr.sLock(blk)
        val buff = myBuffers.getBuffer(blk)!!
        return buff.getInt(offset)
    }

    private fun nonblockingGet(blk: Block, offset: Int, lastTxOffset: Int, op: (Buffer) -> Any): Any? {
        concurMgr.sLock(blk)
        val buff = myBuffers.getBuffer(blk)!!
        val lastTx = buff.getInt(lastTxOffset)
        if (lastTx < readViewBeforeTxNum || lastTx == txnum) {
            concurMgr.unlock(blk)
            return op(buff)
        }
        concurMgr.unlock(blk)
        return recoveryMgr.getValueFromRecovery(blk, offset, readViewBeforeTxNum, txnum)
    }

    fun nonblockingGetInt(blk: Block, offset: Int, lastTxOffset: Int): Int? =
        nonblockingGet(blk, offset, lastTxOffset, {buff ->
            buff.getInt(offset)
        }) as Int?

    fun nonblockingGetString(blk: Block, offset: Int, lastTxOffset: Int): String? =
            nonblockingGet(blk, offset, lastTxOffset, {buff -> buff.getString(offset)}) as String?


    /**
     * Returns the string value stored at the
     * specified offset of the specified block.
     * The method first obtains an SLock on the block,
     * then it calls the buffer to retrieve the value.
     * @param blk a reference to a disk block
     * @param offset the byte offset within the block
     * @return the string stored at that offset
     */
    fun getString(blk: Block, offset: Int): String {
        concurMgr.sLock(blk)
        val buff = myBuffers.getBuffer(blk)!!
        return buff.getString(offset)
    }

    /**
     * Stores an integer at the specified offset
     * of the specified block.
     * The method first obtains an XLock on the block.
     * It then reads the current value at that offset,
     * puts it into an update log record, and
     * writes that record to the log.
     * Finally, it calls the buffer to store the value,
     * passing in the LSN of the log record and the transaction's id.
     * @param blk a reference to the disk block
     * @param offset a byte offset within that block
     * @param val the value to be stored
     */
    fun setInt(blk: Block, offset: Int, `val`: Int) {
        concurMgr.xLock(blk)
        val buff = myBuffers.getBuffer(blk)!!
        val lsn = recoveryMgr.setInt(buff, offset, `val`)
        buff.setInt(offset, `val`, txnum, lsn)
    }

    fun mvccSetInt(blk: Block, offset: Int, `val`: Int, lastTxOffset: Int) {
        concurMgr.xLock(blk)
        val buff = myBuffers.getBuffer(blk)!!
        val lsn = recoveryMgr.setInt(buff, offset, `val`)
        buff.setInt(offset, `val`, txnum, lsn)
        val recordlsn = recoveryMgr.setInt(buff, lastTxOffset, txnum)
        buff.setInt(lastTxOffset, txnum, txnum, recordlsn)
    }

    /**
     * Stores a string at the specified offset
     * of the specified block.
     * The method first obtains an XLock on the block.
     * It then reads the current value at that offset,
     * puts it into an update log record, and
     * writes that record to the log.
     * Finally, it calls the buffer to store the value,
     * passing in the LSN of the log record and the transaction's id.
     * @param blk a reference to the disk block
     * @param offset a byte offset within that block
     * @param val the value to be stored
     */
    fun setString(blk: Block, offset: Int, `val`: String) {
        concurMgr.xLock(blk)
        val buff = myBuffers.getBuffer(blk)!!
        val lsn = recoveryMgr.setString(buff, offset, `val`)
        buff.setString(offset, `val`, txnum, lsn)
    }

    fun mvccSetString(blk: Block, offset: Int, `val`: String, lastTxOffset: Int) {
        concurMgr.xLock(blk)
        val buff = myBuffers.getBuffer(blk)!!
        val lsn = recoveryMgr.setString(buff, offset, `val`)
        buff.setString(offset, `val`, txnum, lsn)
        val recordlsn = recoveryMgr.setInt(buff, lastTxOffset, txnum)
        buff.setInt(lastTxOffset, txnum, txnum, recordlsn)
    }

    /**
     * Returns the number of blocks in the specified file.
     * This method first obtains an SLock on the
     * "end of the file", before asking the file manager
     * to return the file size.
     * @param filename the name of the file
     * @return the number of blocks in the file
     */
    fun size(filename: String): Int {
        val dummyblk = Block(filename, END_OF_FILE)
        concurMgr.sLock(dummyblk)
        return SimpleDB.fileMgr().size(filename)
    }

    /**
     * Appends a new block to the end of the specified file
     * and returns a reference to it.
     * This method first obtains an XLock on the
     * "end of the file", before performing the append.
     * @param filename the name of the file
     * @param fmtr the formatter used to initialize the new page
     * @return a reference to the newly-created disk block
     */
    fun append(filename: String, fmtr: PageFormatter): Block? {
        val dummyblk = Block(filename, END_OF_FILE)
        concurMgr.xLock(dummyblk)
        val blk = myBuffers.pinNew(filename, fmtr)
        unpin(blk)
        return blk
    }

    override fun close() {
        unregisterTransaction(txnum)
    }

    companion object {
        var nextTxNum = 0
        private val END_OF_FILE = -1
        private val transactionList = mutableListOf<Int>()

        data class NewTransactionResult(
                val txNum: Int,
                val readViewBeforeTxNum: Int
        )


        @Synchronized private fun registerNewTansaction(): NewTransactionResult {
            nextTxNum++
            transactionList.add(nextTxNum)
            println("new transaction: " + nextTxNum)
            println("Current transaction: $transactionList")
            return NewTransactionResult(txNum = nextTxNum, readViewBeforeTxNum = transactionList.min()!!)
        }

        @Synchronized private fun unregisterTransaction(txnum: Int) {
            transactionList.remove(txnum)
        }

        @Synchronized private fun getCurrentTransactionList() = transactionList.toList()
    }
}
