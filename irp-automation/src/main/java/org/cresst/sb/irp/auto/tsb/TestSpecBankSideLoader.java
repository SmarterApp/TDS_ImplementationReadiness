package org.cresst.sb.irp.auto.tsb;

import com.google.common.io.BaseEncoding;
import org.apache.commons.lang3.StringUtils;
import org.cresst.sb.irp.auto.engine.Rollbacker;
import org.cresst.sb.irp.auto.web.AutomationRestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DeflaterOutputStream;

/**
 * Side loads Registration Test Packages into TSB. This is a translation of the Perl script that performs
 * the same functionality https://github.com/SmarterApp/TDS_Administrative/tree/master/tsb
 */
public class TestSpecBankSideLoader implements Rollbacker {
    private final static Logger logger = LoggerFactory.getLogger(TestSpecBankSideLoader.class);

    private static final Pattern purposePattern = Pattern.compile(" purpose\\s*=\\s*\\\"([^\\\"]*)\\\"");
    private static final Pattern testPublishDatePattern = Pattern.compile(" publishdate\\s*=\\s*\\\"([^\\\"]*)\\\"");
    private static final Pattern uniqueIdPattern = Pattern.compile(" uniqueid\\s*=\\s*\\\"([^\\\"]*)\\\"");
    private static final Pattern testLabelPattern = Pattern.compile(" label\\s*=\\s*\\\"([^\\\"]*)\\\"");
    private static final Pattern testVersionPattern = Pattern.compile(" version\\s*=\\s*\\\"([^\\\"]*)\\\"");
    private static final Pattern propertyPattern = Pattern.compile("<property\\s*name\\s*=\\s*\\\"([^\\\"]*)\\\"\\s*value\\s*=\\s*\\\"([^\\\"]*)\\\"\\s*label\\s*=\\s*\\\"([^\\\"]*)\\\"");

    // The files aren't large, so the Registration Test Package XML docs are stored in memory.
    private static final List<List<String>> registrationTestPackages = new ArrayList<>();

    private final RestOperations automationRestTemplate;
    private final URL testSpecBankUrl;
    private final String tenantId;

    private final URI testSpecBankSpecificationUri;

    private List<TestSpecBankData> sideLoadedData = new ArrayList<>();

    public TestSpecBankSideLoader(Resource registrationTestPackageDirectoryResource,
                                  RestOperations automationRestTemplate, URL testSpecBankUrl, String tenantId) throws IOException {

        initiateRegistrationTestPackages(registrationTestPackageDirectoryResource);

        this.automationRestTemplate = automationRestTemplate;
        this.testSpecBankUrl = testSpecBankUrl;
        this.tenantId = tenantId;

        this.testSpecBankSpecificationUri = UriComponentsBuilder.fromHttpUrl(testSpecBankUrl.toString())
                .pathSegment("rest", "testSpecification")
                .build()
                .toUri();
    }

    /**
     * Stores the IRP Registration Test Packages in memory.
     * @throws IOException When there is an error reading the Registration Test Packages
     */
    private void initiateRegistrationTestPackages(Resource registrationTestPackageDirectoryResource) throws IOException {
        if (registrationTestPackages.isEmpty()) {
            Path registrationTestPackageDirectory = Paths.get(registrationTestPackageDirectoryResource.getURI());
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(registrationTestPackageDirectory, "*.xml")) {
                for (Path entry : stream) {
                    registrationTestPackages.add(Files.readAllLines(entry, StandardCharsets.UTF_8));
                }
            }
        }
    }

    /**
     * Side-loads the IRP ART Registration Test Packages into the given Test Spec Bank.
     *
     * @return The data for each Registration Test Package that was side-loaded into the vendor's TSB. This information
     * can be useful for selecting tests in ART.
     */
    public List<TestSpecBankData> sideLoadRegistrationTestPackages() {

        sideLoadedData.clear();
        for (List<String> testPackage : registrationTestPackages) {
            TestSpecBankData testSpecBankData = processXmlFile(testPackage, tenantId);
            sideLoadedData.add(sendData(testSpecBankData));
        }

        return sideLoadedData;
    }

    @Override
    public void rollback() {
        for (TestSpecBankData testSpecBankData : sideLoadedData) {
            retireTestSpec(testSpecBankData.getId());
        }

        logger.info("Rolled back (retired) Test Spec Data");
    }

    private void retireTestSpec(String testSpecificationId) {

        UriComponents retireSpecificationUrl = UriComponentsBuilder.fromUri(testSpecBankSpecificationUri)
                .pathSegment(testSpecificationId, "retire")
                .build(true);

        try {
            automationRestTemplate.put(retireSpecificationUrl.toUri(), "");
        } catch (RestClientException e) {
            logger.error("Unable to retire spec (" + testSpecificationId + ")", e);
        }
    }

    /**
     * Does the actual side-loading by calling the TSB web service endpoint to insert the Registration Test Package.
     *
     * @param testSpecBankData The TSB data to pass to the web service endpoint
     * @return The data that was side-loaded into the vendor's TSB.
     */
    private TestSpecBankData sendData(TestSpecBankData testSpecBankData) {

        HttpEntity<TestSpecBankData> request = AutomationRestTemplate.constructHttpEntity(testSpecBankData);
        TestSpecBankData insertedTestSpecBankData = automationRestTemplate.postForObject(testSpecBankSpecificationUri,
                request,
                TestSpecBankData.class);

        logger.debug("TSB response: " + insertedTestSpecBankData);

        return insertedTestSpecBankData;
    }

    /**
     * Mimicks the TSB side-loading perl script that extracts information and prepares the ART Registration Test Package
     * to be side-loaded. It also prepares the data necessary to send to the vendor's TSB web service endpoint.
     * @param testPackageLines The lines of a single ART Registration Test Package. The code processes line-by-line.
     * @param tenantId The vendor's system's Tenant ID
     * @return The data needed to be passed to the vendor's TSB web service endpoint.
     */
    private static TestSpecBankData processXmlFile(List<String> testPackageLines, String tenantId) {
        String testSubjectName = "";
        String testSubjectAbbreviation = "";
        String uniqueId = "";
        List<String> testGrades = new ArrayList<>();
        String testLabel = "";
        String testType = "";
        String testVersion = "";

        boolean testTypeFound = false;
        int state = 0;

        List<String> processedTestPackage = new ArrayList<>();

        Matcher matcher;

        for (String line : testPackageLines) {
            if (line.startsWith("<testspecification") && state == 0) {
                matcher = purposePattern.matcher(line);
                if (matcher.find() && !StringUtils.equalsIgnoreCase("registration", matcher.group(1))) { throw new RuntimeException("Test Package"); }

                matcher = testPublishDatePattern.matcher(line);
                String testPublishDate = getGroup(matcher, 1);

                line = String.format("<testspecification purpose=\"REGISTRATION\" publisher=\"%s\" publishdate=\"%s\" version=\"1.0\">",
                        tenantId, testPublishDate);

                state = 1;
            }

            if (line.contains("<identifier") && state == 1) {
                matcher = uniqueIdPattern.matcher(line);
                uniqueId = getGroup(matcher, 1);

                matcher = testLabelPattern.matcher(line);
                testLabel = getGroup(matcher, 1);

                matcher = testVersionPattern.matcher(line);
                testVersion = matcher.find() ? matcher.group(1) + ".0" : "1.0";

                line = String.format("  <identifier uniqueid=\"%s\" name=\"%s\" label=\"%s\" version=\"%s\" />",
                        uniqueId, uniqueId, testLabel, testVersion);

                state = 2;
            }

            matcher = propertyPattern.matcher(line);
            if (matcher.find() && state == 2) {
                String propName = matcher.group(1);
                String propValue = matcher.group(2);

                if (StringUtils.equalsIgnoreCase("subject", propName)) {
                    boolean subjectFound = false;
                    if (StringUtils.equalsIgnoreCase("ELA", propValue)) {
                        subjectFound = true;
                        testSubjectName = "English Language Arts";
                        testSubjectAbbreviation = "ELA";
                    }

                    if (StringUtils.equalsIgnoreCase("MATH", propValue)) {
                        subjectFound = true;
                        testSubjectName = "Mathematics";
                        testSubjectAbbreviation = "MATH";
                    }

                    if (StringUtils.equalsIgnoreCase("Student Help", propValue)) {
                        subjectFound = true;
                        testSubjectName = "Student Help";
                        testSubjectAbbreviation = "SH";
                    }

                    if (subjectFound) {
                        line = String.format("  <property name=\"subject\" value=\"%s\" label=\"%s\" />",
                                testSubjectAbbreviation, testSubjectName);
                    } else {
                        throw new RuntimeException(String.format("Add a subject name for subject abbreviation = \"%s\"", propValue));
                    }
                }

                if (StringUtils.equalsIgnoreCase("grade", propName)) {
                    testGrades.add(propValue);
                }

                if (StringUtils.equalsIgnoreCase("type", propName)) {
                    if (StringUtils.equalsIgnoreCase("interim", propValue)) {
                        testType = "I";
                        testTypeFound = true;
                    } else if (StringUtils.equalsIgnoreCase("summative", propValue)) {
                        testType = "S";
                        testTypeFound = true;
                    }
                }
            }

            if (line.contains("<registration>") && state == 2) {
                if (!testTypeFound) { throw new RuntimeException("Test Type not found."); }

                line = String.format("  <property name=\"label\" value=\"%s\" label=\"%s\"/>\n" +
                                "  <property name=\"category\"/>\n" +
                                "  <property name=\"comment\"/>\n" +
                                "  <property name=\"description\"/>\n" +
                                "  <property name=\"testfamily\"/>\n" +
                                " <registration>",
                        testLabel, testLabel);
                state = 3;
            }

            if (line.contains("<poolproperty") && state == 3) {
                // do nothing: eat the poolproperty; ART doesn't need them
                continue;
            }

            if (line.contains("<registrationsegment") && state == 3) {
                line = line.replace("fixedform", "FIXEDFORM").replace("adaptive", "ADAPTIVE");
            }


            processedTestPackage.add(line + "\n");
        }

        logger.debug(StringUtils.join(processedTestPackage, ""));

        String encodedTestPackage = compressAndEncodeXmlFile(processedTestPackage);

        TestSpecBankData testSpecBankData = new TestSpecBankData();
        testSpecBankData.setSpecificationXml(encodedTestPackage);
        testSpecBankData.setTenantId(tenantId);
        testSpecBankData.setItemBank("187");
        testSpecBankData.setCategory("");
        testSpecBankData.setSubjectName(testSubjectName);
        testSpecBankData.setSubjectAbbreviation(testSubjectAbbreviation);
        testSpecBankData.setName(uniqueId);
        testSpecBankData.setGrade(testGrades);
        testSpecBankData.setLabel(testLabel);
        testSpecBankData.setPurpose("REGISTRATION");
        testSpecBankData.setType(testType);
        testSpecBankData.setVersion(testVersion);

        logger.debug(testSpecBankData.toString());

        return testSpecBankData;
    }

    private static String getGroup(Matcher matcher, int group) {
        return matcher.find() ? matcher.group(group) : "";
    }

    private static String compressAndEncodeXmlFile(List<String> testPackage) {
        try {
            StringBuilder encodedTestPackage = new StringBuilder();
            ByteArrayOutputStream compressedByteOutputStream = new ByteArrayOutputStream();
            try (DeflaterOutputStream deflatorOutputStream = new DeflaterOutputStream(compressedByteOutputStream)) {
                for (String line : testPackage) {
                    deflatorOutputStream.write(line.replace("\n", "").replace("\r", "").getBytes(StandardCharsets.UTF_8));
                }
            }
            encodedTestPackage.append(BaseEncoding.base64().encode(compressedByteOutputStream.toByteArray()));
            return encodedTestPackage.toString();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
