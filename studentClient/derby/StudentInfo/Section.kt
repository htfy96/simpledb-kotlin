import java.sql.*

class Section(private val dao: SectionDAO, val id: Int, prof: String, val yearOffered: Int, val course: Course) {
    var prof: String? = null
        private set
    private var enrollments: Collection<Enroll>? = null

    init {
        this.prof = prof
    }

    fun getEnrollments(): Collection<Enroll> {
        if (enrollments == null)
            enrollments = dao.getEnrollments(id)
        return enrollments
    }

    fun changeProf(newprof: String) {
        prof = newprof
        dao.changeProf(id, newprof)
    }
}
