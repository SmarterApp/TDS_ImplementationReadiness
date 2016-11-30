package org.cresst.sb.irp.cat;

import java.io.InputStream;

public class ResourceSelector {
    public static InputStream getTrueThetas(String subject, int grade) {
        String filename = String.format("CAT_simulation_packages/%1$sG%2$d/TrueThetas_WeightTuning%1$s%2$dN1000ASL.csv", subject, grade);
        return ResourceSelector.class.getClassLoader().getResourceAsStream(filename);
    }
}
