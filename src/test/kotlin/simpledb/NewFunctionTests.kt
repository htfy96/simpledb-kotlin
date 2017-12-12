package simpledb

import org.junit.jupiter.api.*
import simpledb.remote.SimpleDriver
import java.sql.Connection
import kotlin.test.assertEquals

object NewFunctionTests {
    @BeforeAll
    @JvmStatic
    fun startupServer() {
        TestServerBootstraper.startupDBServer()
        createTables()
    }

    @AfterAll
    @JvmStatic
    fun shutdownServer() {
        TestServerBootstraper.shutdownDBServer()
    }

    private val studentList = listOf(
            "amy",
            "bob",
            "art",
            "lee",
            "max",
            "sue",
            "kim",
            "pat",
            "joe"
    )

    @TestFactory
    fun findStudent() = studentList.map({ input ->
        DynamicTest.dynamicTest("Test student $input") {
            val conn: Connection?
            // Step 1: connect to database server
            val d = SimpleDriver()
            conn = d.connect("jdbc:simpledb://localhost", java.util.Properties())

            // Step 2: execute the query
            val stmt = conn.createStatement()
            val qry = ("select * "
                    + "from student "
                    + "where sname = '" + input + "'")
            val rs = stmt.executeQuery(qry)
            val result = rs.use {
                generateSequence {
                    if (rs.next())
                        rs.getString("sid") +
                                rs.getString("sname") +
                                rs.getString("majorid") +
                                rs.getString("gradyear")
                    else
                        null
                }
            }

            //Step 3: execute the ground truth query
            val expected_stmt = conn.createStatement()
            val expected_qry = ("select sid, sname, majorid, gradyear "
                    + "from student "
                    + "where sname = '" + input + "'")
            val expected_rs = expected_stmt.executeQuery(expected_qry)
            val expected = expected_rs.use {
                generateSequence {
                    if (expected_rs.next())
                        expected_rs.getString("sid") +
                                expected_rs.getString("sname") +
                                expected_rs.getString("majorid") +
                                expected_rs.getString("gradyear")
                    else
                        null
                }
            }

            Assertions.assertEquals(expected, result)
        }
    })
}