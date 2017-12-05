package simpledb.query

import simpledb.record.Schema

/** The Plan class corresponding to the *project*
 * relational algebra operator.
 * @author Edward Sciore
 */
class ProjectPlan
/**
 * Creates a new project node in the query tree,
 * having the specified subquery and field list.
 * @param p the subquery
 * @param fieldlist the list of fields
 */
(private val p: Plan, fieldlist: Collection<String>) : Plan {
    private val schema = Schema()

    init {
        for (fldname in fieldlist)
            schema.add(fldname, p.schema())
    }

    /**
     * Creates a project scan for this query.
     * @see simpledb.query.Plan.open
     */
    override fun open(): Scan {
        val s = p.open()
        return ProjectScan(s, schema.fields())
    }

    /**
     * Estimates the number of block accesses in the projection,
     * which is the same as in the underlying query.
     * @see simpledb.query.Plan.blocksAccessed
     */
    override fun blocksAccessed(): Int {
        return p.blocksAccessed()
    }

    /**
     * Estimates the number of output records in the projection,
     * which is the same as in the underlying query.
     * @see simpledb.query.Plan.recordsOutput
     */
    override fun recordsOutput(): Int {
        return p.recordsOutput()
    }

    /**
     * Estimates the number of distinct field values
     * in the projection,
     * which is the same as in the underlying query.
     * @see simpledb.query.Plan.distinctValues
     */
    override fun distinctValues(fldname: String): Int {
        return p.distinctValues(fldname)
    }

    /**
     * Returns the schema of the projection,
     * which is taken from the field list.
     * @see simpledb.query.Plan.schema
     */
    override fun schema(): Schema {
        return schema
    }
}
