package org.cresst.sb.irp.automation.tsb;

import org.cresst.sb.irp.automation.accesstoken.AccessToken;
import org.cresst.sb.irp.automation.web.AutomationRestTemplate;
import org.cresst.sb.irp.automation.web.IrpAutomationRestTemplate;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.io.PathResource;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestSpecBankSideLoaderTest {

    @Ignore("Further test harness structure needs to be build out to get test parameters")
    @Test
    public void sideLoadRegistrationTestPackagesTest() throws Exception {

        AccessToken accessToken = AccessToken.buildAccessToken(
                new RestTemplate(),
                new URL(""),
                "",
                "",
                "",
                ""
        );

        Path artRegistrationTestPackages = Paths.get("");
        AutomationRestTemplate restTemplate = new IrpAutomationRestTemplate();
        restTemplate.addAccessToken(accessToken);
        TestSpecBankSideLoader sut = new TestSpecBankSideLoader(new PathResource(artRegistrationTestPackages),
                restTemplate,
                new URL(""),
                "");

        sut.sideLoadRegistrationTestPackages();
    }

}