package simpledb.materialize

import simpledb.record.RID
import simpledb.query.*
import java.util.*

/**
 * The Scan class for the *sort* operator.
 * @author Edward Sciore
 */
/**
 * @author sciore
 */
class SortScan
/**
 * Creates a sort scan, given a list of 1 or 2 runs.
 * If there is only 1 run, then s2 will be null and
 * hasmore2 will be false.
 * @param runs the list of runs
 * @param comp the record comparator
 */
(runs: List<TempTable>, private val comp: RecordComparator) : Scan {
    private val s1: UpdateScan
    private var s2: UpdateScan? = null
    private var currentscan: UpdateScan? = null
    private var hasmore1: Boolean = false
    private var hasmore2 = false
    private var savedposition: List<RID?>? = null

    init {
        s1 = runs[0].open()
        hasmore1 = s1.next()
        if (runs.size > 1) {
            s2 = runs[1].open()
            hasmore2 = s2!!.next()
        }
    }

    /**
     * Positions the scan before the first record in sorted order.
     * Internally, it moves to the first record of each underlying scan.
     * The variable currentscan is set to null, indicating that there is
     * no current scan.
     * @see simpledb.query.Scan.beforeFirst
     */
    override fun beforeFirst() {
        currentscan = null
        s1.beforeFirst()
        hasmore1 = s1.next()
        if (s2 != null) {
            s2!!.beforeFirst()
            hasmore2 = s2!!.next()
        }
    }

    /**
     * Moves to the next record in sorted order.
     * First, the current scan is moved to the next record.
     * Then the lowest record of the two scans is found, and that
     * scan is chosen to be the new current scan.
     * @see simpledb.query.Scan.next
     */
    override fun next(): Boolean {
        if (currentscan != null) {
            if (currentscan === s1)
                hasmore1 = s1.next()
            else if (currentscan === s2)
                hasmore2 = s2!!.next()
        }

        if (!hasmore1 && !hasmore2)
            return false
        else if (hasmore1 && hasmore2) {
            if (comp.compare(s1, s2!!) < 0)
                currentscan = s1
            else
                currentscan = s2
        } else if (hasmore1)
            currentscan = s1
        else if (hasmore2)
            currentscan = s2
        return true
    }

    /**
     * Closes the two underlying scans.
     * @see simpledb.query.Scan.close
     */
    override fun close() {
        s1.close()
        if (s2 != null)
            s2!!.close()
    }

    /**
     * Gets the Constant value of the specified field
     * of the current scan.
     * @see simpledb.query.Scan.getVal
     */
    override fun getVal(fldname: String): Constant {
        return currentscan!!.getVal(fldname)
    }

    /**
     * Gets the integer value of the specified field
     * of the current scan.
     * @see simpledb.query.Scan.getInt
     */
    override fun getInt(fldname: String): Int {
        return currentscan!!.getInt(fldname)
    }

    /**
     * Gets the string value of the specified field
     * of the current scan.
     * @see simpledb.query.Scan.getString
     */
    override fun getString(fldname: String): String {
        return currentscan!!.getString(fldname)
    }

    /**
     * Returns true if the specified field is in the current scan.
     * @see simpledb.query.Scan.hasField
     */
    override fun hasField(fldname: String): Boolean {
        return currentscan!!.hasField(fldname)
    }

    /**
     * Saves the position of the current record,
     * so that it can be restored at a later time.
     */
    fun savePosition() {
        val rid1 = s1.rid
        val rid2 = if (s2 == null) null else s2!!.rid
        savedposition = Arrays.asList<RID?>(rid1, rid2)
    }

    /**
     * Moves the scan to its previously-saved position.
     */
    fun restorePosition() {
        if (savedposition != null) {

        }
        val rid1 = savedposition!![0]!!
        val rid2 = savedposition!![1]
        s1.moveToRid(rid1)
        if (rid2 != null)
            s2!!.moveToRid(rid2)
    }
}
