package simpledb.parse

import java.util.*
import simpledb.query.*
import simpledb.record.Schema

/**
 * The SimpleDB parser.
 * @author Edward Sciore
 */
class Parser(s: String) {
    private val lex: Lexer

    init {
        lex = Lexer(s)
    }

    // Methods for parsing predicates, terms, expressions, constants, and fields

    fun field(): String {
        return lex.eatId()
    }

    fun constant(): Constant {
        return if (lex.matchStringConstant())
            StringConstant(lex.eatStringConstant())
        else
            IntConstant(lex.eatIntConstant())
    }

    fun expression(): Expression {
        return if (lex.matchId())
            FieldNameExpression(field())
        else
            ConstantExpression(constant())
    }

    fun term(): Term {
        val lhs = expression()
        lex.eatDelim('=')
        val rhs = expression()
        return Term(lhs, rhs)
    }

    fun predicate(): Predicate {
        val pred = Predicate(term())
        if (lex.matchKeyword("and")) {
            lex.eatKeyword("and")
            pred.conjoinWith(predicate())
        }
        return pred
    }

    // Methods for parsing queries

    fun query(): QueryData {
        lex.eatKeyword("select")
        val fields = selectList()
        lex.eatKeyword("from")
        val tables = tableList()
        var pred = Predicate()
        if (lex.matchKeyword("where")) {
            lex.eatKeyword("where")
            pred = predicate()
        }
        return QueryData(fields, tables, pred)
    }

    private fun selectList(): Collection<String> {
        val L = ArrayList<String>()
        L.add(field())
        if (lex.matchDelim(',')) {
            lex.eatDelim(',')
            L.addAll(selectList())
        }
        return L
    }

    private fun tableList(): Collection<String> {
        val L = ArrayList<String>()
        L.add(lex.eatId())
        if (lex.matchDelim(',')) {
            lex.eatDelim(',')
            L.addAll(tableList())
        }
        return L
    }

    // Methods for parsing the various update commands

    fun updateCmd(): Any {
        return if (lex.matchKeyword("insert"))
            insert()
        else if (lex.matchKeyword("delete"))
            delete()
        else if (lex.matchKeyword("update"))
            modify()
        else
            create()
    }

    private fun create(): Any {
        lex.eatKeyword("create")
        return if (lex.matchKeyword("table"))
            createTable()
        else if (lex.matchKeyword("view"))
            createView()
        else
            createIndex()
    }

    // Method for parsing delete commands

    fun delete(): DeleteData {
        lex.eatKeyword("delete")
        lex.eatKeyword("from")
        val tblname = lex.eatId()
        var pred = Predicate()
        if (lex.matchKeyword("where")) {
            lex.eatKeyword("where")
            pred = predicate()
        }
        return DeleteData(tblname, pred)
    }

    // Methods for parsing insert commands

    fun insert(): InsertData {
        lex.eatKeyword("insert")
        lex.eatKeyword("into")
        val tblname = lex.eatId()
        lex.eatDelim('(')
        val flds = fieldList()
        lex.eatDelim(')')
        lex.eatKeyword("values")
        lex.eatDelim('(')
        val vals = constList()
        lex.eatDelim(')')
        return InsertData(tblname, flds, vals)
    }

    private fun fieldList(): List<String> {
        val L = ArrayList<String>()
        L.add(field())
        if (lex.matchDelim(',')) {
            lex.eatDelim(',')
            L.addAll(fieldList())
        }
        return L
    }

    private fun constList(): List<Constant> {
        val L = ArrayList<Constant>()
        L.add(constant())
        if (lex.matchDelim(',')) {
            lex.eatDelim(',')
            L.addAll(constList())
        }
        return L
    }

    // Method for parsing modify commands

    fun modify(): ModifyData {
        lex.eatKeyword("update")
        val tblname = lex.eatId()
        lex.eatKeyword("set")
        val fldname = field()
        lex.eatDelim('=')
        val newval = expression()
        var pred = Predicate()
        if (lex.matchKeyword("where")) {
            lex.eatKeyword("where")
            pred = predicate()
        }
        return ModifyData(tblname, fldname, newval, pred)
    }

    // Method for parsing create table commands

    fun createTable(): CreateTableData {
        lex.eatKeyword("table")
        val tblname = lex.eatId()
        lex.eatDelim('(')
        val sch = fieldDefs()
        lex.eatDelim(')')
        return CreateTableData(tblname, sch)
    }

    private fun fieldDefs(): Schema {
        val schema = fieldDef()
        if (lex.matchDelim(',')) {
            lex.eatDelim(',')
            val schema2 = fieldDefs()
            schema.addAll(schema2)
        }
        return schema
    }

    private fun fieldDef(): Schema {
        val fldname = field()
        return fieldType(fldname)
    }

    private fun fieldType(fldname: String): Schema {
        val schema = Schema()
        if (lex.matchKeyword("int")) {
            lex.eatKeyword("int")
            schema.addIntField(fldname)
        } else {
            lex.eatKeyword("varchar")
            lex.eatDelim('(')
            val strLen = lex.eatIntConstant()
            lex.eatDelim(')')
            schema.addStringField(fldname, strLen)
        }
        return schema
    }

    // Method for parsing create view commands

    fun createView(): CreateViewData {
        lex.eatKeyword("view")
        val viewname = lex.eatId()
        lex.eatKeyword("as")
        val qd = query()
        return CreateViewData(viewname, qd)
    }


    //  Method for parsing create index commands

    fun createIndex(): CreateIndexData {
        lex.eatKeyword("index")
        val idxname = lex.eatId()
        lex.eatKeyword("on")
        val tblname = lex.eatId()
        lex.eatDelim('(')
        val fldname = field()
        lex.eatDelim(')')
        return CreateIndexData(idxname, tblname, fldname)
    }
}

