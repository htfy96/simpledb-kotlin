import java.sql.*
import java.util.*
import org.apache.derby.jdbc.ClientDriver

class DeptDAO(private val conn: Connection, private val dbm: DatabaseManager) {

    fun find(did: Int): Dept? {
        try {
            val qry = "select DName from DEPT where DId = ?"
            val pstmt = conn.prepareStatement(qry)
            pstmt.setInt(1, did)
            val rs = pstmt.executeQuery()

            // return null if department doesn't exist
            if (!rs.next())
                return null

            val dname = rs.getString("DName")
            rs.close()
            return Dept(this, did, dname)
        } catch (e: SQLException) {
            dbm.cleanup()
            throw RuntimeException("error finding department", e)
        }

    }

    fun insert(did: Int, dname: String): Dept? {
        try {
            // make sure that the did is currently unused
            if (find(did) != null)
                return null

            val cmd = "insert into DEPT(DId, DName) values(?, ?)"
            val pstmt = conn.prepareStatement(cmd)
            pstmt.setInt(1, did)
            pstmt.setString(2, dname)
            pstmt.executeUpdate()
            return Dept(this, did, dname)
        } catch (e: SQLException) {
            dbm.cleanup()
            throw RuntimeException("error inserting new department", e)
        }

    }

    fun getMajors(did: Int): Collection<Student> {
        try {
            val majors = ArrayList<Student>()
            val qry = "select SId from STUDENT where MajorId = ?"
            val pstmt = conn.prepareStatement(qry)
            pstmt.setInt(1, did)
            val rs = pstmt.executeQuery()
            while (rs.next()) {
                val sid = rs.getInt("SId")
                majors.add(dbm.findStudent(sid))
            }
            rs.close()
            return majors
        } catch (e: SQLException) {
            dbm.cleanup()
            throw RuntimeException("error getting student majors", e)
        }

    }

    fun getCourses(did: Int): Collection<Course> {
        try {
            val courses = ArrayList<Course>()
            val qry = "select CId from COURSE where CourseId = ?"
            val pstmt = conn.prepareStatement(qry)
            pstmt.setInt(1, did)
            val rs = pstmt.executeQuery()
            while (rs.next()) {
                val cid = rs.getInt("CId")
                courses.add(dbm.findCourse(cid))
            }
            rs.close()
            return courses
        } catch (e: SQLException) {
            dbm.cleanup()
            throw RuntimeException("error getting offered courses", e)
        }

    }

    fun changeName(did: Int, newname: String) {
        try {
            val cmd = "update DEPT set SName = ? where DId = ?"
            val pstmt = conn.prepareStatement(cmd)
            pstmt.setString(1, newname)
            pstmt.setInt(1, did)
            pstmt.executeUpdate()
        } catch (e: SQLException) {
            dbm.cleanup()
            throw RuntimeException("error changing department name", e)
        }

    }
}
