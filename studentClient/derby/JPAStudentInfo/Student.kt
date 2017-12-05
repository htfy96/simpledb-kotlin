import javax.persistence.*
import java.util.*

@Entity
class Student {

    @Id
    val id: Int
    val name: String
    var gradYear: Int = 0
        private set

    @OneToMany(mappedBy = "student")
    val enrollments: Collection<Enroll>? = null

    @ManyToOne
    @JoinColumn(name = "MajorId")
    var major: Dept? = null
        private set

    constructor() {}

    constructor(sid: Int, sname: String, gradyear: Int, major: Dept) {
        this.id = sid
        this.name = sname
        this.gradYear = gradyear
        this.major = major
    }

    fun changeGradYear(year: Int) {
        gradYear = year
    }

    fun changeMajor(dept: Dept) {
        major = dept
    }
}
