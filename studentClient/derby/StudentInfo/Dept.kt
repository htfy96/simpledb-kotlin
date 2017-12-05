import java.sql.*

class Dept(private val dao: DeptDAO, val id: Int, dname: String) {
    var name: String? = null
        private set
    private var majors: Collection<Student>? = null
    private var courses: Collection<Course>? = null

    init {
        this.name = dname
    }

    fun changeName(newname: String) {
        name = newname
        dao.changeName(id, newname)
    }

    fun getMajors(): Collection<Student> {
        if (majors == null)
            majors = dao.getMajors(id)
        return majors
    }

    fun getCourses(): Collection<Course> {
        if (courses == null)
            courses = dao.getCourses(id)
        return courses
    }
}
