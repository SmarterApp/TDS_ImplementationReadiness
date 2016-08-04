package org.cresst.sb.irp.auto.tsb;

import org.cresst.sb.irp.auto.accesstoken.AccessToken;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.io.PathResource;

import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestSpecBankSideLoaderTest {

    @Ignore("Further test harness structure needs to be build out to get test parameters")
    @Test
    public void sideLoadRegistrationTestPackagesTest() throws Exception {
        Path artRegistrationTestPackages = Paths.get("");
        TestSpecBankSideLoader sut = new TestSpecBankSideLoader(new PathResource(artRegistrationTestPackages));

        sut.initiateRegistrationTestPackages();
        AccessToken accessToken = AccessToken.buildAccessToken(
                new URL(""),
                "",
                "",
                "",
                ""
        );

        sut.sideLoadRegistrationTestPackages(
                new URL(""),
                accessToken,
                "");
    }

}