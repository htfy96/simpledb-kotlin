import java.sql.*
import simpledb.remote.SimpleDriver

object ChangeMajor {
    @JvmStatic
    fun main(args: Array<String>) {
        var conn: Connection? = null
        try {
            val d = SimpleDriver()
            conn = d.connect("jdbc:simpledb://localhost", null)
            val stmt = conn.createStatement()

            val cmd = "update STUDENT set MajorId=30 " + "where SName = 'amy'"
            stmt.executeUpdate(cmd)
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
