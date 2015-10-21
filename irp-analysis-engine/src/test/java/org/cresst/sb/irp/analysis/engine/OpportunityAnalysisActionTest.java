package org.cresst.sb.irp.analysis.engine;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import builders.CellCategoryBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test class to verify the behavior of OpportunityAnalysisAction
 */
@RunWith(MockitoJUnitRunner.class)
public class OpportunityAnalysisActionTest {

	@InjectMocks
	private OpportunityAnalysisAction underTest = new OpportunityAnalysisAction();

	@Test
	public void testAnalysis() throws Exception {

		final TDSReport.Opportunity opportunity = new TDSReport.Opportunity();
		opportunity.setAbnormalStarts(0);
		opportunity.setAssessmentParticipantSessionPlatformUserAgent("");
		opportunity.setClientName("SBAC_PT");
		opportunity.setDatabase("session");
		opportunity.setDateCompleted("2015-09-26T17:01:04.407");
		opportunity.setDateForceCompleted("");
		opportunity.setEffectiveDate("2015-09-23");
		opportunity.setFtCount(0);
		opportunity.setGracePeriodRestarts(0);
		opportunity.setItemCount(4);
		opportunity.setKey("912dd944-ede4-4a6f-b410-f74b66221b43");
		opportunity.setOppId("100029");
		opportunity.setOpportunity(3);
		opportunity.setPauseCount(0);
		opportunity.setQaLevel(null);
		opportunity.setReportingVersion(null); //TODO
		opportunity.setServer("ip-172-31-24-143");
		opportunity.setSessionId("SBC-4");
		opportunity.setStartDate("2015-09-26T17:00:05.406");
		opportunity.setStatus("completed");
		XMLGregorianCalendar calendar =
				DatatypeFactory.newInstance().newXMLGregorianCalendar(2015, 9, 26, 17, 1, 4, 407, 0);
		opportunity.setStatusDate(calendar);
		opportunity.setTaId("sbcresstadmin@smarterapp.cresst.net");
		opportunity.setTaName("Administrator SBCRESST");
		opportunity.setWindowId("ANNUAL");
		opportunity.setWindowOpportunity("3");

		final TDSReport tdsReport = new TDSReport();
		tdsReport.setOpportunity(opportunity);

		final IndividualResponse individualResponse = new IndividualResponse();
		individualResponse.setTDSReport(tdsReport);

	     // Act
        underTest.analyze(individualResponse);

        List<CellCategory> actualCellCategories = new ArrayList<>();
        actualCellCategories = individualResponse.getOpportunityCategory().getCellCategories();
     
        assertThat(actualCellCategories.size(), is(24));  //ONLY 24 fields implemented
	}
	
	/**
	 * <xs:element name="Opportunity" minOccurs="1" maxOccurs="1">
	 * TDS Report should have only one Opportunity attribute. otherwise, invoke exception 
     * in xmlValidate.validateXMLSchema(TDSReportXSDResource, tmpPath.toString()) in TdsReportAnalysisEngine.java
     * 
     * this test won't be invoked in real scenario
	 * 
	 * @throws Exception
	 */
	@Test
	public void whenOpportunityIsNull_EmptyCellCategories() throws Exception {
		
	     // Arrange
        final TDSReport tdsReport = new TDSReport();

        final IndividualResponse individualResponse = new IndividualResponse();
        individualResponse.setTDSReport(tdsReport);
		/*
        // Act
        underTest.analyze(individualResponse);
        
        // Assert
        Assert.assertThat(individualResponse.getOpportunityCategory().getCellCategories().size(), is(0));
        */
	}
}
