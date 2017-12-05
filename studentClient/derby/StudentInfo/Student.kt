import java.sql.*

class Student(private val dao: StudentDAO, val id: Int, val name: String, gradyear: Int, major: Dept) {
    var gradYear: Int = 0
        private set
    var major: Dept? = null
        private set
    private var enrollments: Collection<Enroll>? = null

    init {
        this.gradYear = gradyear
        this.major = major
    }

    fun getEnrollments(): Collection<Enroll> {
        if (enrollments == null)
            enrollments = dao.getEnrollments(id)
        return enrollments
    }

    fun changeGradYear(newyear: Int) {
        gradYear = newyear
        dao.changeGradYear(id, newyear)
    }

    fun changeMajor(newmajor: Dept) {
        major = newmajor
        dao.changeMajor(id, newmajor)
    }
}
