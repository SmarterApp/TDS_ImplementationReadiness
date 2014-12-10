package org.cresst.sb.irp.dao;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.ItemCategory;
import org.cresst.sb.irp.domain.analysis.OpportunityCategory;
import org.cresst.sb.irp.domain.analysis.ResponseCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Item;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Item.Response;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class ItemResponseAnalysisAction extends AnalysisAction {
	private static Logger logger = Logger.getLogger(ItemResponseAnalysisAction.class);

	@Override
	public void analysis() throws IOException {
		try {
			IndividualResponse individualResponse = getIndividualResponse();
			TDSReport tdsReport = individualResponse.getTDSReport();
			OpportunityCategory opportunityCategory = individualResponse.getOpportunityCategory();
			List<ItemCategory> listItemCategory = opportunityCategory.getListItemCategory();

			System.out.println("listItemCategory size --->" + listItemCategory.size());
			Opportunity opportunity = getOpportunity(tdsReport);
			List<Item> listItem = opportunity.getItem();
			
			int indexOfItemCategory = 0;
			for (Item i : listItem) {
				ItemCategory itemCategory = listItemCategory.get(indexOfItemCategory);
				analysisItemResponse(itemCategory, i);
				indexOfItemCategory++;
			}
		} catch (Exception e) {
			logger.error("analysis exception: ", e);
		}
	}
	
	private void analysisItemResponse(ItemCategory itemCategory, Item item) {
		try {
			ResponseCategory responseCategory = new ResponseCategory();
			itemCategory.setResponseCategory(responseCategory);
			Response response = item.getResponse();
			FieldCheckType fieldCheckType;
			
			String strDate = response.getDate().toString();
			responseCategory.setDate(strDate);
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			responseCategory.setDateFieldCheckType(fieldCheckType);
			//validateField(response.getDate(), EnumFieldCheckType.P, EnumItemFieldName.position, fieldCheckType);
			
			String strType = response.getType();
			responseCategory.setType(strType);
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			responseCategory.setTypeFieldCheckType(fieldCheckType);
			//validateField(response.getType(), EnumFieldCheckType.P, EnumItemFieldName.position, fieldCheckType);
			
			String strNodeText = response.getContent();
			responseCategory.setContent(strNodeText);
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			responseCategory.setContentFieldCheckType(fieldCheckType);
			//validateField(response.getContent(), EnumFieldCheckType.P, EnumItemFieldName.position, fieldCheckType);
			
			
		} catch (Exception e) {
			logger.error("analysisItemResponse exception: ", e);
		}
	}

}
