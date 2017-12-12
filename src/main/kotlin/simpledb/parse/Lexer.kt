package simpledb.parse

import java.util.*
import java.io.*

/**
 * The lexical analyzer.
 * @author Edward Sciore
 */
class Lexer
/**
 * Creates a new lexical analyzer for SQL statement s.
 * @param s the SQL statement
 */
(s: String) {
    private var keywords: Collection<String>? = null
    private val tok: StreamTokenizer

    init {
        initKeywords()
        tok = StreamTokenizer(StringReader(s))
        tok.ordinaryChar('.'.toInt())
        tok.lowerCaseMode(true) //ids and keywords are converted
        nextToken()
    }

    //Methods to check the status of the current token

    /**
     * Returns true if the current token is
     * the specified delimiter character.
     * @param d a character denoting the delimiter
     * @return true if the delimiter is the current token
     */
    fun matchDelim(d: Char): Boolean {
        return d == tok.ttype.toChar()
    }

    /**
     * Returns true if the current token is an integer.
     * @return true if the current token is an integer
     */
    fun matchIntConstant(): Boolean {
        return tok.ttype == StreamTokenizer.TT_NUMBER
    }

    /**
     * Returns true if the current token is a string.
     * @return true if the current token is a string
     */
    fun matchStringConstant(): Boolean {
        return '\'' == tok.ttype.toChar()
    }

    /**
     * Returns true if the current token is the specified keyword.
     * @param w the keyword string
     * @return true if that keyword is the current token
     */
    fun matchKeyword(w: String): Boolean {
        return tok.ttype == StreamTokenizer.TT_WORD && tok.sval == w
    }

    /**
     * Returns true if the current token is a legal identifier.
     * @return true if the current token is an identifier
     */
    fun matchId(): Boolean {
        return tok.ttype == 42 || (tok.ttype == StreamTokenizer.TT_WORD && !keywords!!.contains(tok.sval))
    }

    //Methods to "eat" the current token

    /**
     * Throws an exception if the current token is not the
     * specified delimiter.
     * Otherwise, moves to the next token.
     * @param d a character denoting the delimiter
     */
    fun eatDelim(d: Char) {
        if (!matchDelim(d))
            throw BadSyntaxException()
        nextToken()
    }

    /**
     * Throws an exception if the current token is not
     * an integer.
     * Otherwise, returns that integer and moves to the next token.
     * @return the integer value of the current token
     */
    fun eatIntConstant(): Int {
        if (!matchIntConstant())
            throw BadSyntaxException()
        val i = tok.nval.toInt()
        nextToken()
        return i
    }

    /**
     * Throws an exception if the current token is not
     * a string.
     * Otherwise, returns that string and moves to the next token.
     * @return the string value of the current token
     */
    fun eatStringConstant(): String {
        if (!matchStringConstant())
            throw BadSyntaxException()
        val s = tok.sval //constants are not converted to lower case
        nextToken()
        return s
    }

    /**
     * Throws an exception if the current token is not the
     * specified keyword.
     * Otherwise, moves to the next token.
     * @param w the keyword string
     */
    fun eatKeyword(w: String) {
        if (!matchKeyword(w))
            throw BadSyntaxException()
        nextToken()
    }

    /**
     * Throws an exception if the current token is not
     * an identifier.
     * Otherwise, returns the identifier string
     * and moves to the next token.
     * @return the string value of the current token
     */
    fun eatId(): String {
        if (!matchId())
            throw BadSyntaxException()
        val s = tok.sval ?: "*"
        nextToken()
        return s
    }

    private fun nextToken() {
        try {
            tok.nextToken()
        } catch (e: IOException) {
            throw BadSyntaxException()
        }

    }

    private fun initKeywords() {
        keywords = Arrays.asList("select", "from", "where", "and",
                "insert", "into", "values", "delete", "update", "set",
                "create", "table", "int", "varchar", "view", "as", "index", "on")
    }
}