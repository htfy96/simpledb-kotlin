package simpledb.file

import simpledb.file.Page
import java.io.*
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.util.*

/**
 * The SimpleDB file manager.
 * The database system stores its data as files within a specified directory.
 * The file manager provides methods for reading the contents of
 * a file block to a Java byte buffer,
 * writing the contents of a byte buffer to a file block,
 * and appending the contents of a byte buffer to the end of a file.
 * These methods are called exclusively by the class [Page][simpledb.file.Page],
 * and are thus package-private.
 * The class also contains two public methods:
 * Method [isNew][.isNew] is called during system initialization by [simpledb.server.SimpleDB.init].
 * Method [size][.size] is called by the log manager and transaction manager to
 * determine the end of the file.
 * @author Edward Sciore
 */
class FileMgr
/**
 * Creates a file manager for the specified database.
 * The database will be stored in a folder of that name
 * in the user's home directory.
 * If the folder does not exist, then a folder containing
 * an empty database is created automatically.
 * Files for all temporary tables (i.e. tables beginning with "temp") are deleted.
 * @param dbname the name of the directory that holds the database
 */
(dbname: String) {
    private val dbDirectory: File
    /**
     * Returns a boolean indicating whether the file manager
     * had to create a new database directory.
     * @return true if the database is new
     */
    val isNew: Boolean
    private val openFiles = HashMap<String, FileChannel>()

    init {
        val homedir = System.getProperty("user.home")
        dbDirectory = File(homedir, dbname)
        isNew = !dbDirectory.exists()

        // create the directory if the database is new
        if (isNew && !dbDirectory.mkdir())
            throw RuntimeException("cannot create " + dbname)

        // remove any leftover temporary tables
        for (filename in dbDirectory.list()!!)
            if (filename.startsWith("temp"))
                File(dbDirectory, filename).delete()
    }

    /**
     * Reads the contents of a disk block into a bytebuffer.
     * @param blk a reference to a disk block
     * @param bb  the bytebuffer
     */
    @Synchronized internal fun read(blk: Block, bb: ByteBuffer) {
        try {
            bb.clear()
            val fc = getFile(blk.fileName())
            fc.read(bb, (blk.number() * Page.BLOCK_SIZE).toLong())
        } catch (e: IOException) {
            throw RuntimeException("cannot read block " + blk)
        }

    }

    /**
     * Writes the contents of a bytebuffer into a disk block.
     * @param blk a reference to a disk block
     * @param bb  the bytebuffer
     */
    @Synchronized internal fun write(blk: Block, bb: ByteBuffer) {
        try {
            bb.rewind()
            val fc = getFile(blk.fileName())
            fc.write(bb, (blk.number() * Page.BLOCK_SIZE).toLong())
        } catch (e: IOException) {
            throw RuntimeException("cannot write block" + blk)
        }

    }

    /**
     * Appends the contents of a bytebuffer to the end
     * of the specified file.
     * @param filename the name of the file
     * @param bb  the bytebuffer
     * @return a reference to the newly-created block.
     */
    @Synchronized internal fun append(filename: String, bb: ByteBuffer): Block {
        val newblknum = size(filename)
        val blk = Block(filename, newblknum)
        write(blk, bb)
        return blk
    }

    /**
     * Returns the number of blocks in the specified file.
     * @param filename the name of the file
     * @return the number of blocks in the file
     */
    @Synchronized
    fun size(filename: String): Int {
        try {
            val fc = getFile(filename)
            return (fc.size() / Page.BLOCK_SIZE).toInt()
        } catch (e: IOException) {
            throw RuntimeException("cannot access " + filename)
        }

    }

    /**
     * Returns the file channel for the specified filename.
     * The file channel is stored in a map keyed on the filename.
     * If the file is not open, then it is opened and the file channel
     * is added to the map.
     * @param filename the specified filename
     * @return the file channel associated with the open file.
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun getFile(filename: String): FileChannel {
        var fc: FileChannel? = openFiles[filename]
        if (fc == null) {
            val dbTable = File(dbDirectory, filename)
            val f = RandomAccessFile(dbTable, "rws")
            fc = f.channel
            openFiles.put(filename, fc)
        }
        return fc!!
    }
}
