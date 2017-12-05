package simpledb.query

import simpledb.record.Schema

/**
 * An expression consisting entirely of a single field.
 * @author Edward Sciore
 */
class FieldNameExpression
/**
 * Creates a new expression by wrapping a field.
 * @param fldname the name of the wrapped field
 */
(private val fldname: String) : Expression {

    /**
     * Returns false.
     * @see simpledb.query.Expression.isConstant
     */
    override val isConstant: Boolean
        get() = false

    /**
     * Returns true.
     * @see simpledb.query.Expression.isFieldName
     */
    override val isFieldName: Boolean
        get() = true

    /**
     * This method should never be called.
     * Throws a ClassCastException.
     * @see simpledb.query.Expression.asConstant
     */
    override fun asConstant(): Constant {
        throw ClassCastException()
    }

    /**
     * Unwraps the field name and returns it.
     * @see simpledb.query.Expression.asFieldName
     */
    override fun asFieldName(): String {
        return fldname
    }

    /**
     * Evaluates the field by getting its value in the scan.
     * @see simpledb.query.Expression.evaluate
     */
    override fun evaluate(s: Scan): Constant {
        return s.getVal(fldname)
    }

    /**
     * Returns true if the field is in the specified schema.
     * @see simpledb.query.Expression.appliesTo
     */
    override fun appliesTo(sch: Schema): Boolean {
        return sch.hasField(fldname)
    }

    override fun toString(): String {
        return fldname
    }
}
