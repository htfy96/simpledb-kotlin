package simpledb.remote

import java.sql.*
import java.util.*
import java.util.concurrent.*

/**
 * This class implements all of the methods of the Connection interface,
 * by throwing an exception for each one.
 * Subclasses (such as SimpleConnection) can override those methods that
 * it want to implement.
 * @author Edward Sciore
 */
abstract class ConnectionAdapter : Connection {
    @Throws(SQLException::class)
    override fun clearWarnings() {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun close() {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun commit() {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun createArrayOf(typeName: String, elements: Array<Any>): java.sql.Array {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun createBlob(): Blob {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun createClob(): Clob {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun createNClob(): NClob {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun createSQLXML(): SQLXML {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun createStatement(): Statement {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun createStatement(resultSetType: Int, resultSetConcurrency: Int): Statement {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun createStatement(resultSetType: Int, resultSetConcurrency: Int, resultSetHoldability: Int): Statement {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun createStruct(typeName: String, attributes: Array<Any>): Struct {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getAutoCommit(): Boolean {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getCatalog(): String {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getClientInfo(): Properties {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getClientInfo(name: String): String {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getHoldability(): Int {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getMetaData(): DatabaseMetaData {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getTransactionIsolation(): Int {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getTypeMap(): Map<String, Class<*>> {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getWarnings(): SQLWarning {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun isClosed(): Boolean {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun isReadOnly(): Boolean {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun isValid(timeout: Int): Boolean {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun nativeSQL(sql: String): String {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun prepareCall(sql: String): CallableStatement {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun prepareCall(sql: String, resultSetType: Int, resultSetConcurrency: Int): CallableStatement {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun prepareCall(sql: String, resultSetType: Int, resultSetConcurrency: Int, resultSetHoldability: Int): CallableStatement {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun prepareStatement(sql: String): PreparedStatement {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun prepareStatement(sql: String, autoGeneratedKeys: Int): PreparedStatement {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun prepareStatement(sql: String, columnIndexes: IntArray): PreparedStatement {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun prepareStatement(sql: String, resultSetType: Int, resultSetConcurrency: Int): PreparedStatement {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun prepareStatement(sql: String, resultSetType: Int, resultSetConcurrency: Int, resultSetHoldability: Int): PreparedStatement {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun prepareStatement(sql: String, columnNames: Array<String>): PreparedStatement {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun releaseSavepoint(savepoint: Savepoint) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun rollback() {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun rollback(svepoint: Savepoint) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun setAutoCommit(autoCommit: Boolean) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun setCatalog(catalog: String) {
        throw SQLException("operation not implemented")
    }

    override fun setClientInfo(name: String, value: String) {}

    override fun setClientInfo(properties: Properties) {}

    @Throws(SQLException::class)
    override fun setHoldability(holdability: Int) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun setReadOnly(readOnly: Boolean) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun setSavepoint(): Savepoint {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun setSavepoint(name: String): Savepoint {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun setTransactionIsolation(level: Int) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun setTypeMap(map: Map<String, Class<*>>) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun isWrapperFor(iface: Class<*>): Boolean {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun <T> unwrap(iface: Class<T>): T {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun abort(executor: Executor) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getNetworkTimeout(): Int {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getSchema(): String {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun setNetworkTimeout(executor: Executor, milliseconds: Int) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun setSchema(schema: String) {
        throw SQLException("operation not implemented")
    }
}