package org.cresst.sb.irp.automation;

import org.cresst.sb.irp.automation.data.AdapterData;
import org.cresst.sb.irp.domain.analysis.AnalysisResponse;
import org.cresst.sb.irp.service.AnalysisService;
import org.cresst.sb.irp.zip.IrpZipUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;

import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.spi.FileTypeDetector;
import java.util.ArrayList;
import java.util.List;

/**
 * This controller handles automation requests and status reports. It is designed to run as a single instance
 * on a single server since it stores status results in-memory.
 */
@RestController
public class AutomationController {
    private final static Logger logger = LoggerFactory.getLogger(AutomationController.class);

    @Value("${irp.version}")
    String irpVersion;

    @Autowired
    private AnalysisService analysisService;

    @PostMapping(value = "/analysisReports", produces = "application/json")
    public AnalysisResponse analysisReports(@Valid @RequestBody AdapterData adapterData) throws IOException {
        logger.info("TDS Report URIs received: {}", adapterData);

        Iterable<Path> filePaths = downloadReports(adapterData);

        AnalysisResponse analysisResponse = analysisService.analysisProcess(filePaths);
        analysisResponse.setVendorName(adapterData.getVendorName());
        analysisResponse.setIrpVersion(irpVersion);
        analysisResponse.setDateTimeAnalyzed(DateTime.now(DateTimeZone.forID("America/Los_Angeles")).toString());

        return analysisResponse;
    }

    private List<Path> downloadReports(final AdapterData adapterData) throws IOException {
        final List<Path> filePaths = new ArrayList<>();

        final Path tmpDir = Files.createTempDirectory("irp-adapter");
        logger.info("Temp directory {}", tmpDir.toString());

        for (URI tdsReportUri : adapterData.getTdsReportLinks()) {
            final URLConnection urlConnection = tdsReportUri.toURL().openConnection();
            final String mimeType = urlConnection.getContentType();

            final String suffix = "application/zip".equals(mimeType) ? "-zip" : "-xml";
            final Path tmpFile = Files.createTempFile(tmpDir, "irp-", suffix);

            try (InputStream inputStream = urlConnection.getInputStream();
                 ReadableByteChannel rbc = Channels.newChannel(inputStream);
                 FileOutputStream fileOuputStream = new FileOutputStream(tmpFile.toFile())) {

                fileOuputStream.getChannel().transferFrom(rbc, 0, 5242880);

                if ("application/zip".equals(mimeType)) {
                    IrpZipUtils.extractFilesFromZip(filePaths, tmpDir, tmpFile);
                    Files.delete(tmpFile);
                } else {
                    filePaths.add(tmpFile);
                }
            } catch (FileNotFoundException ex) {
                logger.info("The file {} was not found", ex.getMessage());
                // TODO: Should send this info back to UI
            }
        }

        return filePaths;
    }
}
