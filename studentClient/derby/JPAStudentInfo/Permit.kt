import javax.persistence.*
import java.util.*

@Entity
class Permit {

    @Id
    val id: Int
    var plate: String? = null
        private set
    var carModel: String? = null
        private set

    @OneToOne
    @JoinColumn(name = "StudId")
    val student: Student

    constructor() {}

    constructor(pid: Int, licensePlate: String, carModel: String, s: Student) {
        this.id = pid
        this.plate = licensePlate
        this.carModel = carModel
        student = s
    }

    fun changePlate(newplate: String) {
        plate = newplate
    }

    fun changeCarModel(newmodel: String) {
        carModel = newmodel
    }
}
