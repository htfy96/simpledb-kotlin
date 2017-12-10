package simpledb

import org.junit.jupiter.api.*
import simpledb.remote.SimpleDriver
import java.sql.Connection


object FindMajorsTests {
    @BeforeAll
    @JvmStatic
    fun startupServer() {
        TestServerBootstraper.startupDBServer()
    }

    @AfterAll
    @JvmStatic
    fun shutdownServer() {
        TestServerBootstraper.shutdownDBServer()
    }

    private val studentMajorList = listOf(
            "math" to setOf("amy", "sue", "kim", "pat"),
            "compsci" to setOf("joe", "max", "lee"),
            "drama" to setOf("bob", "art")
    )

    @TestFactory
    fun findMajors() = studentMajorList.map({ (input, expected) ->
        DynamicTest.dynamicTest("Test members of dept $input") {
            var conn: Connection? = null
            // Step 1: connect to database server
            val d = SimpleDriver()
            conn = d.connect("jdbc:simpledb://localhost", java.util.Properties())

            // Step 2: execute the query
            val stmt = conn.createStatement()
            val qry = ("select sname, gradyear "
                    + "from student, dept "
                    + "where did = majorid "
                    + "and dname = '" + input + "'")
            val rs = stmt.executeQuery(qry)

            val result = rs.use {
                generateSequence {
                    if (rs.next()) rs.getString("sname") else null
                }.toSet()
            }

            Assertions.assertEquals(expected, result)
        }
    })

}