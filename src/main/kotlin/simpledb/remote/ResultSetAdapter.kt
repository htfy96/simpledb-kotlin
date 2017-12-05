package simpledb.remote

import java.sql.*
import java.util.Calendar
import java.io.*
import java.math.BigDecimal
import java.net.URL

/**
 * This class implements all of the methods of the ResultSet interface,
 * by throwing an exception for each one.
 * Subclasses (such as SimpleResultSet) can override those methods that
 * it want to implement.
 * @author Edward Sciore
 */
abstract class ResultSetAdapter : ResultSet {
    @Throws(SQLException::class)
    override fun absolute(row: Int): Boolean {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun afterLast() {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun beforeFirst() {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun cancelRowUpdates() {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun clearWarnings() {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun close() {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun deleteRow() {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun findColumn(columnLabel: String): Int {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun first(): Boolean {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getArray(columnIndex: Int): java.sql.Array {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getArray(columnLabel: String): java.sql.Array {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getAsciiStream(columnIndex: Int): InputStream {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getAsciiStream(columnLabel: String): InputStream {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getBigDecimal(columnIndex: Int): BigDecimal {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getBigDecimal(columnLabel: String): BigDecimal {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getBigDecimal(columnIndex: Int, scale: Int): BigDecimal {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getBigDecimal(columnLabel: String, scale: Int): BigDecimal {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getBinaryStream(columnIndex: Int): InputStream {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getBinaryStream(columnLabel: String): InputStream {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getBlob(columnIndex: Int): Blob {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getBlob(columnLabel: String): Blob {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getBoolean(columnIndex: Int): Boolean {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getBoolean(columnLabel: String): Boolean {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getByte(columnIndex: Int): Byte {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getByte(columnLabel: String): Byte {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getBytes(columnIndex: Int): ByteArray {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getBytes(columnLabel: String): ByteArray {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getCharacterStream(columnIndex: Int): Reader {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getCharacterStream(columnLabel: String): Reader {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getClob(columnIndex: Int): Clob {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getClob(columnLabel: String): Clob {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getConcurrency(): Int {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getCursorName(): String {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getDate(columnIndex: Int): Date {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getDate(columnLabel: String): Date {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getDate(columnIndex: Int, cal: Calendar): Date {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getDate(columnLabel: String, cal: Calendar): Date {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getDouble(columnIndex: Int): Double {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getDouble(columnLabel: String): Double {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getFetchDirection(): Int {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getFetchSize(): Int {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getFloat(columnIndex: Int): Float {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getFloat(columnLabel: String): Float {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getHoldability(): Int {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getInt(columnIndex: Int): Int {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getInt(columnLabel: String): Int {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getLong(columnIndex: Int): Long {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getLong(columnLabel: String): Long {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getMetaData(): ResultSetMetaData {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getNCharacterStream(columnIndex: Int): Reader {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getNCharacterStream(columnLabel: String): Reader {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getNClob(columnIndex: Int): NClob {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getNClob(columnLabel: String): NClob {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getNString(columnIndex: Int): String {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getNString(columnLabel: String): String {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getObject(columnIndex: Int): Any {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getObject(columnLabel: String): Any {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getObject(columnIndex: Int, map: Map<String, Class<*>>): Any {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getObject(columnLabel: String, map: Map<String, Class<*>>): Any {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getRef(columnIndex: Int): Ref {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getRef(columnLabel: String): Ref {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getRow(): Int {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getRowId(columnIndex: Int): RowId {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getRowId(columnLabel: String): RowId {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getShort(columnIndex: Int): Short {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getShort(columnLabel: String): Short {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getSQLXML(columnIndex: Int): SQLXML {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getSQLXML(columnLabel: String): SQLXML {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getStatement(): Statement {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getString(columnIndex: Int): String {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getString(columnLabel: String): String {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getTime(columnIndex: Int): Time {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getTime(columnLabel: String): Time {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getTime(columnIndex: Int, cal: Calendar): Time {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getTime(columnLabel: String, cal: Calendar): Time {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getTimestamp(columnIndex: Int): Timestamp {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getTimestamp(columnLabel: String): Timestamp {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getTimestamp(columnIndex: Int, cal: Calendar): Timestamp {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getTimestamp(columnLabel: String, cal: Calendar): Timestamp {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getType(): Int {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getUnicodeStream(columnIndex: Int): InputStream {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getUnicodeStream(columnLabel: String): InputStream {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getURL(columnIndex: Int): URL {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getURL(columnLabel: String): URL {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun getWarnings(): SQLWarning {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun insertRow() {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun isAfterLast(): Boolean {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun isBeforeFirst(): Boolean {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun isClosed(): Boolean {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun isFirst(): Boolean {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun isLast(): Boolean {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun last(): Boolean {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun moveToCurrentRow() {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun moveToInsertRow() {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun next(): Boolean {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun previous(): Boolean {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun refreshRow() {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun relative(rows: Int): Boolean {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun rowDeleted(): Boolean {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun rowInserted(): Boolean {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun rowUpdated(): Boolean {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun setFetchDirection(direction: Int) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun setFetchSize(rows: Int) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateArray(columnIndex: Int, x: java.sql.Array) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateArray(columnLabel: String, x: java.sql.Array) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateAsciiStream(columnIndex: Int, x: InputStream) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateAsciiStream(columnLabel: String, x: InputStream) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateAsciiStream(columnIndex: Int, x: InputStream, length: Int) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateAsciiStream(columnLabel: String, x: InputStream, length: Int) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateAsciiStream(columnIndex: Int, x: InputStream, length: Long) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateAsciiStream(columnLabel: String, x: InputStream, length: Long) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateBigDecimal(columnIndex: Int, x: BigDecimal) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateBigDecimal(columnLabel: String, x: BigDecimal) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateBinaryStream(columnIndex: Int, x: InputStream) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateBinaryStream(columnLabel: String, x: InputStream) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateBinaryStream(columnIndex: Int, x: InputStream, length: Int) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateBinaryStream(columnLabel: String, x: InputStream, length: Int) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateBinaryStream(columnIndex: Int, x: InputStream, length: Long) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateBinaryStream(columnLabel: String, x: InputStream, length: Long) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateBlob(columnIndex: Int, x: Blob) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateBlob(columnLabel: String, x: Blob) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateBlob(columnIndex: Int, inputStream: InputStream) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateBlob(columnLabel: String, inputStream: InputStream) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateBlob(columnIndex: Int, inputStream: InputStream, length: Long) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateBlob(columnLabel: String, inputStream: InputStream, length: Long) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateBoolean(columnIndex: Int, x: Boolean) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateBoolean(columnLabel: String, x: Boolean) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateByte(columnIndex: Int, x: Byte) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateByte(columnLabel: String, x: Byte) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateBytes(columnIndex: Int, x: ByteArray) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateBytes(columnLabel: String, x: ByteArray) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateCharacterStream(columnIndex: Int, x: Reader) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateCharacterStream(columnLabel: String, x: Reader) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateCharacterStream(columnIndex: Int, x: Reader, length: Int) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateCharacterStream(columnLabel: String, x: Reader, length: Int) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateCharacterStream(columnIndex: Int, x: Reader, length: Long) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateCharacterStream(columnLabel: String, x: Reader, length: Long) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateClob(columnIndex: Int, x: Clob) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateClob(columnLabel: String, x: Clob) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateClob(columnIndex: Int, reader: Reader) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateClob(columnLabel: String, reader: Reader) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateClob(columnIndex: Int, reader: Reader, length: Long) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateClob(columnLabel: String, reader: Reader, length: Long) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateDate(columnIndex: Int, x: Date) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateDate(columnLabel: String, x: Date) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateDouble(columnIndex: Int, x: Double) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateDouble(columnLabel: String, x: Double) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateFloat(columnIndex: Int, x: Float) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateFloat(columnLabel: String, x: Float) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateInt(columnIndex: Int, x: Int) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateInt(columnLabel: String, x: Int) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateLong(columnIndex: Int, x: Long) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateLong(columnLabel: String, x: Long) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateNCharacterStream(columnIndex: Int, x: Reader) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateNCharacterStream(columnLabel: String, x: Reader) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateNCharacterStream(columnIndex: Int, x: Reader, length: Long) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateNCharacterStream(columnLabel: String, x: Reader, length: Long) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateNClob(columnIndex: Int, nclob: NClob) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateNClob(columnLabel: String, nclob: NClob) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateNClob(columnIndex: Int, reader: Reader) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateNClob(columnLabel: String, reader: Reader) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateNClob(columnIndex: Int, reader: Reader, length: Long) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateNClob(columnLabel: String, reader: Reader, length: Long) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateNString(columnIndex: Int, nstring: String) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateNString(columnLabel: String, nstring: String) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateNull(columnIndex: Int) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateNull(columnLabel: String) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateObject(columnIndex: Int, x: Any) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateObject(columnLabel: String, x: Any) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateObject(columnIndex: Int, x: Any, scale: Int) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateObject(columnLabel: String, x: Any, scale: Int) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateRef(columnIndex: Int, x: Ref) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateRef(columnLabel: String, x: Ref) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateRow() {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateRowId(columnIndex: Int, x: RowId) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateRowId(columnLabel: String, x: RowId) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateShort(columnIndex: Int, x: Short) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateShort(columnLabel: String, x: Short) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateSQLXML(columnIndex: Int, x: SQLXML) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateSQLXML(columnLabel: String, x: SQLXML) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateString(columnIndex: Int, x: String) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateString(columnLabel: String, x: String) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateTime(columnIndex: Int, x: Time) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateTime(columnLabel: String, x: Time) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateTimestamp(columnIndex: Int, x: Timestamp) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun updateTimestamp(columnLabel: String, x: Timestamp) {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun wasNull(): Boolean {
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
    override fun <T> getObject(columnIndex: Int, type: Class<T>): T {
        throw SQLException("operation not implemented")
    }

    @Throws(SQLException::class)
    override fun <T> getObject(columnLabel: String, type: Class<T>): T {
        throw SQLException("operation not implemented")
    }
}