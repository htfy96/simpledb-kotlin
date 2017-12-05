import java.awt.*
import java.awt.event.*
import javax.swing.*
import javax.swing.table.*
import javax.persistence.*

object JPAStudentInfo {
    @JvmStatic
    fun main(args: Array<String>) {
        val emf = Persistence.createEntityManagerFactory("studentdb")
        val em = emf.createEntityManager()
        val frame = TSFrame(em)
        frame.isVisible = true
    }
}

internal class TSFrame(em: EntityManager) : JFrame() {
    init {
        title = "Student Transcript Info"
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        setSize(550, 150)
        setLocation(200, 200)
        contentPane.add(TSPanel(em))
    }
}

internal class TSPanel(private val em: EntityManager) : JPanel() {
    private val inputLbl = JLabel("Enter Student ID: ")
    private val txt = JTextField(4)
    private val btn1 = JButton("SHOW TRANSCRIPT")
    private val btn2 = JButton("CHANGE GRADYEAR")
    private val btn3 = JButton("CLOSE")
    private val outputLbl = JLabel("")
    private val courses: DefaultTableModel

    init {
        val columnNames = arrayOf<Any>("Title", "Year", "Grade")
        courses = DefaultTableModel(columnNames, 0)
        val tbl = JTable(courses)
        val sp = JScrollPane(tbl)
        add(inputLbl)
        add(txt)
        add(btn1)
        add(btn2)
        add(btn3)
        add(outputLbl)
        add(sp)

        btn1.addActionListener {
            val sid = Integer.parseInt(txt.text)
            em.getTransaction().begin()
            val s = em.find(Student::class.java, sid)
            display(s)
            em.getTransaction().commit()
        }

        btn2.addActionListener {
            val yearstring = JOptionPane.showInputDialog("Enter new grad year")
            val sid = Integer.parseInt(txt.text)
            val newyear = Integer.parseInt(yearstring)
            em.getTransaction().begin()
            val s = em.find(Student::class.java, sid)
            s.changeGradYear(newyear)
            display(s)
            em.getTransaction().commit()
        }

        btn3.addActionListener {
            em.close()
            isVisible = false
            System.exit(0)
        }
    }

    private fun display(s: Student?) {
        courses.rowCount = 0
        if (s == null)
            outputLbl.text = "            No such student!"
        else {
            outputLbl.text = ("Name: " + s.name
                    + "    Graduation Year: " + s.gradYear)
            for (e in s.enrollments) {
                val k = e.section
                val c = k.course
                val row = arrayOf(c.title, k.yearOffered, e.grade)
                courses.addRow(row)
            }
        }
    }
}
