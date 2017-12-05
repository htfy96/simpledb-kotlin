import javax.persistence.*
import java.util.*

@Entity
class Course {

    @Id
    val id: Int
    var title: String? = null
        private set

    @OneToMany(mappedBy = "course")
    val sections: Collection<Section>? = null

    @ManyToOne
    @JoinColumn(name = "DeptId")
    val dept: Dept

    constructor() {}

    constructor(cid: Int, title: String, dept: Dept) {
        this.id = cid
        this.title = title
        this.dept = dept
    }

    fun changeTitle(title: String) {
        this.title = title
    }
}
