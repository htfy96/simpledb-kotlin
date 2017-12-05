package simpledb.metadata

/**
 * Holds three pieces of statistical information about a table:
 * the number of blocks, the number of records,
 * and the number of distinct values for each field.
 * @author Edward Sciore
 */
class StatInfo
/**
 * Creates a StatInfo object.
 * Note that the number of distinct values is not
 * passed into the constructor.
 * The object fakes this value.
 * @param numblocks the number of blocks in the table
 * @param numrecs the number of records in the table
 */
(private val numBlocks: Int, private val numRecs: Int) {

    /**
     * Returns the estimated number of blocks in the table.
     * @return the estimated number of blocks in the table
     */
    fun blocksAccessed(): Int {
        return numBlocks
    }

    /**
     * Returns the estimated number of records in the table.
     * @return the estimated number of records in the table
     */
    fun recordsOutput(): Int {
        return numRecs
    }

    /**
     * Returns the estimated number of distinct values
     * for the specified field.
     * In actuality, this estimate is a complete guess.
     * @param fldname the name of the field
     * @return a guess as to the number of distinct field values
     */
    fun distinctValues(fldname: String): Int {
        return 1 + numRecs / 3
    }
}
