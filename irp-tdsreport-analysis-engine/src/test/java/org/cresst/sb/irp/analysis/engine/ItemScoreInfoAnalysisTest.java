package org.cresst.sb.irp.analysis.engine;

import java.util.List;

import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.ItemCategory;
import org.cresst.sb.irp.domain.analysis.OpportunityCategory;
import org.cresst.sb.irp.domain.analysis.ResponseCategory;
import org.cresst.sb.irp.domain.analysis.ItemCategory.ItemStatusEnum;
import org.cresst.sb.irp.domain.analysis.ScoreInfoCategory;
import org.cresst.sb.irp.domain.tdsreport.ScoreInfoType;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.junit.Assert;
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

import com.google.common.collect.ImmutableList;
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
				new ItemAttributeBuilder().bankKey(2).key(2).format("MS").score("1").toOpportunityItem());
		
		// The TDS Report Item ScoreInfo
		final List<ScoreInfoType> tdsScoreInfoType = Lists.newArrayList(
				new ItemScoreInfoBuilder().scorePoint("1").maxScore("1").scoreDimension("overall").scoreStatus("Scored").toScoreInfoType(), 
				new ItemScoreInfoBuilder().scorePoint("0").maxScore("1").scoreDimension("overall").scoreStatus("Scored").toScoreInfoType());
		
		// Add above TDS Report responses to its corresponding TDS Report items
		int index = 0;
		for (TDSReport.Opportunity.Item itemTmp : tdsReportItems) {
			itemTmp.setScoreInfo(tdsScoreInfoType.get(index));
			index++;
		}
		
		// The List<ItemCategory> in OpportunityCategory
		final List<ItemCategory> itemCategories = Lists.newArrayList(
			new ItemCategoryBuilder().itemBankKeyKey("item-1-1").status(ItemStatusEnum.FOUND).itemFormatCorrect(true).toItemCategory(),
			new ItemCategoryBuilder().itemBankKeyKey("item-2-2").status(ItemStatusEnum.FOUND).itemFormatCorrect(true).toItemCategory());
	
		ItemCategory itemCategory;
		ResponseCategory responseCategory;
		
		// add a CellCategories to ItemCategory
		itemCategory = itemCategories.get(0);
		itemCategory.addCellCategory(new CellCategoryBuilder().tdsFieldName("bankKey").tdsFieldNameValue("1").toCellCategory());
		itemCategory.addCellCategory(new CellCategoryBuilder().tdsFieldName("key").tdsFieldNameValue("1").toCellCategory());
		itemCategory.addCellCategory(new CellCategoryBuilder().tdsFieldName("format").tdsFieldNameValue("MC").toCellCategory());
		
		// add a ResponseCategory to ItemCategory
		responseCategory = new ItemResponseCategoryBuilder()
			.scoringStatus(ScoringStatus.Scored).itemScore(1).toResponseCategory();
		itemCategory.setResponseCategory(responseCategory);
		
		// add a CellCategories to ItemCategory
		itemCategory = itemCategories.get(1);
		itemCategory.addCellCategory(new CellCategoryBuilder().tdsFieldName("bankKey").tdsFieldNameValue("2").toCellCategory());
		itemCategory.addCellCategory(new CellCategoryBuilder().tdsFieldName("key").tdsFieldNameValue("2").toCellCategory());
		itemCategory.addCellCategory(new CellCategoryBuilder().tdsFieldName("format").tdsFieldNameValue("MS").toCellCategory());
		
		// add a ResponseCategory to ItemCategory
		responseCategory = new ItemResponseCategoryBuilder()
			.scoringStatus(ScoringStatus.Scored).itemScore(0).toResponseCategory();
		itemCategory.setResponseCategory(responseCategory);
		
		final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems, itemCategories);	
		
		// Act
		underTest.analyze(individualResponse);
		
		// Assert
		OpportunityCategory actualOpportunityCategory = individualResponse.getOpportunityCategory();
		List<ItemCategory> actuaItemCategproes = actualOpportunityCategory.getItemCategories();
		
		ItemCategory actualItemCategory;
		ScoreInfoCategory actualScoreInfoCategory;
		CellCategory actualCellCategory;
		CellCategory expectedCellCategory;
		
		actualItemCategory = actuaItemCategproes.get(0);
		actualScoreInfoCategory = actualItemCategory.getScoreInfoCategory();
		
		actualCellCategory = getCellCategoryByFieldName(actualScoreInfoCategory.getCellCategories(), "scorePoint");
		
		// Setup expected results after analysis that indicates the field has an incorrect value
        expectedCellCategory = new CellCategoryBuilder()
                .tdsFieldName(ItemScoreInfoAnalysisAction.EnumItemScoreInfoFieldName.scorePoint.toString())
                .tdsFieldNameValue("1")
                .tdsExpectedValue("1")
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .fieldEmpty(false)
                .correctDataType(true)
                .acceptableValue(true) 
                .correctValue(true)
                .correctWidth(true)
                .toCellCategory();

        Assert.assertEquals(expectedCellCategory, actualCellCategory);
        Assert.assertEquals("MC", actualScoreInfoCategory.getTdsFormat());
        
        actualCellCategory = getCellCategoryByFieldName(actualScoreInfoCategory.getCellCategories(), "scoreStatus");
		
        expectedCellCategory = new CellCategoryBuilder()
                .tdsFieldName(ItemScoreInfoAnalysisAction.EnumItemScoreInfoFieldName.scoreStatus.toString())
                .tdsFieldNameValue("Scored")
                .tdsExpectedValue("Scored")
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .fieldEmpty(false)
                .correctDataType(true)
                .acceptableValue(true) 
                .correctValue(true)
                .correctWidth(true)
                .toCellCategory();
        
        Assert.assertEquals(expectedCellCategory, actualCellCategory);
        
    	actualItemCategory = actuaItemCategproes.get(1);
		actualScoreInfoCategory = actualItemCategory.getScoreInfoCategory();
		
		actualCellCategory = getCellCategoryByFieldName(actualScoreInfoCategory.getCellCategories(), "scorePoint");
		
		// Setup expected results after analysis that indicates the field has an incorrect value
        expectedCellCategory = new CellCategoryBuilder()
                .tdsFieldName(ItemScoreInfoAnalysisAction.EnumItemScoreInfoFieldName.scorePoint.toString())
                .tdsFieldNameValue("0")
                .tdsExpectedValue("0")
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .fieldEmpty(false)
                .correctDataType(true)
                .acceptableValue(true) 
                .correctValue(true)
                .correctWidth(true)
                .toCellCategory();

        Assert.assertEquals(expectedCellCategory, actualCellCategory);
        Assert.assertEquals("MS", actualScoreInfoCategory.getTdsFormat());
        
        actualCellCategory = getCellCategoryByFieldName(actualScoreInfoCategory.getCellCategories(), "scoreStatus");
		
        expectedCellCategory = new CellCategoryBuilder()
                .tdsFieldName(ItemScoreInfoAnalysisAction.EnumItemScoreInfoFieldName.scoreStatus.toString())
                .tdsFieldNameValue("Scored")
                .tdsExpectedValue("Scored")
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .fieldEmpty(false)
                .correctDataType(true)
                .acceptableValue(true) 
                .correctValue(true)
                .correctWidth(true)
                .toCellCategory();
        
        Assert.assertEquals(expectedCellCategory, actualCellCategory);
		
		
	}
	
	@Test
	public void whenTdsScorePointMatchItemScoreEngineScore_MachineScore() {
		
		// The TDS Report items
		final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
				new ItemAttributeBuilder().bankKey(1).key(1).format("HTQ").score("1").toOpportunityItem(), 
				new ItemAttributeBuilder().bankKey(2).key(2).format("MI").score("0").toOpportunityItem());
		
		// The TDS Report Item ScoreInfo
		final List<ScoreInfoType> tdsScoreInfoType = Lists.newArrayList(
				new ItemScoreInfoBuilder().scorePoint("0").maxScore("1").scoreDimension("overall").scoreStatus("Scored").toScoreInfoType(), 
				new ItemScoreInfoBuilder().scorePoint("0").maxScore("1").scoreDimension("overall").scoreStatus("Scored").toScoreInfoType());
		
		// Add above TDS Report responses to its corresponding TDS Report items
		int index = 0;
		for (TDSReport.Opportunity.Item itemTmp : tdsReportItems) {
			itemTmp.setScoreInfo(tdsScoreInfoType.get(index));
			index++;
		}
		
		// The List<ItemCategory> in OpportunityCategory
		final List<ItemCategory> itemCategories = Lists.newArrayList(
			new ItemCategoryBuilder().itemBankKeyKey("item-1-1").status(ItemStatusEnum.FOUND).itemFormatCorrect(true).toItemCategory(),
			new ItemCategoryBuilder().itemBankKeyKey("item-2-2").status(ItemStatusEnum.FOUND).itemFormatCorrect(true).toItemCategory());
	
		ItemCategory itemCategory;
		ResponseCategory responseCategory;
		
		// add a CellCategories to ItemCategory
		itemCategory = itemCategories.get(0);
		itemCategory.addCellCategory(new CellCategoryBuilder().tdsFieldName("bankKey").tdsFieldNameValue("1").toCellCategory());
		itemCategory.addCellCategory(new CellCategoryBuilder().tdsFieldName("key").tdsFieldNameValue("1").toCellCategory());
		itemCategory.addCellCategory(new CellCategoryBuilder().tdsFieldName("format").tdsFieldNameValue("HTQ").toCellCategory());
		
		// add a ResponseCategory to ItemCategory
		responseCategory = new ItemResponseCategoryBuilder()
			.scoringStatus(ScoringStatus.Scored).itemScore(0).toResponseCategory();
		itemCategory.setResponseCategory(responseCategory);
		
		// add a CellCategories to ItemCategory
		itemCategory = itemCategories.get(1);
		itemCategory.addCellCategory(new CellCategoryBuilder().tdsFieldName("bankKey").tdsFieldNameValue("2").toCellCategory());
		itemCategory.addCellCategory(new CellCategoryBuilder().tdsFieldName("key").tdsFieldNameValue("2").toCellCategory());
		itemCategory.addCellCategory(new CellCategoryBuilder().tdsFieldName("format").tdsFieldNameValue("MI").toCellCategory());
		
		// add a ResponseCategory to ItemCategory
		responseCategory = new ItemResponseCategoryBuilder()
			.scoringStatus(ScoringStatus.Scored).itemScore(0).toResponseCategory();
		itemCategory.setResponseCategory(responseCategory);
		
		final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems, itemCategories);	
		
		// Act
		underTest.analyze(individualResponse);
		
		// Assert
		OpportunityCategory actualOpportunityCategory = individualResponse.getOpportunityCategory();
		List<ItemCategory> actuaItemCategproes = actualOpportunityCategory.getItemCategories();
		
		ItemCategory actualItemCategory;
		ScoreInfoCategory actualScoreInfoCategory;
		CellCategory actualCellCategory;
		CellCategory expectedCellCategory;
		
		actualItemCategory = actuaItemCategproes.get(0);
		actualScoreInfoCategory = actualItemCategory.getScoreInfoCategory();
		
		actualCellCategory = getCellCategoryByFieldName(actualScoreInfoCategory.getCellCategories(), "scorePoint");
		
		// Setup expected results after analysis that indicates the field has an incorrect value
        expectedCellCategory = new CellCategoryBuilder()
                .tdsFieldName(ItemScoreInfoAnalysisAction.EnumItemScoreInfoFieldName.scorePoint.toString())
                .tdsFieldNameValue("0")
                .tdsExpectedValue("0")
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .fieldEmpty(false)
                .correctDataType(true)
                .acceptableValue(true) 
                .correctValue(true)
				.correctWidth(true)
                .toCellCategory();

        Assert.assertEquals(expectedCellCategory, actualCellCategory);
        Assert.assertEquals("HTQ", actualScoreInfoCategory.getTdsFormat());
        
        actualCellCategory = getCellCategoryByFieldName(actualScoreInfoCategory.getCellCategories(), "scoreStatus");
		
        expectedCellCategory = new CellCategoryBuilder()
                .tdsFieldName(ItemScoreInfoAnalysisAction.EnumItemScoreInfoFieldName.scoreStatus.toString())
                .tdsFieldNameValue("Scored")
                .tdsExpectedValue("Scored")
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .fieldEmpty(false)
                .correctDataType(true)
                .acceptableValue(true) 
                .correctValue(true)
                .correctWidth(true)
                .toCellCategory();
        
        Assert.assertEquals(expectedCellCategory, actualCellCategory);
        
     	actualItemCategory = actuaItemCategproes.get(1);
		actualScoreInfoCategory = actualItemCategory.getScoreInfoCategory();
		
		actualCellCategory = getCellCategoryByFieldName(actualScoreInfoCategory.getCellCategories(), "scorePoint");
		
		// Setup expected results after analysis that indicates the field has an incorrect value
        expectedCellCategory = new CellCategoryBuilder()
                .tdsFieldName(ItemScoreInfoAnalysisAction.EnumItemScoreInfoFieldName.scorePoint.toString())
                .tdsFieldNameValue("0")
                .tdsExpectedValue("0")
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .fieldEmpty(false)
                .correctDataType(true)
                .acceptableValue(true) 
                .correctValue(true)
                .correctWidth(true)
                .toCellCategory();

        Assert.assertEquals(expectedCellCategory, actualCellCategory);
        Assert.assertEquals("MI", actualScoreInfoCategory.getTdsFormat());
        
        actualCellCategory = getCellCategoryByFieldName(actualScoreInfoCategory.getCellCategories(), "scoreStatus");
		
        expectedCellCategory = new CellCategoryBuilder()
                .tdsFieldName(ItemScoreInfoAnalysisAction.EnumItemScoreInfoFieldName.scoreStatus.toString())
                .tdsFieldNameValue("Scored")
                .tdsExpectedValue("Scored")
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .fieldEmpty(false)
                .correctDataType(true)
                .acceptableValue(true) 
                .correctValue(true)
                .correctWidth(true)
                .toCellCategory();
        
        Assert.assertEquals(expectedCellCategory, actualCellCategory);
		
	}
	
	/**
	 * 
	 * @param cellCategories
	 *            The cellCategories store the list CellCategory
	 * @param key
	 *            The tdsFieldName used to retrieve tdsFieldValue in CellCategory
	 * @return the cellCategory object
	 */
	protected CellCategory getCellCategoryByFieldName(ImmutableList<CellCategory> cellCategories, String key) {
		for (CellCategory cellCategory : cellCategories) {
			if (cellCategory.getTdsFieldName().equals(key))
				return cellCategory;
		}
		return null;
	}

}
