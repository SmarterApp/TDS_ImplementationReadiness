package org.cresst.sb.irp.auto.tsb;

import com.google.common.io.BaseEncoding;
import org.apache.commons.lang3.StringUtils;
import org.cresst.sb.irp.auto.accesstoken.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
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
@Service
public class TestSpecBankSideLoader {
    private final static Logger logger = LoggerFactory.getLogger(TestSpecBankSideLoader.class);

    private static final Pattern purposePattern = Pattern.compile(" purpose\\s*=\\s*\\\"([^\\\"]*)\\\"");
//    private static final Pattern publisherPattern = Pattern.compile(" publisher\\s*=\\s*\\\"([^\\\"]*)\\\"");
    private static final Pattern testPublishDatePattern = Pattern.compile(" publishdate\\s*=\\s*\\\"([^\\\"]*)\\\"");
    private static final Pattern uniqueIdPattern = Pattern.compile(" uniqueid\\s*=\\s*\\\"([^\\\"]*)\\\"");
    private static final Pattern testLabelPattern = Pattern.compile(" label\\s*=\\s*\\\"([^\\\"]*)\\\"");
    private static final Pattern testVersionPattern = Pattern.compile(" version\\s*=\\s*\\\"([^\\\"]*)\\\"");
    private static final Pattern propertyPattern = Pattern.compile("<property\\s*name\\s*=\\s*\\\"([^\\\"]*)\\\"\\s*value\\s*=\\s*\\\"([^\\\"]*)\\\"\\s*label\\s*=\\s*\\\"([^\\\"]*)\\\"");

    private static final List<List<String>> registrationTestPackages = new ArrayList<>();

    private Path registrationTestPackageDirectory;

    public TestSpecBankSideLoader(@Value("classpath:irp-package/TestPackages/ART/Registration") Resource registrationTestPackageDirectory) throws IOException {
        this.registrationTestPackageDirectory = Paths.get(registrationTestPackageDirectory.getURI());
    }

    /**
     * Stores the IRP Registration Test Packages in memory.
     * @throws IOException
     */
    @PostConstruct
    public void initiateRegistrationTestPackages() throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(registrationTestPackageDirectory, "*.xml")) {
            for (Path entry : stream) {
                registrationTestPackages.add(Files.readAllLines(entry, StandardCharsets.UTF_8));
            }
        }
    }

    public List<String> sideLoadRegistrationTestPackages(URL testSpecBankUrl, AccessToken accessToken, String tenantId) {
        List<String> tsbRegistrationPackageIds = new ArrayList<>();

        for (List<String> testPackage : registrationTestPackages) {
            TestSpecBankData testSpecBankData = processXmlFile(testPackage, tenantId);
            tsbRegistrationPackageIds.add(sendData(testSpecBankUrl, accessToken, testSpecBankData));
        }

        return tsbRegistrationPackageIds;
    }

    private static String sendData(URL testSpecBankUrl, AccessToken accessToken, TestSpecBankData testSpecBankData) {
        URL testSpecBankSpecificationUrl;
        try {
            testSpecBankSpecificationUrl = new URL(testSpecBankUrl, "/rest/testSpecification");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken.getAccessTokenString());
        httpHeaders.add("Accept", "application/json");
        httpHeaders.add("Content-Type", "application/json");

        HttpEntity<TestSpecBankData> request = new HttpEntity<>(testSpecBankData, httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ClientHttpRequestInterceptor ri = new LoggingRequestInterceptor();
        List<ClientHttpRequestInterceptor> ris = new ArrayList<>();
        ris.add(ri);
        restTemplate.setInterceptors(ris);
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));

        ResponseEntity<TestSpecBankData> response = restTemplate.exchange(testSpecBankSpecificationUrl.toString(), HttpMethod.POST, request, TestSpecBankData.class);

        TestSpecBankData responseData = response.getBody();
        logger.debug("TSB response: " + responseData);

        return responseData.getId();
    }

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

//                matcher = publisherPattern.matcher(line);
//                String testPublisher = getGroup(matcher, 1);

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
                String propLabel = matcher.group(3);

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
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
