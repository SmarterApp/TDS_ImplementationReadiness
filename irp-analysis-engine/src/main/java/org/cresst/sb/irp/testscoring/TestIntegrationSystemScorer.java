package org.cresst.sb.irp.testscoring;

import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Calls the Test Integration System (TIS) service to score a TDSReport
 */
public class TestIntegrationSystemScorer implements ITestScorer {

    private RestTemplate restTemplate;
    private String itemScoringServiceUrl;

    public TestIntegrationSystemScorer(RestTemplate restTemplate, String itemScoringServiceUrl) {
        this.restTemplate = restTemplate;
        this.itemScoringServiceUrl = itemScoringServiceUrl;
    }

    @Override
    public TDSReport scoreTDSReport(TDSReport tdsReport) {
        TDSReport scoredTDSReport = restTemplate.postForObject(itemScoringServiceUrl, tdsReport, TDSReport.class);
        return scoredTDSReport;
    }
}
