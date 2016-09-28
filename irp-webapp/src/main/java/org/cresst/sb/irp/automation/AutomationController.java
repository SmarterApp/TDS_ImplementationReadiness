package org.cresst.sb.irp.automation;

import org.cresst.sb.irp.automation.data.TdsReportUris;
import org.cresst.sb.irp.domain.analysis.AnalysisResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller handles automation requests and status reports. It is designed to run as a single instance
 * on a single server since it stores status results in-memory.
 */
@RestController
public class AutomationController {
    private final static Logger logger = LoggerFactory.getLogger(AutomationController.class);

    @PostMapping(value = "/analysisReports", produces = "application/json")
    public AnalysisResponse analysisReports(TdsReportUris tdsReportUris) {
        logger.info("TDS Report URIs received: {}", tdsReportUris);
        return new AnalysisResponse();
    }
}
