package org.cresst.sb.irp.automation.proctor;

import com.google.common.collect.Lists;
import org.cresst.sb.irp.automation.proctor.data.SessionDTO;
import org.cresst.sb.irp.automation.proctor.data.TestSession;
import org.cresst.sb.irp.automation.testhelpers.IntegrationTests;
import org.cresst.sb.irp.automation.web.AutomationRestTemplate;
import org.cresst.sb.irp.automation.web.IrpAutomationRestTemplate;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClientException;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IrpProctorTest {

    @Mock
    private AutomationRestTemplate accessTokenAutomationRestTemplate;

    @Mock
    private AutomationRestTemplate proctorAutomationRestTemplate;

    private final URL testOAuthUrl = new URL("https://oam.server.net/oauth");
    private final URL testProctorUrl = new URL("https://tds.server.net/proctor");
    private final URL testGetInitDataUrl = new URL("https://tds.server.net/proctor/Services/XHR.axd/GetInitData");
    private final URL testInsertSessionTestsUrl = new URL("https://tds.server.net/proctor/Services/XHR.axd/InsertSessionTests");
    private final String testClientId = "testClientId";
    private final String testClientSecret = "testClientSecret";
    private final String testProctorUserId = "testProctorUserId@server.net";
    private final String testProctorPassword = "testProctorPassword";

    public IrpProctorTest() throws MalformedURLException {
    }

    /**
     * Reusable code to initiate a Proctor login
     * @return The logged in Proctor
     * @throws URISyntaxException Not likely to be thrown
     */
    private Proctor loginMockProctor() throws URISyntaxException {
        // Proctor Under Test
        final Proctor proctorUT = new IrpProctor(accessTokenAutomationRestTemplate,
                proctorAutomationRestTemplate,
                testOAuthUrl,
                testProctorUrl,
                testClientId,
                testClientSecret,
                testProctorUserId,
                testProctorPassword);

        final URI proctorUri = new URI(testProctorUrl.toString());
        final URI testGetInitDataUri = new URI(testGetInitDataUrl.toString());

        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Set-Cookie", Lists.newArrayList("testcookie1=true", "testcookie2=1"));
        ResponseEntity<String> mockCookieResponse = new ResponseEntity<>("", new LinkedMultiValueMap<>(headers), HttpStatus.OK);

        List<org.cresst.sb.irp.automation.proctor.data.Test> proctorTests = new ArrayList<>();
        org.cresst.sb.irp.automation.proctor.data.Test t1 = new org.cresst.sb.irp.automation.proctor.data.Test();
        t1.set_key("key1");
        t1.setId("id1");
        proctorTests.add(t1);

        SessionDTO sessionDTO = new SessionDTO();
        sessionDTO.setTests(proctorTests);
        ResponseEntity<SessionDTO> mockGetInitDataResponse = new ResponseEntity<>(sessionDTO, HttpStatus.OK);

        when(proctorAutomationRestTemplate.getForEntity(eq(proctorUri), eq(String.class))).thenReturn(mockCookieResponse);
        when(proctorAutomationRestTemplate.getForEntity(eq(testGetInitDataUri), eq(SessionDTO.class)))
                .thenReturn(mockGetInitDataResponse);
        return proctorUT;
    }


    @Test
    public void whenLoginSuccessful() throws Exception {
        final Proctor proctorUT = loginMockProctor();

        assertTrue(proctorUT.login());
    }

    @Test
    public void whenCredentialsIncorrect_LoginUnsuccessful() throws Exception {

        // Proctor Under Test
        final Proctor proctorUT = new IrpProctor(accessTokenAutomationRestTemplate,
                proctorAutomationRestTemplate,
                testOAuthUrl,
                testProctorUrl,
                testClientId,
                testClientSecret,
                "badUserId",
                "badPassword");

        final URI proctorUri = new URI(testProctorUrl.toString());
        final URI testGetInitDataUri = new URI(testGetInitDataUrl.toString());

        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Set-Cookie", Lists.newArrayList("testcookie1=true", "testcookie2=1"));
        ResponseEntity<String> mockCookieResponse = new ResponseEntity<>("", new LinkedMultiValueMap<>(headers), HttpStatus.OK);

        SessionDTO sessionDTO = new SessionDTO();
        ResponseEntity<SessionDTO> mockGetInitDataResponse = new ResponseEntity<>(sessionDTO, HttpStatus.BAD_REQUEST);

        when(proctorAutomationRestTemplate.getForEntity(eq(proctorUri), eq(String.class))).thenReturn(mockCookieResponse);
        when(proctorAutomationRestTemplate.getForEntity(eq(testGetInitDataUri), eq(SessionDTO.class)))
                .thenReturn(mockGetInitDataResponse);

        assertFalse(proctorUT.login());
    }

    @Test
    public void whenCredentialsIncorrect_RestTemplateThrowsException_LoginUnsuccessful() throws Exception {

        // Proctor Under Test
        final Proctor proctorUT = new IrpProctor(accessTokenAutomationRestTemplate,
                proctorAutomationRestTemplate,
                testOAuthUrl,
                testProctorUrl,
                testClientId,
                testClientSecret,
                "badUserId",
                "badPassword");

        final URI proctorUri = new URI(testProctorUrl.toString());

        when(proctorAutomationRestTemplate.getForEntity(eq(proctorUri), eq(String.class))).thenThrow(new RestClientException("Bad Request"));

        assertFalse(proctorUT.login());
    }

    @Ignore("Ignoring integration test with IP restricted server.")
    @Test
    @Category(IntegrationTests.class)
    public void usingRealServer_whenCredentialsIncorrect_LoginUnsuccessful() throws Exception {

        AutomationRestTemplate realAccessTokenRestTemplate = new IrpAutomationRestTemplate();
        AutomationRestTemplate realProctorRestTemplate = new IrpAutomationRestTemplate();

        // Fill in with real server URLs
        URL realOAuthUrl = new URL("");
        URL realProctorUrl = new URL("");

        // Proctor Under Test
        final Proctor proctorUT = new IrpProctor(realAccessTokenRestTemplate,
                realProctorRestTemplate,
                realOAuthUrl,
                realProctorUrl,
                "",
                "",
                "badUserId",
                "badPassword");

        assertFalse(proctorUT.login());
    }

    @Test
    public void whenInitiatingTestSessionSuccessful_SessionIdAvailable() throws Exception {
        // Proctor Under Test
        final Proctor proctorUT = loginMockProctor();

        final URI testInsertSessionTestsUri = new URI(testInsertSessionTestsUrl.toString());

        Set<String> testKeys = new HashSet<>();
        testKeys.add("key1");

        TestSession testSession = new TestSession();
        testSession.setId("test-session-id");

        SessionDTO sessionDTO = new SessionDTO();
        sessionDTO.setSession(testSession);

        ResponseEntity<SessionDTO> mockInsertSessionTestResponse = new ResponseEntity<>(sessionDTO, HttpStatus.OK);

        when(proctorAutomationRestTemplate.postForEntity(eq(testInsertSessionTestsUri), any(), eq(SessionDTO.class)))
                .thenReturn(mockInsertSessionTestResponse);

        proctorUT.login();
        proctorUT.startTestSession(testKeys);

        assertEquals("test-session-id", proctorUT.getSessionId());
    }
}