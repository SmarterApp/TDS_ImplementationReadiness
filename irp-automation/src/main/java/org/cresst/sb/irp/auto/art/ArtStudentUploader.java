package org.cresst.sb.irp.auto.art;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.cresst.sb.irp.auto.engine.Rollbacker;
import org.opentestsystem.delivery.testreg.domain.FileUploadResponse;
import org.opentestsystem.delivery.testreg.domain.Student;
import org.opentestsystem.shared.search.domain.SearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ArtStudentUploader implements Rollbacker {
    private final static Logger logger = LoggerFactory.getLogger(ArtStudentUploader.class);

    // Must contain the template since it's shared between instances
    private static final List<String> studentTemplateLines = new ArrayList<>();

    final private RestOperations automationRestTemplate;
    final private URL artUrl;
    final private String stateAbbreviation;
    final private String responsibleDistrictId;
    final private String responsibleInstitutionId;

    public ArtStudentUploader(Resource studentTemplate,
                              RestOperations automationRestTemplate, URL artUrl,
                              String stateAbbreviation, String responsibleDistrictId, String responsibleInstitutionId) throws IOException {

        if (studentTemplateLines.isEmpty()) {
            ArtStudentUploader.studentTemplateLines.addAll(
                    Files.readAllLines(Paths.get(studentTemplate.getURI()), StandardCharsets.UTF_8));
        }

        this.automationRestTemplate = automationRestTemplate;
        this.artUrl = artUrl;
        this.stateAbbreviation = stateAbbreviation;
        this.responsibleDistrictId = responsibleDistrictId;
        this.responsibleInstitutionId = responsibleInstitutionId;
    }

    public ArtStudentUploaderResult uploadStudentData() {
        ArtStudentUploaderResult result = new ArtStudentUploaderResult();

        if (upload(result) && verify(result)) {
            save(result);
            result.setNumberOfStudentsUploaded(ArtStudentUploader.studentTemplateLines.size());
        }

        return result;
    }

    public void rollback() {
        // https://art.smarterapp.cresst.net:8443/rest/student/?currentPage=0&pageSize=36&entityId=IRP
        // &studentIdentifier=IRP&firstName=IRPStudent&lastName=IRPLastName&sortDir=asc&sortKey=entityId
        // search for all IRP Students
        // get all their IDs
        // delete the Student based on the ID

        try {
            final URI artStudentSearchUri = UriComponentsBuilder.fromHttpUrl(artUrl.toString())
                    .pathSegment("rest", "student")
                    .queryParam("currentPage", 0)
                    .queryParam("pageSize", studentTemplateLines.size())
                    .queryParam("entityId", "IRP")
                    .queryParam("studentIdentifier", "IRP")
                    .queryParam("firstName", "IRPStudent")
                    .queryParam("lastName", "IRPLastName")
                    .queryParam("sortDir", "asc")
                    .queryParam("sortKey", "entityId")
                    .build()
                    .toUri();

            ResponseEntity<SearchResponse<Student>> searchStudentsResponse = automationRestTemplate.exchange(
                    artStudentSearchUri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<SearchResponse<Student>>() {});

            SearchResponse<Student> students = searchStudentsResponse.getBody();
            for (Student student : students.getSearchResults()) {
                URI artStudentDeleteUri = UriComponentsBuilder.fromHttpUrl(artUrl.toString())
                        .pathSegment("rest", "student", student.getId())
                        .build()
                        .toUri();

                automationRestTemplate.delete(artStudentDeleteUri);
            }

        }  catch (RestClientException ex) {
            logger.error("Error trying to remove Students from ART", ex);
        }
    }

    private boolean upload(ArtStudentUploaderResult result) {
        // ART needs a filename to associate with the data
        final ByteArrayResource studentData = new ByteArrayResource(generateStudentData().getBytes()) {
            @Override
            public String getFilename() {
                return "IRPStudents.csv";
            }
        };

        // Create the formatType part that contains the type of data being uploaded: "STUDENT"
        HttpHeaders formatTypePartHeader = new HttpHeaders();
        HttpEntity<String> formatTypePartEntity = new HttpEntity<>("STUDENT", formatTypePartHeader);

        // Create the uploadFile part that contains the data
        HttpHeaders dataPartHeader = new HttpHeaders();
        dataPartHeader.add("Content-Type", "text/csv");
        HttpEntity<ByteArrayResource> dataPartEntity = new HttpEntity<>(studentData, dataPartHeader);

        // Construct request with both parts
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("formatType", formatTypePartEntity);
        parts.add("uploadFile", dataPartEntity);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(parts, httpHeaders);

        try {
            final URI artStudentUploadUri = UriComponentsBuilder.fromHttpUrl(artUrl.toString())
                    .pathSegment("rest", "uploadFile")
                    .build()
                    .toUri();

            logger.info("Sending Student data to " + artStudentUploadUri);

            ResponseEntity<FileUploadResponse> responseEntity = automationRestTemplate.postForEntity(artStudentUploadUri,
                    request, FileUploadResponse.class);

            final FileUploadResponse fileUploadResponse = responseEntity.getBody();
            final boolean uploadSuccessful = responseEntity.getStatusCode() == HttpStatus.CREATED &&
                    fileUploadResponse != null;

            logger.info("Student data upload was {}", uploadSuccessful ? "successful" : "unsuccessful");

            result.setUploadSuccessful(uploadSuccessful);
            result.setStudentUploadId(uploadSuccessful ? fileUploadResponse.getFileGridFsId() : null);
            result.appendMessage(fileUploadResponse != null ? fileUploadResponse.getMessage() : "Student upload failed");

            return uploadSuccessful;

        } catch (RestClientException ex) {
            result.setUploadSuccessful(false);
            result.setStudentUploadId(null);
            result.appendMessage("Student data upload failed");

            logger.error("Student upload failed", ex);

            return false;
        }
    }

    private boolean verify(ArtStudentUploaderResult result) {
        try {
            final URI artStudentVerifyUri = UriComponentsBuilder.fromHttpUrl(artUrl.toString())
                    .pathSegment("rest", "validateFile", result.getStudentUploadId())
                    .build()
                    .toUri();

            logger.info("Validating Student data upload");

            ResponseEntity<String> responseEntity = automationRestTemplate.getForEntity(artStudentVerifyUri, String.class);

            // Server returns an empty array when verification is successful
            final String validationResults = responseEntity.getBody();
            final boolean validateSuccessful = responseEntity.getStatusCode() == HttpStatus.OK &&
                    "[]".equals(StringUtils.remove(validationResults, ' '));

            result.setValidateSuccessful(validateSuccessful);
            result.appendMessage(validationResults);

            logger.info("Validation was {}", validateSuccessful ? "successful" : "unsuccessful");

            return validateSuccessful;
        } catch (RestClientException ex) {
            result.setValidateSuccessful(false);
            result.appendMessage("Student upload data validation failed");

            logger.error("Student upload validation failed", ex);

            return false;
        }
    }

    private void save(ArtStudentUploaderResult result) {
        try {
            final URI artStudentSaveUri = UriComponentsBuilder.fromHttpUrl(artUrl.toString())
                    .pathSegment("rest", "saveEntity", result.getStudentUploadId())
                    .build()
                    .toUri();

            logger.info("Saving uploaded Student data");

            ResponseEntity<String> responseEntity = automationRestTemplate.getForEntity(artStudentSaveUri, String.class);

            final String saveResults = responseEntity.getBody();
            final boolean saveSuccessful = responseEntity.getStatusCode() == HttpStatus.OK;

            result.setSaveSuccessful(saveSuccessful);
            result.appendMessage(saveResults);

            logger.info("Saved uploaded Student data");

        } catch (RestClientException ex) {
            result.setSaveSuccessful(false);
            result.appendMessage("Student upload verification failed");

            logger.error("Student upload verify failed", ex);
        }
    }

    private String generateStudentData() {
        StringBuilder builder = new StringBuilder(studentTemplateLines.get(0));
        builder.append("\n");
        for (int i = 1; i < studentTemplateLines.size(); i++) {
            builder.append(studentTemplateLines.get(i)
                        .replace("{SA}", stateAbbreviation)
                        .replace("{RDI}", responsibleDistrictId)
                        .replace("{RII}", responsibleInstitutionId))
                    .append("\n");
        }

        return builder.toString();
    }
}
