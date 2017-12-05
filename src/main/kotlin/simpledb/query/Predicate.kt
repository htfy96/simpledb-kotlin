package simpledb.query

import simpledb.record.Schema
import java.util.*

/**
 * A predicate is a Boolean combination of terms.
 * @author Edward Sciore
 */
class Predicate {
    private val terms = ArrayList<Term>()

    /**
     * Creates an empty predicate, corresponding to "true".
     */
    constructor()

    /**
     * Creates a predicate containing a single term.
     * @param t the term
     */
    constructor(t: Term) {
        terms.add(t)
    }

    /**
     * Modifies the predicate to be the conjunction of
     * itself and the specified predicate.
     * @param pred the other predicate
     */
    fun conjoinWith(pred: Predicate) {
        terms.addAll(pred.terms)
    }

    /**
     * Returns true if the predicate evaluates to true
     * with respect to the specified scan.
     * @param s the scan
     * @return true if the predicate is true in the scan
     */
    fun isSatisfied(s: Scan): Boolean {
        for (t in terms)
            if (!t.isSatisfied(s))
                return false
        return true
    }

    /**
     * Calculates the extent to which selecting on the predicate
     * reduces the number of records output by a query.
     * For example if the reduction factor is 2, then the
     * predicate cuts the size of the output in half.
     * @param p the query's plan
     * @return the integer reduction factor.
     */
    fun reductionFactor(p: Plan): Int {
        var factor = 1
        for (t in terms)
            factor *= t.reductionFactor(p)
        return factor
    }

    /**
     * Returns the subpredicate that applies to the specified schema.
     * @param sch the schema
     * @return the subpredicate applying to the schema
     */
    fun selectPred(sch: Schema): Predicate? {
        val result = Predicate()
        for (t in terms)
            if (t.appliesTo(sch))
                result.terms.add(t)
        return if (result.terms.size == 0)
            null
        else
            result
    }

    /**
     * Returns the subpredicate consisting of terms that apply
     * to the union of the two specified schemas,
     * but not to either schema separately.
     * @param sch1 the first schema
     * @param sch2 the second schema
     * @return the subpredicate whose terms apply to the union of the two schemas but not either schema separately.
     */
    fun joinPred(sch1: Schema, sch2: Schema): Predicate? {
        val result = Predicate()
        val newsch = Schema()
        newsch.addAll(sch1)
        newsch.addAll(sch2)
        for (t in terms)
            if (!t.appliesTo(sch1) &&
                    !t.appliesTo(sch2) &&
                    t.appliesTo(newsch))
                result.terms.add(t)
        return if (result.terms.size == 0)
            null
        else
            result
    }

    /**
     * Determines if there is a term of the form "F=c"
     * where F is the specified field and c is some constant.
     * If so, the method returns that constant.
     * If not, the method returns null.
     * @param fldname the name of the field
     * @return either the constant or null
     */
    fun equatesWithConstant(fldname: String): Constant? {
        for (t in terms) {
            val c = t.equatesWithConstant(fldname)
            if (c != null)
                return c
        }
        return null
    }

    /**
     * Determines if there is a term of the form "F1=F2"
     * where F1 is the specified field and F2 is another field.
     * If so, the method returns the name of that field.
     * If not, the method returns null.
     * @param fldname the name of the field
     * @return the name of the other field, or null
     */
    fun equatesWithField(fldname: String): String? {
        for (t in terms) {
            val s = t.equatesWithField(fldname)
            if (s != null)
                return s
        }
        return null
    }

    override fun toString(): String {
        val iter = terms.iterator()
        if (!iter.hasNext())
            return ""
        var result = iter.next().toString()
        while (iter.hasNext())
            result += " and " + iter.next().toString()
        return result
    }
}
