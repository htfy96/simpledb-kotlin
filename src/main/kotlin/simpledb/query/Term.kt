package simpledb.query

import simpledb.record.Schema

/**
 * A term is a comparison between two expressions.
 * @author Edward Sciore
 */
class Term
/**
 * Creates a new term that compares two expressions
 * for equality.
 * @param lhs  the LHS expression
 * @param rhs  the RHS expression
 */
(private val lhs: Expression, private val rhs: Expression) {

    /**
     * Calculates the extent to which selecting on the term reduces
     * the number of records output by a query.
     * For example if the reduction factor is 2, then the
     * term cuts the size of the output in half.
     * @param p the query's plan
     * @return the integer reduction factor.
     */
    fun reductionFactor(p: Plan): Int {
        val lhsName: String
        val rhsName: String
        if (lhs.isFieldName && rhs.isFieldName) {
            lhsName = lhs.asFieldName()
            rhsName = rhs.asFieldName()
            return Math.max(p.distinctValues(lhsName),
                    p.distinctValues(rhsName))
        }
        if (lhs.isFieldName) {
            lhsName = lhs.asFieldName()
            return p.distinctValues(lhsName)
        }
        if (rhs.isFieldName) {
            rhsName = rhs.asFieldName()
            return p.distinctValues(rhsName)
        }
        // otherwise, the term equates constants
        return if (lhs.asConstant() == rhs.asConstant())
            1
        else
            Integer.MAX_VALUE
    }

    /**
     * Determines if this term is of the form "F=c"
     * where F is the specified field and c is some constant.
     * If so, the method returns that constant.
     * If not, the method returns null.
     * @param fldname the name of the field
     * @return either the constant or null
     */
    fun equatesWithConstant(fldname: String): Constant? {
        return if (lhs.isFieldName &&
                lhs.asFieldName() == fldname &&
                rhs.isConstant)
            rhs.asConstant()
        else if (rhs.isFieldName &&
                rhs.asFieldName() == fldname &&
                lhs.isConstant)
            lhs.asConstant()
        else
            null
    }

    /**
     * Determines if this term is of the form "F1=F2"
     * where F1 is the specified field and F2 is another field.
     * If so, the method returns the name of that field.
     * If not, the method returns null.
     * @param fldname the name of the field
     * @return either the name of the other field, or null
     */
    fun equatesWithField(fldname: String): String? {
        return if (lhs.isFieldName &&
                lhs.asFieldName() == fldname &&
                rhs.isFieldName)
            rhs.asFieldName()
        else if (rhs.isFieldName &&
                rhs.asFieldName() == fldname &&
                lhs.isFieldName)
            lhs.asFieldName()
        else
            null
    }

    /**
     * Returns true if both of the term's expressions
     * apply to the specified schema.
     * @param sch the schema
     * @return true if both expressions apply to the schema
     */
    fun appliesTo(sch: Schema): Boolean {
        return lhs.appliesTo(sch) && rhs.appliesTo(sch)
    }

    /**
     * Returns true if both of the term's expressions
     * evaluate to the same constant,
     * with respect to the specified scan.
     * @param s the scan
     * @return true if both expressions have the same value in the scan
     */
    fun isSatisfied(s: Scan): Boolean {
        val lhsval = lhs.evaluate(s)
        val rhsval = rhs.evaluate(s)
        return rhsval == lhsval
    }

    override fun toString(): String {
        return lhs.toString() + "=" + rhs.toString()
    }
}
