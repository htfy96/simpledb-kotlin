package simpledb.metadata

import simpledb.tx.Transaction
import simpledb.record.*
import java.util.*

/**
 * The statistics manager, which is responsible for
 * keeping statistical information about each table.
 * The manager does not store this information in the database.
 * Instead, it calculates this information on system startup,
 * and periodically refreshes it.
 * @author Edward Sciore
 */
internal class StatMgr
/**
 * Creates the statistics manager.
 * The initial statistics are calculated by
 * traversing the entire database.
 * @param tx the startup transaction
 */
(private val tblMgr: TableMgr, tx: Transaction) {
    private var tablestats: MutableMap<String, StatInfo>? = null
    private var numcalls: Int = 0

    init {
        refreshStatistics(tx)
    }

    /**
     * Returns the statistical information about the specified table.
     * @param tblname the name of the table
     * @param ti the table's metadata
     * @param tx the calling transaction
     * @return the statistical information about the table
     */
    @Synchronized
    fun getStatInfo(tblname: String, ti: TableInfo, tx: Transaction): StatInfo {
        numcalls++
        if (numcalls > 100)
            refreshStatistics(tx)
        var si: StatInfo? = tablestats!![tblname]
        if (si == null) {
            si = calcTableStats(ti, tx)
            tablestats!!.put(tblname, si)
        }
        return si
    }

    @Synchronized private fun refreshStatistics(tx: Transaction) {
        tablestats = HashMap()
        numcalls = 0
        val tcatmd = tblMgr.getTableInfo("tblcat", tx)
        val tcatfile = RecordFile(tcatmd, tx)
        while (tcatfile.next()) {
            val tblname = tcatfile.getString("tblname")
            val md = tblMgr.getTableInfo(tblname, tx)
            val si = calcTableStats(md, tx)
            tablestats!!.put(tblname, si)
        }
        tcatfile.close()
    }

    @Synchronized private fun calcTableStats(ti: TableInfo, tx: Transaction): StatInfo {
        var numRecs = 0
        val rf = RecordFile(ti, tx)
        var numblocks = 0
        while (rf.next()) {
            numRecs++
            numblocks = rf.currentRid().blockNumber() + 1
        }
        rf.close()
        return StatInfo(numblocks, numRecs)
    }
}
