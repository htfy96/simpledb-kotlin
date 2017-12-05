import java.sql.*
import java.util.*

class Enroll(private val dao: EnrollDAO, val id: Int, grade: String, val student: Student, val section: Section) {
    var grade: String? = null
        private set

    init {
        this.grade = grade
    }

    fun changeGrade(newgrade: String) {
        grade = newgrade
        dao.changeGrade(id, newgrade)
    }
}
