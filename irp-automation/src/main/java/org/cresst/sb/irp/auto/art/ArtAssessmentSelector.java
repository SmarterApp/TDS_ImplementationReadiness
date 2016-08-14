package org.cresst.sb.irp.auto.art;

import org.codehaus.janino.Access;
import org.cresst.sb.irp.auto.accesstoken.AccessToken;
import org.cresst.sb.irp.auto.engine.Rollbacker;
import org.cresst.sb.irp.auto.tsb.TestSpecBankData;
import org.cresst.sb.irp.common.web.LoggingRequestInterceptor;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.opentestsystem.delivery.testreg.domain.Assessment;
import org.opentestsystem.delivery.testreg.domain.ImplicitEligibilityRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ArtAssessmentSelector implements Rollbacker {
    private final static Logger logger = LoggerFactory.getLogger(ArtAssessmentSelector.class);

    private final static int NUM_OPPORTUNITIES = 1000;

    private final AccessToken accessToken;
    private final URL artUrl;
    private final String stateAbbreviation;

    private List<String> selectedAssessmentIds = new ArrayList<>();

    public ArtAssessmentSelector(AccessToken accessToken, URL artUrl, String stateAbbreviation) {
        this.accessToken = accessToken;
        this.artUrl = artUrl;
        this.stateAbbreviation = stateAbbreviation;
    }

    /**
     * In ART, selects/registers the given Assessments that was side-loaded into TSB.
     * @param testSpecBankData The data that was side-loaded into the vendor's TSB
     * @return The list of IDs for each Assessment in the vendor's ART application.
     */
    public List<String> selectAssessments(List<TestSpecBankData> testSpecBankData) {

        RestTemplate restTemplate = new RestTemplate();
        ClientHttpRequestInterceptor ri = new LoggingRequestInterceptor();
        List<ClientHttpRequestInterceptor> ris = new ArrayList<>();
        ris.add(ri);
        restTemplate.setInterceptors(ris);
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));

        final DateTime testWindowStart = DateTime.now();
        final DateTime testWindowEnd = testWindowStart.plus(Duration.standardDays(2));

        URL artAssessmentUrl;
        try {
            artAssessmentUrl = new URL(artUrl, "/rest/assessment");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        selectedAssessmentIds.clear();
        for (TestSpecBankData tsbData : testSpecBankData) {
            Assessment assessment = generateAssessment(tsbData, stateAbbreviation, testWindowStart, testWindowEnd);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Bearer " + accessToken);
            httpHeaders.add("Accept", "application/json");
            httpHeaders.add("Content-Type", "application/json");

            HttpEntity<Assessment> request = new HttpEntity<>(assessment, httpHeaders);

            ResponseEntity<Assessment> response = restTemplate.exchange(artAssessmentUrl.toString(),
                    HttpMethod.POST,
                    request,
                    Assessment.class);

            String assessmentId = response.getBody().getId();
            selectedAssessmentIds.add(assessmentId);

            logger.debug("Selected Assessment {} and received ID={}", assessment.getEntityId(), assessmentId);
        }

        return selectedAssessmentIds;
    }

    @Override
    public void rollback() {

        RestTemplate restTemplate = new RestTemplate();
        ClientHttpRequestInterceptor ri = new LoggingRequestInterceptor();
        List<ClientHttpRequestInterceptor> ris = new ArrayList<>();
        ris.add(ri);
        restTemplate.setInterceptors(ris);
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));

        for (String assessmentId : selectedAssessmentIds) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Bearer " + accessToken);
            httpHeaders.add("Accept", "application/json");
            httpHeaders.add("Content-Type", "application/json");

            HttpEntity<?> request = new HttpEntity<>(httpHeaders);

            URL artAssessmentUrl;
            try {
                artAssessmentUrl = new URL(artUrl, "/rest/assessment/" + assessmentId);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            restTemplate.exchange(artAssessmentUrl.toString(),
                    HttpMethod.DELETE,
                    request,
                    Assessment.class);
        }
    }

    /**
     * Example data to send
{
  "id":null,
  "entityId":"(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016",
  "numGlobalOpportunities":"9999",
  "testWindow":[{
    "beginWindow":"2016-07-19T08:47:38.428Z",
    "endWindow":"2016-07-19T16:00:00.000Z",
    "numOpportunities":"9999",
    "beginWindowOpened":false,
    "endWindowOpened":false
    }],
  "delayRule":"0",
  "eligibilityType":"IMPLICIT",
  "implicitEligibilityRules":[{
    "field":"stateAbbreviation",
    "value":"CA",
    "operatorType":"EQUALS",
    "ruleType":"ENABLER"
    }],
  "subjectCode":"ELA",
  "testName":"(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016",
  "version":"8185.0",
  "tenantId":"55661e17e4b0c4ebd30aa19f",
  "exists":false,
  "updatedVersion":false,
  "testForm":null,
  "grade":"11",
  "academicYear":"2016",
  "category":"",
  "type":"summative",
  "testLabel":"Grade 11 ELA",
  "atLeastOneImplicitRule":"",
  "sourceTsb":"https://tsb.smarterapp.cresst.net:8443/rest/",
  "alternateKey":"testName: (SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016, version: 8185.0, testLabel: Grade 11 ELA, sourceTsb: https://tsb.smarterapp.cresst.net:8443/rest/, tenantId: 55661e17e4b0c4ebd30aa19f",
  "formatType":"ASSESSMENT",
  "enablerRules":[],
  "disablerRules":[],
  "action":null,
  "url":"/assessment/null"
 }
     */
    private Assessment generateAssessment(final TestSpecBankData tsbData, final String stateAbbreviation,
                                          final DateTime testWindowStart, final DateTime testWindowEnd) {

        Assessment assessment = new Assessment();

        assessment.setEntityId(tsbData.getName());
        assessment.setNumGlobalOpportunities(NUM_OPPORTUNITIES);
        assessment.setTestWindow(new Assessment.TestWindow[] {
                new Assessment.TestWindow(testWindowStart, testWindowEnd, NUM_OPPORTUNITIES)
        });
        assessment.setDelayRule(0);
        assessment.setEligibilityType(Assessment.EligibilityType.IMPLICIT);
        assessment.setImplicitEligibilityRules(new ImplicitEligibilityRule[] {
                new ImplicitEligibilityRule("stateAbbreviation", stateAbbreviation, ImplicitEligibilityRule.RuleType.ENABLER)
        });
        assessment.setSubjectCode(tsbData.getSubjectAbbreviation());
        assessment.setTestName(tsbData.getName());
        assessment.setVersion(tsbData.getVersion());
        assessment.setTenantId(tsbData.getTenantId());
        assessment.setExists(false);
        assessment.setUpdatedVersion(false);
        assessment.setTestForm(null);
        assessment.setGrade(tsbData.getGrade().get(0));
        assessment.setAcademicYear(Integer.toString(testWindowStart.getYear()));
        assessment.setCategory(tsbData.getCategory());
        assessment.setType(tsbData.getType());
        assessment.setTestLabel(tsbData.getLabel());

        return assessment;
    }
}
