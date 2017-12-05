package simpledb.query

import simpledb.record.Schema

/**
 * The interface corresponding to SQL expressions.
 * @author Edward Sciore
 */
interface Expression {

    /**
     * Returns true if the expression is a constant.
     * @return true if the expression is a constant
     */
    val isConstant: Boolean

    /**
     * Returns true if the expression is a field reference.
     * @return true if the expression denotes a field
     */
    val isFieldName: Boolean

    /**
     * Returns the constant corresponding to a constant expression.
     * Throws an exception if the expression does not
     * denote a constant.
     * @return the expression as a constant
     */
    fun asConstant(): Constant

    /**
     * Returns the field name corresponding to a constant expression.
     * Throws an exception if the expression does not
     * denote a field.
     * @return the expression as a field name
     */
    fun asFieldName(): String

    /**
     * Evaluates the expression with respect to the
     * current record of the specified scan.
     * @param s the scan
     * @return the value of the expression, as a Constant
     */
    fun evaluate(s: Scan): Constant

    /**
     * Determines if all of the fields mentioned in this expression
     * are contained in the specified schema.
     * @param sch the schema
     * @return true if all fields in the expression are in the schema
     */
    fun appliesTo(sch: Schema): Boolean
}
