package simpledb.metadata

import simpledb.tx.Transaction
import simpledb.record.*

class MetadataMgr(isnew: Boolean, tx: Transaction) {

    init {
        tblmgr = TableMgr(isnew, tx)
        viewmgr = ViewMgr(isnew, tblmgr!!, tx)
        statmgr = StatMgr(tblmgr!!, tx)
        idxmgr = IndexMgr(isnew, tblmgr!!, tx)
    }

    fun createTable(tblname: String, sch: Schema, tx: Transaction) {
        tblmgr!!.createTable(tblname, sch, tx)
    }

    fun getTableInfo(tblname: String, tx: Transaction): TableInfo {
        return tblmgr!!.getTableInfo(tblname, tx)
    }

    fun createView(viewname: String, viewdef: String, tx: Transaction) {
        viewmgr!!.createView(viewname, viewdef, tx)
    }

    fun getViewDef(viewname: String, tx: Transaction): String? {
        return viewmgr!!.getViewDef(viewname, tx)
    }

    fun createIndex(idxname: String, tblname: String, fldname: String, tx: Transaction) {
        idxmgr!!.createIndex(idxname, tblname, fldname, tx)
    }

    fun getIndexInfo(tblname: String, tx: Transaction): Map<String, IndexInfo> {
        return idxmgr!!.getIndexInfo(tblname, tx)
    }

    fun getStatInfo(tblname: String, ti: TableInfo, tx: Transaction): StatInfo {
        return statmgr!!.getStatInfo(tblname, ti, tx)
    }

    companion object {
        private var tblmgr: TableMgr? = null
        private var viewmgr: ViewMgr? = null
        private var statmgr: StatMgr? = null
        private var idxmgr: IndexMgr? = null
    }
}
