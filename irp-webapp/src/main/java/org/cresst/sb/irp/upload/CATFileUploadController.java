package org.cresst.sb.irp.upload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cresst.sb.irp.cat.ResourceSelector;
import org.cresst.sb.irp.cat.analysis.engine.CATParsingService;
import org.cresst.sb.irp.cat.domain.analysis.BlueprintStatement;
import org.cresst.sb.irp.cat.domain.analysis.CATAnalysisResponse;
import org.cresst.sb.irp.cat.domain.analysis.CATDataModel;
import org.cresst.sb.irp.cat.domain.analysis.ItemResponseCAT;
import org.cresst.sb.irp.cat.domain.analysis.PoolItem;
import org.cresst.sb.irp.cat.domain.analysis.StudentScoreCAT;
import org.cresst.sb.irp.cat.domain.analysis.TrueTheta;
import org.cresst.sb.irp.cat.service.CATAnalysisService;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final static Logger logger = LoggerFactory.getLogger(CATFileUploadController.class);

    @Value("${irp.version}")
    String irpVersion;

    @Value("${cat.datafolder}")
    private String catDataFolder;

    @Autowired
    private CATParsingService catParsingService;

    @Autowired
    private CATAnalysisService catAnalysisService;

    @RequestMapping(value = "/catUpload/subject/{subject}/grade/{grade}", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public CATAnalysisResponse upload(
            @RequestParam("catItemFile") MultipartFile itemFile, 
            @RequestParam("catStudentFile") MultipartFile studentFile,
            @RequestParam("vendorName") String vendorName,
            @PathVariable("subject") String subject,
            @PathVariable("grade") int grade
            ) throws FileUploadException, IOException {
        if(itemFile.isEmpty()) {
            throw new FileUploadException(itemFile.getName() + " not uploaded");
        } else if (studentFile.isEmpty()) {
            throw new FileUploadException(studentFile.getName() + " not uploaded");
        } else {
            logger.info("uploaded: " + itemFile.getName());
            logger.info("uploaded: " + studentFile.getName());

            List<ItemResponseCAT> itemResponses = null;
            List<? extends StudentScoreCAT> studentScores = null;
            List<PoolItem> allItems = new ArrayList<>();
            List<TrueTheta> trueThetas = null;
            List<BlueprintStatement> blueprintStatements = null;
            try {
                itemResponses = catParsingService.parseItemCsv(itemFile.getInputStream());
            } catch (IOException e) {
                return createErrorMessage(String.format("Error parsing file: %s Reason: %s",
                        itemFile.getOriginalFilename(), e.getMessage()));
            }
            if (subject.equals("ela")) {
                try {
                    allItems.addAll(
                            catParsingService.parsePoolItemsELA(ResourceSelector.getItemPool(catDataFolder, subject)));
                } catch (IOException e) {
                    logger.error(e.getMessage());
                    return createErrorMessage("Error parsing the pool items");
                }
                try {
                    studentScores = catParsingService.parseStudentELACsv(studentFile.getInputStream());
                } catch (IOException e) {
                    return createErrorMessage(String.format("Error parsing file: %s Reason: %s",
                            studentFile.getOriginalFilename(), e.getMessage()));
                }
            } else if (subject.equals("math")) {
                try {
                    allItems.addAll(
                            catParsingService.parsePoolItemsMath(ResourceSelector.getItemPool(catDataFolder, subject)));
                } catch (IOException e) {
                    logger.error(e.getMessage());
                    return createErrorMessage("Error parsing math pool items");
                }
                try {
                    studentScores = catParsingService.parseStudentMathCsv(studentFile.getInputStream());
                } catch (IOException e) {
                    return createErrorMessage(String.format("Error parsing file: %s Reason: %s",
                            studentFile.getOriginalFilename(), e.getMessage()));
                }
            }
            try {
                blueprintStatements = catParsingService
                        .parseBlueprintCsv(ResourceSelector.getBlueprints(catDataFolder));
            } catch (IOException e) {
                return createErrorMessage("Could not parse blueprint csv: " + e.getMessage());
            }
            try {
                trueThetas = catParsingService
                        .parseTrueThetas(ResourceSelector.getTrueThetasGz(catDataFolder, subject, grade));
            } catch (IOException e) {
                return createErrorMessage("Could not parse the true theta file: " + e.getMessage());
            }

            CATDataModel catData = new CATDataModel();
            catData.setItemResponses(itemResponses);
            catData.setStudentScores(studentScores);
            catData.setPoolItems(allItems);
            catData.setTrueThetas(trueThetas);
            catData.setBlueprintStatements(filterGrade(blueprintStatements, subject, grade));
            catData.setGrade(grade);
            catData.setSubject(subject);

            CATAnalysisResponse response = catAnalysisService.analyzeCatResults(catData);
            response.setVendorName(vendorName);
            response.setIrpVersion(irpVersion);
            response.setDateTimeAnalyzed(DateTime.now(DateTimeZone.forID("America/Los_Angeles")).toString());

            return response;
        }
    }

    private CATAnalysisResponse createErrorMessage(String errorMessage) {
        logger.error(errorMessage);
        CATAnalysisResponse resp = new CATAnalysisResponse();
        resp.setError(true);
        resp.setErrorMessage(errorMessage);
        return resp;
    }

    private List<BlueprintStatement> filterGrade(List<BlueprintStatement> blueprintStatements, String subject,
            int grade) {
        List<BlueprintStatement> results = new ArrayList<>();
        for (BlueprintStatement s : blueprintStatements) {
            if (s.getGrade() == grade && s.getSubject().equalsIgnoreCase(subject))
                results.add(s);
        }
        return results;
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
