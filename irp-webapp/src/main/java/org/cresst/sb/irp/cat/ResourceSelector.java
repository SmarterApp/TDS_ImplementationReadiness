package org.cresst.sb.irp.cat;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class ResourceSelector {
    public static InputStream getTrueThetas(String subject, int grade) throws IOException {
        String filename = getTrueThetasFilename(subject, grade) + ".gz";
        return new GZIPInputStream(ResourceSelector.class.getClassLoader().getResourceAsStream(filename));
    }
    
    public static String getTrueThetasFilename(String subject, int grade) {
        return String.format("CAT_simulation_packages/%1$s%2$d/TrueThetas_WeightTuning%1$s%2$dN1000ASL.csv",
                subject.toLowerCase(), grade);
    }

    public static InputStream getStudentResponses(String subject, int grade) throws IOException {
        String filename = getStudentResponsesFilename(subject, grade) + ".gz";
        return new GZIPInputStream(ResourceSelector.class.getClassLoader().getResourceAsStream(filename));
    }
    
    public static String getStudentResponsesFilename(String subject, int grade) {
        return String.format("CAT_simulation_packages/%1$s%2$d/studentItemResponses_%1$s%2$dN1000ASL.csv",
                subject.toLowerCase(), grade);
    }
}