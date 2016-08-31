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

public class ArtStudentAccommodationsUploader extends ArtUploader implements Rollbacker {
    private final static Logger logger = LoggerFactory.getLogger(ArtStudentAccommodationsUploader.class);

    // Must contain the template since it's shared between instances
    private static final List<String> studentAccommodationsTemplateLines = new ArrayList<>();

    final private String stateAbbreviation;

    public ArtStudentAccommodationsUploader(Resource studentAccommodationsTemplate, AutomationRestTemplate automationRestTemplate,
                                            URL artUrl, String stateAbbreviation) throws IOException {
        super(automationRestTemplate, artUrl);

        if (studentAccommodationsTemplateLines.isEmpty()) {
            ArtStudentAccommodationsUploader.studentAccommodationsTemplateLines.addAll(
                    Files.readAllLines(Paths.get(studentAccommodationsTemplate.getURI()), StandardCharsets.UTF_8));
        }

        this.stateAbbreviation = stateAbbreviation;
    }

    @Override
    public void rollback() {
    }

    @Override
    String generateData() {
        StringBuilder builder = new StringBuilder(studentAccommodationsTemplateLines.get(0));
        builder.append("\n");
        for (int i = 1; i < studentAccommodationsTemplateLines.size(); i++) {
            builder.append(studentAccommodationsTemplateLines.get(i).replace("{SA}", stateAbbreviation))
                    .append("\n");
        }

        return builder.toString();
    }

    @Override
    String getFormatType() {
        return "DESIGNATEDSUPPORTSANDACCOMMODATIONS";
    }

    @Override
    String getFilename() {
        return "IRPStudentsDesignatedSupportsAndAccommodations.csv";
    }
}
