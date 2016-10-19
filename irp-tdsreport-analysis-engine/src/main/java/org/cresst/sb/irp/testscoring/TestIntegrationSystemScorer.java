package org.cresst.sb.irp.testscoring;

import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Calls the Test Integration System (TIS) service to score a TDSReport
 */
public class TestIntegrationSystemScorer implements ITestScorer {
    private final static Logger logger = LoggerFactory.getLogger(TestIntegrationSystemScorer.class);

    private final static int MAX_TRIES = 3;

    // This parameters is needed by the TestResult POST endpoint. The actual value isn't important.
    private final static String postParameter = "?statusCallback=http://localhost";
    private final static String getParameter = "?fileId={fileId}";

    private RestTemplate restTemplate;
    private String testScoringServiceUrl;

    public TestIntegrationSystemScorer(RestTemplate restTemplate, String testScoringServiceUrl) {
        this.restTemplate = restTemplate;
        this.testScoringServiceUrl = testScoringServiceUrl;
    }

    @Override
    public TDSReport scoreTDSReport(TDSReport tdsReport) {
        TDSReport scoredTDSReport = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);

        HttpEntity<TDSReport> httpEntity = new HttpEntity<>(tdsReport, headers);
        String fileId = restTemplate.postForObject(testScoringServiceUrl + postParameter, httpEntity, String.class);
        logger.debug("File ID: {}", fileId);

        if (fileId != null) {
            HttpStatus httpStatus;
            ResponseEntity<TDSReport> responseEntity = null;

            try {
                Thread.sleep(1000);
                responseEntity = restTemplate.exchange(
                        testScoringServiceUrl + getParameter,
                        HttpMethod.GET,
                        null,
                        TDSReport.class,
                        fileId);
                httpStatus = responseEntity.getStatusCode();
            } catch (InterruptedException ex) {
                httpStatus = HttpStatus.NOT_FOUND;
            } catch (HttpClientErrorException ex) {
                httpStatus = ex.getStatusCode();
            }

            int tries = 0;
            while (httpStatus == HttpStatus.NOT_FOUND && tries++ < MAX_TRIES) {
                try {
                    logger.info("Retrying TIS invocation. Attempt {}", tries);

                    Thread.sleep(100);

                    responseEntity = restTemplate.exchange(
                            testScoringServiceUrl + getParameter,
                            HttpMethod.GET,
                            null,
                            TDSReport.class,
                            fileId);
                    httpStatus = responseEntity.getStatusCode();
                } catch (InterruptedException e) {
                    break;
                } catch (HttpClientErrorException ex) {
                    httpStatus = ex.getStatusCode();
                }
            }

            if (httpStatus == HttpStatus.OK && responseEntity != null) {
                scoredTDSReport = responseEntity.getBody();
            }
        }

        return scoredTDSReport;
    }
}
