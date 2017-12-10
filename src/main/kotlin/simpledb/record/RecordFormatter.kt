package simpledb.record

import java.sql.Types.INTEGER
import simpledb.record.RecordPage.Companion.EMPTY
import simpledb.file.Page.Companion.INT_SIZE
import simpledb.file.Page.Companion.BLOCK_SIZE
import simpledb.file.Page
import simpledb.buffer.PageFormatter

/**
 * An object that can format a page to look like a block of
 * empty records.
 * @author Edward Sciore
 */
internal class RecordFormatter
/**
 * Creates a formatter for a new page of a table.
 * @param ti the table's metadata
 */
(private val ti: TableInfo) : PageFormatter {

    /**
     * Formats the page by allocating as many record slots
     * as possible, given the record length.
     * Each record slot is assigned a flag of EMPTY.
     * Each integer field is given a value of 0, and
     * each string field is given a value of "".
     * @see simpledb.buffer.PageFormatter.format
     */
    override fun format(p: Page) {
        val recsize = ti.recordLength() + INT_SIZE
        var pos = 0
        while (pos + recsize <= BLOCK_SIZE) {
            p.setInt(pos, EMPTY)
            makeDefaultRecord(p, pos)
            pos += recsize
        }
    }

    private fun makeDefaultRecord(page: Page, pos: Int) {
        for (fldname in ti.schema().fields()) {
            val offset = ti.offset(fldname)
            if (ti.schema().type(fldname) == INTEGER)
                page.setInt(pos + INT_SIZE + offset, 0)
            else
                page.setString(pos + INT_SIZE + offset, "")
        }
    }
}
