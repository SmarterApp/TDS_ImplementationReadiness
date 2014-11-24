package org.cresst.sb.irp.upload;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

/**
 * Handles file uploads
 */
@Controller
public class FileUploadController {
    private static Logger logger = Logger.getLogger(FileUploadController.class);

    @Autowired
    private Unmarshaller unmarshaller;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ModelAndView upload(@RequestParam("file") MultipartFile file) {

        String validationMessage = "Invalid";

        ModelAndView mav = new ModelAndView();
        mav.setViewName("report");

        if (!file.isEmpty()) {
            try {
                TDSReport tdsReport = (TDSReport)unmarshaller.unmarshal(new StreamSource(file.getInputStream()));
                // TODO: Run validation engine on TDS Report and return an analysis report
                if (tdsReport != null) {
                    validationMessage = "Valid";
                }
            } catch (IOException | XmlMappingException | ClassCastException e) {
                logger.info("File upload failed", e);
            }
        }

        mav.addObject("validation", validationMessage);

        return mav;
    }
}
