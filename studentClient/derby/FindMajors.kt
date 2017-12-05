import java.sql.*
import org.apache.derby.jdbc.ClientDriver

object FindMajors {
    @JvmStatic
    fun main(args: Array<String>) {
        val major = args[0]
        println("Here are the $major majors")
        println("Name\tGradYear")

        var conn: Connection? = null
        try {
            // Step 1: connect to database server
            val d = ClientDriver()
            val url = "jdbc:derby://localhost/studentdb"
            conn = d.connect(url, null)

            // Step 2: execute the query
            val stmt = conn!!.createStatement()
            val qry = ("select sname, gradyear "
                    + "from student, dept "
                    + "where did = majorid "
                    + "and dname = '" + major + "'")
            val rs = stmt.executeQuery(qry)

            // Step 3: loop through the result set
            while (rs.next()) {
                val sname = rs.getString("sname")
                val gradyear = rs.getInt("gradyear")
                println(sname + "\t" + gradyear)
            }
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
