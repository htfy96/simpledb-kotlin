package simpledb.record

/**
 * An identifier for a record within a file.
 * A RID consists of the block number in the file,
 * and the ID of the record in that block.
 * @author Edward Sciore
 */
class RID
/**
 * Creates a RID for the record having the
 * specified ID in the specified block.
 * @param blknum the block number where the record lives
 * @param id the record's ID
 */
(private val blknum: Int, private val id: Int) {

    /**
     * Returns the block number associated with this RID.
     * @return the block number
     */
    fun blockNumber(): Int {
        return blknum
    }

    /**
     * Returns the ID associated with this RID.
     * @return the ID
     */
    fun id(): Int {
        return id
    }

    override fun equals(other: Any?): Boolean {
        val r = other as RID?
        return blknum == r!!.blknum && id == r.id
    }

    override fun toString(): String {
        return "[$blknum, $id]"
    }
}
