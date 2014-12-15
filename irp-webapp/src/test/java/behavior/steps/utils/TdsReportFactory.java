package behavior.steps.utils;

import org.apache.commons.io.FileUtils;
import org.mockito.Mock;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Creates stubbed TdsReport XML documents
 */
public class TdsReportFactory {
    public static MockMultipartFile getTdsReport(String type) throws IOException {
        MockMultipartFile tdsReport = null;
        switch(type) {
            case "malformed":
                tdsReport = new MockMultipartFile("file", "bad-tds-report.xml", "text/xml", "<tdsreport></tdsreport>".getBytes());
                break;
            case "valid":
                Resource resource = new ClassPathResource("good-tds-report.xml");
                tdsReport = new MockMultipartFile("file", resource.getFilename(), "text/xml",
                        FileUtils.readFileToByteArray(resource.getFile()));
                break;
            case "bad-content-type":
                tdsReport = new MockMultipartFile("file", "bad-tds-report.xml", MediaType.IMAGE_GIF_VALUE, "<tdsreport></tdsreport>".getBytes());
                break;
            default:
                throw new IllegalArgumentException(type + " is not a valid input");
        }

        return tdsReport;
    }

    public static MockMultipartFile getZippedTdsReport() throws IOException {
        Resource resource = new ClassPathResource("good-tds-report.xml");
        byte[] reportByteArray = FileUtils.readFileToByteArray(resource.getFile());
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(boas)) {

            ZipEntry entry = new ZipEntry("tdsreport-1.xml");
            zos.putNextEntry(entry);
            zos.write(reportByteArray);
            zos.closeEntry();

            entry = new ZipEntry("tdsreport-2.xml");
            zos.putNextEntry(entry);
            zos.write(reportByteArray);
            zos.closeEntry();

            entry = new ZipEntry("tdsreport-3.xml");
            zos.putNextEntry(entry);
            zos.write(reportByteArray);
            zos.closeEntry();
        }

        MockMultipartFile zippedTdsReport = new MockMultipartFile("file", "tdsreports.zip", "application/zip",
                boas.toByteArray());

        return zippedTdsReport;
    }
}
