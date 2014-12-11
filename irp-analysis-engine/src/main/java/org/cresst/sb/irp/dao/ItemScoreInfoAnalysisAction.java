package org.cresst.sb.irp.dao;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.ItemCategory;
import org.cresst.sb.irp.domain.analysis.OpportunityCategory;
import org.cresst.sb.irp.domain.analysis.ScoreInfoCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.tdsreport.ScoreInfoType;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Item;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class ItemScoreInfoAnalysisAction extends AnalysisAction {
	private static Logger logger = Logger.getLogger(ItemScoreInfoAnalysisAction.class);

	@Override
	public void analysis() throws IOException {
		try {
			IndividualResponse individualResponse = getIndividualResponse();
			TDSReport tdsReport = individualResponse.getTDSReport();
			OpportunityCategory opportunityCategory = individualResponse.getOpportunityCategory();
			List<ItemCategory> listItemCategory = opportunityCategory.getListItemCategory();
			Opportunity opportunity = getOpportunity(tdsReport);
			List<Item> listItem = opportunity.getItem();
			int indexOfItemCategory = 0;
			for (Item i : listItem) {
				ItemCategory itemCategory = listItemCategory.get(indexOfItemCategory);
				analysisItemScoreInfo(itemCategory, i);
				indexOfItemCategory++;
			}
		} catch (Exception e) {
			logger.error("analysis exception: ", e);
		}
	}

	private void analysisItemScoreInfo(ItemCategory itemCategory, Item item) {
		try {
			ScoreInfoCategory scoreInfoCategory = new ScoreInfoCategory();
			itemCategory.setScoreInfoCategory(scoreInfoCategory);
			
			ScoreInfoType scoreInfoType = item.getScoreInfo();
			FieldCheckType fieldCheckType;
			
			String scorePoint = scoreInfoType.getScorePoint();
			scoreInfoCategory.setScorePoint(scorePoint);
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			scoreInfoCategory.setScorePointFieldCheckType(fieldCheckType);
			//validateField(response.getDate(), EnumFieldCheckType.P, EnumItemFieldName.position, fieldCheckType);
			
			//need to double check UPDATE dox with AIR as "old" version does NOT have this value
			String maxScore = scoreInfoType.getMaxScore();
			scoreInfoCategory.setMaxScore(maxScore);
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			scoreInfoCategory.setScorePointFieldCheckType(fieldCheckType);
			//validateField(response.getDate(), EnumFieldCheckType.P, EnumItemFieldName.position, fieldCheckType);
			
			
		}catch (Exception e) {
			logger.error("analysisItemScoreInfo exception: ", e);
		}
	}
}
