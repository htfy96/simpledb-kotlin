package simpledb.query

/**
 * The class that wraps Java strings as database constants.
 * @author Edward Sciore
 */
class StringConstant
/**
 * Create a constant by wrapping the specified string.
 * @param s the string value
 */
(private val `val`: String) : Constant {

    /**
     * Unwraps the string and returns it.
     * @see simpledb.query.Constant.asJavaVal
     */
    override fun asJavaVal(): String {
        return `val`
    }

    override fun equals(other: Any?): Boolean {
        val sc = other as StringConstant?
        return sc != null && `val` == sc.`val`
    }

    override fun compareTo(other: Constant): Int {
        val sc = other as StringConstant
        return `val`.compareTo(sc.`val`)
    }

    override fun hashCode(): Int {
        return `val`.hashCode()
    }

    override fun toString(): String {
        return `val`
    }
}
