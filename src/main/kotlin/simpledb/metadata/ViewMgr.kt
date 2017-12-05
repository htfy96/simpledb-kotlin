package simpledb.metadata

import simpledb.record.*
import simpledb.tx.Transaction

internal class ViewMgr(isNew: Boolean, var tblMgr: TableMgr, tx: Transaction) {

    init {
        if (isNew) {
            val sch = Schema()
            sch.addStringField("viewname", TableMgr.MAX_NAME)
            sch.addStringField("viewdef", MAX_VIEWDEF)
            tblMgr.createTable("viewcat", sch, tx)
        }
    }

    fun createView(vname: String, vdef: String, tx: Transaction) {
        val ti = tblMgr.getTableInfo("viewcat", tx)
        val rf = RecordFile(ti, tx)
        rf.insert()
        rf.setString("viewname", vname)
        rf.setString("viewdef", vdef)
        rf.close()
    }

    fun getViewDef(vname: String, tx: Transaction): String? {
        var result: String? = null
        val ti = tblMgr.getTableInfo("viewcat", tx)
        val rf = RecordFile(ti, tx)
        while (rf.next())
            if (rf.getString("viewname") == vname) {
                result = rf.getString("viewdef")
                break
            }
        rf.close()
        return result
    }

    companion object {
        private val MAX_VIEWDEF = 100
    }
}
