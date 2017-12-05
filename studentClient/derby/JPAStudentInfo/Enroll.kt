import javax.persistence.*
import java.util.*

@Entity
class Enroll {

    @Id
    val id: Int
    var grade: String? = null
        private set

    @ManyToOne
    @JoinColumn(name = "StudentId")
    val student: Student

    @ManyToOne
    @JoinColumn(name = "SectionId")
    val section: Section

    constructor() {}

    constructor(eid: Int, student: Student, section: Section) {
        this.id = eid
        this.student = student
        this.section = section
    }

    fun changeGrade(grade: String) {
        this.grade = grade
    }
}
