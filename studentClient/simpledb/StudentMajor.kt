import java.sql.*
import simpledb.remote.SimpleDriver

object StudentMajor {
    @JvmStatic
    fun main(args: Array<String>) {
        var conn: Connection? = null
        try {
            // Step 1: connect to database server
            val d = SimpleDriver()
            conn = d.connect("jdbc:simpledb://localhost", null)

            // Step 2: execute the query
            val stmt = conn.createStatement()
            val qry = ("select SName, DName "
                    + "from DEPT, STUDENT "
                    + "where MajorId = DId")
            val rs = stmt.executeQuery(qry)

            // Step 3: loop through the result set
            println("Name\tMajor")
            while (rs.next()) {
                val sname = rs.getString("SName")
                val dname = rs.getString("DName")
                println(sname + "\t" + dname)
            }
            rs.close()
        } catch (e: SQLException) {
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
