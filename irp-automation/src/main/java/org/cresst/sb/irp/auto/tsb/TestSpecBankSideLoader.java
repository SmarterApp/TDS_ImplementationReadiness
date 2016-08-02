package org.cresst.sb.irp.auto.tsb;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Side loads Registration Test Packages into TSB. This is a translation of the Perl script that performs
 * the same functionality https://github.com/SmarterApp/TDS_Administrative/tree/master/tsb
 */
public class TestSpecBankSideLoader {
    private final static Logger logger = LoggerFactory.getLogger(TestSpecBankSideLoader.class);

    private static final List<List<String>> registrationTestPackages = new ArrayList<>();

    /**
     * Stores the IRP Registration Test Packages in memory
     * @param registrationTestPackageDirectory
     * @throws IOException
     */
    public static void readRegistrationTestPackages(Path registrationTestPackageDirectory) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(registrationTestPackageDirectory, "*.xml")) {
            for (Path entry : stream) {
                registrationTestPackages.add(Files.readAllLines(entry, StandardCharsets.UTF_8));
            }
        }
    }

    public void sideLoadRegistrationTestPackages(String tenantId) {
        for (List<String> testPackage : registrationTestPackages) {
            processXmlFile(testPackage);
        }
    }

    private static void processXmlFile(List<String> testPackageLines, String tenantId) {
        String testSubjectName;
        String testSubjectAbbreviation;
        String uniqueId;
        String[] testGrades;
        String testLabel;
        String testType;
        String testVersion;

        boolean testTypeFound = false;
        int state = 0;

        StringBuilder processedTestPackage = new StringBuilder();

        Matcher matcher;

        for (String line : testPackageLines) {
            if (line.startsWith("<testspecification") && state == 0) {
                Pattern purposePattern = Pattern.compile(" purpose\\s*=\\s*\\\"([^\\\"]*)\\\"");
                matcher = purposePattern.matcher(line);
                if (!StringUtils.equalsIgnoreCase("registration", matcher.group(1))) { throw new RuntimeException("Test Package"); }

                Pattern publisherPattern = Pattern.compile(" publisher\\s*=\\s*\\\"([^\\\"]*)\\\"");
                matcher = publisherPattern.matcher(line);
                String testPublisher = matcher.group(1);

                Pattern testPublishDatePattern = Pattern.compile(" publishdate\\s*=\\s*\\\"([^\\\"]*)\\\"");
                matcher = testPublishDatePattern.matcher(line);
                String testPublishDate = matcher.group(1);

                processedTestPackage.append(String.format("<testspecification purpose=\"REGISTRATION\" publisher=\"%s\" publishdate=\"%s\" version=\"1.0\">\n",
                        testPublisher, testPublishDate));

                state = 1;
            }

            if (line.contains("<identifier") && state == 1) {
                Pattern uniqueIdPattern = Pattern.compile(" uniqueid\\s*=\\s*\\\"([^\\\"]*)\\\"");
                matcher = uniqueIdPattern.matcher(line);
                String uniqueId = matcher.group(1);

                Pattern testLabelPattern = Pattern.compile(" label\\s*=\\s*\\\"([^\\\"]*)\\\"");
                matcher = testLabelPattern.matcher(line);
                String testLabel = matcher.group(1);


            }
        }
    }
}
