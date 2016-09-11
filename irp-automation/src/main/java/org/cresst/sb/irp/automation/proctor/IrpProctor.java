package org.cresst.sb.irp.automation.proctor;

import org.apache.commons.lang3.StringUtils;
import org.cresst.sb.irp.automation.accesstoken.AccessToken;
import org.cresst.sb.irp.automation.proctor.data.SessionDTO;
import org.cresst.sb.irp.automation.proctor.data.Test;
import org.cresst.sb.irp.automation.proctor.data.TestOpportunity;
import org.cresst.sb.irp.automation.tsb.TestSpecBankData;
import org.cresst.sb.irp.automation.web.AutomationRestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IrpProctor implements Proctor {
    private final static Logger logger = LoggerFactory.getLogger(IrpProctor.class);

    private AutomationRestTemplate proctorRestTemplate;
    private URL proctorUrl;
    private SessionDTO sessionDTO;

    public IrpProctor(AutomationRestTemplate accessTokenRestTemplate,
                      AutomationRestTemplate proctorRestTemplate,
                      URL oAuthUrl,
                      URL proctorUrl,
                      String clientId,
                      String clientSecret,
                      String proctorUserId,
                      String proctorPassword) {

        this.proctorUrl = proctorUrl;

        AccessToken proctorAccessToken = AccessToken.buildAccessToken(accessTokenRestTemplate,
                oAuthUrl,
                clientId,
                clientSecret,
                proctorUserId,
                proctorPassword);

        this.proctorRestTemplate = proctorRestTemplate;
        proctorRestTemplate.addAccessToken(proctorAccessToken);
    }

    /**
     * Logins in the Proctor by getting the initial Session data
     * @return True if login was successful; otherwise false.
     */
    @Override
    public boolean login() {

        try {
            List<String> cookies = getCookies();
            proctorRestTemplate.setCookies(cookies);

            URI getInitDataUri = UriComponentsBuilder.fromHttpUrl(proctorUrl.toString())
                    .pathSegment("Services", "XHR.axd", "GetInitData")
                    .build()
                    .toUri();

            int retries = 3;
            do {
                ResponseEntity<SessionDTO> response = proctorRestTemplate.getForEntity(getInitDataUri, SessionDTO.class);

                if (response.getStatusCode() == HttpStatus.OK && response.hasBody()) {
                    sessionDTO = response.getBody();
                }
            } while (retries-- > 0 && (sessionDTO == null || sessionDTO.getTests() == null));

            return sessionDTO != null && sessionDTO.getTests() != null;
        } catch (RestClientException ex) {
            logger.info("Unable to login as proctor", ex);
        }

        return false;
    }

    @Override
    public boolean startTestSession(Set<String> irpTestKeys) {

        try {
            URI insertSessionTestsUri = UriComponentsBuilder.fromHttpUrl(proctorUrl.toString())
                    .pathSegment("Services", "XHR.axd", "InsertSessionTests")
                    .build()
                    .toUri();

            List<String> testKeys = new ArrayList<>();
            List<String> testIDs = new ArrayList<>();

            for (Test test : sessionDTO.getTests()) {
                if (irpTestKeys.contains(test.get_key())) {
                    testKeys.add(test.get_key());
                    testIDs.add(test.getId());
                }
            }

            MultiValueMap<String, String> postBody = new LinkedMultiValueMap<>();
            postBody.add("testKeys", StringUtils.join(testKeys, "|"));
            postBody.add("testIDs", StringUtils.join(testIDs, "|"));

            ResponseEntity<SessionDTO> response = proctorRestTemplate.postForEntity(insertSessionTestsUri, postBody, SessionDTO.class);

            if (response.getStatusCode() == HttpStatus.OK && response.hasBody()) {
                sessionDTO = response.getBody();
                return sessionDTO.getSession() != null && sessionDTO.getSession().getId() != null;
            }
        } catch (RestClientException ex) {
            logger.info("Unable to start test session", ex);
        }

        return false;
    }

    @Override
    public String getSessionId() {
        return sessionDTO != null && sessionDTO.getSession() != null ? sessionDTO.getSession().getId() : null;
    }

    /**
     * Gets HTTP Cookies returned from the Proctor application so operations against the Proctor application can be done
     * @return A list of cookies if they exist
     */
    private List<String> getCookies() {

        URI proctorUri = UriComponentsBuilder.fromHttpUrl(proctorUrl.toString()).build(true).toUri();

        ResponseEntity<String> response = proctorRestTemplate.getForEntity(proctorUri, String.class);

        List<String> rawCookies = response.getHeaders().get("Set-Cookie");

        return rawCookies;
    }
}
