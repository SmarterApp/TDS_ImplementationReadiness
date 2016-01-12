package org.cresst.sb.irp.analysis.engine;

import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.List;

import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.GenericVariableCategory;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.OpportunityCategory;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import builders.CellCategoryBuilder;
import builders.GenericVariableBuilder;

/**
 * Test class to verify the behavior of GenericVariableAnalysisAction
 */
@RunWith(MockitoJUnitRunner.class)
public class GenericVariableAnalysisActionTest {

	@InjectMocks
	private GenericVariableAnalysisAction underTest = new GenericVariableAnalysisAction();

	@Test
	public void testAnalysis() throws Exception {
		final List<TDSReport.Opportunity.GenericVariable> genericVariables = new ArrayList<>();
		genericVariables.add(new GenericVariableBuilder().name("OPPID").value("100074").context("COMPONENT_2").toGenericVariable());
		genericVariables
				.add(new GenericVariableBuilder().name("STATUS").value("completed").context("COMPONENT_2").toGenericVariable());
		genericVariables.add(new GenericVariableBuilder().name("TESTID").value("SBAC-IRP-Perf-MATH-3").context("COMPONENT_2")
				.toGenericVariable());

		final TDSReport.Opportunity opportunity = new TDSReport.Opportunity();
		opportunity.getGenericVariable().addAll(genericVariables);

		final TDSReport tdsReport = new TDSReport();
		tdsReport.setOpportunity(opportunity);

		final IndividualResponse individualResponse = new IndividualResponse();
		individualResponse.setTDSReport(tdsReport);
		individualResponse.setOpportunityCategory(new OpportunityCategory());

		// Act
		underTest.analyze(individualResponse);

		OpportunityCategory actualOpportunityCategory = individualResponse.getOpportunityCategory();

		List<GenericVariableCategory> genericVariableCategories = new ArrayList<>();

		GenericVariableCategory genericVariableCategory = new GenericVariableCategory();
		genericVariableCategories.add(genericVariableCategory);
		for (CellCategory cellCategory : createValidCellCategories("OPPID", "100074", "COMPONENT_2")) {
			genericVariableCategory.addCellCategory(cellCategory);
		}

		genericVariableCategory = new GenericVariableCategory();
		genericVariableCategories.add(genericVariableCategory);
		for (CellCategory cellCategory : createValidCellCategories("STATUS", "completed", "COMPONENT_2")) {
			genericVariableCategory.addCellCategory(cellCategory);
		}

		genericVariableCategory = new GenericVariableCategory();
		genericVariableCategories.add(genericVariableCategory);
		for (CellCategory cellCategory : createValidCellCategories("TESTID", "SBAC-IRP-Perf-MATH-3", "COMPONENT_2")) {
			genericVariableCategory.addCellCategory(cellCategory);
		}

		assertArrayEquals(genericVariableCategories.toArray(), actualOpportunityCategory.getGenericVariableCategories().toArray());
	}

	private CellCategory[] createValidCellCategories(String name, String value, String context) {
		CellCategory[] cellCategories =
				new CellCategory[] {
						new CellCategoryBuilder().correctDataType(true).acceptableValue(true).isFieldEmpty(false)
								.enumFieldCheckType(FieldCheckType.EnumFieldCheckType.P)
								.tdsFieldName(GenericVariableAnalysisAction.EnumGenericVariablFieldName.name.toString())
								.tdsFieldNameValue(name).toCellCategory(),

						new CellCategoryBuilder().correctDataType(true).acceptableValue(true).isFieldEmpty(false)
								.enumFieldCheckType(FieldCheckType.EnumFieldCheckType.P)
								.tdsFieldName(GenericVariableAnalysisAction.EnumGenericVariablFieldName.value.toString())
								.tdsFieldNameValue(value).toCellCategory(),

						new CellCategoryBuilder().correctDataType(true).acceptableValue(true).isFieldEmpty(false)
								.enumFieldCheckType(FieldCheckType.EnumFieldCheckType.P)
								.tdsFieldName(GenericVariableAnalysisAction.EnumGenericVariablFieldName.context.toString())
								.tdsFieldNameValue(context).toCellCategory() };

		return cellCategories;
	}

}
