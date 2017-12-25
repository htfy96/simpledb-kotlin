package simpledb.tx.recovery

import simpledb.file.Block

interface LogSetRecord {
    fun getBlock(): Block
    fun getOffset(): Int
    fun getValue(): Any
}