package org.cresst.sb.irp.auto.engine;

import org.cresst.sb.irp.auto.accesstoken.AccessToken;
import org.cresst.sb.irp.auto.art.ArtAssessmentSelector;
import org.cresst.sb.irp.auto.progman.ProgManTenantId;
import org.cresst.sb.irp.auto.tsb.TestSpecBankData;
import org.cresst.sb.irp.auto.tsb.TestSpecBankSideLoader;
import org.cresst.sb.irp.auto.web.AutomationRestTemplate;
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

    private static Resource registrationTestPackageDirectory;

    @Autowired
    public IrpAutomationEngine(
            @Value("classpath:irp-package/TestPackages/ART/Registration") Resource registrationTestPackageDirectory) {
        IrpAutomationEngine.registrationTestPackageDirectory = registrationTestPackageDirectory;
    }

    @Override
    public AutomationResponse automate(AutomationRequest automationRequest) throws Exception {
        AutomationResponse automationResponse = new AutomationResponse();

        AutomationRestTemplate accessTokenRestTemplate = new AutomationRestTemplate();
        AutomationRestTemplate automationRestTemplate = new AutomationRestTemplate();

        logger.info("Building Access Token");
        final AccessToken accessToken = AccessToken.buildAccessToken(accessTokenRestTemplate,
                automationRequest.getoAuthUrl(),
                automationRequest.getProgramManagementClientId(),
                automationRequest.getProgramManagementClientSecret(),
                automationRequest.getProgramManagementUserId(),
                automationRequest.getProgramManagementUserPassword());

        automationRestTemplate.addAccessToken(accessToken);

        logger.info("Getting Tenant ID");
        final ProgManTenantId progManTenantId = new ProgManTenantId(automationRestTemplate,
                automationRequest.getProgramManagementUrl(),
                automationRequest.getStateAbbreviation());
        final String tenantId = progManTenantId.getTenantId();
        logger.info("Tenant ID {}", tenantId);

        List<Rollbacker> rollbackers = new ArrayList<>();
        try {
            logger.info("Side-loading Registration Test Packages");
            final TestSpecBankSideLoader testSpecBankSideLoader = new TestSpecBankSideLoader(registrationTestPackageDirectory,
                    automationRestTemplate,
                    automationRequest.getTestSpecBankUrl(),
                    tenantId);
            rollbackers.add(testSpecBankSideLoader);

            List<TestSpecBankData> testSpecBankData = testSpecBankSideLoader.sideLoadRegistrationTestPackages();

            logger.info("Selecting Registration Test Packages in vendor's ART application");
            final ArtAssessmentSelector artAssessmentSelector = new ArtAssessmentSelector(automationRestTemplate,
                    automationRequest.getArtUrl(),
                    automationRequest.getStateAbbreviation());
            rollbackers.add(artAssessmentSelector);

            List<String> selectedAssessmentIds = artAssessmentSelector.selectAssessments(testSpecBankData);

            logger.info("Verifying all Registration Test Packages are selected");
            if (!artAssessmentSelector.verifySelectedAssessments(tenantId, testSpecBankData)) {
                throw new Exception("IRP found an error with the Test Package data loaded into your system.");
            }
        } catch (Exception ex) {
            logger.error("Automation error occurred. Rolling back data now.", ex);
            for (Rollbacker rollbacker : rollbackers) {
                rollbacker.rollback();
            }
        }

        return automationResponse;
    }
}
