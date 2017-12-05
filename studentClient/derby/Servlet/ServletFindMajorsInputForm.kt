import java.sql.*
import org.apache.derby.jdbc.ClientDriver
import java.io.*
import javax.servlet.ServletException
import javax.servlet.http.*


class ServletFindMajorsInputForm : HttpServlet() {
    @Throws(IOException::class, ServletException::class)
    fun doGet(request: HttpServletRequest,
              response: HttpServletResponse) {
        response.setContentType("text/html")
        val out = response.getWriter()
        out.println("<html>")
        out.println("<head>")
        out.println("<title> Find Majors </title>")
        out.println("</head>")
        out.println("<body>")
        out.println("<form method=\"get\" " + "action=\"/studentdb/ServletFindMajors\">")
        out.println("Enter major:")
        out.println("<select name=\"major\">")

        var conn: Connection? = null
        try {
            // Step 1: connect to database server
            val d = ClientDriver()
            val url = "jdbc:derby://localhost/studentdb"
            conn = d.connect(url, null)

            // Step 2: execute the query
            val stmt = conn!!.createStatement()
            val qry = "select dname from dept"
            val rs = stmt.executeQuery(qry)

            // Step 3: loop through the result set
            while (rs.next()) {
                val dname = rs.getString("DName")
                out.println("<option value=\"" + dname + "\">"
                        + dname + "</option>")
            }
            rs.close()
            out.println("</select>")
            out.println("<p><input type=\"submit\" value=\"Find Majors\">")
            out.println("</form>")
        } catch (e: Exception) {
            e.printStackTrace()
            out.println("SQL Exception. Execution aborted")
        } finally {
            out.println("</body>")
            out.println("</html>")
            try {
                if (conn != null)
                    conn.close()
            } catch (e: SQLException) {
                e.printStackTrace()
                out.println("Could not close database")
            }

        }
    }
}



