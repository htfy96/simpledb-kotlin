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

    override fun equals(obj: Any?): Boolean {
        val sc = obj as StringConstant?
        return sc != null && `val` == sc.`val`
    }

    override fun compareTo(c: Constant): Int {
        val sc = c as StringConstant
        return `val`.compareTo(sc.`val`)
    }

    override fun hashCode(): Int {
        return `val`.hashCode()
    }

    override fun toString(): String {
        return `val`
    }
}
