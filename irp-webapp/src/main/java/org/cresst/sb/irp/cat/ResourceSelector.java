package org.cresst.sb.irp.cat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceSelector {
    private final static Logger logger = LoggerFactory.getLogger(ResourceSelector.class);

    public static InputStream getTrueThetasGz(String folderPath, String subject, int grade) throws IOException {
        String filename = getTrueThetas(folderPath, subject, grade) + ".gz";
        return new GZIPInputStream(new FileInputStream(filename));
    }
    
    public static String getTrueThetas(String folderPath, String subject, int grade) {
        String subfolder = String.format("%1$s%2$d", subject.toLowerCase(), grade);
        String filename = getTrueThetasFilename(subject, grade);
        Path path = Paths.get(folderPath, subfolder, filename);
        return path.toString();
    }

    public static String getTrueThetasFilename(String subject, int grade) {
        return String.format("TrueThetas_WeightTuning%1$s%2$dN1000ASL.csv", subject.toLowerCase(), grade);
    }

    public static InputStream getStudentResponsesGz(String folderPath, String subject, int grade) throws IOException {
        String filename = getStudentResponses(folderPath, subject, grade) + ".gz";
        return new GZIPInputStream(new FileInputStream(filename));
    }
    
    public static String getStudentResponses(String folderPath, String subject, int grade) {
        String subfolder = String.format("%1$s%2$d", subject.toLowerCase(), grade);
        String filename = getStudentResponsesFilename(subject.toLowerCase(), grade);
        Path path = Paths.get(folderPath, subfolder, filename);
        return path.toString();
    }

    public static String getStudentResponsesFilename(String subject, int grade) {
        return String.format("studentItemResponses_%1$s%2$dN1000ASL.csv", subject.toLowerCase(), grade);
    }

    public static InputStream getBlueprints(String folderPath) throws FileNotFoundException {
        return new FileInputStream(Paths.get(folderPath, "Blueprints.csv").toFile());
    }

    public static InputStream getItemPool(String folderPath, String subject) throws FileNotFoundException {
        return new FileInputStream(Paths.get(folderPath, "ItemPools", getItemPoolFilename(subject)).toFile());
    }

    public static String getItemPoolFilename(String subject) {
        return String.format("%s_itempool.csv", subject.toLowerCase());
    }
}
