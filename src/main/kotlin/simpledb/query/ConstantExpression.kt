package simpledb.query

import simpledb.record.Schema

/**
 * An expression consisting entirely of a single constant.
 * @author Edward Sciore
 */
class ConstantExpression
/**
 * Creates a new expression by wrapping a constant.
 * @param c the constant
 */
(private val `val`: Constant) : Expression {

    /**
     * Returns true.
     * @see simpledb.query.Expression.isConstant
     */
    override val isConstant: Boolean
        get() = true

    /**
     * Returns false.
     * @see simpledb.query.Expression.isFieldName
     */
    override val isFieldName: Boolean
        get() = false

    /**
     * Unwraps the constant and returns it.
     * @see simpledb.query.Expression.asConstant
     */
    override fun asConstant(): Constant {
        return `val`
    }

    /**
     * This method should never be called.
     * Throws a ClassCastException.
     * @see simpledb.query.Expression.asFieldName
     */
    override fun asFieldName(): String {
        throw ClassCastException()
    }

    /**
     * Returns the constant, regardless of the scan.
     * @see simpledb.query.Expression.evaluate
     */
    override fun evaluate(s: Scan): Constant {
        return `val`
    }

    /**
     * Returns true, because a constant applies to any schema.
     * @see simpledb.query.Expression.appliesTo
     */
    override fun appliesTo(sch: Schema): Boolean {
        return true
    }

    override fun toString(): String {
        return `val`.toString()
    }
}
