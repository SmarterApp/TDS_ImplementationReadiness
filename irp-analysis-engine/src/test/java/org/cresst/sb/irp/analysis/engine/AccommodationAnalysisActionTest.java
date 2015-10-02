package org.cresst.sb.irp.analysis.engine;

import static org.junit.Assert.*;

import java.util.List;

import org.cresst.sb.irp.domain.analysis.AccommodationCategory;
import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.OpportunityCategory;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import builders.AccommodationBuilder;
import builders.CellCategoryBuilder;

import com.google.common.collect.Lists;

/**
 * Test class to verify the behavior of AccommodationAnalysisAction
 */
@RunWith(MockitoJUnitRunner.class)
public class AccommodationAnalysisActionTest {

	@InjectMocks
	private AccommodationAnalysisAction underTest = new AccommodationAnalysisAction();

	@Test
	public void testAccommodationType_CaseOrWhiteSpaces() {

		final List<TDSReport.Opportunity.Accommodation> accommodations = Lists.newArrayList(
				new AccommodationBuilder()
					.type("language")
					.value("English")
					.code("ENU")
					.segment(Long.valueOf(0))
					.toAccommodation(),
				new AccommodationBuilder()
					.type("Other")
					.value("None")
					.code("TDS_Other")
					.segment(Long.valueOf(0))
					.toAccommodation(),
				new AccommodationBuilder()
					.type("Print Size")
					.value("No default zoom applied")
					.code("TDS_PS_L0")
					.segment(Long.valueOf(0))
					.toAccommodation()
			);
		
		TDSReport.Opportunity opportunity = new TDSReport.Opportunity();
		opportunity.getAccommodation().addAll(accommodations);
		
		final TDSReport tdsReport = new TDSReport();
		tdsReport.setOpportunity(opportunity);
		
		final IndividualResponse individualResponse = new IndividualResponse();
		individualResponse.setTDSReport(tdsReport);
		
		final OpportunityCategory opportunityCategory = new OpportunityCategory();
		individualResponse.setOpportunityCategory(opportunityCategory);

		// Act
		underTest.analyze(individualResponse);
		
		OpportunityCategory actualOpportunityCategory = individualResponse.getOpportunityCategory();
		List<AccommodationCategory> accommodationCategories = actualOpportunityCategory.getAccommodationCategories();
		AccommodationCategory accommodationCategory0 = accommodationCategories.get(0); // <Accommodation type="language" value="English" 
		List<CellCategory> actualCellCategories0 = accommodationCategory0.getCellCategories();
		
		AccommodationCategory accommodationCategory2 = accommodationCategories.get(2); //    <Accommodation type="Print Size" value="No default zoom applied"
		List<CellCategory> actualCellCategories2 = accommodationCategory2.getCellCategories();
		
		List<CellCategory> expectedCellCategories0 = Lists.newArrayList(
			new CellCategoryBuilder()
		            .tdsFieldName(AccommodationAnalysisAction.EnumAccommodationFieldName.type.toString())
		            .tdsFieldNameValue("language")
		        	.correctValue(false)
		        	.correctDataType(true)
		        	.isFieldEmpty(false)
		        	.enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
		        	.acceptableValue(true)
		        	.toCellCategory(),
		     new CellCategoryBuilder()
			      	.tdsFieldName(AccommodationAnalysisAction.EnumAccommodationFieldName.value.toString())
		        	.tdsFieldNameValue("English")
		        	.correctValue(false)
		        	.correctDataType(true)
		        	.isFieldEmpty(false)
		        	.enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
		        	.acceptableValue(true)
		        	.toCellCategory(),
		     new CellCategoryBuilder()
			      	.tdsFieldName(AccommodationAnalysisAction.EnumAccommodationFieldName.code.toString())
		        	.tdsFieldNameValue("ENU")
		        	.correctValue(false)
		        	.correctDataType(true)
		        	.isFieldEmpty(false)
		        	.enumFieldCheckType(FieldCheckType.EnumFieldCheckType.P)
		        	.acceptableValue(true)
		        	.toCellCategory(),
		     new CellCategoryBuilder()
			      	.tdsFieldName(AccommodationAnalysisAction.EnumAccommodationFieldName.segment.toString())
			    	.tdsFieldNameValue("0")
			    	.correctValue(false)
			    	.correctDataType(true)
			    	.isFieldEmpty(false)
			    	.enumFieldCheckType(FieldCheckType.EnumFieldCheckType.P)
			    	.acceptableValue(true)
			    	.toCellCategory());
		
		List<CellCategory> expectedCellCategories2 = Lists.newArrayList(
				new CellCategoryBuilder()
			            .tdsFieldName(AccommodationAnalysisAction.EnumAccommodationFieldName.type.toString())
			            .tdsFieldNameValue("Print Size")
			        	.correctValue(false)
			        	.correctDataType(true)
			        	.isFieldEmpty(false)
			        	.enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
			        	.acceptableValue(true)
			        	.toCellCategory(),
			     new CellCategoryBuilder()
				      	.tdsFieldName(AccommodationAnalysisAction.EnumAccommodationFieldName.value.toString())
			        	.tdsFieldNameValue("No default zoom applied")
			        	.correctValue(false)
			        	.correctDataType(true)
			        	.isFieldEmpty(false)
			        	.enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
			        	.acceptableValue(true)
			        	.toCellCategory(),
			     new CellCategoryBuilder()
				      	.tdsFieldName(AccommodationAnalysisAction.EnumAccommodationFieldName.code.toString())
			        	.tdsFieldNameValue("TDS_PS_L0")
			        	.correctValue(false)
			        	.correctDataType(true)
			        	.isFieldEmpty(false)
			        	.enumFieldCheckType(FieldCheckType.EnumFieldCheckType.P)
			        	.acceptableValue(true)
			        	.toCellCategory(),
			     new CellCategoryBuilder()
				      	.tdsFieldName(AccommodationAnalysisAction.EnumAccommodationFieldName.segment.toString())
				    	.tdsFieldNameValue("0")
				    	.correctValue(false)
				    	.correctDataType(true)
				    	.isFieldEmpty(false)
				    	.enumFieldCheckType(FieldCheckType.EnumFieldCheckType.P)
				    	.acceptableValue(true)
				    	.toCellCategory());
		
		
	    assertEquals(expectedCellCategories0, actualCellCategories0);
	    assertEquals(expectedCellCategories2, actualCellCategories2);
	}
}
