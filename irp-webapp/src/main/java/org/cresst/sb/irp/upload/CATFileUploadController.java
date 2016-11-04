package org.cresst.sb.irp.upload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cresst.sb.irp.cat.analysis.engine.CATParsingService;
import org.cresst.sb.irp.cat.domain.analysis.Blueprint;
import org.cresst.sb.irp.cat.domain.analysis.CATAnalysisResponse;
import org.cresst.sb.irp.cat.domain.analysis.CATDataModel;
import org.cresst.sb.irp.cat.domain.analysis.ItemResponseCAT;
import org.cresst.sb.irp.cat.domain.analysis.PoolItem;
import org.cresst.sb.irp.cat.domain.analysis.PoolItemELA;
import org.cresst.sb.irp.cat.domain.analysis.PoolItemMath;
import org.cresst.sb.irp.cat.domain.analysis.StudentScoreCAT;
import org.cresst.sb.irp.cat.domain.analysis.TrueTheta;
import org.cresst.sb.irp.cat.service.CATAnalysisService;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
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
    private final static Logger logger = LoggerFactory.getLogger(CATFileUploadController.class);

    @Value("${irp.version}")
    String irpVersion;

    @Value("${cat.math.itempool}")
    private Resource mathItemPoolResource;

    @Value("${cat.elag11.itempool}")
    private Resource itemPoolResource;

    @Value("${cat.elag11.blueprint}")
    private Resource blueprintResource;

    @Value("${cat.elag11.studentdata}")
    private Resource studentDataResource;

    @Value("${cat.elag11.truethetas}")
    private Resource trueThetasResource;

    @Autowired
    private CATParsingService catParsingService;

    @Autowired
    private CATAnalysisService catAnalysisService;

    @RequestMapping(value = "/catUpload", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public CATAnalysisResponse upload(@RequestParam("catItemFile") MultipartFile itemFile, @RequestParam("catStudentFile") MultipartFile studentFile) throws FileUploadException, IOException {
        if(itemFile.isEmpty()) {
            throw new FileUploadException(itemFile.getName() + " not uploaded");
        } else if (studentFile.isEmpty()) {
            throw new FileUploadException(studentFile.getName() + " not uploaded");
        } else {
            logger.info("uploaded: " + itemFile.getName());
            logger.info("uploaded: " + studentFile.getName());

            List<ItemResponseCAT> itemResponses = null;
            List<StudentScoreCAT> studentScores = null;
            List<PoolItemELA> poolItems = null;
            List<PoolItemMath> mathPoolItems = null;
            List<PoolItem> allItems = new ArrayList<>();
            List<TrueTheta> trueThetas = null;
            List<Blueprint> blueprints = null;
            try {
                itemResponses = catParsingService.parseItemCsv(itemFile.getInputStream());
                studentScores = catParsingService.parseStudentCsv(studentFile.getInputStream());
                //mathPoolItems = catParsingService.parsePoolItemsMath(mathItemPoolResource.getInputStream());
                poolItems = catParsingService.parsePoolItemsELA(itemPoolResource.getInputStream());
                allItems.addAll(mathPoolItems);
                //allItems.addAll(poolItems);
                trueThetas = catParsingService.parseTrueThetas(trueThetasResource.getInputStream());
                //blueprints = catAnalysisService.parseBlueprint(blueprintResource.getInputStream());
            } catch (IOException e) {
                logger.error("{}", e.getMessage());
                throw e;
            }

            CATDataModel catData = new CATDataModel();
            catData.setItemResponses(itemResponses);
            catData.setStudentScores(studentScores);
            catData.setPoolItems(allItems);
            catData.setTrueThetas(trueThetas);
            // TODO: Need to manually fix blueprint files
            catData.setBlueprints(blueprints);

            CATAnalysisResponse response = catAnalysisService.analyzeCatResults(catData);

            return response;
        }
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
