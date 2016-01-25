package org.cresst.sb.irp.analysis.engine;

import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.GenericVariableCategory;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.OpportunityCategory;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.GenericVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GenericVariableAnalysisAction extends
		AnalysisAction<TDSReport.Opportunity.GenericVariable, GenericVariableAnalysisAction.EnumGenericVariablFieldName, Object> {
	private final static Logger logger = LoggerFactory.getLogger(GenericVariableAnalysisAction.class);

	public enum EnumGenericVariablFieldName {
		name, value, context
	}

	@Override
	public void analyze(IndividualResponse individualResponse) {
		List<TDSReport.Opportunity.GenericVariable> genericVariables =
				individualResponse.getTDSReport().getOpportunity().getGenericVariable();
		if (genericVariables.size() > 0) {
			List<GenericVariableCategory> genericVariableCategories = new ArrayList<>();
			OpportunityCategory opportunityCategory = individualResponse.getOpportunityCategory();
			opportunityCategory.setGenericVariableCategories(genericVariableCategories);

			for (TDSReport.Opportunity.GenericVariable genericVariable : genericVariables) {
				GenericVariableCategory genericVariableCategory = new GenericVariableCategory();
				genericVariableCategories.add(genericVariableCategory);
				analyzeGenericVariable(genericVariableCategory, genericVariable);
			}
		}
	}

	private void analyzeGenericVariable(GenericVariableCategory genericVariableCategory, GenericVariable genericVariable) {
		validate(genericVariableCategory, genericVariable, genericVariable.getName(), FieldCheckType.EnumFieldCheckType.P,
				EnumGenericVariablFieldName.name, null);
		validate(genericVariableCategory, genericVariable, genericVariable.getValue(), FieldCheckType.EnumFieldCheckType.P,
				EnumGenericVariablFieldName.value, null);
		validate(genericVariableCategory, genericVariable, genericVariable.getContext(), FieldCheckType.EnumFieldCheckType.P,
				EnumGenericVariablFieldName.context, null);
	}

	  /**
     * Field Check Type (P) --> check that field is not empty and field value is of correct data type and within acceptable
     * values
     *
     * @param genericVariable	GenericVariable object with fields to check
     * @param enumFieldName  Specifies the field to check
     * @param fieldCheckType Stores the analysis data for the field being analyzed
     */
	@Override
	protected void checkP(GenericVariable genericVariable, EnumGenericVariablFieldName enumFieldName, FieldCheckType fieldCheckType) {
		  switch (enumFieldName) {
          case name:
              processP_PritableASCIIone(genericVariable.getName(), fieldCheckType);
              break;
          case value:
              processP_PritableASCIIone(genericVariable.getValue(), fieldCheckType);
              break;
          case context:
              processP_PritableASCIIone(genericVariable.getContext(), fieldCheckType);
              break;
          default:
              break;
      }
	}

	@Override
	protected void checkC(GenericVariable checkObj, EnumGenericVariablFieldName enumFieldName, FieldCheckType fieldCheckType,
			Object comparisonData) {

	}

}
