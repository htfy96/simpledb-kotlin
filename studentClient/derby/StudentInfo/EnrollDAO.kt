import java.sql.*
import org.apache.derby.jdbc.ClientDriver

class EnrollDAO(private val conn: Connection, private val dbm: DatabaseManager) {

    fun find(eid: Int): Enroll? {
        try {
            val qry = "select Grade, StudentId, SectionId from ENROLL where EId = ?"
            val pstmt = conn.prepareStatement(qry)
            pstmt.setInt(1, eid)
            val rs = pstmt.executeQuery()

            // return null if enrollment doesn't exist
            if (!rs.next())
                return null

            val grade = rs.getString("Grade")
            val sid = rs.getInt("StudentId")
            val sectid = rs.getInt("SectionId")
            rs.close()
            val student = dbm.findStudent(sid)
            val section = dbm.findSection(sectid)
            return Enroll(this, eid, grade, student, section)
        } catch (e: SQLException) {
            dbm.cleanup()
            throw RuntimeException("error finding enrollment", e)
        }

    }

    fun insert(eid: Int, student: Student, section: Section): Enroll? {
        try {
            // make sure that the eid is currently unused
            if (find(eid) != null)
                return null

            // the grade for a new enrollment is ""
            val grade = ""

            val cmd = "insert into ENROLL(EId, Grade, StudentId, SectionId) " + "values(?, ?, ?, ?)"
            val pstmt = conn.prepareStatement(cmd)
            pstmt.setInt(1, eid)
            pstmt.setString(2, grade)
            pstmt.setInt(3, student.id)
            pstmt.setInt(4, section.id)
            pstmt.executeUpdate()
            return Enroll(this, eid, grade, student, section)
        } catch (e: SQLException) {
            dbm.cleanup()
            throw RuntimeException("error inserting new enrollment", e)
        }

    }

    fun changeGrade(eid: Int, newgrade: String) {
        try {
            val cmd = "update ENROLL set Grade = ? where EId = ?"
            val pstmt = conn.prepareStatement(cmd)
            pstmt.setString(1, newgrade)
            pstmt.setInt(1, eid)
            pstmt.executeUpdate()
        } catch (e: SQLException) {
            dbm.cleanup()
            throw RuntimeException("error changing grade", e)
        }

    }
}
