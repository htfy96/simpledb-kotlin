import javax.persistence.*
import java.util.*

@Entity
class Section {

    @Id
    val id: Int
    var prof: String? = null
        private set
    val yearOffered: Int

    @OneToMany(mappedBy = "section")
    val students: Collection<Enroll>? = null

    @ManyToOne
    @JoinColumn(name = "CourseId")
    val course: Course

    constructor() {}

    constructor(sectid: Int, prof: String, year: Int, course: Course) {
        this.id = sectid
        this.prof = prof
        this.yearOffered = year
        this.course = course
    }

    fun changeProf(prof: String) {
        this.prof = prof
    }
}
