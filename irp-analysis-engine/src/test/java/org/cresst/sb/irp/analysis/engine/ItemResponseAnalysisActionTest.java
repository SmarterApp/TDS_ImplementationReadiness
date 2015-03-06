package org.cresst.sb.irp.analysis.engine;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.List;

import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.ExamineeCategory;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.ItemCategory;
import org.cresst.sb.irp.domain.analysis.OpportunityCategory;
import org.cresst.sb.irp.domain.studentresponse.StudentResponse;
import org.cresst.sb.irp.domain.studentresponse.TestItemResponse;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.service.StudentResponseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import builders.ItemAttributeBuilder;
import builders.ItemResponseBuilder;
import builders.StudentResponseBuilder;

import com.google.common.collect.Lists;

/**
 * Test class to verify that Response are analyzed correctly by ItemResponseAnalysisAction
 */
@RunWith(MockitoJUnitRunner.class)
public class ItemResponseAnalysisActionTest {

    @Mock
    private StudentResponseService studentResponseService;
	
    @InjectMocks
    private ItemAttributesAnalysisAction itemAttributesAnalysisAction = new ItemAttributesAnalysisAction();
    
	@InjectMocks
	private ItemResponseAnalysisAction itemResponseAnalysisAction = new ItemResponseAnalysisAction();

	private IndividualResponse generateIndividualResponse(List<TDSReport.Opportunity.Item> opportunityItems) {

		final TDSReport.Opportunity opportunity = new TDSReport.Opportunity();

		if (opportunityItems != null) {
			opportunity.getItem().addAll(opportunityItems);
		}

		final TDSReport tdsReport = new TDSReport();
		tdsReport.setOpportunity(opportunity);

		final IndividualResponse individualResponse = new IndividualResponse();
		individualResponse.setTDSReport(tdsReport);

		final CellCategory cellCategory = new CellCategory();
		cellCategory.setTdsFieldName("key");
		cellCategory.setTdsFieldNameValue("9999");

		final ExamineeCategory examineeCategory = new ExamineeCategory();
		examineeCategory.addCellCategory(cellCategory);

		individualResponse.setExamineeCategory(examineeCategory);

		final OpportunityCategory opportunityCategory = new OpportunityCategory();
		individualResponse.setOpportunityCategory(opportunityCategory);

		return individualResponse;
	}

	@Test
	public void test1() {
		
		   // Represents all the Items in the IRP package
        List<StudentResponse> irpPackageItems = Lists.newArrayList(
                new StudentResponseBuilder(9999).bankKey(1).id(1).itemType("MC").excelResponse("A").toStudentResponse(),
                new StudentResponseBuilder(9999).bankKey(2).id(2).itemType("MS").excelResponse("A,B,C,D,E,F").toStudentResponse()
        );
		
		// The TDS Report items
	    final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
                new ItemAttributeBuilder().bankKey(1).key(1).format("MC").toOpportunityItem(),
                new ItemAttributeBuilder().bankKey(2).key(2).format("MS").toOpportunityItem()
        );

	    // The TDS Report responses
	    final List<TDSReport.Opportunity.Item.Response> tdsReportItemResponses = Lists.newArrayList(
	    		new ItemResponseBuilder().content("A").toIemResponse(),
	    		new ItemResponseBuilder().content("A,B,C,D,E,F").toIemResponse()
	    );
	    
	    // The TDS Report items with responses
	    int index = 0;
	    for(TDSReport.Opportunity.Item itemTmp: tdsReportItems){
	    	itemTmp.setResponse(tdsReportItemResponses.get(index));
	    	index++;
	    }
	    
	    final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems);
		
	    TestItemResponse testItemResponse = new TestItemResponse();
        testItemResponse.setStudentID(9999L);
        testItemResponse.setStudentResponses(irpPackageItems);

        when(studentResponseService.getTestItemResponseByStudentID(9999L)).thenReturn(testItemResponse);

	    // Act
        itemAttributesAnalysisAction.analyze(individualResponse);
        
        // Act
        itemResponseAnalysisAction.analyze(individualResponse);
        
        
	    
	}

}
