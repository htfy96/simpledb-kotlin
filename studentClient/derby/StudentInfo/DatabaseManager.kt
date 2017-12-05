import java.sql.*
import org.apache.derby.jdbc.ClientDriver

class DatabaseManager {
    private var conn: Connection? = null
    private var studentDAO: StudentDAO? = null
    private var deptDAO: DeptDAO? = null
    private var enrollDAO: EnrollDAO? = null
    private var sectionDAO: SectionDAO? = null
    private var courseDAO: CourseDAO? = null

    init {
        try {
            val d = ClientDriver()
            val url = "jdbc:derby://localhost/studentdb"
            conn = d.connect(url, null)
            conn!!.autoCommit = false

            studentDAO = StudentDAO(conn, this)
            deptDAO = DeptDAO(conn, this)
            enrollDAO = EnrollDAO(conn, this)
            sectionDAO = SectionDAO(conn, this)
            courseDAO = CourseDAO(conn, this)
        } catch (e: SQLException) {
            throw RuntimeException("cannot connect to database", e)
        }

    }

    fun commit() {
        try {
            conn!!.commit()
        } catch (e: SQLException) {
            throw RuntimeException("cannot commit database", e)
        }

    }

    fun close() {
        try {
            conn!!.close()
        } catch (e: SQLException) {
            throw RuntimeException("cannot close database", e)
        }

    }

    fun findStudent(sid: Int): Student? {
        return studentDAO!!.find(sid)
    }

    fun findDept(did: Int): Dept? {
        return deptDAO!!.find(did)
    }

    fun findEnroll(eid: Int): Enroll? {
        return enrollDAO!!.find(eid)
    }

    fun findCourse(cid: Int): Course? {
        return courseDAO!!.find(cid)
    }

    fun findSection(sectid: Int): Section? {
        return sectionDAO!!.find(sectid)
    }

    fun insertStudent(sid: Int, sname: String, gradyear: Int, major: Dept): Student? {
        return studentDAO!!.insert(sid, sname, gradyear, major)
    }

    fun insertDept(did: Int, dname: String): Dept? {
        return deptDAO!!.insert(did, dname)
    }

    fun insertEnroll(eid: Int, student: Student, section: Section): Enroll? {
        return enrollDAO!!.insert(eid, student, section)
    }

    fun insertSection(sectid: Int, prof: String, year: Int, course: Course): Section? {
        return sectionDAO!!.insert(sectid, prof, year, course)
    }

    fun insertCourse(cid: Int, title: String, dept: Dept): Course? {
        return courseDAO!!.insert(cid, title, dept)
    }

    fun cleanup() {
        try {
            conn!!.rollback()
            conn!!.close()
        } catch (e: SQLException) {
            println("fatal error: cannot cleanup connection")
        }

    }
}
