package simpledb.buffer

import simpledb.server.SimpleDB
import simpledb.file.*

/**
 * An individual buffer.
 * A buffer wraps a page and stores information about its status,
 * such as the disk block associated with the page,
 * the number of times the block has been pinned,
 * whether the contents of the page have been modified,
 * and if so, the id of the modifying transaction and
 * the LSN of the corresponding log record.
 * @author Edward Sciore
 */
/**
 * Creates a new buffer, wrapping a new
 * [page][simpledb.file.Page].
 * This constructor is called exclusively by the
 * class [BasicBufferMgr].
 * It depends on  the
 * [LogMgr][simpledb.log.LogMgr] object
 * that it gets from the class
 * [simpledb.server.SimpleDB].
 * That object is created during system initialization.
 * Thus this constructor cannot be called until
 * [simpledb.server.SimpleDB.initFileAndLogMgr] or
 * is called first.
 */
class Buffer {
    private val contents = Page()
    private var blk: Block? = null
    private var pins = 0
    private var modifiedBy = -1  // negative means not modified
    private var logSequenceNumber = -1 // negative means no corresponding log record

    /**
     * Returns true if the buffer is currently pinned
     * (that is, if it has a nonzero pin count).
     * @return true if the buffer is pinned
     */
    internal val isPinned: Boolean
        get() = pins > 0

    /**
     * Returns the integer value at the specified offset of the
     * buffer's page.
     * If an integer was not stored at that location,
     * the behavior of the method is unpredictable.
     * @param offset the byte offset of the page
     * @return the integer value at that offset
     */
    fun getInt(offset: Int): Int {
        return contents.getInt(offset)
    }

    /**
     * Returns the string value at the specified offset of the
     * buffer's page.
     * If a string was not stored at that location,
     * the behavior of the method is unpredictable.
     * @param offset the byte offset of the page
     * @return the string value at that offset
     */
    fun getString(offset: Int): String {
        return contents.getString(offset)
    }

    /**
     * Writes an integer to the specified offset of the
     * buffer's page.
     * This method assumes that the transaction has already
     * written an appropriate log record.
     * The buffer saves the id of the transaction
     * and the LSN of the log record.
     * A negative lsn value indicates that a log record
     * was not necessary.
     * @param offset the byte offset within the page
     * @param val the new integer value to be written
     * @param txnum the id of the transaction performing the modification
     * @param lsn the LSN of the corresponding log record
     */
    fun setInt(offset: Int, `val`: Int, txnum: Int, lsn: Int) {
        modifiedBy = txnum
        if (lsn >= 0)
            logSequenceNumber = lsn
        contents.setInt(offset, `val`)
    }

    /**
     * Writes a string to the specified offset of the
     * buffer's page.
     * This method assumes that the transaction has already
     * written an appropriate log record.
     * A negative lsn value indicates that a log record
     * was not necessary.
     * The buffer saves the id of the transaction
     * and the LSN of the log record.
     * @param offset the byte offset within the page
     * @param val the new string value to be written
     * @param txnum the id of the transaction performing the modification
     * @param lsn the LSN of the corresponding log record
     */
    fun setString(offset: Int, `val`: String, txnum: Int, lsn: Int) {
        modifiedBy = txnum
        if (lsn >= 0)
            logSequenceNumber = lsn
        contents.setString(offset, `val`)
    }

    /**
     * Returns a reference to the disk block
     * that the buffer is pinned to.
     * @return a reference to a disk block
     */
    fun block(): Block? {
        return blk
    }

    /**
     * Writes the page to its disk block if the
     * page is dirty.
     * The method ensures that the corresponding log
     * record has been written to disk prior to writing
     * the page to disk.
     */
    internal fun flush() {
        if (modifiedBy >= 0) {
            SimpleDB.logMgr().flush(logSequenceNumber)
            contents.write(blk!!)
            modifiedBy = -1
        }
    }

    /**
     * Increases the buffer's pin count.
     */
    internal fun pin() {
        pins++
    }

    /**
     * Decreases the buffer's pin count.
     */
    internal fun unpin() {
        pins--
    }

    /**
     * Returns true if the buffer is dirty
     * due to a modification by the specified transaction.
     * @param txnum the id of the transaction
     * @return true if the transaction modified the buffer
     */
    internal fun isModifiedBy(txnum: Int): Boolean {
        return txnum == modifiedBy
    }

    /**
     * Reads the contents of the specified block into
     * the buffer's page.
     * If the buffer was dirty, then the contents
     * of the previous page are first written to disk.
     * @param b a reference to the data block
     */
    internal fun assignToBlock(b: Block) {
        flush()
        blk = b
        contents.read(blk!!)
        pins = 0
    }

    /**
     * Initializes the buffer's page according to the specified formatter,
     * and appends the page to the specified file.
     * If the buffer was dirty, then the contents
     * of the previous page are first written to disk.
     * @param filename the name of the file
     * @param fmtr a page formatter, used to initialize the page
     */
    internal fun assignToNew(filename: String, fmtr: PageFormatter) {
        flush()
        fmtr.format(contents)
        blk = contents.append(filename)
        pins = 0
    }
}