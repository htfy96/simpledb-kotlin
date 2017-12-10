package simpledb

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeAll

object StartupTests {
    @BeforeAll
    @JvmStatic
    fun startupServer() {
        startupDBServer()
    }

    @Test
    fun createStudentDB() {
        createTables()
    }
}