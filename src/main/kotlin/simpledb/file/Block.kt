package simpledb.file

/**
 * A reference to a disk block.
 * A Block object consists of a filename and a block number.
 * It does not hold the contents of the block;
 * instead, that is the job of a [Page] object.
 * @author Edward Sciore
 */
class Block
/**
 * Constructs a block reference
 * for the specified filename and block number.
 * @param filename the name of the file
 * @param blknum the block number
 */
(private val filename: String, private val blknum: Int) {

    /**
     * Returns the name of the file where the block lives.
     * @return the filename
     */
    fun fileName(): String {
        return filename
    }

    /**
     * Returns the location of the block within the file.
     * @return the block number
     */
    fun number(): Int {
        return blknum
    }

    override fun equals(other: Any?): Boolean {
        val blk = other as Block?
        return filename == blk!!.filename && blknum == blk.blknum
    }

    override fun toString(): String {
        return "[file $filename, block $blknum]"
    }

    override fun hashCode(): Int {
        return toString().hashCode()
    }
}
