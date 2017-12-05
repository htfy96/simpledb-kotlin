import simpledb.tx.Transaction
import simpledb.query.*
import simpledb.server.SimpleDB

/* This is a version of the StudentMajor program that
 * accesses the SimpleDB classes directly (instead of
 * connecting to it as a JDBC client).  You can run it
 * without having the server also run.
 *
 * These kind of programs are useful for debugging
 * your changes to the SimpleDB source code.
 */

object StudentMajorNoServer {
    @JvmStatic
    fun main(args: Array<String>) {
        try {
            // analogous to the driver
            SimpleDB.init("studentdb")

            // analogous to the connection
            val tx = Transaction()

            // analogous to the statement
            val qry = ("select SName, DName "
                    + "from DEPT, STUDENT "
                    + "where MajorId = DId")
            val p = SimpleDB.planner().createQueryPlan(qry, tx)

            // analogous to the result set
            val s = p.open()

            println("Name\tMajor")
            while (s.next()) {
                val sname = s.getString("sname") //SimpleDB stores field names
                val dname = s.getString("dname") //in lower case
                println(sname + "\t" + dname)
            }
            s.close()
            tx.commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
