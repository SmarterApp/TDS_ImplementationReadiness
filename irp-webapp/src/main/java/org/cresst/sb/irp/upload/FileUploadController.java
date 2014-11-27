package org.cresst.sb.irp.upload;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.analysis.AnalysisResponse;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.cresst.sb.irp.service.AnalysisService;
import org.cresst.sb.irp.utils.XMLValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.xml.transform.stream.StreamSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles file uploads
 */
@Controller
public class FileUploadController {
    private static Logger logger = Logger.getLogger(FileUploadController.class);

    List<AnalysisResponse> analysisResponseList = new ArrayList<AnalysisResponse>();
    
	@Value("classpath:irp-package/TDSReport.xsd")
	private Resource TDSReportXSDResource;
    
	@Autowired
	public AnalysisService analysisService;
    
    @Autowired
    private Unmarshaller unmarshaller;

    @Autowired
	private XMLValidate xmlValidate;
    
    
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ModelAndView upload(@RequestParam("file") MultipartFile file) {

        String validationMessage = "Invalid";

        ModelAndView mav = new ModelAndView();
        mav.setViewName("report");

        /*
         * validate TDSResult XML against Schema temporarily put here. //TODO: need to continue code
         */
        try {
			boolean bln = xmlValidate.validateXMLSchema(TDSReportXSDResource, file);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        
        if (!file.isEmpty()) {
            try {
                TDSReport tdsReport = (TDSReport)unmarshaller.unmarshal(new StreamSource(file.getInputStream()));
                AnalysisResponse analysisResponse = new AnalysisResponse();
                analysisResponseList.add(analysisResponse);
                analysisResponse.setTDSReport(tdsReport);
                analysisResponse.setMultipartFile(file);
                
                analysisService.analysisProcess(analysisResponse);
                // TODO: Run validation engine on TDS Report and return an analysis report
                if (tdsReport != null) {
                    validationMessage = "Valid";
                }
            } catch (IOException | XmlMappingException | ClassCastException e) {
                logger.info("File upload failed", e);
            }
        }
        
        //Paul may need to modify here, no return object yet for analysisProcess function
        /*
        java.util.List<File> fileList = new java.util.ArrayList<>();
        fileList.add((File) file);
        analysisService.analysisProcess(fileList);
        */
        
        mav.addObject("validation", validationMessage);

        return mav;
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
