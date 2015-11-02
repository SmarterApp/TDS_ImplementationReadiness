package org.cresst.sb.irp.analysis.engine;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.List;

import builders.ExamineeAttributeBuilder;
import org.cresst.sb.irp.analysis.engine.examinee.EnumExamineeAttributeFieldName;
import org.cresst.sb.irp.domain.analysis.AccommodationCategory;
import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.ExamineeCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.OpportunityCategory;
import org.cresst.sb.irp.domain.tdsreport.Context;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.service.AccommodationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import builders.AccommodationBuilder;
import builders.AccommodationExcelBuilder;
import builders.CellCategoryBuilder;

import com.google.common.collect.Lists;

/**
 * Test class to verify the behavior of AccommodationAnalysisAction
 */
@RunWith(MockitoJUnitRunner.class)
public class AccommodationAnalysisActionTest {

	@Mock
	private AccommodationService accommodationService;
	
	@InjectMocks
	private AccommodationAnalysisAction underTest = new AccommodationAnalysisAction();

	@Test
	public void testAccommodation() {

		final List<TDSReport.Opportunity.Accommodation> accommodations = Lists.newArrayList(
				new AccommodationBuilder()
					.type("language")
					.value("Enu")
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
		
		final CellCategory cellCategory = new CellCategory();
		cellCategory.setTdsFieldName("key");
		cellCategory.setTdsFieldNameValue("9999");
		
		final ExamineeCategory examineeCategory = new ExamineeCategory();
		examineeCategory.addCellCategory(cellCategory);
		individualResponse.setExamineeCategory(examineeCategory);
		
		final OpportunityCategory opportunityCategory = new OpportunityCategory();
		individualResponse.setOpportunityCategory(opportunityCategory);

	     when(accommodationService.getAccommodationByStudentIdentifier("StudentID")).thenReturn(new AccommodationExcelBuilder("StudentID")
	     	.language("ENU")
	     	.other("None")
	     	.zoom("TDS_PS_L1")
	     	.toAccommodation());
		
		// Act
		underTest.analyze(individualResponse);
		
		OpportunityCategory actualOpportunityCategory = individualResponse.getOpportunityCategory();
		List<AccommodationCategory> accommodationCategories = actualOpportunityCategory.getAccommodationCategories();
		
		assertThat(accommodationCategories.size(), is(3));
	}
	
	@Test
	public void testAccommodationTypeValue_CaseOrWhiteSpaces() {

		final List<TDSReport.Opportunity.Accommodation> accommodations = Lists.newArrayList(
				new AccommodationBuilder()
					.type("language")
					.value("France")
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

		final TDSReport.Examinee examinee = new TDSReport.Examinee();
		examinee.getExamineeAttributeOrExamineeRelationship().add(new ExamineeAttributeBuilder()
				.name(EnumExamineeAttributeFieldName.StudentIdentifier.name())
				.value("StudentID")
				.context(Context.FINAL)
				.toExamineeAttribute());
		tdsReport.setExaminee(examinee);
		
		final OpportunityCategory opportunityCategory = new OpportunityCategory();
		individualResponse.setOpportunityCategory(opportunityCategory);

	     when(accommodationService.getAccommodationByStudentIdentifier("StudentID")).thenReturn(new AccommodationExcelBuilder("StudentID")
	     	.language("france")
	     	.other("None")
	     	.zoom("No default zoom applied")
	     	.toAccommodation());
		
		// Act
		underTest.analyze(individualResponse);
		
		OpportunityCategory actualOpportunityCategory = individualResponse.getOpportunityCategory();
		List<AccommodationCategory> accommodationCategories = actualOpportunityCategory.getAccommodationCategories();
	
		AccommodationCategory accommodationCategory0 = accommodationCategories.get(0); // <Accommodation type="language" value="English" 
		List<CellCategory> actualCellCategories0 = accommodationCategory0.getCellCategories();
		
		AccommodationCategory accommodationCategory1 = accommodationCategories.get(1); // <Accommodation type="language" value="English" 
		List<CellCategory> actualCellCategories1 = accommodationCategory1.getCellCategories();
		
		AccommodationCategory accommodationCategory2 = accommodationCategories.get(2); //    <Accommodation type="Print Size" value="No default zoom applied"
		List<CellCategory> actualCellCategories2 = accommodationCategory2.getCellCategories();
		
		List<CellCategory> expectedCellCategories0 = Lists.newArrayList(
			new CellCategoryBuilder()
		        	.isFieldEmpty(false)
		        	.correctDataType(true)
		        	.acceptableValue(true)
		        	.correctValue(true)
		        	.enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
		        	.tdsFieldName(AccommodationAnalysisAction.EnumAccommodationFieldName.Language.name())
		            .tdsFieldNameValue("France")
		            .tdsExpectedValue("france")
		        	.toCellCategory());
		
		List<CellCategory> expectedCellCategories1 = Lists.newArrayList(
				new CellCategoryBuilder()
		        	.isFieldEmpty(false)
		        	.correctDataType(true)
		        	.acceptableValue(true)
		        	.correctValue(true)
		        	.enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
		        	.tdsFieldName(AccommodationAnalysisAction.EnumAccommodationFieldName.Other.name())
		            .tdsFieldNameValue("None")
		            .tdsExpectedValue("None")
		        	.toCellCategory());
		
		List<CellCategory> expectedCellCategories2 = Lists.newArrayList(
				new CellCategoryBuilder()
		        	.isFieldEmpty(false)
		        	.correctDataType(true)
		        	.acceptableValue(true)
		        	.correctValue(true)
		        	.enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
		        	.tdsFieldName(AccommodationAnalysisAction.EnumAccommodationFieldName.PrintSize.name())
		            .tdsFieldNameValue("No default zoom applied")
		            .tdsExpectedValue("No default zoom applied")
		        	.toCellCategory());
		
	    assertEquals(expectedCellCategories0, actualCellCategories0);
	    assertEquals(expectedCellCategories1, actualCellCategories1);
	    assertEquals(expectedCellCategories2, actualCellCategories2);
	}
}
