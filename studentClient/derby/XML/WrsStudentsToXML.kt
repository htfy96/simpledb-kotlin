import java.sql.*
import javax.sql.rowset.*
import org.apache.derby.jdbc.ClientDriver
import com.sun.rowset.*
import java.io.*

object WrsStudentsToXML {
    val OUTFILE = "students2005.xml"

    @JvmStatic
    fun main(args: Array<String>) {
        var conn: Connection? = null
        try {
            // Step 1: connect to database server
            val d = ClientDriver()
            val url = "jdbc:derby://localhost/studentdb"
            conn = d.connect(url, null)

            // Step 2: execute the query
            val stmt = conn!!.createStatement()
            val qry = ("select s.SName, s.GradYear, c.Title, "
                    + "k.YearOffered, e.Grade "
                    + "from STUDENT s, ENROLL e, SECTION k, COURSE c "
                    + "where s.SId=e.StudentId and e.SectionId=k.SectId "
                    + "and k.CourseId=c.CId and s.GradYear=2005")

            val rs = stmt.executeQuery(qry)

            val w = FileWriter(OUTFILE)
            val wrs = WebRowSetImpl()
            wrs.populate(rs)
            wrs.writeXml(w)
            rs.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            // Step 4: close the connection
            try {
                if (conn != null)
                    conn.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }

        }
    }
}
