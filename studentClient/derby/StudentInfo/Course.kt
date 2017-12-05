import java.sql.*

class Course(private val dao: CourseDAO, val id: Int, title: String, val dept: Dept) {
    var title: String? = null
        private set
    private var sections: Collection<Section>? = null

    init {
        this.title = title
    }

    fun getSections(): Collection<Section> {
        if (sections == null)
            sections = dao.getSections(id)
        return sections
    }

    fun changeTitle(newtitle: String) {
        title = newtitle
        dao.changeTitle(id, newtitle)
    }
}
