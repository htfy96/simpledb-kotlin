package simpledb.buffer

import simpledb.file.Page

/**
 * An interface used to initialize a new block on disk.
 * There will be an implementing class for each "type" of
 * disk block.
 * @author Edward Sciore
 */
interface PageFormatter {
    /**
     * Initializes a page, whose contents will be
     * written to a new disk block.
     * This method is called only during the method
     * [Buffer.assignToNew].
     * @param p a buffer page
     */
    fun format(p: Page)
}
