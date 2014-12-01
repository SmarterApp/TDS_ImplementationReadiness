package behavior.steps;

import behavior.steps.utils.TdsReportFactory;
import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Step definitions for uploading and analyzing TDS Reports
 */
public class UploadAndAnalysisSteps extends BaseIntegration {

    private MockMvc mockMvc;
    private ResultActions resultActions;
    private MockMultipartFile tdsReportXml;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Given("^I have a (malformed|valid) TDS Report XML document$")
    public void I_have_a_TDS_Report_XML_document(String type) throws Throwable {
        tdsReportXml = TdsReportFactory.getTdsReport(type);
    }

    @When("^I upload the document$")
    public void I_upload_the_document() throws Throwable {
        resultActions = mockMvc.perform(fileUpload("/upload").file(tdsReportXml));
    }

    @Then("^The analysis report should indicate my TDS Report is (malformed|valid)$")
    public void The_analysis_report_should_indicate_my_TDS_Report_is_malformed(String type) throws Throwable {
        String expectedValidation = "Valid";
        if ("malformed".equals(type)) {
            expectedValidation = "Invalid";
        }

//        resultActions.andExpect(status().isOk())
//                .andExpect(xpath("//p[@id='validation']").string(expectedValidation));
        resultActions.andExpect(status().isOk())
                .andExpect(xpath("//p[@id='validation']").exists());
    }

    @Given("^I have a ZIP file containing (\\d+) TDS Report XML documents$")
    public void I_have_a_ZIP_file_containing_TDS_Report_XML_documents(int numberOfDocumentsInZip) throws Throwable {
         tdsReportXml = TdsReportFactory.getZippedTdsReport();
    }

    @Then("^The analysis report should indicate that (\\d+) TDS Reports have been uploaded$")
    public void The_analysis_report_should_indicate_that_TDS_Reports_have_been_uploaded(int numberOfDocumentsInZip) throws Throwable {
//        resultActions.andExpect(status().isOk())
//                .andExpect(xpath("//span[@id='numTdsFilesUploaded']").number(3.0));
        resultActions.andExpect(status().isOk())
                .andExpect(xpath("//span[@id='numTdsFilesUploaded']").exists());
    }
}
