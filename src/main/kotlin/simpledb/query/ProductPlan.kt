package simpledb.query

import simpledb.record.Schema

/** The Plan class corresponding to the *product*
 * relational algebra operator.
 * @author Edward Sciore
 */
class ProductPlan
/**
 * Creates a new product node in the query tree,
 * having the two specified subqueries.
 * @param p1 the left-hand subquery
 * @param p2 the right-hand subquery
 */
(private val p1: Plan, private val p2: Plan) : Plan {
    private val schema = Schema()

    init {
        schema.addAll(p1.schema())
        schema.addAll(p2.schema())
    }

    /**
     * Creates a product scan for this query.
     * @see simpledb.query.Plan.open
     */
    override fun open(): Scan {
        val s1 = p1.open()
        val s2 = p2.open()
        return ProductScan(s1, s2)
    }

    /**
     * Estimates the number of block accesses in the product.
     * The formula is:
     * <pre> B(product(p1,p2)) = B(p1) + R(p1)*B(p2) </pre>
     * @see simpledb.query.Plan.blocksAccessed
     */
    override fun blocksAccessed(): Int {
        return p1.blocksAccessed() + p1.recordsOutput() * p2.blocksAccessed()
    }

    /**
     * Estimates the number of output records in the product.
     * The formula is:
     * <pre> R(product(p1,p2)) = R(p1)*R(p2) </pre>
     * @see simpledb.query.Plan.recordsOutput
     */
    override fun recordsOutput(): Int {
        return p1.recordsOutput() * p2.recordsOutput()
    }

    /**
     * Estimates the distinct number of field values in the product.
     * Since the product does not increase or decrease field values,
     * the estimate is the same as in the appropriate underlying query.
     * @see simpledb.query.Plan.distinctValues
     */
    override fun distinctValues(fldname: String): Int {
        return if (p1.schema().hasField(fldname))
            p1.distinctValues(fldname)
        else
            p2.distinctValues(fldname)
    }

    /**
     * Returns the schema of the product,
     * which is the union of the schemas of the underlying queries.
     * @see simpledb.query.Plan.schema
     */
    override fun schema(): Schema {
        return schema
    }
}
