package org.cresst.sb.irp.analysis.engine;

import builders.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import org.cresst.sb.irp.domain.analysis.*;
import org.cresst.sb.irp.domain.analysis.ItemCategory.ItemStatusEnum;
import org.cresst.sb.irp.domain.items.Itemrelease;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.itemscoring.client.IrpProxyItemScorer;
import org.cresst.sb.irp.itemscoring.client.converter.ItemScoreMessageConverter;
import org.cresst.sb.irp.itemscoring.rubric.MachineRubricLoader;
import org.cresst.sb.irp.service.ItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import tds.itemscoringengine.IItemScorer;
import tds.itemscoringengine.ItemScore;
import tds.itemscoringengine.ItemScoreInfo;
import tds.itemscoringengine.ResponseInfo;
import tds.itemscoringengine.RubricContentType;
import tds.itemscoringengine.ScoringStatus;

import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
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
	    assertEquals(true, fieldCheckType1.isFieldEmpty());
	    assertEquals(false, fieldCheckType1.isCorrectValue());
	    
	    //Response type existed with value blank
	    ItemCategory itemCategoryAfterAnalyze2 = itemCategoriesAfterAnalyze.get(1);
	    ResponseCategory responseCategory2 = itemCategoryAfterAnalyze2.getResponseCategory();
	    CellCategory cellCategory2 = getCellCategoryByFieldName(responseCategory2.getCellCategories(), "type");
	    FieldCheckType fieldCheckType2 = cellCategory2.getFieldCheckType();
	    
	    assertEquals("type",cellCategory2.getTdsFieldName());
	    assertEquals(" ", cellCategory2.getTdsFieldNameValue());
	    assertEquals("", cellCategory2.getTdsExpectedValue());
	    assertEquals(false, fieldCheckType2.isFieldEmpty());
	    assertEquals(true, fieldCheckType2.isCorrectValue());
	    
	    //Response type existed with value "value"
	    ItemCategory itemCategoryAfterAnalyze3 = itemCategoriesAfterAnalyze.get(2);
	    ResponseCategory responseCategory3 = itemCategoryAfterAnalyze3.getResponseCategory();
	    CellCategory cellCategory3 = getCellCategoryByFieldName(responseCategory3.getCellCategories(), "type");
	    FieldCheckType fieldCheckType3 = cellCategory3.getFieldCheckType();
	    
	    assertEquals("type",cellCategory3.getTdsFieldName());
	    assertEquals("value", cellCategory3.getTdsFieldNameValue());
	    assertEquals("value", cellCategory3.getTdsExpectedValue());
	    assertEquals(false, fieldCheckType3.isFieldEmpty());
	    assertEquals(true, fieldCheckType3.isCorrectValue());
	    
	    //Response type existed with value "reference"
	    ItemCategory itemCategoryAfterAnalyze4 = itemCategoriesAfterAnalyze.get(3);
	    ResponseCategory responseCategory4 = itemCategoryAfterAnalyze4.getResponseCategory();
	    CellCategory cellCategory4 = getCellCategoryByFieldName(responseCategory4.getCellCategories(), "type");
	    FieldCheckType fieldCheckType4 = cellCategory4.getFieldCheckType();
	    
	    assertEquals("type",cellCategory4.getTdsFieldName());
	    assertEquals("reference", cellCategory4.getTdsFieldNameValue());
	    assertEquals("reference", cellCategory4.getTdsExpectedValue());
	    assertEquals(false, fieldCheckType4.isFieldEmpty());
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
	    
	    assertEquals(true,responseCategory1.isResponseValid());
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
	//	underTest.analyze(individualResponse);
		
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
	    messageConverters.add(new ItemScoreMessageConverter());

	    RestTemplate restTemplate = new RestTemplate();
	    restTemplate.setMessageConverters(messageConverters);

	    IItemScorer proxyItemScorer = new IrpProxyItemScorer(restTemplate, "http://tds.smarterapp.cresst.net:8080/item-scoring-service/Scoring/ItemScoring");
		
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
	public void isResponseValidTest_TI_RubricContentTypeUri() {
		try {
			String response1 = " <responseSpec><responseTable><tr><th id=\"col0\"/><th id=\"col1\"/></tr><tr><td/><td>72</td></tr><tr><td/><td>42</td></tr><tr><td/><td>54</td></tr><tr><td/><td>50</td></tr></responseTable></responseSpec> ";
		
			List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
		    messageConverters.add(new ItemScoreMessageConverter());
	
		    RestTemplate restTemplate = new RestTemplate();
		    restTemplate.setMessageConverters(messageConverters);
	
		    IItemScorer proxyItemScorer = new IrpProxyItemScorer(restTemplate, "http://tds.smarterapp.cresst.net:8080/item-scoring-service/Scoring/ItemScoring");
			
		    String thePath = "file:///C:/Users/mzhang/Desktop/IRP/irp-webapp/src/main/resources/irp-package/IrpContentPackage/Items/Item-187-2788/Item_2788_v8.qrx";
		    thePath = URLEncoder.encode(thePath, "UTF-8" );
		    URI rubricUri = new URI (thePath);
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
		  } catch (Exception exp) {
		      exp.printStackTrace ();
		  }
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
