import java.sql.*
import java.util.*
import org.apache.derby.jdbc.ClientDriver

class StudentDAO(private val conn: Connection, private val dbm: DatabaseManager) {

    fun find(sid: Int): Student? {
        try {
            val qry = "select SName, GradYear, MajorId from STUDENT where SId = ? "
            val pstmt = conn.prepareStatement(qry)
            pstmt.setInt(1, sid)
            val rs = pstmt.executeQuery()

            // return null if student doesn't exist
            if (!rs.next())
                return null

            val sname = rs.getString("SName")
            val gradyear = rs.getInt("GradYear")
            val majorid = rs.getInt("MajorId")
            rs.close()

            val major = dbm.findDept(majorid)
            return Student(this, sid, sname, gradyear, major)
        } catch (e: SQLException) {
            dbm.cleanup()
            throw RuntimeException("error finding student", e)
        }

    }

    fun insert(sid: Int, sname: String, gradyear: Int, major: Dept): Student? {
        try {
            // make sure that the sid is currently unused
            if (find(sid) != null)
                return null

            val cmd = "insert into STUDENT(SId, SName, GradYear, MajorId) " + "values(?, ?, ?, ?)"
            val pstmt = conn.prepareStatement(cmd)
            pstmt.setInt(1, sid)
            pstmt.setString(2, sname)
            pstmt.setInt(3, gradyear)
            pstmt.setInt(4, major.id)
            pstmt.executeUpdate()
            return Student(this, sid, sname, gradyear, major)
        } catch (e: SQLException) {
            dbm.cleanup()
            throw RuntimeException("error inserting new student", e)
        }

    }

    fun getEnrollments(sid: Int): Collection<Enroll> {
        try {
            val enrollments = ArrayList<Enroll>()
            val qry = "select EId from ENROLL where StudentId = ?"
            val pstmt = conn.prepareStatement(qry)
            pstmt.setInt(1, sid)
            val rs = pstmt.executeQuery()
            while (rs.next()) {
                val eid = rs.getInt("EId")
                enrollments.add(dbm.findEnroll(eid))
            }
            rs.close()
            return enrollments
        } catch (e: SQLException) {
            dbm.cleanup()
            throw RuntimeException("error getting student enrollments", e)
        }

    }

    fun changeGradYear(sid: Int, newyear: Int) {
        try {
            val cmd = "update STUDENT set GradYear = ? where SId = ?"
            val pstmt = conn.prepareStatement(cmd)
            pstmt.setInt(1, newyear)
            pstmt.setInt(2, sid)
            pstmt.executeUpdate()
        } catch (e: SQLException) {
            dbm.cleanup()
            throw RuntimeException("error changing grad year", e)
        }

    }

    fun changeMajor(sid: Int, newmajor: Dept) {
        try {
            val cmd = "update STUDENT set MajorId = ? where SId = ?"
            val pstmt = conn.prepareStatement(cmd)
            pstmt.setInt(1, newmajor.id)
            pstmt.setInt(2, sid)
            pstmt.executeUpdate()
        } catch (e: SQLException) {
            dbm.cleanup()
            throw RuntimeException("error changing major", e)
        }

    }
}
