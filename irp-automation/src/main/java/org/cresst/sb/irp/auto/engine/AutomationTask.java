package org.cresst.sb.irp.auto.engine;

import org.cresst.sb.irp.auto.accesstoken.AccessToken;
import org.cresst.sb.irp.auto.art.ArtAssessmentSelector;
import org.cresst.sb.irp.auto.art.ArtStudentUploader;
import org.cresst.sb.irp.auto.art.ArtStudentUploaderResult;
import org.cresst.sb.irp.auto.progman.ProgManTenantId;
import org.cresst.sb.irp.auto.tsb.TestSpecBankData;
import org.cresst.sb.irp.auto.tsb.TestSpecBankSideLoader;
import org.cresst.sb.irp.auto.web.AutomationRestTemplate;
import org.cresst.sb.irp.domain.automation.AutomationRequest;
import org.cresst.sb.irp.domain.automation.AutomationStatus;
import org.cresst.sb.irp.domain.automation.AutomationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Stack;

/**
 * Runs IRP Automation against a vendor's implementation. Only a single one of these should run per vendor implementation.
 */
class AutomationTask implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(AutomationTask.class);

    private final AutomationRequest automationRequest;
    private final AutomationToken automationToken;
    private final AutomationStatusHandler automationStatusHandler;

    private Runnable onCompletionCallback;

    AutomationTask(AutomationRequest automationRequest, AutomationToken automationToken,
                   AutomationStatusHandler automationStatusHandler) {
        this.automationRequest = automationRequest;
        this.automationToken = automationToken;
        this.automationStatusHandler = automationStatusHandler;
    }

    /**
     * Sets a external method to call after automation is completed. It will be run regardless of automation errors.
     * @param callback The method to call after automation is finished.
     */
    void onCompletion(Runnable callback) {
        onCompletionCallback = callback;
    }

    @Override
    public void run() {
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
        emitMessage("Fetching your Tenant ID from " + automationRequest.getProgramManagementUrl());
        final ProgManTenantId progManTenantId = new ProgManTenantId(automationRestTemplate,
                automationRequest.getProgramManagementUrl(),
                automationRequest.getStateAbbreviation());
        final String tenantId = progManTenantId.getTenantId();
        logger.info("Tenant ID {}", tenantId);
        emitMessage("Tenant ID received");

        Stack<Rollbacker> rollbackers = new Stack<>();
        try {
            logger.info("Side-loading Registration Test Packages");
            final TestSpecBankSideLoader testSpecBankSideLoader = new TestSpecBankSideLoader(
                    RunnableAutomationRequestProcessor.registrationTestPackageDirectory,
                    automationRestTemplate,
                    automationRequest.getTestSpecBankUrl(),
                    tenantId);
            rollbackers.push(testSpecBankSideLoader);

            emitMessage("Side-loading IRP's ART Registration Test Packages into your TSB");
            List<TestSpecBankData> testSpecBankData = testSpecBankSideLoader.sideLoadRegistrationTestPackages();
            emitMessage("Side-loading complete");

            logger.info("Selecting Registration Test Packages in vendor's ART application");
            final ArtAssessmentSelector artAssessmentSelector = new ArtAssessmentSelector(automationRestTemplate,
                    automationRequest.getArtUrl(),
                    automationRequest.getStateAbbreviation());
            rollbackers.push(artAssessmentSelector);

            emitMessage("Registering IRP Test Packages in ART");
            final List<String> selectedAssessmentIds = artAssessmentSelector.selectAssessments(testSpecBankData);

            logger.info("Verifying all Registration Test Packages are selected");
            if (!artAssessmentSelector.verifySelectedAssessments(tenantId, testSpecBankData)) {
                throw new Exception("IRP found an error with the Test Package data loaded into your system.");
            }
            emitMessage("Registered " + selectedAssessmentIds.size() + " IRP Assessments in ART");

            final ArtStudentUploader artStudentUploader = new ArtStudentUploader(
                    RunnableAutomationRequestProcessor.studentTemplatePath,
                    automationRestTemplate,
                    automationRequest.getArtUrl(),
                    automationRequest.getStateAbbreviation(),
                    automationRequest.getDistrict(),
                    automationRequest.getInstitution());
            rollbackers.push(artStudentUploader);

            emitMessage("Loading IRP Students into ART");
            final ArtStudentUploaderResult artStudentUploaderResult = artStudentUploader.uploadStudentData();
            if (!artStudentUploaderResult.isSuccessful()) {
                throw new Exception("Unable to upload Student data because " + artStudentUploaderResult.getMessage());
            }
            emitMessage("IRP Students successfully loaded into ART");
        } catch (Exception ex) {
            logger.error("Automation error occurred. Rolling back data now.", ex);
            emitMessage("An error occurred, IRP is rolling back any data it has loading into your implementation");
            while (!rollbackers.isEmpty()) {
                Rollbacker rollbacker = rollbackers.pop();
                rollbacker.rollback();
            }
            emitMessage("Rollback is complete");
        } finally {
            if (onCompletionCallback != null) {
                onCompletionCallback.run();
            }
            emitMessage("Done");
        }

        logger.info("Automation task for {} is complete.", automationToken);
    }

    private void emitMessage(final String message) {
        AutomationStatus automationStatus = new AutomationStatus(message);
        automationStatusHandler.handleAutomationStatus(automationToken, automationStatus);
    }
}
