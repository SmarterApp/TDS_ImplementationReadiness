package org.cresst.sb.irp.upload;

import org.cresst.sb.irp.domain.analysis.AnalysisResponse;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.cresst.sb.irp.service.AnalysisService;
import org.cresst.sb.irp.zip.IrpZipUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles file uploads
 */
@Controller
public class FileUploadController {
    private final static Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Value("${irp.version}")
    String irpVersion;

	@Autowired
	private AnalysisService analysisService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public AnalysisResponse upload(@RequestParam("name") String name, @RequestParam("file") MultipartFile file) throws FileUploadException {

        if (!file.isEmpty()) {
            try {
                // Get the paths to all the uploaded TDS report files
                Iterable<Path> tdsReportPaths = getTdsReportFiles(file);

                // Analyze the TDS Report files
                AnalysisResponse analysisResponse = analysisService.analysisProcess(tdsReportPaths);
                analysisResponse.setVendorName(name);
                analysisResponse.setIrpVersion(irpVersion);
                analysisResponse.setDateTimeAnalyzed(DateTime.now(DateTimeZone.forID("America/Los_Angeles")).toString());

                return analysisResponse;

            } catch (IOException | XmlMappingException | ClassCastException e) {
                logger.info("File upload failed", e);

                throw new FileUploadException(e);
            }
        }

        throw new FileUploadException("File not uploaded.");
    }

    /**
     * Returns all the Paths to the uploaded TDS Report files.
     * @param multipartFile
     * @return
     * @throws IOException
     */
    private Iterable<Path> getTdsReportFiles(MultipartFile multipartFile) throws IOException {

        final List<Path> paths = new ArrayList<>();
        String fileType = multipartFile.getContentType();

        // Determine file type
        if ("application/zip".equals(fileType)) {

            getTdsReportFilesFromZip(multipartFile, paths);

        } else if ("text/xml".equals(fileType)) {
            final Path tmpDir = Files.createTempDirectory("irp");
            final Path tmpFile = Paths.get(tmpDir.toString(), multipartFile.getOriginalFilename());
            
            // Move the uploaded file to the new temporary directory
            multipartFile.transferTo(tmpFile.toFile());

            paths.add(tmpFile);

        } else {
            throw new IOException("Unable to identify file upload type");
        }

        return paths;
    }

    /**
     * Gets all the XML documents in a ZIP file
     * @param multipartFile The file that was uploaded
     * @param paths Where the Path to each XML document will be stored
     * @throws IOException
     */
    private void getTdsReportFilesFromZip(MultipartFile multipartFile, final List<Path> paths) throws IOException {

        // Create a temporary directory and file to store the zip file
        final Path tmpDir = Files.createTempDirectory("irp");
        final Path tmpZip = Files.createTempFile(tmpDir, null, ".zip");

        // Move the uploaded ZIP file to the new temporary directory
        multipartFile.transferTo(tmpZip.toFile());

        IrpZipUtils.extractFilesFromZip(paths, tmpDir, tmpZip);

        // Delete the ZIP file. It's not needed anymore.
        Files.delete(tmpZip);
    }

    /**
     * Exception handler if something goes wrong in the upload method
     * @param ex
     * @return
     */
    @ExceptionHandler(FileUploadException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Map<String, String> handleFileUploadException(FileUploadException ex) {
        Map<String, String> error = new HashMap<>(1);
        error.put("error", "true");
        return error;
    }

    /**
     * Exception handler for not found exception
     * @param ex
     * @return
     */
	@ExceptionHandler(NotFoundException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Not Found")
	public String handleNotFoundException(NotFoundException ex) {
		logger.info("NotFoundException: " + ex.getMessage());
		return ex.getMessage();
	}
    
}
