import java.sql.*
import org.apache.derby.jdbc.ClientDriver
import java.io.*

object SQLInterpreter {
    private var conn: Connection? = null

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val d = ClientDriver()
            val url = "jdbc:derby://localhost/studentdb"
            conn = d.connect(url, null)

            val rdr = InputStreamReader(System.`in`)
            val br = BufferedReader(rdr)

            while (true) {
                // process one line of input
                print("\nSQL> ")
                val cmd = br.readLine().trim { it <= ' ' }
                println()
                if (cmd.startsWith("exit"))
                    break
                else if (cmd.startsWith("select"))
                    doQuery(cmd)
                else
                    doUpdate(cmd)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (conn != null)
                    conn!!.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    private fun doQuery(cmd: String) {
        try {
            val stmt = conn!!.createStatement()
            val rs = stmt.executeQuery(cmd)
            val md = rs.metaData
            val numcols = md.columnCount
            var totalwidth = 0

            // print header
            for (i in 1..numcols) {
                val width = md.getColumnDisplaySize(i)
                totalwidth += width
                val fmt = "%" + width + "s"
                System.out.format(fmt, md.getColumnName(i))
            }
            println()
            for (i in 0 until totalwidth)
                print("-")
            println()

            // print records
            while (rs.next()) {
                for (i in 1..numcols) {
                    val fldname = md.getColumnName(i)
                    val fldtype = md.getColumnType(i)
                    val fmt = "%" + md.getColumnDisplaySize(i)
                    if (fldtype == Types.INTEGER)
                        System.out.format(fmt + "d", rs.getInt(fldname))
                    else
                        System.out.format(fmt + "s", rs.getString(fldname))
                }
                println()
            }
            rs.close()
        } catch (e: SQLException) {
            println("SQL Exception: " + e.message)
            e.printStackTrace()
        }

    }

    private fun doUpdate(cmd: String) {
        try {
            val stmt = conn!!.createStatement()
            val howmany = stmt.executeUpdate(cmd)
            println(howmany.toString() + " records processed")
        } catch (e: SQLException) {
            println("SQL Exception: " + e.message)
            e.printStackTrace()
        }

    }
}