package org.cresst.sb.irp.auto.engine;

import org.cresst.sb.irp.auto.accesstoken.AccessToken;
import org.cresst.sb.irp.auto.art.ArtAssessmentSelector;
import org.cresst.sb.irp.auto.art.ArtStudentUploader;
import org.cresst.sb.irp.auto.art.ArtStudentUploaderResult;
import org.cresst.sb.irp.domain.automation.*;
import org.cresst.sb.irp.auto.progman.ProgManTenantId;
import org.cresst.sb.irp.auto.statusreporting.AutomationStatusReporter;
import org.cresst.sb.irp.auto.statusreporting.IrpAutomationStatusReporter;
import org.cresst.sb.irp.auto.tsb.TestSpecBankData;
import org.cresst.sb.irp.auto.tsb.TestSpecBankSideLoader;
import org.cresst.sb.irp.auto.web.AutomationRestTemplate;
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

        final AutomationStatusReport automationReport = new AutomationStatusReport();
        AutomationStatusReporter initializationStatusReporter = new IrpAutomationStatusReporter(AutomationPhase.INITIALIZATION,
                automationToken,
                automationReport,
                automationStatusHandler);
        AutomationStatusReporter preloadingStatusReporter = new IrpAutomationStatusReporter(AutomationPhase.PRELOADING,
                automationToken,
                automationReport,
                automationStatusHandler);
        AutomationStatusReporter simulationStatusReporter = new IrpAutomationStatusReporter(AutomationPhase.SIMULATION,
                automationToken,
                automationReport,
                automationStatusHandler);
        AutomationStatusReporter analysisStatusReporter = new IrpAutomationStatusReporter(AutomationPhase.ANALYSIS,
                automationToken,
                automationReport,
                automationStatusHandler);
        AutomationStatusReporter cleanupStatusReporter = new IrpAutomationStatusReporter(AutomationPhase.CLEANUP,
                automationToken,
                automationReport,
                automationStatusHandler);

        logger.info("Building Access Token");
        final AccessToken accessToken = AccessToken.buildAccessToken(accessTokenRestTemplate,
                automationRequest.getoAuthUrl(),
                automationRequest.getProgramManagementClientId(),
                automationRequest.getProgramManagementClientSecret(),
                automationRequest.getProgramManagementUserId(),
                automationRequest.getProgramManagementUserPassword());

        automationRestTemplate.addAccessToken(accessToken);

        try {
            final String tenantId = initialize(automationRestTemplate, initializationStatusReporter);

            preload(automationRestTemplate, preloadingStatusReporter, tenantId);
            //simulate();
            //analyze();
            //cleanup();
        } catch (Exception ex) {
            logger.error("Ending automation task because of exception", ex);
        } finally {
            logger.info("Automation task for {} is complete.", automationToken);
            cleanupStatusReporter.markAutomationComplete();
        }
    }

    private String initialize(AutomationRestTemplate automationRestTemplate, AutomationStatusReporter initializationStatusReporter) {
        try {
            logger.info("Getting Tenant ID");
            initializationStatusReporter.status("Fetching your Tenant ID from " + automationRequest.getProgramManagementUrl());

            final ProgManTenantId progManTenantId = new ProgManTenantId(automationRestTemplate,
                    automationRequest.getProgramManagementUrl(),
                    automationRequest.getStateAbbreviation());

            final String tenantId = progManTenantId.getTenantId();

            logger.info("Tenant ID {}", tenantId);
            initializationStatusReporter.status("Tenant ID received");

            return tenantId;
        } catch (Exception ex) {
            logger.info("Unable to get Tenant ID");
            initializationStatusReporter.status("Unable to get Tenant ID. Check OpenAM URL, PM URL, and PM credentials.");
            throw ex;
        }
    }

    private void preload(AutomationRestTemplate automationRestTemplate, AutomationStatusReporter preloadingStatusReporter,
                         String tenantId) throws Exception {
        Stack<Rollbacker> rollbackers = new Stack<>();
        try {
            logger.info("Side-loading Registration Test Packages");

            final TestSpecBankSideLoader testSpecBankSideLoader = new TestSpecBankSideLoader(
                    RunnableAutomationRequestProcessor.registrationTestPackageDirectory,
                    automationRestTemplate,
                    automationRequest.getTestSpecBankUrl(),
                    tenantId);

            rollbackers.push(testSpecBankSideLoader);

            preloadingStatusReporter.status("Side-loading IRP's ART Registration Test Packages into your TSB");

            List<TestSpecBankData> testSpecBankData = testSpecBankSideLoader.sideLoadRegistrationTestPackages();

            preloadingStatusReporter.status(String.format("Side-loading complete. Side-loaded %d Registration Test Packages into TSB.",
                    testSpecBankData.size()));

            logger.info("Selecting Registration Test Packages in vendor's ART application");

            final ArtAssessmentSelector artAssessmentSelector = new ArtAssessmentSelector(automationRestTemplate,
                    automationRequest.getArtUrl(),
                    automationRequest.getStateAbbreviation());

            rollbackers.push(artAssessmentSelector);

            preloadingStatusReporter.status("Registering IRP Test Packages in ART");

            final List<String> selectedAssessmentIds = artAssessmentSelector.selectAssessments(testSpecBankData);

            logger.info("Verifying all Registration Test Packages are selected");

            if (!artAssessmentSelector.verifySelectedAssessments(tenantId, testSpecBankData)) {
                throw new Exception("IRP found an error with the Test Package data loaded into your system.");
            }

            preloadingStatusReporter.status("Registered " + selectedAssessmentIds.size() + " IRP Assessments in ART");

            final ArtStudentUploader artStudentUploader = new ArtStudentUploader(
                    RunnableAutomationRequestProcessor.studentTemplatePath,
                    automationRestTemplate,
                    automationRequest.getArtUrl(),
                    automationRequest.getStateAbbreviation(),
                    automationRequest.getDistrict(),
                    automationRequest.getInstitution());

            rollbackers.push(artStudentUploader);

            preloadingStatusReporter.status("Loading IRP Students into ART");

            final ArtStudentUploaderResult artStudentUploaderResult = artStudentUploader.uploadStudentData();
            if (!artStudentUploaderResult.isSuccessful()) {
                preloadingStatusReporter.status("Failed to load IRP Students into ART: " + artStudentUploaderResult.getMessage());
                throw new Exception("Unable to upload Student data because " + artStudentUploaderResult.getMessage());
            }

            preloadingStatusReporter.status(String.format("Successfully loaded %d IRP Students into ART. SSIDs start with 'IRP.'",
                    artStudentUploaderResult.getNumberOfStudentsUploaded()));
        } catch (Exception ex) {
            logger.error("Automation error occurred. Rolling back data now.", ex);
            preloadingStatusReporter.status("An error occurred, IRP is rolling back any data it has loading into your implementation");

            while (!rollbackers.isEmpty()) {
                Rollbacker rollbacker = rollbackers.pop();
                rollbacker.rollback();
            }

            preloadingStatusReporter.status("Rollback is complete");

            throw ex;
        } finally {
            if (onCompletionCallback != null) {
                onCompletionCallback.run();
            }

            preloadingStatusReporter.status("Done");
        }
    }
}
