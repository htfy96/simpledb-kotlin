package simpledb.multibuffer

import simpledb.server.SimpleDB

/**
 * A class containing static methods,
 * which estimate the optimal number of buffers
 * to allocate for a scan.
 * @author Edward Sciore
 */
object BufferNeeds {

    /**
     * This method considers the various roots
     * of the specified output size (in blocks),
     * and returns the highest root that is less than
     * the number of available buffers.
     * @param size the size of the output file
     * @return the highest number less than the number of available buffers, that is a root of the plan's output size
     */
    fun bestRoot(size: Int): Int {
        val avail = SimpleDB.bufferMgr().available()
        if (avail <= 1)
            return 1
        var k = Integer.MAX_VALUE
        var i = 1.0
        while (k > avail) {
            i++
            k = Math.ceil(Math.pow(size.toDouble(), 1 / i)).toInt()
        }
        return k
    }

    /**
     * This method considers the various factors
     * of the specified output size (in blocks),
     * and returns the highest factor that is less than
     * the number of available buffers.
     * @param size the size of the output file
     * @return the highest number less than the number of available buffers, that is a factor of the plan's output size
     */
    fun bestFactor(size: Int): Int {
        val avail = SimpleDB.bufferMgr().available()
        if (avail <= 1)
            return 1
        var k = size
        var i = 1.0
        while (k > avail) {
            i++
            k = Math.ceil(size / i).toInt()
        }
        return k
    }
}
