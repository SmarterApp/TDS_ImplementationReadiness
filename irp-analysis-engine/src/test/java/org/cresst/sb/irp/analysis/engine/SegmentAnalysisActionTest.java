package org.cresst.sb.irp.analysis.engine;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.List;

import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.OpportunityCategory;
import org.cresst.sb.irp.domain.analysis.SegmentCategory;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test class to verify the behavior of SegmentAnalysisAction
 */
@RunWith(MockitoJUnitRunner.class)
public class SegmentAnalysisActionTest {

	@InjectMocks
	private SegmentAnalysisAction underTest = new SegmentAnalysisAction();

	@Test
	public void testAnalysis() throws Exception {

		final TDSReport.Opportunity.Segment segment = new TDSReport.Opportunity.Segment();
		segment.setAlgorithm("fixedform");
		segment.setAlgorithmVersion("0");
		segment.setFormId("IRP::MathG3::Perf::SP15");
		segment.setFormKey("187-764");
		segment.setId("(SBAC_PT)SBAC-IRP-Perf-MATH-3-Summer-2015-2016");
		segment.setPosition((short) 1);

		TDSReport.Opportunity opportunity = new TDSReport.Opportunity();
		opportunity.getSegment().add(segment);

		final TDSReport tdsReport = new TDSReport();
		tdsReport.setOpportunity(opportunity);

		final IndividualResponse individualResponse = new IndividualResponse();
		individualResponse.setTDSReport(tdsReport);
		
		final OpportunityCategory opportunityCategory = new OpportunityCategory();
		individualResponse.setOpportunityCategory(opportunityCategory);

		// Act
		underTest.analyze(individualResponse);

		OpportunityCategory actualOpportunityCategory = individualResponse.getOpportunityCategory();
		List<SegmentCategory> segmentCategories = actualOpportunityCategory.getSegmentCategories();
		SegmentCategory actualSegmentCategory = segmentCategories.get(0);

	    assertThat(actualSegmentCategory.getCellCategories().size(), is(6)); 
	}

	//<xs:element name="Segment" minOccurs="0" maxOccurs="unbounded">
	@Test
	public void whenSegmentIsNull_EmptyCellCategories() throws Exception {
		//minOccurs="0"
	}
}
