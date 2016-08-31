package org.cresst.sb.irp.automation.art;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.cresst.sb.irp.automation.web.AutomationRestTemplate;
import org.opentestsystem.delivery.testreg.domain.FileUploadResponse;
import org.opentestsystem.delivery.testreg.domain.FileUploadSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URL;
import java.util.List;

abstract class ArtUploader {
    private final static Logger logger = LoggerFactory.getLogger(ArtUploader.class);

    final private AutomationRestTemplate automationRestTemplate;
    final private URL artUrl;

    ArtUploader(AutomationRestTemplate automationRestTemplate, URL artUrl) {
        this.automationRestTemplate = automationRestTemplate;
        this.artUrl = artUrl;
    }

    abstract String generateData();
    abstract String getFormatType();
    abstract String getFilename();

    public ArtUploaderResult uploadData() {
        ArtUploaderResult result = new ArtUploaderResult();

        if (upload(result) && verify(result)) {
            save(result);
        }

        return result;
    }

    protected boolean upload(ArtUploaderResult result) {
        // ART needs a filename to associate with the data
        final String filename = getFilename();
        final ByteArrayResource studentData = new ByteArrayResource(generateData().getBytes()) {
            @Override
            public String getFilename() {
                return filename;
            }
        };

        // Create the formatType part that contains the type of data being uploaded
        HttpHeaders formatTypePartHeader = new HttpHeaders();
        HttpEntity<String> formatTypePartEntity = new HttpEntity<>(getFormatType(), formatTypePartHeader);

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
            final URI artUploadUri = UriComponentsBuilder.fromHttpUrl(artUrl.toString())
                    .pathSegment("rest", "uploadFile")
                    .build()
                    .toUri();

            logger.info("Uploading data to " + artUploadUri);

            ResponseEntity<FileUploadResponse> responseEntity = automationRestTemplate.postForEntity(artUploadUri,
                    request, FileUploadResponse.class);

            final FileUploadResponse fileUploadResponse = responseEntity.getBody();
            final boolean uploadSuccessful = responseEntity.getStatusCode() == HttpStatus.CREATED &&
                    fileUploadResponse != null;

            logger.info("Data upload was {}", uploadSuccessful ? "successful" : "unsuccessful");

            result.setUploadSuccessful(uploadSuccessful);
            result.setDataUploadId(uploadSuccessful ? fileUploadResponse.getFileGridFsId() : null);
            result.appendMessage(fileUploadResponse != null ? fileUploadResponse.getMessage() : "Upload failed");

            return uploadSuccessful;

        } catch (RestClientException ex) {
            result.setUploadSuccessful(false);
            result.setDataUploadId(null);
            result.appendMessage("Data upload failed");

            logger.error("Data upload failed", ex);

            return false;
        }
    }

    boolean verify(ArtUploaderResult result) {
        try {
            final URI artVerifyUri = UriComponentsBuilder.fromHttpUrl(artUrl.toString())
                    .pathSegment("rest", "validateFile", result.getDataUploadId())
                    .build()
                    .toUri();

            logger.info("Validating data upload");

            ResponseEntity<String> responseEntity = automationRestTemplate.getForEntity(artVerifyUri, String.class);

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
            result.appendMessage("Upload data validation failed");

            logger.error("Upload data validation failed", ex);

            return false;
        }
    }

    void save(ArtUploaderResult result) {
        try {
            final URI artSaveUri = UriComponentsBuilder.fromHttpUrl(artUrl.toString())
                    .pathSegment("rest", "saveEntity", result.getDataUploadId())
                    .build()
                    .toUri();

            logger.info("Saving uploaded data");

            ResponseEntity<List<FileUploadSummary>> responseEntity =
                    automationRestTemplate.exchange(artSaveUri, HttpMethod.GET, null
                            , new ParameterizedTypeReference<List<FileUploadSummary>>() {});

            List<FileUploadSummary> saveResults = responseEntity.getBody();
            boolean saveSuccessful = responseEntity.getStatusCode() == HttpStatus.OK && !saveResults.isEmpty();
            String message;

            if (saveSuccessful) {
                FileUploadSummary fileUploadSummary = saveResults.get(0);
                int numSavedRecords = fileUploadSummary.getAddedRecords() + fileUploadSummary.getUpdatedRecords();
                message = String.format("Saved %d records", numSavedRecords);
                result.setNumberOfRecordsUploaded(numSavedRecords);
            } else {
                message = "Error while saving records";
                result.setNumberOfRecordsUploaded(0);
            }

            result.setSaveSuccessful(saveSuccessful);
            result.appendMessage(message);

            logger.info("Saved uploaded data");

        } catch (RestClientException ex) {
            result.setSaveSuccessful(false);
            result.appendMessage("Upload data save failed");

            logger.error("Upload data save failed", ex);
        }
    }
}
