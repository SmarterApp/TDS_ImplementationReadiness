package org.cresst.sb.irp.upload;

import java.util.HashMap;
import java.util.Map;

import org.cresst.sb.irp.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

/**
 * Handles file uploads
 */
@Controller
public class CATFileUploadController {
    private final static Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Value("${irp.version}")
    String irpVersion;

    @RequestMapping(value = "/catUpload", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String upload(@RequestParam("catItemFile") MultipartFile itemFile, @RequestParam("catStudentFile") MultipartFile studentFile) throws FileUploadException {
        if(itemFile.isEmpty()) {
            throw new FileUploadException(itemFile.getName() + " not uploaded");
        } else if (studentFile.isEmpty()) {
            throw new FileUploadException(studentFile.getName() + " not uploaded");
        } else {
            logger.info("uploaded: " + itemFile.getName());
            logger.info("uploaded: " + studentFile.getName());
        }

        return "";
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
