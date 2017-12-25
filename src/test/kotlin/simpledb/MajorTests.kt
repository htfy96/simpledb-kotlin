package simpledb

import org.junit.jupiter.api.*
import simpledb.remote.SimpleDriver
import java.sql.Connection
import kotlin.test.assertEquals


object MajorTests {
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

    private val studentMajorList = listOf(
            "math" to setOf("amy", "sue", "kim", "pat"),
            "compsci" to setOf("joe", "max", "lee"),
            "drama" to setOf("bob", "art")
    )

    @TestFactory
    fun findMajors() = studentMajorList.map({ (input, expected) ->
        DynamicTest.dynamicTest("Test members of dept $input") {
            val conn: Connection?
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

    private val changeMajorList = listOf(
            "amy" to "drama",
            "bob" to "compsci",
            "art" to "compsci",
            "lee" to "math",
            "max" to "math",
            "sue" to "drama",
            "kim" to "compsci",
            "pat" to "drama",
            "joe" to "drama"
    )

    private val majorNameToIdMap = mapOf(
            "compsci" to 10,
            "math" to 20,
            "drama" to 30
    )

    private fun getMajorIdBySname(conn: Connection, sname: String): List<Int> {
        val q1stmt = conn.createStatement()
        val q1rs = q1stmt.executeQuery("select MajorId from student where SName='$sname'")
        return q1rs.use {
            generateSequence {
                if (q1rs.next()) q1rs.getInt("MajorId") else null
            }.toList()
        }
    }

    private fun dumpStudentTable(conn: Connection) {
        val stmt = conn.createStatement()
        val rs = stmt.executeQuery("select MajorId, SName from student")
        rs.use {
            while (rs.next()) {
                val sname = rs.getString("SName")
                val majorid = rs.getInt("MajorId")
                println("Sname=$sname majorid=$majorid")
            }
        }
    }

        @TestFactory
        fun changeMajors() = changeMajorList.map({ (sname, targetmajor) ->
            DynamicTest.dynamicTest("Changing $sname 's major to $targetmajor") {
                val d = SimpleDriver()
                val conn = d.connect("jdbc:simpledb://localhost", java.util.Properties())

                val oldResult = getMajorIdBySname(conn, sname)
                print("old result=$oldResult")

                try {
                    Assertions.assertEquals(1, oldResult.count())
                } catch (e: AssertionError) {
                    dumpStudentTable(conn)
                    throw e
                }
                val oldMajorId = oldResult[0]
                println("old major id=$oldMajorId")

                val stmt = conn.createStatement()
                val targetMajorId = majorNameToIdMap[targetmajor]!!
                val cmd = "update STUDENT set MajorId=$targetMajorId where SName = '$sname'"
                Assertions.assertEquals(1, stmt.executeUpdate(cmd))

                val newResult = getMajorIdBySname(conn, sname)
                Assertions.assertEquals(1, newResult.count())
                val newMajorId = newResult[0]
                print("new major id=$newMajorId")
                Assertions.assertEquals(targetMajorId, newMajorId)

                val recoverStmt = conn.createStatement()
                val recoverResult = recoverStmt.executeUpdate(
                        "update STUDENT set MajorId=$oldMajorId where SName = '$sname'"
                )
                assertEquals(1, recoverResult)
                conn.close()
            }
        })

    }