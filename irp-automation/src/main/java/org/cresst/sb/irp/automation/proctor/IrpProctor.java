package org.cresst.sb.irp.automation.proctor;

import org.cresst.sb.irp.automation.accesstoken.AccessToken;
import org.cresst.sb.irp.automation.proctor.data.SessionDTO;
import org.cresst.sb.irp.automation.web.AutomationRestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URL;
import java.util.List;

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

            ResponseEntity<SessionDTO> response = proctorRestTemplate.getForEntity(getInitDataUri, SessionDTO.class);

            if (response.getStatusCode() == HttpStatus.OK && response.hasBody()) {
                sessionDTO = response.getBody();
                return true;
            }
        } catch (RestClientException ex) {
            logger.info("Unable to login as proctor", ex);
        }

        return false;
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
