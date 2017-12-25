package simpledb.server

import simpledb.file.FileMgr
import simpledb.buffer.*
import simpledb.tx.Transaction
import simpledb.log.LogMgr
import simpledb.metadata.MetadataMgr
import simpledb.planner.*
import simpledb.opt.HeuristicQueryPlanner
import simpledb.index.planner.IndexUpdatePlanner

/**
 * The class that provides system-wide static global values.
 * These values must be initialized by the method
 * [init][.init] before use.
 * The methods [initFileMgr][.initFileMgr],
 * [initFileAndLogMgr][.initFileAndLogMgr],
 * [initFileLogAndBufferMgr][.initFileLogAndBufferMgr],
 * and [initMetadataMgr][.initMetadataMgr]
 * provide limited initialization, and are useful for
 * debugging purposes.
 *
 * @author Edward Sciore
 */
object SimpleDB {
    var BUFFER_SIZE = 8
    var LOG_FILE = "simpledb.log"

    private var fm: FileMgr? = null
    private var bm: BufferMgr? = null
    private var logm: LogMgr? = null
    private var mdm: MetadataMgr? = null

    /**
     * Initializes the system.
     * This method is called during system startup.
     * @param dirname the name of the database directory
     */
    fun init(dirname: String) {
        initFileLogAndBufferMgr(dirname)
        val tx = Transaction()
        val isnew = fm!!.isNew
        if (isnew)
            println("creating new database")
        else {
            println("recovering existing database")
            tx.recover()
        }
        initMetadataMgr(isnew, tx)
        tx.commit()
        tx.close()
    }

    // The following initialization methods are useful for
    // testing the lower-level components of the system
    // without having to initialize everything.

    /**
     * Initializes only the file manager.
     * @param dirname the name of the database directory
     */
    fun initFileMgr(dirname: String) {
        fm = FileMgr(dirname)
    }

    /**
     * Initializes the file and log managers.
     * @param dirname the name of the database directory
     */
    fun initFileAndLogMgr(dirname: String) {
        initFileMgr(dirname)
        logm = LogMgr(LOG_FILE)
    }

    /**
     * Initializes the file, log, and buffer managers.
     * @param dirname the name of the database directory
     */
    fun initFileLogAndBufferMgr(dirname: String) {
        initFileAndLogMgr(dirname)
        bm = BufferMgr(BUFFER_SIZE)
    }

    /**
     * Initializes metadata manager.
     * @param isnew an indication of whether a new
     * database needs to be created.
     * @param tx the transaction performing the initialization
     */
    fun initMetadataMgr(isnew: Boolean, tx: Transaction) {
        mdm = MetadataMgr(isnew, tx)
    }

    fun fileMgr(): FileMgr {
        return fm!!
    }

    fun bufferMgr(): BufferMgr {
        return bm!!
    }

    fun logMgr(): LogMgr {
        return logm!!
    }

    fun mdMgr(): MetadataMgr {
        return mdm!!
    }

    /**
     * Creates a planner for SQL commands.
     * To change how the planner works, modify this method.
     * @return the system's planner for SQL commands
     */
    fun planner(): Planner {
        val qplanner = BasicQueryPlanner()
        val uplanner = BasicUpdatePlanner()
        return Planner(qplanner, uplanner)
    }
}
