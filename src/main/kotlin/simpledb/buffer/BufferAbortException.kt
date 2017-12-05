package simpledb.buffer

/**
 * A runtime exception indicating that the transaction
 * needs to abort because a buffer request could not be satisfied.
 * @author Edward Sciore
 */
class BufferAbortException : RuntimeException()
