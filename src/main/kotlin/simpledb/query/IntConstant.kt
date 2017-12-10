package simpledb.query

/**
 * The class that wraps Java ints as database constants.
 * @author Edward Sciore
 */
class IntConstant
/**
 * Create a constant by wrapping the specified int.
 * @param n the int value
 */
(n: Int) : Constant {
    private val `val`: Int

    init {
        `val` = n
    }

    /**
     * Unwraps the Integer and returns it.
     * @see simpledb.query.Constant.asJavaVal
     */
    override fun asJavaVal(): Any {
        return `val`
    }

    override fun equals(other: Any?): Boolean {
        val ic = other as IntConstant?
        return ic != null && `val` == ic.`val`
    }

    override fun compareTo(other: Constant): Int {
        val ic = other as IntConstant
        return `val`.compareTo(ic.`val`)
    }

    override fun hashCode(): Int {
        return `val`.hashCode()
    }

    override fun toString(): String {
        return `val`.toString()
    }
}
