import java.awt.*
import java.awt.event.*
import javax.swing.*
import javax.swing.table.*

object StudentInfo {
    @JvmStatic
    fun main(args: Array<String>) {
        val dbmgr = DatabaseManager()
        val frame = TSFrame(dbmgr)
        frame.isVisible = true
    }
}

internal class TSFrame(dbmgr: DatabaseManager) : JFrame() {
    init {
        title = "Student Info"
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        setSize(550, 150)
        setLocation(200, 200)
        contentPane.add(TSPanel(dbmgr))
    }
}

internal class TSPanel(dbmgr: DatabaseManager) : JPanel() {
    private val inputLbl = JLabel("Enter Student ID: ")
    private val txt = JTextField(4)
    private val btn1 = JButton("SHOW INFO")
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
            val s = dbmgr.findStudent(sid)
            display(s)
            dbmgr.commit()
        }

        btn2.addActionListener {
            val yearstring = JOptionPane.showInputDialog("Enter new grad year")
            val sid = Integer.parseInt(txt.text)
            val newyear = Integer.parseInt(yearstring)
            val s = dbmgr.findStudent(sid)
            s!!.changeGradYear(newyear)
            display(s)
            dbmgr.commit()
        }

        btn3.addActionListener {
            dbmgr.close()
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
