import java.sql.*
import org.apache.derby.jdbc.ClientDriver
import java.io.*

object StudentsToXML {
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
            writeXML(w, rs, "Student")
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

    @Throws(Exception::class)
    fun writeXML(w: Writer, rs: ResultSet, elementname: String) {
        w.write("<" + elementname + "s>\n")

        val md = rs.metaData
        val colcount = md.columnCount
        while (rs.next()) {
            w.write("\t<$elementname>\n")
            for (i in 1..colcount) {
                val col = md.getColumnName(i)
                val `val` = rs.getString(i)
                w.write("\t\t<$col>$`val`</$col>\n")
            }
            w.write("\t</$elementname>\n")
        }
        w.write("</" + elementname + "s>\n")
    }
}
