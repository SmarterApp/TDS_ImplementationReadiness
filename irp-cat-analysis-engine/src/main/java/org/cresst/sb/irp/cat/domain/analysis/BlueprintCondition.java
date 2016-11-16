package org.cresst.sb.irp.cat.domain.analysis;

/**
 * A BlueprintCondition has a way to test a pool item to tell if it fails a blueprint specification 
 */
public interface BlueprintCondition {
    boolean test(PoolItem item);
}
