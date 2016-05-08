package org.cresst.sb.irp.analysis.engine;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.List;

import builders.ExamineeAttributeBuilder;

import org.cresst.sb.irp.analysis.engine.examinee.EnumExamineeAttributeFieldName;
import org.cresst.sb.irp.domain.analysis.AccommodationCategory;
import org.cresst.sb.irp.domain.analysis.CellCategory;
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

	private IndividualResponse generateIndividualResponse(List<TDSReport.Opportunity.Accommodation> accommodations, 
			List<TDSReport.Examinee.ExamineeAttribute> examineeAttributes) {

		final TDSReport.Opportunity opportunity = new TDSReport.Opportunity();

		if (accommodations != null) {
			opportunity.getAccommodation().addAll(accommodations);
		}

		final TDSReport tdsReport = new TDSReport();
		tdsReport.setOpportunity(opportunity);

		final TDSReport.Examinee examinee = new TDSReport.Examinee();
		
		if (examineeAttributes != null){
			examinee.getExamineeAttributeOrExamineeRelationship().addAll(examineeAttributes);
		}
		
		tdsReport.setExaminee(examinee);
		
		final IndividualResponse individualResponse = new IndividualResponse();
		individualResponse.setTDSReport(tdsReport);

		final OpportunityCategory opportunityCategory = new OpportunityCategory();
		individualResponse.setOpportunityCategory(opportunityCategory);
		
		return individualResponse;
	}

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
		
		final List<TDSReport.Examinee.ExamineeAttribute> examineeAttributes = Lists.newArrayList(
			new ExamineeAttributeBuilder()
				.name(EnumExamineeAttributeFieldName.StudentIdentifier.name())
				.value("StudentID")
				.context(Context.FINAL)
				.toExamineeAttribute()
			);
		
		final IndividualResponse individualResponse = generateIndividualResponse(accommodations, examineeAttributes);

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
	public void testAccommodationCodeValue_isFieldValueCorrect() {

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
		
		final List<TDSReport.Examinee.ExamineeAttribute> examineeAttributes = Lists.newArrayList(
				new ExamineeAttributeBuilder()
					.name(EnumExamineeAttributeFieldName.StudentIdentifier.name())
					.value("StudentID")
					.context(Context.FINAL)
					.toExamineeAttribute()
				);
		
		final IndividualResponse individualResponse = generateIndividualResponse(accommodations, examineeAttributes);

	     when(accommodationService.getAccommodationByStudentIdentifier("StudentID")).thenReturn(
	    		 new AccommodationExcelBuilder("StudentID")
			     	.language("ENU")
			     	.other("TDS_Other")
			     	.zoom("TDS_PS_L0")
			     	.toAccommodation());
		
		// Act
		underTest.analyze(individualResponse);
		
		OpportunityCategory actualOpportunityCategory = individualResponse.getOpportunityCategory();
		List<AccommodationCategory> accommodationCategories = actualOpportunityCategory.getAccommodationCategories();
	
		AccommodationCategory accommodationCategory0 = accommodationCategories.get(0); 
		List<CellCategory> actualCellCategories0 = accommodationCategory0.getCellCategories();
		
		AccommodationCategory accommodationCategory1 = accommodationCategories.get(1);  
		List<CellCategory> actualCellCategories1 = accommodationCategory1.getCellCategories();
		
		AccommodationCategory accommodationCategory2 = accommodationCategories.get(2); 
		List<CellCategory> actualCellCategories2 = accommodationCategory2.getCellCategories();
	
		List<CellCategory> expectedCellCategories0 = Lists.newArrayList(
			new CellCategoryBuilder()
		        	.fieldEmpty(false)
		        	.correctDataType(true)
		        	.acceptableValue(true)
		        	.correctValue(false)
		        	.enumFieldCheckType(FieldCheckType.EnumFieldCheckType.P)
		        	.tdsFieldName("type")
		            .tdsFieldNameValue("language")
		            .tdsExpectedValue(null)
		        	.toCellCategory(),
			new CellCategoryBuilder()
		        	.fieldEmpty(false)
		        	.correctDataType(true)
		        	.acceptableValue(true)
		        	.correctValue(false)
		        	.enumFieldCheckType(FieldCheckType.EnumFieldCheckType.P)
		        	.tdsFieldName("value")
		            .tdsFieldNameValue("France")
		            .tdsExpectedValue(null)
		        	.toCellCategory(),	
			new CellCategoryBuilder()
		        	.fieldEmpty(false)
		        	.correctDataType(true)
		        	.acceptableValue(true)
		        	.correctValue(true)
		        	.enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
		        	.tdsFieldName("code")
		            .tdsFieldNameValue("ENU")
		            .tdsExpectedValue("ENU")
		        	.toCellCategory(),
			new CellCategoryBuilder()
		        	.fieldEmpty(false)
		        	.correctDataType(true)
		        	.acceptableValue(true)
		        	.correctValue(false)
		        	.enumFieldCheckType(FieldCheckType.EnumFieldCheckType.P)
		        	.tdsFieldName("segment")
		            .tdsFieldNameValue("0")
		            .tdsExpectedValue(null)
		        	.toCellCategory());
		
		List<CellCategory> expectedCellCategories1 = Lists.newArrayList(
			new CellCategoryBuilder()
		        	.fieldEmpty(false)
		        	.correctDataType(true)
		        	.acceptableValue(true)
		        	.correctValue(false)
		        	.enumFieldCheckType(FieldCheckType.EnumFieldCheckType.P)
		        	.tdsFieldName("type")
		            .tdsFieldNameValue("Other")
		            .tdsExpectedValue(null)
		        	.toCellCategory(),
		    new CellCategoryBuilder()
		        	.fieldEmpty(false)
		        	.correctDataType(true)
		        	.acceptableValue(true)
		        	.correctValue(false)
		        	.enumFieldCheckType(FieldCheckType.EnumFieldCheckType.P)
		        	.tdsFieldName("value")
		            .tdsFieldNameValue("None")
		            .tdsExpectedValue(null)
		        	.toCellCategory(),		
		    new CellCategoryBuilder()
		        	.fieldEmpty(false)
		        	.correctDataType(true)
		        	.acceptableValue(true)
		        	.correctValue(true)
		        	.enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
		        	.tdsFieldName("code")
		            .tdsFieldNameValue("TDS_Other")
		            .tdsExpectedValue("TDS_Other")
		        	.toCellCategory(),
		    new CellCategoryBuilder()
		        	.fieldEmpty(false)
		        	.correctDataType(true)
		        	.acceptableValue(true)
		        	.correctValue(false)
		        	.enumFieldCheckType(FieldCheckType.EnumFieldCheckType.P)
		        	.tdsFieldName("segment")
		            .tdsFieldNameValue("0")
		            .tdsExpectedValue(null)
		        	.toCellCategory());
		
		List<CellCategory> expectedCellCategories2 = Lists.newArrayList(
			new CellCategoryBuilder()
		        	.fieldEmpty(false)
		        	.correctDataType(true)
		        	.acceptableValue(true)
		        	.correctValue(false)
		        	.enumFieldCheckType(FieldCheckType.EnumFieldCheckType.P)
		        	.tdsFieldName("type")
		            .tdsFieldNameValue("Print Size")
		            .tdsExpectedValue(null)
		        	.toCellCategory(),
	        new CellCategoryBuilder()
		        	.fieldEmpty(false)
		        	.correctDataType(true)
		        	.acceptableValue(true)
		        	.correctValue(false)
		        	.enumFieldCheckType(FieldCheckType.EnumFieldCheckType.P)
		        	.tdsFieldName("value")
		            .tdsFieldNameValue("No default zoom applied")
		            .tdsExpectedValue(null)
		        	.toCellCategory(),				
			new CellCategoryBuilder()
		        	.fieldEmpty(false)
		        	.correctDataType(true)
		        	.acceptableValue(true)
		        	.correctValue(true)
		        	.enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
		        	.tdsFieldName("code")
		            .tdsFieldNameValue("TDS_PS_L0")
		            .tdsExpectedValue("TDS_PS_L0")
		        	.toCellCategory(),
		    new CellCategoryBuilder()
		        	.fieldEmpty(false)
		        	.correctDataType(true)
		        	.acceptableValue(true)
		        	.correctValue(false)
		        	.enumFieldCheckType(FieldCheckType.EnumFieldCheckType.P)
		        	.tdsFieldName("segment")
		            .tdsFieldNameValue("0")
		            .tdsExpectedValue(null)
		        	.toCellCategory());
		
	    assertEquals(expectedCellCategories0, actualCellCategories0);
	    assertEquals(expectedCellCategories1, actualCellCategories1);
	    assertEquals(expectedCellCategories2, actualCellCategories2);
	  
	}
}
