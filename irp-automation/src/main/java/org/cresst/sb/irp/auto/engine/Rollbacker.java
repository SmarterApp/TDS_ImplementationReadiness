package org.cresst.sb.irp.auto.engine;

/**
 * When errors occur implementors will rollback data that was inserted into the vendor's system.
 */
public interface Rollbacker {
    /**
     * Rollback data that was inserted into the vendor's system.
     */
     void rollback();
}
