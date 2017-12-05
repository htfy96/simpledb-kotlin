import java.sql.*
import java.io.*
import javax.servlet.ServletException
import javax.servlet.http.*
import org.apache.derby.jdbc.ClientDriver

class ServletFindMajors : HttpServlet() {
    @Throws(IOException::class, ServletException::class)
    fun doGet(request: HttpServletRequest,
              response: HttpServletResponse) {
        val major = request.getParameter("major")

        response.setContentType("text/html")
        val out = response.getWriter()
        out.println("<html>")
        out.println("<head>")
        out.println("<title> Student Majors </title>")
        out.println("</head>")
        out.println("<body>")
        out.println("<P>Here are the $major majors:")

        var conn: Connection? = null
        try {
            // Step 1: connect to database server
            val d = ClientDriver()
            val url = "jdbc:derby://localhost/studentdb"
            conn = d.connect(url, null)

            // Step 2: execute the query
            val qry = ("select SName, GradYear "
                    + "from STUDENT, DEPT "
                    + "where DId = MajorId and DName = ?")
            val pstmt = conn!!.prepareStatement(qry)
            pstmt.setString(1, major)
            val rs = pstmt.executeQuery()

            // Step 3: loop through the result set
            out.println("<P><table border=1 cellpadding=2>")
            out.println("<tr><th>Name</th><th>GradYear</th></tr>")
            while (rs.next()) {
                val name = rs.getString("SName")
                val year = rs.getInt("GradYear")
                out.println("<tr><td>$name</td><td>$year</td></tr>")
            }
            out.println("</table>")
            rs.close()
        } catch (e: Exception) {
            e.printStackTrace()
            out.println("SQL Exception. Execution aborted")
        } finally {
            try {
                if (conn != null)
                    conn.close()
            } catch (e: SQLException) {
                e.printStackTrace()
                out.println("Could not close database")
            }

            out.println("</body>")
            out.println("</html>")
        }
    }
}



