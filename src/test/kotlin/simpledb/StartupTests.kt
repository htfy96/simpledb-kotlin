package simpledb

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeAll

object StartupTests {
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

    @Test
    fun createStudentDB() {
        createTables()
    }
}