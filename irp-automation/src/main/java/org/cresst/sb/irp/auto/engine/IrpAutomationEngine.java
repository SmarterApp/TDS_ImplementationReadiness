package org.cresst.sb.irp.auto.engine;

import org.cresst.sb.irp.auto.accesstoken.AccessToken;
import org.cresst.sb.irp.auto.art.ArtAssessmentSelector;
import org.cresst.sb.irp.auto.progman.ProgMan;
import org.cresst.sb.irp.auto.tsb.TestSpecBankData;
import org.cresst.sb.irp.auto.tsb.TestSpecBankSideLoader;
import org.cresst.sb.irp.domain.automation.AutomationRequest;
import org.cresst.sb.irp.domain.automation.AutomationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IrpAutomationEngine implements AutomationEngine {
    private final static Logger logger = LoggerFactory.getLogger(IrpAutomationEngine.class);

    @Value("classpath:irp-package/TestPackages/ART/Registration")
    private static Resource registrationTestPackageDirectory;

    @Override
    public AutomationResponse automate(AutomationRequest automationRequest) throws Exception {
        AutomationResponse automationResponse = new AutomationResponse();

        logger.info("Building Access Token");
        AccessToken accessToken = AccessToken.buildAccessToken(automationRequest.getoAuthUrl(),
                automationRequest.getProgramManagementClientId(),
                automationRequest.getProgramManagementClientSecret(),
                automationRequest.getProgramManagementUserId(),
                automationRequest.getProgramManagementUserPassword());

        logger.info("Getting Tenant ID");
        ProgMan progMan = new ProgMan();
        String tenantId = progMan.getTenantId(accessToken,
                automationRequest.getProgramManagementUrl(),
                automationRequest.getStateAbbreviation());
        logger.info("Tenant ID {0}", tenantId);

        List<Rollbacker> rollbackers = new ArrayList<>();
        try {
            logger.info("Side-loading Registration Test Packages");
            TestSpecBankSideLoader testSpecBankSideLoader = new TestSpecBankSideLoader(registrationTestPackageDirectory,
                    accessToken,
                    automationRequest.getTestSpecBankUrl(),
                    tenantId);
            rollbackers.add(testSpecBankSideLoader);

            List<TestSpecBankData> testSpecBankData = testSpecBankSideLoader.sideLoadRegistrationTestPackages();

            logger.info("Selecting Registration Test Packages in vendor's ART application");
            ArtAssessmentSelector artAssessmentSelector = new ArtAssessmentSelector(accessToken,
                    automationRequest.getArtUrl(),
                    automationRequest.getStateAbbreviation());
            rollbackers.add(artAssessmentSelector);

            List<String> selectedAssessmentIds = artAssessmentSelector.selectAssessments(testSpecBankData);
        } catch (Exception ex) {
            for (Rollbacker rollbacker : rollbackers) {
                rollbacker.rollback();;
            }
        }

        return automationResponse;
    }
}
