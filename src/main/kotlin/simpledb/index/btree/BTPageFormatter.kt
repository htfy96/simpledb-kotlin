package simpledb.index.btree

import simpledb.file.Page.*
import java.sql.Types.INTEGER
import simpledb.file.Page
import simpledb.buffer.PageFormatter
import simpledb.record.TableInfo

/**
 * An object that can format a page to look like an
 * empty B-tree block.
 * @author Edward Sciore
 */
class BTPageFormatter
/**
 * Creates a formatter for a new page of the
 * specified B-tree index.
 * @param ti the index's metadata
 * @param flag the page's initial flag value
 */
(private val ti: TableInfo, private val flag: Int) : PageFormatter {

    /**
     * Formats the page by initializing as many index-record slots
     * as possible to have default values.
     * Each integer field is given a value of 0, and
     * each string field is given a value of "".
     * The location that indicates the number of records
     * in the page is also set to 0.
     * @see simpledb.buffer.PageFormatter.format
     */
    override fun format(p: Page) {
        p.setInt(0, flag)
        p.setInt(Page.INT_SIZE, 0)  // #records = 0
        val recsize = ti.recordLength()
        var pos = 2 * Page.INT_SIZE
        while (pos + recsize <= Page.BLOCK_SIZE) {
            makeDefaultRecord(p, pos)
            pos += recsize
        }
    }

    private fun makeDefaultRecord(page: Page, pos: Int) {
        for (fldname in ti.schema().fields()) {
            val offset = ti.offset(fldname)
            if (ti.schema().type(fldname) == INTEGER)
                page.setInt(pos + offset, 0)
            else
                page.setString(pos + offset, "")
        }
    }
}
