package org.cresst.sb.irp.itemscoring.rubric;

/**
 * Interface for machine rubric loaders that return the content of a rubric
 */
public interface MachineRubricLoader {
    String getContents(String fileName);
}
