package behavior.steps.utils;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Creates stubbed TdsReport XML documents
 */
public class TdsReportFactory {
    public static MockMultipartFile getTdsReport(String type) throws IOException {
        MockMultipartFile tdsReport = null;
        switch(type) {
            case "malformed":
                tdsReport = new MockMultipartFile("file", "bad-tds-report.xml", "text/plain", "<tdsreport></tdsreport>".getBytes());
                break;
            case "valid":
                Resource resource = new ClassPathResource("good-tds-report.xml");
                tdsReport = new MockMultipartFile("file", resource.getFilename(), "text/plain",
                        FileUtils.readFileToByteArray(resource.getFile()));
                break;
            default:
                throw new IllegalArgumentException(type + " is not a valid input");
        }

        return tdsReport;
    }
}
