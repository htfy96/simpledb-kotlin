import java.sql.*
import org.apache.derby.jdbc.ClientDriver

object ChangeMajor {
    @JvmStatic
    fun main(args: Array<String>) {
        var conn: Connection? = null
        try {
            val d = ClientDriver()
            val url = "jdbc:derby://localhost/studentdb"
            conn = d.connect(url, null)
            val stmt = conn!!.createStatement()

            stmt.executeUpdate("update STUDENT set MajorId=30 where SName='amy'")
            println("Amy is now a drama major.")
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            try {
                if (conn != null)
                    conn.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }

        }
    }
}
