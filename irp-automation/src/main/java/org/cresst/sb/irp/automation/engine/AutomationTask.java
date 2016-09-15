package org.cresst.sb.irp.automation.engine;

import org.cresst.sb.irp.automation.accesstoken.AccessToken;
import org.cresst.sb.irp.automation.art.*;
import org.cresst.sb.irp.automation.proctor.IrpProctor;
import org.cresst.sb.irp.automation.proctor.Proctor;
import org.cresst.sb.irp.automation.progman.ProgManTenantId;
import org.cresst.sb.irp.automation.statusreporting.AutomationStatusReporter;
import org.cresst.sb.irp.automation.statusreporting.IrpAutomationStatusReporter;
import org.cresst.sb.irp.automation.tsb.TestSpecBankData;
import org.cresst.sb.irp.automation.tsb.TestSpecBankSideLoader;
import org.cresst.sb.irp.automation.web.AutomationRestTemplate;
import org.cresst.sb.irp.automation.web.IrpAutomationRestTemplate;
import org.cresst.sb.irp.domain.automation.AutomationPhase;
import org.cresst.sb.irp.domain.automation.AutomationRequest;
import org.cresst.sb.irp.domain.automation.AutomationStatusReport;
import org.cresst.sb.irp.domain.automation.AutomationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        AutomationRestTemplate accessTokenRestTemplate = new IrpAutomationRestTemplate();
        AutomationRestTemplate automationRestTemplate = new IrpAutomationRestTemplate();

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

            Set<String> irpTestKeys = preload(automationRestTemplate, preloadingStatusReporter, tenantId);
            simulate(accessTokenRestTemplate, simulationStatusReporter, irpTestKeys);
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

    private Set<String> preload(AutomationRestTemplate automationRestTemplate, AutomationStatusReporter preloadingStatusReporter,
                         String tenantId) throws Exception {

        Set<String> irpTestKeys = new HashSet<>();
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

            for (TestSpecBankData data : testSpecBankData) {
                irpTestKeys.add(data.getName());
            }

            preloadingStatusReporter.status(String.format("Side-loading complete. Side-loaded %d Registration Test Packages into TSB.",
                    testSpecBankData.size()));

            logger.info("Selecting Registration Test Packages in vendor's ART application");

            final ArtAssessmentSelector artAssessmentSelector = new ArtAssessmentSelector(automationRestTemplate,
                    automationRequest.getArtUrl(),
                    automationRequest.getStateAbbreviation());

            rollbackers.push(artAssessmentSelector);

            preloadingStatusReporter.status("Registering IRP Test Packages in ART");

            int numOfSelectedAssessments = artAssessmentSelector.selectAssessments(testSpecBankData);

            logger.info("Verifying all Registration Test Packages are selected");

            if (!artAssessmentSelector.verifySelectedAssessments(tenantId, testSpecBankData)) {
                throw new Exception("IRP found an error with the Test Package data loaded into your system.");
            }

            preloadingStatusReporter.status("Registered " + numOfSelectedAssessments + " IRP Assessments in ART");

            final ArtStudentUploader artStudentUploader = new ArtStudentUploader(
                    RunnableAutomationRequestProcessor.studentTemplatePath,
                    automationRestTemplate,
                    automationRequest.getArtUrl(),
                    automationRequest.getStateAbbreviation(),
                    automationRequest.getDistrict(),
                    automationRequest.getInstitution());

            rollbackers.push(artStudentUploader);

            preloadingStatusReporter.status("Loading IRP Students into ART");

            final ArtUploaderResult artStudentUploaderResult = artStudentUploader.uploadData();
            if (!artStudentUploaderResult.isSuccessful()) {
                preloadingStatusReporter.status("Failed to load IRP Students into ART: " + artStudentUploaderResult.getMessage());
                throw new Exception("Unable to upload Student data because " + artStudentUploaderResult.getMessage());
            }

            preloadingStatusReporter.status(String.format("Successfully loaded %d IRP Students into ART. SSIDs start with 'IRP.'",
                    artStudentUploaderResult.getNumberOfRecordsUploaded()));

            final ArtStudentAccommodationsUploader artStudentAccommodationsUploader = new ArtStudentAccommodationsUploader(
                    RunnableAutomationRequestProcessor.studentAccommodationsTemplatePath,
                    automationRestTemplate,
                    automationRequest.getArtUrl(),
                    automationRequest.getStateAbbreviation());

            preloadingStatusReporter.status("Loading IRP Student Accommodations into ART");

            final ArtUploaderResult artStudentAccommodationsUploaderResult = artStudentAccommodationsUploader.uploadData();
            if (!artStudentAccommodationsUploaderResult.isSuccessful()) {
                preloadingStatusReporter.status("Failed to load IRP Student Accommodations into ART: "
                        + artStudentAccommodationsUploaderResult.getMessage());
                throw new Exception("Unable to upload Student Accommodations data because "
                        + artStudentAccommodationsUploaderResult.getMessage());
            }

            preloadingStatusReporter.status(String.format("Successfully loaded %d IRP Student Accommodations into ART.",
                    artStudentAccommodationsUploaderResult.getNumberOfRecordsUploaded()));


            final ArtStudentGroupUploader artStudentGroupUploader = new ArtStudentGroupUploader(
                    RunnableAutomationRequestProcessor.studentGroupTemplatePath,
                    automationRestTemplate,
                    automationRequest.getArtUrl(),
                    automationRequest.getStateAbbreviation(),
                    automationRequest.getDistrict(),
                    automationRequest.getInstitution(),
                    automationRequest.getProctorUserId());

            rollbackers.push(artStudentGroupUploader);

            preloadingStatusReporter.status("Loading IRP Student Group into ART");

            final ArtUploaderResult artStudentGroupUploaderResult = artStudentGroupUploader.uploadData();
            if (!artStudentGroupUploaderResult.isSuccessful()) {
                preloadingStatusReporter.status("Failed to load IRP Student Group into ART: " + artStudentGroupUploaderResult.getMessage());
                throw new Exception("Unable to upload IRP Student Group data because " + artStudentGroupUploaderResult.getMessage());
            }

            preloadingStatusReporter.status(String.format("Successfully added %d IRP Students to the IRPStudentGroup in ART.",
                    artStudentGroupUploaderResult.getNumberOfRecordsUploaded()));

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

        return irpTestKeys;
    }

    private void simulate(AutomationRestTemplate accessTokenRestTemplate, AutomationStatusReporter simulationStatusReporter,
                          Set<String> irpTestKeys) {

        final Proctor proctor = new IrpProctor(accessTokenRestTemplate,
                new IrpAutomationRestTemplate(),
                automationRequest.getoAuthUrl(),
                automationRequest.getProctorUrl(),
                automationRequest.getProgramManagementClientId(),
                automationRequest.getProgramManagementClientSecret(),
                automationRequest.getProctorUserId(),
                automationRequest.getProctorPassword());

        simulationStatusReporter.status(String.format("Logging in as Proctor (%s)", automationRequest.getProctorUserId()));
        if (proctor.login()) {
            logger.info("Proctor login successful");
            simulationStatusReporter.status("Proctor login successful. Initiating Test Session.");

            if (proctor.startTestSession(irpTestKeys)) {
                logger.info("Successfully started test session");
                simulationStatusReporter.status("Test Session has been initiated by the Proctor");


            } else {
                logger.info("Proctor was unable to start a Test Session");
                simulationStatusReporter.status("Proctor was unable to start a Test Session");
            }
        } else {
            logger.info("Proctor login was unsuccessful");
            simulationStatusReporter.status("Proctor login was unsuccessful");
        }
    }
}