package simpledb.file

import simpledb.server.SimpleDB
import java.nio.ByteBuffer
import java.nio.charset.Charset

/**
 * The contents of a disk block in memory.
 * A page is treated as an array of BLOCK_SIZE bytes.
 * There are methods to get/set values into this array,
 * and to read/write the contents of this array to a disk block.
 *
 * For an example of how to use Page and
 * [Block] objects,
 * consider the following code fragment.
 * The first portion increments the integer at offset 792 of block 6 of file junk.
 * The second portion stores the string "hello" at offset 20 of a page,
 * and then appends it to a new block of the file.
 * It then reads that block into another page
 * and extracts the value "hello" into variable s.
 * <pre>
 * Page p1 = new Page();
 * Block blk = new Block("junk", 6);
 * p1.read(blk);
 * int n = p1.getInt(792);
 * p1.setInt(792, n+1);
 * p1.write(blk);
 *
 * Page p2 = new Page();
 * p2.setString(20, "hello");
 * blk = p2.append("junk");
 * Page p3 = new Page();
 * p3.read(blk);
 * String s = p3.getString(20);
</pre> *
 * @author Edward Sciore
 */
/**
 * Creates a new page.  Although the constructor takes no arguments,
 * it depends on a [FileMgr] object that it gets from the
 * method [simpledb.server.SimpleDB.fileMgr].
 * That object is created during system initialization.
 * Thus this constructor cannot be called until either
 * [simpledb.server.SimpleDB.init] or
 * [simpledb.server.SimpleDB.initFileMgr] or
 * [simpledb.server.SimpleDB.initFileAndLogMgr] or
 * [simpledb.server.SimpleDB.initFileLogAndBufferMgr]
 * is called first.
 */
class Page {

    private val contents = ByteBuffer.allocateDirect(BLOCK_SIZE)
    private val filemgr = SimpleDB.fileMgr()

    /**
     * Populates the page with the contents of the specified disk block.
     * @param blk a reference to a disk block
     */
    @Synchronized
    fun read(blk: Block) {
        filemgr.read(blk, contents)
    }

    /**
     * Writes the contents of the page to the specified disk block.
     * @param blk a reference to a disk block
     */
    @Synchronized
    fun write(blk: Block) {
        filemgr.write(blk, contents)
    }

    /**
     * Appends the contents of the page to the specified file.
     * @param filename the name of the file
     * @return the reference to the newly-created disk block
     */
    @Synchronized
    fun append(filename: String): Block {
        return filemgr.append(filename, contents)
    }

    /**
     * Returns the integer value at a specified offset of the page.
     * If an integer was not stored at that location,
     * the behavior of the method is unpredictable.
     * @param offset the byte offset within the page
     * @return the integer value at that offset
     */
    @Synchronized
    fun getInt(offset: Int): Int {
        contents.position(offset)
        return contents.int
    }

    /**
     * Writes an integer to the specified offset on the page.
     * @param offset the byte offset within the page
     * @param val the integer to be written to the page
     */
    @Synchronized
    fun setInt(offset: Int, `val`: Int) {
        contents.position(offset)
        contents.putInt(`val`)
    }

    /**
     * Returns the string value at the specified offset of the page.
     * If a string was not stored at that location,
     * the behavior of the method is unpredictable.
     * @param offset the byte offset within the page
     * @return the string value at that offset
     */
    @Synchronized
    fun getString(offset: Int): String {
        contents.position(offset)
        val len = contents.int
        val byteval = ByteArray(len)
        contents.get(byteval)
        return String(byteval)
    }

    /**
     * Writes a string to the specified offset on the page.
     * @param offset the byte offset within the page
     * @param val the string to be written to the page
     */
    @Synchronized
    fun setString(offset: Int, `val`: String) {
        contents.position(offset)
        val byteval = `val`.toByteArray()
        contents.putInt(byteval.size)
        contents.put(byteval)
    }

    companion object {
        /**
         * The number of bytes in a block.
         * This value is set unreasonably low, so that it is easier
         * to create and test databases having a lot of blocks.
         * A more realistic value would be 4K.
         */
        val BLOCK_SIZE = 400

        /**
         * The size of an integer in bytes.
         * This value is almost certainly 4, but it is
         * a good idea to encode this value as a constant.
         */
        val INT_SIZE = Integer.SIZE / java.lang.Byte.SIZE

        /**
         * The maximum size, in bytes, of a string of length n.
         * A string is represented as the encoding of its characters,
         * preceded by an integer denoting the number of bytes in this encoding.
         * If the JVM uses the US-ASCII encoding, then each char
         * is stored in one byte, so a string of n characters
         * has a size of 4+n bytes.
         * @param n the size of the string
         * @return the maximum number of bytes required to store a string of size n
         */
        fun STR_SIZE(n: Int): Int {
            val bytesPerChar = Charset.defaultCharset().newEncoder().maxBytesPerChar()
            return INT_SIZE + n * bytesPerChar.toInt()
        }
    }
}
