package org.cresst.sb.irp.auto.art;

import org.apache.commons.lang.StringUtils;
import org.cresst.sb.irp.auto.accesstoken.AccessToken;
import org.cresst.sb.irp.auto.testhelpers.IntegrationTests;
import org.cresst.sb.irp.auto.web.AutomationRestTemplate;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.opentestsystem.delivery.testreg.domain.FileUploadResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URL;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ArtStudentUploaderTest {

    private URI uploadUri;
    private URI validateUri;
    private URI saveUri;

    public ArtStudentUploaderTest() throws Exception {
        uploadUri = new URI("https://art.localhost/rest/uploadFile");
        validateUri = new URI("https://art.localhost/rest/validateFile/5768c360e4b05d7be6e43272");
        saveUri = new URI("https://art.localhost/rest/saveEntity/5768c360e4b05d7be6e43272");
    }

    @Ignore("Further test harness structure needs to be build out to get test parameters")
    @Category(IntegrationTests.class)
    @Test
    public void uploadStudentIntegrationTest() throws Exception {

        AccessToken accessToken = AccessToken.buildAccessToken(
                new RestTemplate(),
                new URL(""),
                "",
                "",
                "",
                ""
        );

        AutomationRestTemplate restTemplate = new AutomationRestTemplate();
        restTemplate.addAccessToken(accessToken);

        ArtStudentUploader sut = new ArtStudentUploader(new ClassPathResource("IRPStudents_template.csv"),
                restTemplate,
                new URL(""),
                "",
                "",
                "");

        ArtStudentUploaderResult result = sut.uploadStudentData();

        assertTrue(result.isSuccessful());
        assertTrue(StringUtils.isNotBlank(result.getStudentUploadId()));
    }

    @Test
    public void whenUploadStudentDataSuccessful() throws Exception {

        RestOperations mockRestOperations = mock(RestOperations.class);

        FileUploadResponse mockResponse = new FileUploadResponse();
        mockResponse.setStatusCode(HttpStatus.CREATED.value());
        mockResponse.setFileName("IRPStudents.csv");
        mockResponse.setMessage("File uploaded successfully");
        mockResponse.setFileGridFsId("5768c360e4b05d7be6e43272");

        ResponseEntity<FileUploadResponse> mockUploadResponseEntity = new ResponseEntity<>(mockResponse, HttpStatus.CREATED);
        ResponseEntity<String> mockValidateResponseEntity = new ResponseEntity<>("[]", HttpStatus.OK);
        ResponseEntity<String> mockSaveResponseEntity = new ResponseEntity<>("[ {\n" +
                "  \"formatType\" : \"STUDENT\",\n" +
                "  \"updatedRecords\" : 1,\n" +
                "  \"addedRecords\" : 0,\n" +
                "  \"deletedRecords\" : 0\n" +
                "} ]", HttpStatus.OK);

        when(mockRestOperations.postForEntity(eq(uploadUri), any(HttpEntity.class), eq(FileUploadResponse.class)))
                .thenReturn(mockUploadResponseEntity);
        when(mockRestOperations.getForEntity(validateUri, String.class)).thenReturn(mockValidateResponseEntity);
        when(mockRestOperations.getForEntity(saveUri, String.class)).thenReturn(mockSaveResponseEntity);

        ArtStudentUploader sut = new ArtStudentUploader(new ClassPathResource("IRPStudents_template.csv"),
                mockRestOperations,
                new URL("https://art.localhost"),
                "CA",
                "IRPDistrict",
                "IRPInstitution");

        ArtStudentUploaderResult result = sut.uploadStudentData();

        assertTrue(result.isSuccessful());
        assertTrue(StringUtils.isNotBlank(result.getStudentUploadId()));
    }

    @Test
    public void whenUploadStudentDataNotSuccessful() throws Exception {
        RestOperations mockRestOperations = mock(RestOperations.class);

        FileUploadResponse mockResponse = new FileUploadResponse();
        mockResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        mockResponse.setFileName("IRPStudents.csv");
        mockResponse.setMessage("File uploaded failed");
        mockResponse.setFileGridFsId(null);

        ResponseEntity<FileUploadResponse> mockResponseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(mockRestOperations.postForEntity(any(URI.class), any(HttpEntity.class), eq(FileUploadResponse.class)))
                .thenReturn(mockResponseEntity);

        ArtStudentUploader sut = new ArtStudentUploader(new ClassPathResource("IRPStudents_template.csv"),
                mockRestOperations,
                new URL("https://art.localhost"),
                "CA",
                "IRPDistrict",
                "IRPInstitution");

        ArtStudentUploaderResult result = sut.uploadStudentData();

        assertFalse(result.isSuccessful());
        assertTrue(StringUtils.isBlank(result.getStudentUploadId()));
    }

    @Test
    public void whenUploadStudentDataNotSuccessfulBecauseServerError() throws Exception {
        RestOperations mockRestOperations = mock(RestOperations.class);

        when(mockRestOperations.postForEntity(any(URI.class), any(HttpEntity.class), eq(FileUploadResponse.class)))
                .thenThrow(new RestClientException("Invalid file extension"));

        ArtStudentUploader sut = new ArtStudentUploader(new ClassPathResource("IRPStudents_template.csv"),
                mockRestOperations,
                new URL("https://art.localhost"),
                "CA",
                "IRPDistrict",
                "IRPInstitution");

        ArtStudentUploaderResult result = sut.uploadStudentData();

        assertFalse(result.isSuccessful());
        assertTrue(StringUtils.isBlank(result.getStudentUploadId()));
    }

    @Test
    public void whenValidationNotSuccessful() throws Exception {
        RestOperations mockRestOperations = mock(RestOperations.class);

        FileUploadResponse mockResponse = new FileUploadResponse();
        mockResponse.setStatusCode(HttpStatus.CREATED.value());
        mockResponse.setFileName("IRPStudents.csv");
        mockResponse.setMessage("File uploaded successfully");
        mockResponse.setFileGridFsId("5768c360e4b05d7be6e43272");

        ResponseEntity<FileUploadResponse> mockUploadResponseEntity = new ResponseEntity<>(mockResponse, HttpStatus.CREATED);
        ResponseEntity<String> mockValidateResponseEntity = new ResponseEntity<>("[ {\n" +
                "  \"formatType\" : \"STUDENT\",\n" +
                "  \"fatalErrors\" : [ {\n" +
                "    \"message\" : \"This is an invalid header for this format. Valid value is ExternalSSID\",\n" +
                "    \"recordNumber\" : 1,\n" +
                "    \"fieldName\" : \"Header [9]\",\n" +
                "    \"fieldValue\" : \"AlternateSSID\",\n" +
                "    \"entityName\" : \"STUDENT\",\n" +
                "    \"type\" : \"FATAL_ERROR\"\n" +
                "  } ],\n" +
                "  \"recordErrors\" : null,\n" +
                "  \"warnings\" : null,\n" +
                "  \"fatalErrorsTotalCount\" : 0,\n" +
                "  \"recordErrorsTotalCount\" : 0,\n" +
                "  \"warningsTotalCount\" : 0\n" +
                "} ]", HttpStatus.OK);

        when(mockRestOperations.postForEntity(eq(uploadUri), any(HttpEntity.class), eq(FileUploadResponse.class)))
                .thenReturn(mockUploadResponseEntity);
        when(mockRestOperations.getForEntity(validateUri, String.class)).thenReturn(mockValidateResponseEntity);

        ArtStudentUploader sut = new ArtStudentUploader(new ClassPathResource("IRPStudents_template.csv"),
                mockRestOperations,
                new URL("https://art.localhost"),
                "CA",
                "IRPDistrict",
                "IRPInstitution");

        ArtStudentUploaderResult result = sut.uploadStudentData();

        assertFalse(result.isSuccessful());
    }

    @Test
    public void whenSaveNotSuccessful() throws Exception {
        RestOperations mockRestOperations = mock(RestOperations.class);

        FileUploadResponse mockResponse = new FileUploadResponse();
        mockResponse.setStatusCode(HttpStatus.CREATED.value());
        mockResponse.setFileName("IRPStudents.csv");
        mockResponse.setMessage("File uploaded successfully");
        mockResponse.setFileGridFsId("5768c360e4b05d7be6e43272");

        ResponseEntity<FileUploadResponse> mockUploadResponseEntity = new ResponseEntity<>(mockResponse, HttpStatus.CREATED);
        ResponseEntity<String> mockValidateResponseEntity = new ResponseEntity<>("[]", HttpStatus.OK);

        when(mockRestOperations.postForEntity(eq(uploadUri), any(HttpEntity.class), eq(FileUploadResponse.class)))
                .thenReturn(mockUploadResponseEntity);
        when(mockRestOperations.getForEntity(validateUri, String.class)).thenReturn(mockValidateResponseEntity);
        when(mockRestOperations.getForEntity(saveUri, String.class)).thenThrow(new RestClientException("Save failed"));

        ArtStudentUploader sut = new ArtStudentUploader(new ClassPathResource("IRPStudents_template.csv"),
                mockRestOperations,
                new URL("https://art.localhost"),
                "CA",
                "IRPDistrict",
                "IRPInstitution");

        ArtStudentUploaderResult result = sut.uploadStudentData();

        assertFalse(result.isSuccessful());
    }
}