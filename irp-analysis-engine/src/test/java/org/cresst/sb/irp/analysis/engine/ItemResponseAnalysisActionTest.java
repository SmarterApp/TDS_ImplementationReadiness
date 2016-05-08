package org.cresst.sb.irp.analysis.engine;

import builders.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import org.cresst.sb.irp.domain.analysis.*;
import org.cresst.sb.irp.domain.analysis.ItemCategory.ItemStatusEnum;
import org.cresst.sb.irp.domain.items.Itemrelease;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.itemscoring.rubric.MachineRubricLoader;
import org.cresst.sb.irp.service.ItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tds.itemscoringengine.IItemScorer;
import tds.itemscoringengine.ItemScore;
import tds.itemscoringengine.ItemScoreInfo;
import tds.itemscoringengine.ResponseInfo;
import tds.itemscoringengine.RubricContentType;
import tds.itemscoringengine.ScoringStatus;

import java.net.URI;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Test class to verify that Response are analyzed correctly by ItemResponseAnalysisAction
 */
@RunWith(MockitoJUnitRunner.class)
public class ItemResponseAnalysisActionTest {
	private final static Logger logger = LoggerFactory.getLogger(ItemResponseAnalysisActionTest.class);


	@Mock
	private ItemService itemService;

	@Mock
	private IItemScorer itemScorer;

	@Mock
	private MachineRubricLoader machineRubricLoader;

	@InjectMocks
	private ItemAttributesAnalysisAction itemAttributesAnalysisAction = new ItemAttributesAnalysisAction();

	@InjectMocks
	private ItemResponseAnalysisAction underTest = new ItemResponseAnalysisAction(itemScorer, machineRubricLoader);

	private IndividualResponse generateIndividualResponse(
			List<TDSReport.Opportunity.Item> opportunityItems, List<ItemCategory> itemCategories) {

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

	/**
	 * test TDS Response type
	 * Response type not existed
	 * Response type existed with value blank
	 * Response type existed with value "value"
	 * Response type existed with value "reference"
	 */
	@Test
	public void testResponseType(){
		
		// The TDS Report items
		final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
				new ItemAttributeBuilder().bankKey(1).key(1).format("MC").score("1").toOpportunityItem(), 
				new ItemAttributeBuilder().bankKey(2).key(2).format("MS").score("1").toOpportunityItem(),
				new ItemAttributeBuilder().bankKey(3).key(3).format("MC").score("1").toOpportunityItem(), 
				new ItemAttributeBuilder().bankKey(4).key(4).format("MS").score("1").toOpportunityItem());

		// The TDS Report responses
		final List<TDSReport.Opportunity.Item.Response> tdsReportItemResponses = Lists.newArrayList(
				new ItemResponseBuilder().content("A").toIemResponse(), 
				new ItemResponseBuilder().type(" ").content("A,B,C,D,E,F").toIemResponse(),
				new ItemResponseBuilder().type("value").content("B").toIemResponse(),
				new ItemResponseBuilder().type("reference").content("A,B,C").toIemResponse());
		
		// Add above TDS Report responses to its corresponding TDS Report items
		int index = 0;
		for (TDSReport.Opportunity.Item itemTmp : tdsReportItems) {
			itemTmp.setResponse(tdsReportItemResponses.get(index));
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
		
		// add a CellCategories to ItemCategory 2
		ItemCategory itemCategory1 = itemCategories.get(1);
		itemCategory1.addCellCategory(new CellCategoryBuilder().tdsFieldName("bankKey").tdsFieldNameValue("2").toCellCategory());
		itemCategory1.addCellCategory(new CellCategoryBuilder().tdsFieldName("key").tdsFieldNameValue("2").toCellCategory());
		
		// add a CellCategories to ItemCategory 1
		ItemCategory itemCategory3 = itemCategories.get(2);
		itemCategory3.addCellCategory(new CellCategoryBuilder().tdsFieldName("bankKey").tdsFieldNameValue("3").toCellCategory());
		itemCategory3.addCellCategory(new CellCategoryBuilder().tdsFieldName("key").tdsFieldNameValue("3").toCellCategory());
		
		// add a CellCategories to ItemCategory 2
		ItemCategory itemCategory4 = itemCategories.get(3);
		itemCategory4.addCellCategory(new CellCategoryBuilder().tdsFieldName("bankKey").tdsFieldNameValue("4").toCellCategory());
		itemCategory4.addCellCategory(new CellCategoryBuilder().tdsFieldName("key").tdsFieldNameValue("4").toCellCategory());
		
		final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems, itemCategories);
		
		//IRP Item 
		org.cresst.sb.irp.domain.items.Itemrelease.Item item1 = new ItemreleaseItemBuilder("mc", "1", "26", "1").toItem();
		
		// Add attriblist (list of attrib) into above mentioned IRP item1 
		final List<org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib> attribs1 = Lists.newArrayList(
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("A").desc("").toAttrib(), 
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1 pt.").desc("").toAttrib());
		List<Itemrelease.Item.Attriblist> attribList1 = item1.getAttriblist();
		if (attribList1.size() == 0) {
			Itemrelease.Item.Attriblist attriblist = new Itemrelease.Item.Attriblist();
			attribList1.add(attriblist);
			attriblist.getAttrib().addAll(attribs1);
		}
		
		// Create another Item 
		org.cresst.sb.irp.domain.items.Itemrelease.Item item2 = new ItemreleaseItemBuilder("ms", "2", "26", "2").toItem();
		
		// Add attriblist (list of attrib) into above mentioned item2
		final List<org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib> attribs2 = Lists.newArrayList(
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("A,B,C,D,E,F").desc("").toAttrib(),
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1pt").desc("").toAttrib());
		List<Itemrelease.Item.Attriblist> attribList2 = item2.getAttriblist();
		if (attribList2.size() == 0) {
			Itemrelease.Item.Attriblist attriblist = new Itemrelease.Item.Attriblist();
			attribList2.add(attriblist);
			attriblist.getAttrib().addAll(attribs2);
		}
		
		// Create another Item 
		org.cresst.sb.irp.domain.items.Itemrelease.Item item3 = new ItemreleaseItemBuilder("mc", "3", "26", "3").toItem();
		
		// Add attriblist (list of attrib) into above mentioned IRP item1 
		final List<org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib> attribs3 = Lists.newArrayList(
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("B").desc("").toAttrib(), 
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1 pt.").desc("").toAttrib());
		List<Itemrelease.Item.Attriblist> attribList3 = item3.getAttriblist();
		if (attribList3.size() == 0) {
			Itemrelease.Item.Attriblist attriblist = new Itemrelease.Item.Attriblist();
			attribList3.add(attriblist);
			attriblist.getAttrib().addAll(attribs3);
		}
		
		// Create another Item 
		org.cresst.sb.irp.domain.items.Itemrelease.Item item4 = new ItemreleaseItemBuilder("ms", "4", "26", "4").toItem();
		
		// Add attriblist (list of attrib) into above mentioned item2
		final List<org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib> attribs4 = Lists.newArrayList(
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("A,B,C").desc("").toAttrib(),
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1pt").desc("").toAttrib());
		List<Itemrelease.Item.Attriblist> attribList4 = item4.getAttriblist();
		if (attribList4.size() == 0) {
			Itemrelease.Item.Attriblist attriblist = new Itemrelease.Item.Attriblist();
			attribList4.add(attriblist);
			attriblist.getAttrib().addAll(attribs4);
		}
		
		when(itemService.getItemByIdentifier("item-1-1")).thenReturn(item1);
		when(itemService.getItemByIdentifier("item-2-2")).thenReturn(item2);
		when(itemService.getItemByIdentifier("item-3-3")).thenReturn(item3);
		when(itemService.getItemByIdentifier("item-4-4")).thenReturn(item4);
		
		when(itemService.getItemAttriblistFromIRPitem(item1)).thenReturn(item1.getAttriblist().get(0)); //attribs1
		when(itemService.getItemAttriblistFromIRPitem(item2)).thenReturn(item2.getAttriblist().get(0)); //attribs2
		when(itemService.getItemAttriblistFromIRPitem(item3)).thenReturn(item3.getAttriblist().get(0)); //attribs3
		when(itemService.getItemAttriblistFromIRPitem(item4)).thenReturn(item4.getAttriblist().get(0)); //attribs4
		
		// Attrib for name of "itm_att_Answer Key"
		when(itemService.getItemAttribValueFromIRPitemAttriblist(item1.getAttriblist().get(0), "itm_att_Answer Key")).thenReturn(
				attribs1.get(0));
		// Attrib for name of "itm_att_Item Point"
		when(itemService.getItemAttribValueFromIRPitemAttriblist(item1.getAttriblist().get(0), "itm_att_Item Point")).thenReturn(
				attribs1.get(1));
		
		// Attrib for name of "itm_att_Answer Key"
		when(itemService.getItemAttribValueFromIRPitemAttriblist(item2.getAttriblist().get(0), "itm_att_Answer Key")).thenReturn(
				attribs2.get(0));
		// Attrib for name of "itm_att_Item Point"
		when(itemService.getItemAttribValueFromIRPitemAttriblist(item2.getAttriblist().get(0), "itm_att_Item Point")).thenReturn(
				attribs2.get(1));
		
		// Attrib for name of "itm_att_Answer Key"
		when(itemService.getItemAttribValueFromIRPitemAttriblist(item3.getAttriblist().get(0), "itm_att_Answer Key")).thenReturn(
				attribs3.get(0));
		// Attrib for name of "itm_att_Item Point"
		when(itemService.getItemAttribValueFromIRPitemAttriblist(item3.getAttriblist().get(0), "itm_att_Item Point")).thenReturn(
				attribs3.get(1));
		
		// Attrib for name of "itm_att_Answer Key"
		when(itemService.getItemAttribValueFromIRPitemAttriblist(item4.getAttriblist().get(0), "itm_att_Answer Key")).thenReturn(
				attribs4.get(0));
		// Attrib for name of "itm_att_Item Point"
		when(itemService.getItemAttribValueFromIRPitemAttriblist(item4.getAttriblist().get(0), "itm_att_Item Point")).thenReturn(
				attribs4.get(1));
		
		// Act
		underTest.analyze(individualResponse);
		
		// Assert
	    List<ItemCategory> itemCategoriesAfterAnalyze = individualResponse.getOpportunityCategory().getItemCategories();
	    
	    //Response type not existed
	    ItemCategory itemCategoryAfterAnalyze1 = itemCategoriesAfterAnalyze.get(0);
	    ResponseCategory responseCategory1 = itemCategoryAfterAnalyze1.getResponseCategory();
	    CellCategory cellCategory1 = getCellCategoryByFieldName(responseCategory1.getCellCategories(), "type");
	    FieldCheckType fieldCheckType1 = cellCategory1.getFieldCheckType();
	    
	    assertEquals("type",cellCategory1.getTdsFieldName());
	    assertEquals("", cellCategory1.getTdsFieldNameValue());
	    assertNull(cellCategory1.getTdsExpectedValue());
	    assertEquals(true, fieldCheckType1.isFieldValueEmpty());
	    assertEquals(false, fieldCheckType1.isCorrectValue());
	    
	    //Response type existed with value blank
	    ItemCategory itemCategoryAfterAnalyze2 = itemCategoriesAfterAnalyze.get(1);
	    ResponseCategory responseCategory2 = itemCategoryAfterAnalyze2.getResponseCategory();
	    CellCategory cellCategory2 = getCellCategoryByFieldName(responseCategory2.getCellCategories(), "type");
	    FieldCheckType fieldCheckType2 = cellCategory2.getFieldCheckType();
	    
	    assertEquals("type",cellCategory2.getTdsFieldName());
	    assertEquals(" ", cellCategory2.getTdsFieldNameValue());
	    assertEquals("", cellCategory2.getTdsExpectedValue());
	    assertEquals(false, fieldCheckType2.isFieldValueEmpty());
	    assertEquals(true, fieldCheckType2.isCorrectValue());
	    
	    //Response type existed with value "value"
	    ItemCategory itemCategoryAfterAnalyze3 = itemCategoriesAfterAnalyze.get(2);
	    ResponseCategory responseCategory3 = itemCategoryAfterAnalyze3.getResponseCategory();
	    CellCategory cellCategory3 = getCellCategoryByFieldName(responseCategory3.getCellCategories(), "type");
	    FieldCheckType fieldCheckType3 = cellCategory3.getFieldCheckType();
	    
	    assertEquals("type",cellCategory3.getTdsFieldName());
	    assertEquals("value", cellCategory3.getTdsFieldNameValue());
	    assertEquals("value", cellCategory3.getTdsExpectedValue());
	    assertEquals(false, fieldCheckType3.isFieldValueEmpty());
	    assertEquals(true, fieldCheckType3.isCorrectValue());
	    
	    //Response type existed with value "reference"
	    ItemCategory itemCategoryAfterAnalyze4 = itemCategoriesAfterAnalyze.get(3);
	    ResponseCategory responseCategory4 = itemCategoryAfterAnalyze4.getResponseCategory();
	    CellCategory cellCategory4 = getCellCategoryByFieldName(responseCategory4.getCellCategories(), "type");
	    FieldCheckType fieldCheckType4 = cellCategory4.getFieldCheckType();
	    
	    assertEquals("type",cellCategory4.getTdsFieldName());
	    assertEquals("reference", cellCategory4.getTdsFieldNameValue());
	    assertEquals("reference", cellCategory4.getTdsExpectedValue());
	    assertEquals(false, fieldCheckType4.isFieldValueEmpty());
	    assertEquals(true, fieldCheckType4.isCorrectValue());
		
	}
	
	/**
	 * test isValidStudentResponse of ResponseCategory
	 * test isCorrectValue of FieldCheckType of CellCategory(content) in ResponseCategory 
	 * format type = MS or = MC
	 * when TDS report item (bankKey & key) matching ItemCategory itemBankKeyKey and ItemStatusEnum.FOUND
	 * validate/parse item response
	 * TDS report item response format MC response "A", parse correct
	 * TDS report item response format MS response "A,B,C,D,E,F", parse correct 
	 */
	@Test
	public void isResponseValidTest_isFieldValueCorrect_MCMS() {
		
		// The TDS Report items
		final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
				new ItemAttributeBuilder().bankKey(1).key(1).format("MC").score("1").toOpportunityItem(), 
				new ItemAttributeBuilder().bankKey(2).key(2).format("MS").score("1").toOpportunityItem());

		// The TDS Report responses
		final List<TDSReport.Opportunity.Item.Response> tdsReportItemResponses = Lists.newArrayList(
				new ItemResponseBuilder().content("A").toIemResponse(), 
				new ItemResponseBuilder().content("A,B,C,D,E,F").toIemResponse());
		
		// Add above TDS Report responses to its corresponding TDS Report items
		int index = 0;
		for (TDSReport.Opportunity.Item itemTmp : tdsReportItems) {
			itemTmp.setResponse(tdsReportItemResponses.get(index));
			index++;
		}
		
		// The List<ItemCategory> in OpportunityCategory
		final List<ItemCategory> itemCategories = Lists.newArrayList(
			new ItemCategoryBuilder().itemBankKeyKey("item-1-1").status(ItemStatusEnum.FOUND).itemFormatCorrect(true).toItemCategory(),
			new ItemCategoryBuilder().itemBankKeyKey("item-2-2").status(ItemStatusEnum.FOUND).itemFormatCorrect(true).toItemCategory());
		
		// add 2 CellCategories to ItemCategory 1
		ItemCategory itemCategory0 = itemCategories.get(0);
		itemCategory0.addCellCategory(new CellCategoryBuilder().tdsFieldName("bankKey").tdsFieldNameValue("1").toCellCategory());
		itemCategory0.addCellCategory(new CellCategoryBuilder().tdsFieldName("key").tdsFieldNameValue("1").toCellCategory());
		
		// add 2 CellCategories to ItemCategory 2
		ItemCategory itemCategory1 = itemCategories.get(1);
		itemCategory1.addCellCategory(new CellCategoryBuilder().tdsFieldName("bankKey").tdsFieldNameValue("2").toCellCategory());
		itemCategory1.addCellCategory(new CellCategoryBuilder().tdsFieldName("key").tdsFieldNameValue("2").toCellCategory());
		
		final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems, itemCategories);
		
		//IRP Item 
		org.cresst.sb.irp.domain.items.Itemrelease.Item item1 = new ItemreleaseItemBuilder("mc", "1", "26", "1").toItem();
		
		// Add attriblist (list of attrib) into above mentioned IRP item1 
		final List<org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib> attribs1 = Lists.newArrayList(
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("A").desc("").toAttrib(),
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1 pt.").desc("").toAttrib());
		List<Itemrelease.Item.Attriblist> attribList1 = item1.getAttriblist();
		if (attribList1.size() == 0) {
			Itemrelease.Item.Attriblist attriblist = new Itemrelease.Item.Attriblist();
			attribList1.add(attriblist);
			attriblist.getAttrib().addAll(attribs1);
		}
		
		// Create another Item like item1 above
		org.cresst.sb.irp.domain.items.Itemrelease.Item item2 = new ItemreleaseItemBuilder("ms", "2", "26", "2").toItem();
		
		// Add attriblist (list of attrib) into above mentioned item2
		final List<org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib> attribs2 = Lists.newArrayList(
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("A,B,C,D,E,F").desc("").toAttrib(),
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1pt").desc("").toAttrib());
		List<Itemrelease.Item.Attriblist> attribList2 = item2.getAttriblist();
		if (attribList2.size() == 0) {
			Itemrelease.Item.Attriblist attriblist = new Itemrelease.Item.Attriblist();
			attribList2.add(attriblist);
			attriblist.getAttrib().addAll(attribs2);
		}
		
		when(itemService.getItemByIdentifier("item-1-1")).thenReturn(item1);
		when(itemService.getItemByIdentifier("item-2-2")).thenReturn(item2);
		
		when(itemService.getItemAttriblistFromIRPitem(item1)).thenReturn(item1.getAttriblist().get(0)); //attribs1
		when(itemService.getItemAttriblistFromIRPitem(item2)).thenReturn(item2.getAttriblist().get(0)); //attribs2
		
		// Attrib for name of "itm_att_Answer Key"
		when(itemService.getItemAttribValueFromIRPitemAttriblist(item1.getAttriblist().get(0), "itm_att_Answer Key")).thenReturn(
				attribs1.get(0));
		// Attrib for name of "itm_att_Item Point"
		when(itemService.getItemAttribValueFromIRPitemAttriblist(item1.getAttriblist().get(0), "itm_att_Item Point")).thenReturn(
				attribs1.get(1));
		
		// Attrib for name of "itm_att_Answer Key"
		when(itemService.getItemAttribValueFromIRPitemAttriblist(item2.getAttriblist().get(0), "itm_att_Answer Key")).thenReturn(
				attribs2.get(0));
		// Attrib for name of "itm_att_Item Point"
		when(itemService.getItemAttribValueFromIRPitemAttriblist(item2.getAttriblist().get(0), "itm_att_Item Point")).thenReturn(
				attribs2.get(1));
		
		// Act
		underTest.analyze(individualResponse);
		
		// Assert
	    List<ItemCategory> itemCategoriesAfterAnalyze = individualResponse.getOpportunityCategory().getItemCategories();
	    
	    ItemCategory itemCategoryAfterAnalyze1 = itemCategoriesAfterAnalyze.get(0);
	    ResponseCategory responseCategory1 = itemCategoryAfterAnalyze1.getResponseCategory();
	    CellCategory cellCategory1 = getCellCategoryByFieldName(responseCategory1.getCellCategories(), "content");
	    FieldCheckType fieldCheckType1 = cellCategory1.getFieldCheckType();
	    
	    assertEquals(true, responseCategory1.isResponseValid());
	    assertEquals(true,fieldCheckType1.isCorrectValue());
	    
        ItemCategory itemCategoryAfterAnalyze2 = itemCategoriesAfterAnalyze.get(1);
	    ResponseCategory responseCategory2 = itemCategoryAfterAnalyze2.getResponseCategory();
	    CellCategory cellCategory2 = getCellCategoryByFieldName(responseCategory2.getCellCategories(), "content");
	    FieldCheckType fieldCheckType2 = cellCategory2.getFieldCheckType();
	    
	    assertEquals(true,responseCategory2.isResponseValid());
	    assertEquals(true,fieldCheckType2.isCorrectValue());
	    
	}
	
	/**
	 * test isValidStudentResponse of ResponseCategory
	 * test isCorrectValue of FieldCheckType of CellCategory(content) in ResponseCategory 
	 * format type = MS or = MC
	 * when TDS report item (bankKey & key) matching ItemCategory itemBankKeyKey and ItemStatusEnum.FOUND
	 * validate/parse item response 
	 * TDS report item response format MC response "ABC", parse error
	 */
	@Test
	public void isResponseValidTest_MCMS2() {
		
		// The TDS Report items
		final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
				new ItemAttributeBuilder().bankKey(1).key(1).format("MC").score("0").toOpportunityItem(), 
				new ItemAttributeBuilder().bankKey(2).key(2).format("MS").score("1").toOpportunityItem());
		
		// The TDS Report responses
		final List<TDSReport.Opportunity.Item.Response> tdsReportItemResponses = Lists.newArrayList(
				new ItemResponseBuilder().content("ABC").toIemResponse(), 
				new ItemResponseBuilder().content("A,B,C,D,E,F").toIemResponse());

		// Add above TDS Report responses to its corresponding TDS Report items
		int index = 0;
		for (TDSReport.Opportunity.Item itemTmp : tdsReportItems) {
			itemTmp.setResponse(tdsReportItemResponses.get(index));
			index++;
		}
		
		// The List<ItemCategory> in OpportunityCategory
		final List<ItemCategory> itemCategories = Lists.newArrayList(
			new ItemCategoryBuilder().itemBankKeyKey("item-1-1").status(ItemStatusEnum.FOUND).itemFormatCorrect(true).toItemCategory(),
			new ItemCategoryBuilder().itemBankKeyKey("item-2-2").status(ItemStatusEnum.FOUND).itemFormatCorrect(true).toItemCategory());
		
		// add CellCategories to ItemCategory 1
		ItemCategory itemCategory0 = itemCategories.get(0);
		itemCategory0.addCellCategory(new CellCategoryBuilder().tdsFieldName("bankKey").tdsFieldNameValue("1").toCellCategory());
		itemCategory0.addCellCategory(new CellCategoryBuilder().tdsFieldName("key").tdsFieldNameValue("1").toCellCategory());
		
		// add CellCategories to ItemCategory 2
		ItemCategory itemCategory1 = itemCategories.get(1);
		itemCategory1.addCellCategory(new CellCategoryBuilder().tdsFieldName("bankKey").tdsFieldNameValue("2").toCellCategory());
		itemCategory1.addCellCategory(new CellCategoryBuilder().tdsFieldName("key").tdsFieldNameValue("2").toCellCategory());
		
		final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems, itemCategories);
		
		//IRP Item 
		org.cresst.sb.irp.domain.items.Itemrelease.Item item1 = new ItemreleaseItemBuilder("mc", "1", "26", "1").toItem();
		
		// Add attriblist (list of attrib) into above mentioned IRP item1 
		final List<org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib> attribs1 = Lists.newArrayList(
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("A").desc("").toAttrib(), 
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1 pt.").desc("").toAttrib());
		List<Itemrelease.Item.Attriblist> attribList1 = item1.getAttriblist();
		if (attribList1.size() == 0) {
			Itemrelease.Item.Attriblist attriblist = new Itemrelease.Item.Attriblist();
			attribList1.add(attriblist);
			attriblist.getAttrib().addAll(attribs1);
		}

		// Create another Item like item1 above
		org.cresst.sb.irp.domain.items.Itemrelease.Item item2 = new ItemreleaseItemBuilder("ms", "2", "26", "2").toItem();
		
		// Add attriblist (list of attrib) into above mentioned item2
		final List<org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib> attribs2 = Lists.newArrayList(
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("A,B,C,D,E,F").desc("").toAttrib(),
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1pt").desc("").toAttrib());
		List<Itemrelease.Item.Attriblist> attribList2 = item2.getAttriblist();
		if (attribList2.size() == 0) {
			Itemrelease.Item.Attriblist attriblist = new Itemrelease.Item.Attriblist();
			attribList2.add(attriblist);
			attriblist.getAttrib().addAll(attribs2);
		}
		
		when(itemService.getItemByIdentifier("item-1-1")).thenReturn(item1);
		when(itemService.getItemByIdentifier("item-2-2")).thenReturn(item2);
		
		when(itemService.getItemAttriblistFromIRPitem(item1)).thenReturn(item1.getAttriblist().get(0)); //attribs1
		when(itemService.getItemAttriblistFromIRPitem(item2)).thenReturn(item2.getAttriblist().get(0)); //attribs2
		
		// Attrib for name of "itm_att_Answer Key"
		when(itemService.getItemAttribValueFromIRPitemAttriblist(item1.getAttriblist().get(0), "itm_att_Answer Key")).thenReturn(
				attribs1.get(0));
		// Attrib for name of "itm_att_Item Point"
		when(itemService.getItemAttribValueFromIRPitemAttriblist(item1.getAttriblist().get(0), "itm_att_Item Point")).thenReturn(
				attribs1.get(1));
		
		// Attrib for name of "itm_att_Answer Key"
		when(itemService.getItemAttribValueFromIRPitemAttriblist(item2.getAttriblist().get(0), "itm_att_Answer Key")).thenReturn(
				attribs2.get(0));
		// Attrib for name of "itm_att_Item Point"
		when(itemService.getItemAttribValueFromIRPitemAttriblist(item2.getAttriblist().get(0), "itm_att_Item Point")).thenReturn(
				attribs2.get(1));
		
		// Act
		underTest.analyze(individualResponse);
		
		// Assert
	    List<ItemCategory> itemCategoriesAfterAnalyze = individualResponse.getOpportunityCategory().getItemCategories();
	    
	    ItemCategory itemCategoryAfterAnalyze1 = itemCategoriesAfterAnalyze.get(0);
	    ResponseCategory responseCategory1 = itemCategoryAfterAnalyze1.getResponseCategory();
	    CellCategory cellCategory1 = getCellCategoryByFieldName(responseCategory1.getCellCategories(), "content");
	    FieldCheckType fieldCheckType1 = cellCategory1.getFieldCheckType();
	    
	    assertEquals(false,responseCategory1.isResponseValid());
	    assertEquals(false,fieldCheckType1.isCorrectValue());
	    
        ItemCategory itemCategoryAfterAnalyze2 = itemCategoriesAfterAnalyze.get(1);
	    ResponseCategory responseCategory2 = itemCategoryAfterAnalyze2.getResponseCategory();
	    CellCategory cellCategory2 = getCellCategoryByFieldName(responseCategory2.getCellCategories(), "content");
	    FieldCheckType fieldCheckType2 = cellCategory2.getFieldCheckType();
	    
	    assertEquals(true,responseCategory2.isResponseValid());
	    assertEquals(true,fieldCheckType2.isCorrectValue());
		
	}
	
	@Test
	public void isResponseValidTest_HTQ_RubricContentTypeContentString() {
		
		String response1 = "<itemResponse><response id=\"1\"><value>2</value></response></itemResponse> ";
		
		// The TDS Report items
		final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
				new ItemAttributeBuilder().bankKey(187).key(2624).format("HTQ").score("0").toOpportunityItem());
		
		// The TDS Report responses
		final List<TDSReport.Opportunity.Item.Response> tdsReportItemResponses = Lists.newArrayList(
				new ItemResponseBuilder().content(response1).toIemResponse());
		
		// Add above TDS Report responses to its corresponding TDS Report items
		int index = 0;
		for (TDSReport.Opportunity.Item itemTmp : tdsReportItems) {
			itemTmp.setResponse(tdsReportItemResponses.get(index));
			index++;
		}
		
		// The List<ItemCategory> in OpportunityCategory
		final List<ItemCategory> itemCategories = Lists.newArrayList(
			new ItemCategoryBuilder().itemBankKeyKey("item-187-2624").status(ItemStatusEnum.FOUND).itemFormatCorrect(true).toItemCategory());
		
		// add CellCategories to ItemCategory 1
		ItemCategory itemCategory0 = itemCategories.get(0);
		itemCategory0.addCellCategory(new CellCategoryBuilder().tdsFieldName("bankKey").tdsFieldNameValue("187").toCellCategory());
		itemCategory0.addCellCategory(new CellCategoryBuilder().tdsFieldName("key").tdsFieldNameValue("2624").toCellCategory());
		
		final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems, itemCategories);
		
		//IRP Item 
		org.cresst.sb.irp.domain.items.Itemrelease.Item item1 = new ItemreleaseItemBuilder("htq", "2624", "3", "187").toItem();
		
		final List<Itemrelease.Item.MachineRubric> machineRubrics = Lists.newArrayList(
				new MachineRubricBuilder().filename("Item_2624_v3.qrx").toMachineRubric());
		List<Itemrelease.Item.MachineRubric> machineRubricList = item1.getMachineRubric();
		machineRubricList.addAll(machineRubrics);
		
		when(itemService.getItemByIdentifier("item-187-2624")).thenReturn(item1);
		
		String rubric = "<assessmentItem xmlns=\"http://www.imsglobal.org/xsd/apip/apipv1p0/qtiitem/imsqti_v2p1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.w3.org/1998/Math/MathML http://www.w3.org/Math/XMLSchema/mathml2/mathml2.xsd http://www.imsglobal.org/xsd/apip/apipv1p0/qtiitem/imsqti_v2p1 http://www.imsglobal.org/profile/apip/apipv1p0/apipv1p0_qtiitemv2p1_v1p0.xsd http://www.imsglobal.org/xsd/apip/apipv1p0/imsapip_qtiv1p0 http://www.imsglobal.org/profile/apip/apipv1p0/apipv1p0_qtiextv2p1_v1p0.xsd\" identifier=\"item_100870\" title=\"Item 100870\" adaptive=\"false\" timeDependent=\"false\" xmlns:apip=\"http://www.imsglobal.org/xsd/apip/apipv1p0/imsapip_qtiv1p0\"> " +
			 " <responseDeclaration identifier=\"1\" baseType=\"identifier\" cardinality=\"multiple\" /> " + 
			 " <outcomeDeclaration identifier=\"SCORE\" baseType=\"float\" cardinality=\"single\"> " + 
			  "  <defaultValue> " +
			    "  <value>0</value> " +
			   " </defaultValue> " +
			  " </outcomeDeclaration> " +
			  " <outcomeDeclaration identifier=\"FEEDBACK\" cardinality=\"single\" baseType=\"identifier\" /> " +
			  " <itemBody /> " +
			  " <responseProcessing> " +
			    " <responseCondition> " +
			      " <responseIf> " +
			        " <isNull> " +
			          " <variable identifier=\"1\" /> " +
			        " </isNull> " +
			        " <setOutcomeValue identifier=\"SCORE\"> " +
			          " <baseValue baseType=\"float\">0</baseValue> " +
			        " </setOutcomeValue> " +
			      " </responseIf> " +
			      " <responseElseIf> " +
			        " <and> " +
			          " <equal toleranceMode=\"exact\"> " +
			            " <baseValue baseType=\"integer\">2</baseValue> " +
			            " <containerSize> " +
			              " <variable identifier=\"1\" /> " +
			            " </containerSize> " +
			          " </equal> " +
			          " <contains> " +
			            " <multiple> " +
			              " <baseValue baseType=\"identifier\">5</baseValue> " +
			              " <baseValue baseType=\"identifier\">6</baseValue> " +
			            " </multiple> " +
			            " <variable identifier=\"1\" /> " +
			          " </contains> " +
			        " </and> " +
			        " <setOutcomeValue identifier=\"SCORE\"> " +
			          " <baseValue baseType=\"float\">1</baseValue> " +
			        " </setOutcomeValue> " +
			      " </responseElseIf> " +
			    " </responseCondition> " +
			  " </responseProcessing> " +
			  " <modalFeedback outcomeIdentifier=\"FEEDBACK\" showHide=\"show\" identifier=\"solution\"> " +
			    " <p id=\"Item100870_7abad020-c370-4910-a4d0-e966ddc85e1a\">T5 Wolves then raise lots of pups, and their numbers increase. T6 More wolves mean more mouths to feed and more moose get eaten.</p> " +
			  " </modalFeedback> " +
			  " <apip:apipAccessibility /> " +
			" </assessmentItem> ";
		when(machineRubricLoader.getContents("item-187-2624/Item_2624_v3.qrx")).thenReturn(rubric);
		
		// Act
		//underTest.analyze(individualResponse);
		
		IItemScorer proxyItemScorer = new IItemScorerBuilder()
			.restTemplate()
			.itemScoringServiceUrl("http://tds.smarterapp.cresst.net:8080/item-scoring-service/Scoring/ItemScoring")
			.toIItemScorer();
		
	    ResponseInfo responseInfo = new ResponseInfo(
	                "HTQ",
	                "2624",
	                response1,
	                rubric,
	                RubricContentType.ContentString,
	                "",
	                false);
	     
	    ItemScore itemScore = proxyItemScorer.ScoreItem(responseInfo, null);
        ItemScoreInfo itemScoreInfo = itemScore.getScoreInfo();

        logger.info("{} - {} - {}", itemScoreInfo.getStatus(), itemScoreInfo.getPoints(), itemScoreInfo.getRationale().getMsg());

        assertEquals(ScoringStatus.Scored, itemScoreInfo.getStatus());
        assertEquals(0, itemScoreInfo.getPoints());
	    
	}
	
	@Test
	public void isResponseValidTest_TI_RubricContentTypeContentString() {
		String response = " <responseSpec><responseTable><tr><th id=\"col0\"/><th id=\"col1\"/></tr><tr><td/><td>72</td></tr><tr><td/><td>42</td></tr><tr><td/><td>54</td></tr><tr><td/><td>50</td></tr></responseTable></responseSpec> ";
		
		IItemScorer proxyItemScorer = new IItemScorerBuilder()
			.restTemplate()
			.itemScoringServiceUrl("http://tds.smarterapp.cresst.net:8080/item-scoring-service/Scoring/ItemScoring")
			.toIItemScorer();
	    
		String rubric = "<assessmentItem xmlns=\"http://www.imsglobal.org/xsd/imsqti_v2p1\" identifier=\"\" title=\"\" timeDependent=\"false\"><responseDeclaration baseType=\"string\" cardinality=\"single\" identifier=\"RESPONSE\" /><outcomeDeclaration baseType=\"integer\" cardinality=\"single\" identifier=\"SCORE\"><defaultValue><value>0</value></defaultValue></outcomeDeclaration><outcomeDeclaration identifier=\"colA\" baseType=\"string\" cardinality=\"single\" /><outcomeDeclaration identifier=\"A0\" baseType=\"float\" cardinality=\"single\" /><outcomeDeclaration identifier=\"A1\" baseType=\"float\" cardinality=\"single\" /><outcomeDeclaration identifier=\"A2\" baseType=\"float\" cardinality=\"single\" /><outcomeDeclaration identifier=\"A3\" baseType=\"float\" cardinality=\"single\" /><responseProcessing><setOutcomeValue identifier=\"colA\"><customOperator type=\"TABLE\" functionName=\"GETCOLUMN\" table=\"#\" columnName=\"col1\" /></setOutcomeValue><setOutcomeValue identifier=\"A0\"><customOperator type=\"TABLE\" functionName=\"GETVALUENUMERIC\" tableVector=\"colA\" index=\"0\" /></setOutcomeValue><setOutcomeValue identifier=\"A1\"><customOperator type=\"TABLE\" functionName=\"GETVALUENUMERIC\" tableVector=\"colA\" index=\"1\" /></setOutcomeValue><setOutcomeValue identifier=\"A2\"><customOperator type=\"TABLE\" functionName=\"GETVALUENUMERIC\" tableVector=\"colA\" index=\"2\" /></setOutcomeValue><setOutcomeValue identifier=\"A3\"><customOperator type=\"TABLE\" functionName=\"GETVALUENUMERIC\" tableVector=\"colA\" index=\"3\" /></setOutcomeValue><responseCondition><responseIf><and><equal><variable identifier=\"A0\" /><baseValue baseType=\"float\">72</baseValue></equal><equal><variable identifier=\"A1\" /><baseValue baseType=\"float\">42</baseValue></equal><equal><variable identifier=\"A2\" /><baseValue baseType=\"float\">54</baseValue></equal><equal><variable identifier=\"A3\" /><baseValue baseType=\"float\">50</baseValue></equal></and><setOutcomeValue identifier=\"SCORE\"><baseValue baseType=\"integer\">1</baseValue></setOutcomeValue></responseIf></responseCondition></responseProcessing></assessmentItem>";
	    
	    ResponseInfo responseInfo = new ResponseInfo(
                "TI",
                "2788",
                response,
                rubric,
                RubricContentType.ContentString,
                "",
                false);
     
	    ItemScore itemScore = proxyItemScorer.ScoreItem(responseInfo, null);
	    ItemScoreInfo itemScoreInfo = itemScore.getScoreInfo();
	
	    logger.info("{} - {} - {}", itemScoreInfo.getStatus(), itemScoreInfo.getPoints(), itemScoreInfo.getRationale().getMsg());
	
	    assertEquals(ScoringStatus.Scored, itemScoreInfo.getStatus());
	    assertEquals(1, itemScoreInfo.getPoints());
	}
	
	@Test
	public void isResponseValidTest_HTQ_RubricContentTypeContentString_WithoutRubirc() {
		
		String response1 = "<itemResponse><response id=\"1\"><value>2</value></response></itemResponse> ";
		
		// The TDS Report items
		final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
				new ItemAttributeBuilder().bankKey(187).key(2624).format("HTQ").score("0").toOpportunityItem());
		
		// The TDS Report responses
		final List<TDSReport.Opportunity.Item.Response> tdsReportItemResponses = Lists.newArrayList(
				new ItemResponseBuilder().content(response1).toIemResponse());
		
		// Add above TDS Report responses to its corresponding TDS Report items
		int index = 0;
		for (TDSReport.Opportunity.Item itemTmp : tdsReportItems) {
			itemTmp.setResponse(tdsReportItemResponses.get(index));
			index++;
		}
		
		// The List<ItemCategory> in OpportunityCategory
		final List<ItemCategory> itemCategories = Lists.newArrayList(
			new ItemCategoryBuilder().itemBankKeyKey("item-187-2624").status(ItemStatusEnum.FOUND).itemFormatCorrect(true).toItemCategory());
		
		// add CellCategories to ItemCategory 1
		ItemCategory itemCategory0 = itemCategories.get(0);
		itemCategory0.addCellCategory(new CellCategoryBuilder().tdsFieldName("bankKey").tdsFieldNameValue("187").toCellCategory());
		itemCategory0.addCellCategory(new CellCategoryBuilder().tdsFieldName("key").tdsFieldNameValue("2624").toCellCategory());
		
		final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems, itemCategories);
		
		//IRP Item 
		org.cresst.sb.irp.domain.items.Itemrelease.Item item1 = new ItemreleaseItemBuilder("htq", "2624", "3", "187").toItem();
		
		final List<Itemrelease.Item.MachineRubric> machineRubrics = Lists.newArrayList(
				new MachineRubricBuilder().filename("Item_2624_v3.qrx").toMachineRubric());
		List<Itemrelease.Item.MachineRubric> machineRubricList = item1.getMachineRubric();
		//machineRubricList.addAll(machineRubrics);
		
		when(itemService.getItemByIdentifier("item-187-2624")).thenReturn(item1);
		
		// Act
		underTest.analyze(individualResponse);
	     
		// Assert
	    List<ItemCategory> itemCategoriesAfterAnalyze = individualResponse.getOpportunityCategory().getItemCategories();
	    
	    ItemCategory itemCategoryAfterAnalyze1 = itemCategoriesAfterAnalyze.get(0);
	    ResponseCategory responseCategory1 = itemCategoryAfterAnalyze1.getResponseCategory();
	 
	    
	    assertEquals(true,responseCategory1.isRubricMissing());
	}
	
	@Test
	public void isResponseValidTest_EQ_RubricContentTypeContentString() {
		String response = " <response><math xmlns=\"http://www.w3.org/1998/Math/MathML\" title=\"218\"><mstyle><mn>218</mn></mstyle></math></response> ";
		
		IItemScorer proxyItemScorer = new IItemScorerBuilder()
			.restTemplate()
			.itemScoringServiceUrl("http://tds.smarterapp.cresst.net:8080/item-scoring-service/Scoring/ItemScoring")
			.toIItemScorer();
	    
		String rubric = "<assessmentItem xmlns=\"http://www.imsglobal.org/xsd/imsqti_v2p1\" identifier=\"\" title=\"\" timeDependent=\"false\"><responseDeclaration baseType=\"string\" cardinality=\"single\" identifier=\"RESPONSE\" /><outcomeDeclaration baseType=\"integer\" cardinality=\"single\" identifier=\"SCORE\"><defaultValue><value>0</value></defaultValue></outcomeDeclaration><outcomeDeclaration baseType=\"string\" cardinality=\"ordered\" identifier=\"PP_RESPONSE\" /><responseProcessing><setOutcomeValue identifier=\"PP_RESPONSE\"><customOperator type=\"EQ\" functionName=\"PREPROCESSRESPONSE\" response=\"RESPONSE\" /></setOutcomeValue></responseProcessing></assessmentItem>";
		
	    ResponseInfo responseInfo = new ResponseInfo(
                "EQ",
                "1576",
                response,
                rubric,
                RubricContentType.ContentString,
                "",
                false);
     
	    ItemScore itemScore = proxyItemScorer.ScoreItem(responseInfo, null);
	    ItemScoreInfo itemScoreInfo = itemScore.getScoreInfo();
	
	    logger.info("{} - {} - {}", itemScoreInfo.getStatus(), itemScoreInfo.getPoints(), itemScoreInfo.getRationale().getMsg());
	
	    assertEquals(ScoringStatus.Scored, itemScoreInfo.getStatus());
	    assertEquals(0, itemScoreInfo.getPoints());
	}
	
	@Test
	public void isResponseValidTest_GI_RubricContentTypeContentString() {
	
		String response = " <?xml version=\"1.0\" encoding=\"UTF-8\"?><AnswerSet><Question id=\"\"><QuestionPart id=\"1\"><ObjectSet><RegionGroupObject name=\"Key\" numselected=\"1\"><RegionObject name=\"4cups\" isselected=\"false\"/><RegionObject name=\"10cups\" isselected=\"true\"/><RegionObject name=\"20cups\" isselected=\"false\"/></RegionGroupObject><RegionGroupObject name=\"WeekOne\" numselected=\"1\"><RegionObject name=\"WeekOneHalf\" isselected=\"false\"/><RegionObject name=\"WeekOne1\" isselected=\"false\"/><RegionObject name=\"WeekOne1Half\" isselected=\"false\"/><RegionObject name=\"WeekOne2\" isselected=\"false\"/><RegionObject name=\"WeekOne2Half\" isselected=\"false\"/><RegionObject name=\"WeekOne3\" isselected=\"false\"/><RegionObject name=\"WeekOne3Half\" isselected=\"false\"/><RegionObject name=\"WeekOne4\" isselected=\"false\"/><RegionObject name=\"WeekOne4Half\" isselected=\"false\"/><RegionObject name=\"WeekOne5\" isselected=\"true\"/><RegionObject name=\"WeekOne5Half\" isselected=\"false\"/><RegionObject name=\"WeekOne6\" isselected=\"false\"/><RegionObject name=\"WeekOne6Half\" isselected=\"false\"/><RegionObject name=\"WeekOne7\" isselected=\"false\"/><RegionObject name=\"WeekOne7Half\" isselected=\"false\"/><RegionObject name=\"WeekOne8\" isselected=\"false\"/></RegionGroupObject><RegionGroupObject name=\"WeekTwo\" numselected=\"1\"><RegionObject name=\"WeekTwoHalf\" isselected=\"false\"/><RegionObject name=\"WeekTwo1\" isselected=\"false\"/><RegionObject name=\"WeekTwo1Half\" isselected=\"false\"/><RegionObject name=\"WeekTwo2\" isselected=\"false\"/><RegionObject name=\"WeekTwo2Half\" isselected=\"false\"/><RegionObject name=\"WeekTwo3\" isselected=\"false\"/><RegionObject name=\"WeekTwo3Half\" isselected=\"false\"/><RegionObject name=\"WeekTwo4\" isselected=\"false\"/><RegionObject name=\"WeekTwo4Half\" isselected=\"false\"/><RegionObject name=\"WeekTwo5\" isselected=\"false\"/><RegionObject name=\"WeekTwo5Half\" isselected=\"false\"/><RegionObject name=\"WeekTwo6\" isselected=\"true\"/><RegionObject name=\"WeekTwo6Half\" isselected=\"false\"/><RegionObject name=\"WeekTwo7\" isselected=\"false\"/><RegionObject name=\"WeekTwo7Half\" isselected=\"false\"/><RegionObject name=\"WeekTwo8\" isselected=\"false\"/></RegionGroupObject><RegionGroupObject name=\"WeekThree\" numselected=\"1\"><RegionObject name=\"WeekThreeHalf\" isselected=\"false\"/><RegionObject name=\"WeekThree1\" isselected=\"false\"/><RegionObject name=\"WeekThree1Half\" isselected=\"false\"/><RegionObject name=\"WeekThree2\" isselected=\"false\"/><RegionObject name=\"WeekThree2Half\" isselected=\"false\"/><RegionObject name=\"WeekThree3\" isselected=\"false\"/><RegionObject name=\"WeekThree3Half\" isselected=\"false\"/><RegionObject name=\"WeekThree4\" isselected=\"false\"/><RegionObject name=\"WeekThree4Half\" isselected=\"false\"/><RegionObject name=\"WeekThree5\" isselected=\"false\"/><RegionObject name=\"WeekThree5Half\" isselected=\"false\"/><RegionObject name=\"WeekThree6\" isselected=\"false\"/><RegionObject name=\"WeekThree6Half\" isselected=\"false\"/><RegionObject name=\"WeekThree7\" isselected=\"false\"/><RegionObject name=\"WeekThree7Half\" isselected=\"false\"/><RegionObject name=\"WeekThree8\" isselected=\"true\"/></RegionGroupObject><RegionGroupObject name=\"WeekFour\" numselected=\"1\"><RegionObject name=\"WeekFourHalf\" isselected=\"false\"/><RegionObject name=\"WeekFour1\" isselected=\"false\"/><RegionObject name=\"WeekFour1Half\" isselected=\"false\"/><RegionObject name=\"WeekFour2\" isselected=\"false\"/><RegionObject name=\"WeekFour2Half\" isselected=\"true\"/><RegionObject name=\"WeekFour3\" isselected=\"false\"/><RegionObject name=\"WeekFour3Half\" isselected=\"false\"/><RegionObject name=\"WeekFour4\" isselected=\"false\"/><RegionObject name=\"WeekFour4Half\" isselected=\"false\"/><RegionObject name=\"WeekFour5\" isselected=\"false\"/><RegionObject name=\"WeekFour5Half\" isselected=\"false\"/><RegionObject name=\"WeekFour6\" isselected=\"false\"/><RegionObject name=\"WeekFour6Half\" isselected=\"false\"/><RegionObject name=\"WeekFour7\" isselected=\"false\"/><RegionObject name=\"WeekFour7Half\" isselected=\"false\"/><RegionObject name=\"WeekFour8\" isselected=\"false\"/></RegionGroupObject></ObjectSet><SnapPoint></SnapPoint></QuestionPart></Question></AnswerSet> ";
		
		IItemScorer proxyItemScorer = new IItemScorerBuilder()
			.restTemplate()
			.itemScoringServiceUrl("http://tds.smarterapp.cresst.net:8080/item-scoring-service/Scoring/ItemScoring")
			.toIItemScorer();
		
		String rubric = "<assessmentItem xmlns=\"http://www.imsglobal.org/xsd/imsqti_v2p1\" identifier=\"\" title=\"\" timeDependent=\"false\"><responseDeclaration baseType=\"string\" cardinality=\"single\" identifier=\"RESPONSE\" /><outcomeDeclaration baseType=\"integer\" cardinality=\"single\" identifier=\"SCORE\"><defaultValue><value>0</value></defaultValue></outcomeDeclaration><outcomeDeclaration baseType=\"string\" cardinality=\"ordered\" identifier=\"PP_RESPONSE\" /><responseProcessing><setOutcomeValue identifier=\"PP_RESPONSE\"><customOperator type=\"GRAPHIC\" functionName=\"PREPROCESSRESPONSE\" response=\"RESPONSE\" /></setOutcomeValue></responseProcessing></assessmentItem>";
		
	    ResponseInfo responseInfo = new ResponseInfo(
                "GI",
                "1578",
                response,
                rubric,
                RubricContentType.ContentString,
                "",
                false);
	     
	    ItemScore itemScore = proxyItemScorer.ScoreItem(responseInfo, null);
        ItemScoreInfo itemScoreInfo = itemScore.getScoreInfo();

        logger.info("{} - {} - {}", itemScoreInfo.getStatus(), itemScoreInfo.getPoints(), itemScoreInfo.getRationale().getMsg());

        assertEquals(ScoringStatus.Scored, itemScoreInfo.getStatus());
        assertEquals(0, itemScoreInfo.getPoints());
	}
	
	@Test
	public void isResponseValidTest_MI_RubricContentTypeContentString() {
	
		String response = " <itemResponse><response id=\"RESPONSE\"><value>1 a</value><value>2 a</value><value>3 b</value><value>4 a</value><value>5 b</value></response></itemResponse> ";
		
		IItemScorer proxyItemScorer = new IItemScorerBuilder()
			.restTemplate()
			.itemScoringServiceUrl("http://tds.smarterapp.cresst.net:8080/item-scoring-service/Scoring/ItemScoring")
			.toIItemScorer();
		
		String rubric = "<assessmentItem xmlns=\"http://www.imsglobal.org/xsd/apip/apipv1p0/qtiitem/imsqti_v2p1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.w3.org/1998/Math/MathML http://www.w3.org/Math/XMLSchema/mathml2/mathml2.xsd http://www.imsglobal.org/xsd/apip/apipv1p0/qtiitem/imsqti_v2p1 http://www.imsglobal.org/profile/apip/apipv1p0/apipv1p0_qtiitemv2p1_v1p0.xsd http://www.imsglobal.org/xsd/apip/apipv1p0/imsapip_qtiv1p0 http://www.imsglobal.org/profile/apip/apipv1p0/apipv1p0_qtiextv2p1_v1p0.xsd\" identifier=\"item_64335\" title=\"Item 64335\" adaptive=\"false\" timeDependent=\"false\" xmlns:apip=\"http://www.imsglobal.org/xsd/apip/apipv1p0/imsapip_qtiv1p0\"> " +
			" <responseDeclaration identifier=\"RESPONSE\" baseType=\"directedPair\" cardinality=\"multiple\"> " +
			" <correctResponse> " + 
			" <value>1 a</value> " +
			" <value>5 a</value> " +
			" <value>2 a</value> " +
			" <value>3 b</value> " +
			" <value>4 b</value> " +
			" </correctResponse> " +
			" </responseDeclaration> " +
			" <outcomeDeclaration identifier=\"SCORE\" baseType=\"float\" cardinality=\"single\" /> " +
			" <itemBody /> " +
			" <responseProcessing>   <responseCondition>       <responseIf>          <match>                <variable identifier=\"RESPONSE\"/>                <correct identifier=\"RESPONSE\"/>            </match>            <setOutcomeValue identifier=\"SCORE\">                <baseValue baseType=\"float\">1</baseValue>            </setOutcomeValue>        </responseIf>        <responseElse>           <setOutcomeValue identifier=\"SCORE\">                <baseValue baseType=\"float\">0</baseValue>            </setOutcomeValue>        </responseElse>    </responseCondition> </responseProcessing> " + 
			" <apip:apipAccessibility /> " +
			" </assessmentItem> ";
	    ResponseInfo responseInfo = new ResponseInfo(
                "MI",
                "1873",
                response,
                rubric,
                RubricContentType.ContentString,
                "",
                false);
	     
	    ItemScore itemScore = proxyItemScorer.ScoreItem(responseInfo, null);
        ItemScoreInfo itemScoreInfo = itemScore.getScoreInfo();

        logger.info("{} - {} - {}", itemScoreInfo.getStatus(), itemScoreInfo.getPoints(), itemScoreInfo.getRationale().getMsg());

        assertEquals(ScoringStatus.Scored, itemScoreInfo.getStatus());
        assertEquals(0, itemScoreInfo.getPoints());
	}
	
	@Test
	public void isResponseValidTest_EBSR_RubricContentTypeContentString() {
	
		String response = " <itemResponse><response id=\"EBSR1\"><value>B</value></response><response id=\"EBSR2\"><value>A</value><value>C</value></response></itemResponse> ";
		
		IItemScorer proxyItemScorer = new IItemScorerBuilder()
			.restTemplate()
			.itemScoringServiceUrl("http://tds.smarterapp.cresst.net:8080/item-scoring-service/Scoring/ItemScoring")
			.toIItemScorer();
		
		String rubric = "﻿<assessmentItem xmlns=\"http://www.imsglobal.org/xsd/apip/apipv1p0/qtiitem/imsqti_v2p1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.w3.org/1998/Math/MathML http://www.w3.org/Math/XMLSchema/mathml2/mathml2.xsd http://www.imsglobal.org/xsd/apip/apipv1p0/qtiitem/imsqti_v2p1 http://www.imsglobal.org/profile/apip/apipv1p0/apipv1p0_qtiitemv2p1_v1p0.xsd http://www.imsglobal.org/xsd/apip/apipv1p0/imsapip_qtiv1p0 http://www.imsglobal.org/profile/apip/apipv1p0/apipv1p0_qtiextv2p1_v1p0.xsd\" identifier=\"item_2616\" title=\"Item 2616\" adaptive=\"false\" timeDependent=\"false\" xmlns:apip=\"http://www.imsglobal.org/xsd/apip/apipv1p0/imsapip_qtiv1p0\"> " +
                    " <responseDeclaration identifier=\"EBSR1\" baseType=\"identifier\" cardinality=\"single\"> " +
                        " <correctResponse> " +
                                " <value>A</value> " +

                        " </correctResponse> " +
                    " </responseDeclaration> " +
                    " <responseDeclaration identifier=\"EBSR2\" baseType=\"identifier\" > " +
                        " <correctResponse> " +
                                
                        " </correctResponse> " +
                    " </responseDeclaration> " +
                    " <outcomeDeclaration identifier=\"SCORE\" baseType=\"float\" cardinality=\"single\" /> " +
                    " <outcomeDeclaration identifier=\"FEEDBACK\" cardinality=\"single\" baseType=\"identifier\" /> " +
                    " <itemBody /> " +
                    " <responseProcessing> " +
                    " <responseCondition> " +
                    " <responseIf> " +
	                    " <and> " +
		                    " <match> " +
			                    " <variable identifier=\"EBSR1\"/> " +
			                    " <correct identifier=\"EBSR1\"/> " +
		                    " </match> " +
		                    " <match> " +
			                    " <variable identifier=\"EBSR2\"/> " +
			                    " <correct identifier=\"EBSR2\"/> " +
		                    " </match> " +
	                    " </and> " +
                    " <setOutcomeValue identifier=\"SCORE\"> " +
                      " <baseValue baseType=\"float\">1</baseValue> " +
                    " </setOutcomeValue> " +
                    " </responseIf> " +
                    " <responseElse> " +
	                    " <setOutcomeValue identifier=\"SCORE\"> " +
		                    " <baseValue baseType=\"float\">0</baseValue> " +
	                    " </setOutcomeValue> " +
                    " </responseElse> " +
                    " </responseCondition> " +
                    " </responseProcessing> " +
                     " <modalFeedback outcomeIdentifier=\"FEEDBACK\" showHide=\"show\" identifier=\"solution\"> " +
                       " <p id=\"\" /> " +
                     " </modalFeedback> " +
                     " <apip:apipAccessibility /> " +
                " </assessmentItem>";
				
	    ResponseInfo responseInfo = new ResponseInfo(
                "EBSR",
                "2616",
                response,
                rubric,
                RubricContentType.ContentString,
                "",
                false);
	     
	    ItemScore itemScore = proxyItemScorer.ScoreItem(responseInfo, null);
        ItemScoreInfo itemScoreInfo = itemScore.getScoreInfo();

        logger.info("{} - {} - {}", itemScoreInfo.getStatus(), itemScoreInfo.getPoints(), itemScoreInfo.getRationale().getMsg());

        assertEquals(ScoringStatus.NotScored, itemScoreInfo.getStatus());
        assertEquals(-1, itemScoreInfo.getPoints());
	}
	
	
	/**
	 * RubricContentType Uri
	 */
	@Test
	public void isResponseValidTest_TI_RubricContentTypeUri() throws Exception {
		String response1 = " <responseSpec><responseTable><tr><th id=\"col0\"/><th id=\"col1\"/></tr><tr><td/><td>72</td></tr><tr><td/><td>42</td></tr><tr><td/><td>54</td></tr><tr><td/><td>50</td></tr></responseTable></responseSpec> ";

		IItemScorer proxyItemScorer = new IItemScorerBuilder()
			.restTemplate()
			.itemScoringServiceUrl("http://tds.smarterapp.cresst.net:8080/item-scoring-service/Scoring/ItemScoring")
			.toIItemScorer();

		String thePath = "file:////usr/local/tomcat/resources/tds/bank/Items/Item-187-2788/Item_2788_v8.qrx";
		URI rubricUri = new URI(thePath);
		ResponseInfo responseInfo = new ResponseInfo(
					"TI",
					"2788",
					response1,
					rubricUri,
					RubricContentType.Uri,
					"",
					false);

		ItemScore itemScore = proxyItemScorer.ScoreItem(responseInfo, null);
		ItemScoreInfo itemScoreInfo = itemScore.getScoreInfo();

		logger.info("{} - {} - {}", itemScoreInfo.getStatus(), itemScoreInfo.getPoints(), itemScoreInfo.getRationale().getMsg());

		assertEquals(ScoringStatus.Scored, itemScoreInfo.getStatus());
		assertEquals(1, itemScoreInfo.getPoints());
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
