package org.cresst.sb.irp.automation.art;

import org.cresst.sb.irp.automation.engine.Rollbacker;
import org.cresst.sb.irp.automation.web.AutomationRestTemplate;
import org.opentestsystem.delivery.testreg.domain.Student;
import org.opentestsystem.shared.search.domain.SearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ArtStudentUploader extends ArtUploader implements Rollbacker {
    private final static Logger logger = LoggerFactory.getLogger(ArtStudentUploader.class);

    // Must contain the template since it's shared between instances
    private static final List<String> studentTemplateLines = new ArrayList<>();

    final private AutomationRestTemplate automationRestTemplate;
    final private URL artUrl;
    final private String stateAbbreviation;
    final private String responsibleDistrictId;
    final private String responsibleInstitutionId;

    public ArtStudentUploader(Resource studentTemplate,
                              AutomationRestTemplate automationRestTemplate, URL artUrl,
                              String stateAbbreviation, String responsibleDistrictId, String responsibleInstitutionId) throws IOException {
        super(automationRestTemplate, artUrl);

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

    @Override
    String generateData() {
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

    @Override
    String getFormatType() {
        return "STUDENT";
    }

    @Override
    String getFilename() {
        return "IRPStudents.csv";
    }

}
