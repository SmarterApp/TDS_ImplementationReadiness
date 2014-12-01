package org.cresst.sb.irp.upload;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.analysis.AnalysisResponse;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.cresst.sb.irp.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Handles file uploads
 */
@Controller
public class FileUploadController {
    private static Logger logger = Logger.getLogger(FileUploadController.class);

	@Autowired
	public AnalysisService analysisService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ModelAndView upload(@RequestParam("file") MultipartFile file) {

        String validationMessage = "Invalid";
        int numTdsFilesUploaded = 1;

        ModelAndView mav = new ModelAndView();
        mav.setViewName("report");

        if (!file.isEmpty()) {
            try {
                Iterable<Path> tdsReportPaths = getTdsReportFiles(file);

                // TODO: Run validation/analysis engine on TDS Reports and return an analysis report
                AnalysisResponse analysisResponse = analysisService.analysisProcess(tdsReportPaths);

            } catch (IOException | XmlMappingException | ClassCastException e) {
                logger.info("File upload failed", e);
            }
        }

        mav.addObject("validation", validationMessage);
        mav.addObject("numTdsFilesUploaded", numTdsFilesUploaded);

        return mav;
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

        // Find all XML documents in the ZIP file and add them to paths
        final URI uri = URI.create("jar:file:" + tmpZip.toUri().getPath());
        try (FileSystem fs = FileSystems.newFileSystem(uri, new HashMap<String, Object>())) {
            for (Path root : fs.getRootDirectories()) {

                // Walk the virtual ZIP filesystem looking for XML documents
                Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
                    private final PathMatcher matcher = fs.getPathMatcher("glob:*.xml");

                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        if (matcher.matches(file.getFileName())) {
                            // Copy the XML file from the ZIP file to the temp directory
                            Path destFile = Paths.get(tmpDir.toString(), file.getFileName().toString());
                            Path copiedFile = Files.copy(file, destFile, StandardCopyOption.REPLACE_EXISTING);

                            // Add the copied XML file to paths
                            paths.add(copiedFile);
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFileFailed(Path file, IOException ex) {
                        return FileVisitResult.CONTINUE;
                    }
                });
            }
        }

        // Delete the ZIP file. It's not needed anymore.
        Files.delete(tmpZip);
    }

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
	/* . . . . . . . . . . . . . EXCEPTION HANDLERS . . . . . . . . . . . . .. */
	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	@ExceptionHandler(NotFoundException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Not Found")
	public String handleNotFoundException(NotFoundException ex) {
		logger.info("NotFoundException: " + ex.getMessage());
		return ex.getMessage();
	}
    
}
