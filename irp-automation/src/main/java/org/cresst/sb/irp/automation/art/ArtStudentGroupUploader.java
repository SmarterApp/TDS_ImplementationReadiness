package org.cresst.sb.irp.automation.art;

import org.cresst.sb.irp.automation.engine.Rollbacker;
import org.cresst.sb.irp.automation.web.AutomationRestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ArtStudentGroupUploader extends ArtUploader implements Rollbacker {
    private final static Logger logger = LoggerFactory.getLogger(ArtStudentGroupUploader.class);

    // Must contain the template since it's shared between instances
    private static final List<String> studentGroupTemplateLines = new ArrayList<>();

    final private String stateAbbreviation;
    final private String responsibleDistrictId;
    final private String responsibleInstitutionId;
    final private String studentGroupEmail;

    private boolean rollbackState = false;


    public ArtStudentGroupUploader(Resource studentGroupTemplate, AutomationRestTemplate automationRestTemplate, URL artUrl,
                            String stateAbbreviation, String responsibleDistrictId, String responsibleInstitutionId,
                            String studentGroupEmail) throws IOException {
        super(automationRestTemplate, artUrl);

        if (studentGroupTemplateLines.isEmpty()) {
            studentGroupTemplateLines.addAll(Files.readAllLines(Paths.get(studentGroupTemplate.getURI()), StandardCharsets.UTF_8));
        }

        this.stateAbbreviation = stateAbbreviation;
        this.responsibleDistrictId = responsibleDistrictId;
        this.responsibleInstitutionId = responsibleInstitutionId;
        this.studentGroupEmail = studentGroupEmail;
    }

    @Override
    public void rollback() {
        rollbackState = true;
        ArtUploaderResult result = uploadData();
        rollbackState = false;

        logger.info("ART StudentGroup roll back was{}successful. Message: {}", result.isSuccessful() ? "" : " un", result.getMessage());
    }

    @Override
    String generateData() {
        return generateDataForStudentGroup(rollbackState);
    }

    @Override
    String getFormatType() {
        return "STUDENTGROUP";
    }

    @Override
    String getFilename() {
        return "IRPStudentGroup.csv";
    }

    private String generateDataForStudentGroup(boolean deleteGroup) {
        StringBuilder builder = new StringBuilder(studentGroupTemplateLines.get(0));
        builder.append("\n");
        for (int i = 1; i < studentGroupTemplateLines.size(); i++) {
            builder.append(studentGroupTemplateLines.get(i)
                    .replace("{SA}", stateAbbreviation)
                    .replace("{RDI}", responsibleDistrictId)
                    .replace("{RII}", responsibleInstitutionId)
                    .replace("{EMAIL}", studentGroupEmail)
                    .replace("{DELETE}", deleteGroup ? "DELETE" : ""))
                    .append("\n");
        }

        return builder.toString();
    }
}
