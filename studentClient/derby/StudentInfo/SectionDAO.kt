import java.sql.*
import java.util.*
import org.apache.derby.jdbc.ClientDriver

class SectionDAO(private val conn: Connection, private val dbm: DatabaseManager) {

    fun find(sectid: Int): Section? {
        try {
            val qry = "select Prof, YearOffered, CourseId " + "from SECTION where SectId = ?"
            val pstmt = conn.prepareStatement(qry)
            pstmt.setInt(1, sectid)
            val rs = pstmt.executeQuery()

            // return null if section doesn't exist
            if (!rs.next())
                return null

            val prof = rs.getString("Prof")
            val year = rs.getInt("YearOffered")
            val courseid = rs.getInt("CourseId")
            rs.close()
            val course = dbm.findCourse(courseid)
            return Section(this, sectid, prof, year, course)
        } catch (e: SQLException) {
            dbm.cleanup()
            throw RuntimeException("error finding section", e)
        }

    }

    fun insert(sectid: Int, prof: String, year: Int, course: Course): Section? {
        try {
            // make sure that the sectid is currently unused
            if (find(sectid) != null)
                return null

            val cmd = "insert into SECTION(SectId, Prof, YearOffered, CourseId) " + "values(?, ?, ?, ?)"
            val pstmt = conn.prepareStatement(cmd)
            pstmt.setInt(1, sectid)
            pstmt.setString(2, prof)
            pstmt.setInt(3, year)
            pstmt.setInt(4, course.id)
            pstmt.executeUpdate()
            return Section(this, sectid, prof, year, course)
        } catch (e: SQLException) {
            dbm.cleanup()
            throw RuntimeException("error inserting new section", e)
        }

    }

    fun getEnrollments(sectid: Int): Collection<Enroll> {
        try {
            val enrollments = ArrayList<Enroll>()
            val qry = "select EId from ENROLL where SectionId = ?"
            val pstmt = conn.prepareStatement(qry)
            pstmt.setInt(1, sectid)
            val rs = pstmt.executeQuery()
            while (rs.next()) {
                val eid = rs.getInt("EId")
                enrollments.add(dbm.findEnroll(eid))
            }
            rs.close()
            return enrollments
        } catch (e: SQLException) {
            dbm.cleanup()
            throw RuntimeException("error getting section enrollments", e)
        }

    }

    fun changeProf(sectid: Int, newprof: String) {
        try {
            val cmd = "update SECTION set Prof = ? where SectId = ?"
            val pstmt = conn.prepareStatement(cmd)
            pstmt.setString(1, newprof)
            pstmt.setInt(1, sectid)
            pstmt.executeUpdate()
        } catch (e: SQLException) {
            dbm.cleanup()
            throw RuntimeException("error changing prof", e)
        }

    }
}
