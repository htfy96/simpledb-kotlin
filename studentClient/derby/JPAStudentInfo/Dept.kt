import javax.persistence.*
import java.util.*

@Entity
class Dept {

    @Id
    val id: Int
    var name: String? = null
        private set

    @OneToMany(mappedBy = "major")
    val majors: Collection<Student>? = null

    constructor() {}

    constructor(did: Int, dname: String) {
        this.id = did
        this.name = dname
    }

    fun changeName(newname: String) {
        name = newname
    }
}
