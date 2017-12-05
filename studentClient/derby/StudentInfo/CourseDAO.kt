import java.sql.*
import java.util.*
import org.apache.derby.jdbc.ClientDriver

class CourseDAO(private val conn: Connection, private val dbm: DatabaseManager) {

    fun find(cid: Int): Course? {
        try {
            val qry = "select Title, DeptId from COURSE where CId = ?"
            val pstmt = conn.prepareStatement(qry)
            pstmt.setInt(1, cid)
            val rs = pstmt.executeQuery()

            // return null if course doesn't exist
            if (!rs.next())
                return null

            val title = rs.getString("Title")
            val deptid = rs.getInt("DeptId")
            rs.close()
            val dept = dbm.findDept(deptid)
            return Course(this, cid, title, dept)
        } catch (e: SQLException) {
            dbm.cleanup()
            throw RuntimeException("error finding course", e)
        }

    }

    fun insert(cid: Int, title: String, dept: Dept): Course? {
        try {
            // make sure that the cid is currently unused
            if (find(cid) != null)
                return null

            val cmd = "insert into COURSE(CId, Title, DeptId) " + "values(?, ?, ?, ?)"
            val pstmt = conn.prepareStatement(cmd)
            pstmt.setInt(1, cid)
            pstmt.setString(2, title)
            pstmt.setInt(3, dept.id)
            pstmt.executeUpdate()
            return Course(this, cid, title, dept)
        } catch (e: SQLException) {
            dbm.cleanup()
            throw RuntimeException("error inserting new course", e)
        }

    }

    fun getSections(cid: Int): Collection<Section> {
        try {
            val sections = ArrayList<Section>()
            val qry = "select SectId from SECTION where CourseId = ?"
            val pstmt = conn.prepareStatement(qry)
            pstmt.setInt(1, cid)
            val rs = pstmt.executeQuery()
            while (rs.next()) {
                val sectid = rs.getInt("SectId")
                sections.add(dbm.findSection(sectid))
            }
            rs.close()
            return sections
        } catch (e: SQLException) {
            dbm.cleanup()
            throw RuntimeException("error getting course sections", e)
        }

    }

    fun changeTitle(cid: Int, newtitle: String) {
        try {
            val cmd = "update COURSE set Title = ? where CId = ?"
            val pstmt = conn.prepareStatement(cmd)
            pstmt.setString(1, newtitle)
            pstmt.setInt(1, cid)
            pstmt.executeUpdate()
        } catch (e: SQLException) {
            dbm.cleanup()
            throw RuntimeException("error changing title", e)
        }

    }
}
