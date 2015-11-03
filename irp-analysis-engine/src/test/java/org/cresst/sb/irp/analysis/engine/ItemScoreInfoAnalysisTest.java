package org.cresst.sb.irp.analysis.engine;

import java.util.List;

import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.ItemCategory;
import org.cresst.sb.irp.domain.analysis.OpportunityCategory;
import org.cresst.sb.irp.domain.analysis.ResponseCategory;
import org.cresst.sb.irp.domain.analysis.ItemCategory.ItemStatusEnum;
import org.cresst.sb.irp.domain.tdsreport.ScoreInfoType;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import tds.itemscoringengine.ScoringStatus;
import builders.CellCategoryBuilder;
import builders.ItemAttributeBuilder;
import builders.ItemCategoryBuilder;
import builders.ItemResponseCategoryBuilder;
import builders.ItemScoreInfoBuilder;

import com.google.common.collect.Lists;

/**
 * Test class to verify that ScoreInfo are analyzed correctly by ItemScoreInfoAnalysisAction
 */
@RunWith(MockitoJUnitRunner.class)
public class ItemScoreInfoAnalysisTest {

	@InjectMocks
	private ItemScoreInfoAnalysisAction underTest = new ItemScoreInfoAnalysisAction();

	
	private IndividualResponse generateIndividualResponse(List<TDSReport.Opportunity.Item> opportunityItems, List<ItemCategory> itemCategories) {

		final TDSReport.Opportunity opportunity = new TDSReport.Opportunity();

		if (opportunityItems != null) {
			opportunity.getItem().addAll(opportunityItems);
		}

		final TDSReport tdsReport = new TDSReport();
		tdsReport.setOpportunity(opportunity);

		final IndividualResponse individualResponse = new IndividualResponse();
		individualResponse.setTDSReport(tdsReport);

		final OpportunityCategory opportunityCategory = new OpportunityCategory();
		individualResponse.setOpportunityCategory(opportunityCategory);
		
		if (itemCategories != null){
			opportunityCategory.setItemCategories(itemCategories);
		}
		
		return individualResponse;
	}
	
	@Test
	public void whenTdsScorePointMatchIRPItemScore_MCMS() {
		
		// The TDS Report items
		final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
				new ItemAttributeBuilder().bankKey(1).key(1).format("MC").score("1").toOpportunityItem(), 
				new ItemAttributeBuilder().bankKey(2).key(2).format("MS").score("1").toOpportunityItem(),
				new ItemAttributeBuilder().bankKey(3).key(3).format("MC").score("1").toOpportunityItem(), 
				new ItemAttributeBuilder().bankKey(4).key(4).format("MS").score("1").toOpportunityItem());
		
		// The TDS Report Item ScoreInfo
		final List<ScoreInfoType> tdsScoreInfoType = Lists.newArrayList(
				new ItemScoreInfoBuilder().scorePoint("1").maxScore("1").scoreDimension("overall").scoreStatus("Scored").toScoreInfoType(), 
				new ItemScoreInfoBuilder().scorePoint("0").maxScore("1").scoreDimension("overall").scoreStatus("Scored").toScoreInfoType(),
				new ItemScoreInfoBuilder().scorePoint("1").maxScore("2").scoreDimension("overall").scoreStatus("Scored").toScoreInfoType(),
				new ItemScoreInfoBuilder().scorePoint("1").maxScore("1").scoreDimension("overall").scoreStatus("Scored").toScoreInfoType());
		
		// Add above TDS Report responses to its corresponding TDS Report items
		int index = 0;
		for (TDSReport.Opportunity.Item itemTmp : tdsReportItems) {
			itemTmp.setScoreInfo(tdsScoreInfoType.get(index));
			index++;
		}
		
		// The List<ItemCategory> in OpportunityCategory
		final List<ItemCategory> itemCategories = Lists.newArrayList(
			new ItemCategoryBuilder().itemBankKeyKey("item-1-1").status(ItemStatusEnum.FOUND).itemFormatCorrect(true).toItemCategory(),
			new ItemCategoryBuilder().itemBankKeyKey("item-2-2").status(ItemStatusEnum.FOUND).itemFormatCorrect(true).toItemCategory(),
			new ItemCategoryBuilder().itemBankKeyKey("item-3-3").status(ItemStatusEnum.FOUND).itemFormatCorrect(true).toItemCategory(),
			new ItemCategoryBuilder().itemBankKeyKey("item-4-4").status(ItemStatusEnum.FOUND).itemFormatCorrect(true).toItemCategory());
		
		// add a CellCategories to ItemCategory 1
		ItemCategory itemCategory0 = itemCategories.get(0);
		itemCategory0.addCellCategory(new CellCategoryBuilder().tdsFieldName("bankKey").tdsFieldNameValue("1").toCellCategory());
		itemCategory0.addCellCategory(new CellCategoryBuilder().tdsFieldName("key").tdsFieldNameValue("1").toCellCategory());
		
		// add a ResponseCategory to ItemCategory 1
		final ResponseCategory responseCategory0 = new ItemResponseCategoryBuilder()
			.scoringStatus(ScoringStatus.Scored).itemScore(1).toResponseCategory();
		itemCategory0.setResponseCategory(responseCategory0);
		
		// add a CellCategories to ItemCategory 2
		ItemCategory itemCategory1 = itemCategories.get(1);
		itemCategory1.addCellCategory(new CellCategoryBuilder().tdsFieldName("bankKey").tdsFieldNameValue("2").toCellCategory());
		itemCategory1.addCellCategory(new CellCategoryBuilder().tdsFieldName("key").tdsFieldNameValue("2").toCellCategory());
		
		// add a ResponseCategory to ItemCategory 2
		final ResponseCategory responseCategory1 = new ItemResponseCategoryBuilder()
			.scoringStatus(ScoringStatus.Scored).itemScore(1).toResponseCategory();
		itemCategory0.setResponseCategory(responseCategory1);
		
		// add a CellCategories to ItemCategory 1
		ItemCategory itemCategory3 = itemCategories.get(2);
		itemCategory3.addCellCategory(new CellCategoryBuilder().tdsFieldName("bankKey").tdsFieldNameValue("3").toCellCategory());
		itemCategory3.addCellCategory(new CellCategoryBuilder().tdsFieldName("key").tdsFieldNameValue("3").toCellCategory());
		
		// add a ResponseCategory to ItemCategory 3
		final ResponseCategory responseCategory2 = new ItemResponseCategoryBuilder()
			.scoringStatus(ScoringStatus.Scored).itemScore(1).toResponseCategory();
		itemCategory0.setResponseCategory(responseCategory2);
		
		// add a CellCategories to ItemCategory 2
		ItemCategory itemCategory4 = itemCategories.get(3);
		itemCategory4.addCellCategory(new CellCategoryBuilder().tdsFieldName("bankKey").tdsFieldNameValue("4").toCellCategory());
		itemCategory4.addCellCategory(new CellCategoryBuilder().tdsFieldName("key").tdsFieldNameValue("4").toCellCategory());
		
		// add a ResponseCategory to ItemCategory 4
		final ResponseCategory responseCategory3 = new ItemResponseCategoryBuilder()
			.scoringStatus(ScoringStatus.Scored).itemScore(1).toResponseCategory();
		itemCategory0.setResponseCategory(responseCategory3);
		
		final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems, itemCategories);	
		
		// Act
		underTest.analyze(individualResponse);
	}
	
	@Test
	public void whenTdsScorePointMatchItemScoreEngineScore_MachineScore() {

	}

}
