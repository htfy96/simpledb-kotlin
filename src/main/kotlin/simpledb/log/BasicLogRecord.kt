package simpledb.log

import simpledb.file.Page.*
import simpledb.file.Page

/**
 * A class that provides the ability to read the values of
 * a log record.
 * The class has no idea what values are there.
 * Instead, the methods [nextInt][.nextInt]
 * and [nextString][.nextString] read the values
 * sequentially.
 * Thus the client is responsible for knowing how many values
 * are in the log record, and what their types are.
 * @author Edward Sciore
 */
class BasicLogRecord
/**
 * A log record located at the specified position of the specified page.
 * This constructor is called exclusively by
 * [LogIterator.next].
 * @param pg the page containing the log record
 * @param pos the position of the log record
 */
(private val pg: Page, private var pos: Int) {

    /**
     * Returns the next value of the current log record,
     * assuming it is an integer.
     * @return the next value of the current log record
     */
    fun nextInt(): Int {
        val result = pg.getInt(pos)
        pos += Page.INT_SIZE
        return result
    }

    /**
     * Returns the next value of the current log record,
     * assuming it is a string.
     * @return the next value of the current log record
     */
    fun nextString(): String {
        val result = pg.getString(pos)
        pos += Page.STR_SIZE(result.length)
        return result
    }
}
