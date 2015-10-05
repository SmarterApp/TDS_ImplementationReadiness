package org.cresst.sb.irp.analysis.engine;

import builders.*;
import com.google.common.collect.Lists;
import org.cresst.sb.irp.domain.analysis.*;
import org.cresst.sb.irp.domain.items.Itemrelease;
import org.cresst.sb.irp.domain.studentresponse.StudentResponse;
import org.cresst.sb.irp.domain.studentresponse.TestItemResponse;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.itemscoring.rubric.MachineRubricLoader;
import org.cresst.sb.irp.service.ItemService;
import org.cresst.sb.irp.service.StudentResponseService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import tds.itemscoringengine.IItemScorer;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Test class to verify that Response are analyzed correctly by ItemResponseAnalysisAction
 */
@RunWith(MockitoJUnitRunner.class)
public class ItemResponseAnalysisActionTest {

	@Mock
	private StudentResponseService studentResponseService;

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

	private IndividualResponse generateIndividualResponse(List<TDSReport.Opportunity.Item> opportunityItems) {

		final TDSReport.Opportunity opportunity = new TDSReport.Opportunity();

		if (opportunityItems != null) {
			opportunity.getItem().addAll(opportunityItems);
		}

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

		return individualResponse;
	}

	/**
	 * test isValidStudentResponse method in ItemResponseAnalysisAction.java
	 * format type = MS or = MC
	 * when TDS report item exist (bankKey & key) in IRP package (Excel)
	 * and when the format match IRP Package (Excel)
	 * validate/parse item response
	 * TDS report item response format MC response "A", parse correct
	 * TDS report item response format MS response "A,B,C,D,E,F", parse correct 
	 */
	@Test
	public void isResponseValidTest_MCMS() {
		
		// Represents all the Items in the IRP package (Excel)
		List<StudentResponse> irpPackageItems = Lists.newArrayList(
				new StudentResponseBuilder(9999).bankKey(1).id(1).itemType("MC").excelResponse("A").toStudentResponse(),
				new StudentResponseBuilder(9999).bankKey(2).id(2).itemType("MS").excelResponse("A,B,C,D,E,F").toStudentResponse());

		// The TDS Report items
		final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
				new ItemAttributeBuilder().bankKey(1).key(1).format("MC").toOpportunityItem(), 
				new ItemAttributeBuilder().bankKey(2).key(2).format("MS").toOpportunityItem());

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

		final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems);

		// Represents list of StudentResponse for a particular studentID in IRP package (Excel)
		TestItemResponse testItemResponse = new TestItemResponse();
		testItemResponse.setStudentID(9999L);
		testItemResponse.setStudentResponses(irpPackageItems);

		when(studentResponseService.getTestItemResponseByStudentID(9999L)).thenReturn(testItemResponse);

		itemAttributesAnalysisAction.analyze(individualResponse);

		// IRP package (Excel) StudentResponse object
		StudentResponse StudentResponse1 = irpPackageItems.get(0);
		StudentResponse StudentResponse2 = irpPackageItems.get(1);

		when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "1", "1")).thenReturn(StudentResponse1);
		when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "2", "2")).thenReturn(StudentResponse2);

		// Item created like item retrieved item-xxx-xxx.xml from TrainingTestContent/Items/Item-xxx-xxx based on imsmanifest.xml
		org.cresst.sb.irp.domain.items.Itemrelease.Item item1 = new ItemreleaseItemBuilder("mc", "1", "26", "1").toItem();
		
		// Add attriblist (list of attrib) into above mentioned item1 
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
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1 pt.").desc("").toAttrib());
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
		// Attrib for name of "itm_att_Answer Key"
		when(itemService.getItemAttribValueFromIRPitemAttriblist(item2.getAttriblist().get(0), "itm_att_Answer Key")).thenReturn(
				attribs2.get(0));

		// Act
		underTest.analyze(individualResponse);

		// Assert
	    List<ItemCategory> itemCategories = individualResponse.getOpportunityCategory().getItemCategories();
	    ItemCategory itemCategory1 = itemCategories.get(0);
	    ResponseCategory responseCategory1 = itemCategory1.getResponseCategory();
	    assertEquals(true,responseCategory1.isResponseValid());
	 
        ItemCategory itemCategory2 = itemCategories.get(1);
	    ResponseCategory responseCategory2 = itemCategory2.getResponseCategory();
	    assertEquals(true,responseCategory2.isResponseValid());

	}
	
	/**
	 * test isValidStudentResponse method in ItemResponseAnalysisAction.java
	 * format type = MS or = MC
	 * when TDS report item exist (bankKey & key) in IRP package (Excel)
	 * and when the format match IRP Package (Excel)
	 * validate/parse item response 
	 * TDS report item response format MC response "ABC", parse error
	 */
	@Test
	public void isResponseValidTest_MCMS2() {
		
		// Represents all the Items in the IRP package (Excel)
		List<StudentResponse> irpPackageItems = Lists.newArrayList(
				new StudentResponseBuilder(9999).bankKey(1).id(1).itemType("MC").excelResponse("A").toStudentResponse(),
				new StudentResponseBuilder(9999).bankKey(2).id(2).itemType("MS").excelResponse("A,B,C,D,E,F").toStudentResponse());

		// The TDS Report items
		final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
				new ItemAttributeBuilder().bankKey(1).key(1).format("MC").toOpportunityItem(), 
				new ItemAttributeBuilder().bankKey(2).key(2).format("MS").toOpportunityItem());

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

		final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems);

		// Represents list of StudentResponse for a particular studentID in IRP package (Excel)
		TestItemResponse testItemResponse = new TestItemResponse();
		testItemResponse.setStudentID(9999L);
		testItemResponse.setStudentResponses(irpPackageItems);

		when(studentResponseService.getTestItemResponseByStudentID(9999L)).thenReturn(testItemResponse);

		itemAttributesAnalysisAction.analyze(individualResponse);

		// IRP package (Excel) StudentResponse object
		StudentResponse StudentResponse1 = irpPackageItems.get(0);
		StudentResponse StudentResponse2 = irpPackageItems.get(1);

		when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "1", "1")).thenReturn(StudentResponse1);
		when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "2", "2")).thenReturn(StudentResponse2);

		// Item created like item retrieved item-xxx-xxx.xml from TrainingTestContent/Items/Item-xxx-xxx based on imsmanifest.xml
		org.cresst.sb.irp.domain.items.Itemrelease.Item item1 = new ItemreleaseItemBuilder("mc", "1", "26", "1").toItem();
		
		// Add attriblist (list of attrib) into above mentioned item1 
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
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1 pt.").desc("").toAttrib());
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
		// Attrib for name of "itm_att_Answer Key"
		when(itemService.getItemAttribValueFromIRPitemAttriblist(item2.getAttriblist().get(0), "itm_att_Answer Key")).thenReturn(
				attribs2.get(0));

		// Act
		underTest.analyze(individualResponse);

		// Assert
	    List<ItemCategory> itemCategories = individualResponse.getOpportunityCategory().getItemCategories();
	    ItemCategory itemCategory1 = itemCategories.get(0);
	    ResponseCategory responseCategory1 = itemCategory1.getResponseCategory();
	    assertEquals(false,responseCategory1.isResponseValid());
	 
        ItemCategory itemCategory2 = itemCategories.get(1);
	    ResponseCategory responseCategory2 = itemCategory2.getResponseCategory();
	    assertEquals(true,responseCategory2.isResponseValid());

	}
	
	/**
	 * test isValidStudentResponse method in ItemResponseAnalysisAction.java
	 * format type = ER
	 * when TDS report item exist (bankKey & key) in IRP package (Excel)
	 * and when the format match IRP Package (Excel)
	 * validate/parse item response 
	 * TDS report 1st item response format ER response "", parse error
	 * TDS report 2nd item response format ER response "hello", parse correct
	 */
	@Test
	public void isResponseValidTest_ER() {
		
		// Represents all the Items in the IRP package (Excel)
		List<StudentResponse> irpPackageItems = Lists.newArrayList(
				new StudentResponseBuilder(9999).bankKey(1).id(1).itemType("ER").excelResponse("B").toStudentResponse(),
				new StudentResponseBuilder(9999).bankKey(2).id(2).itemType("ER").excelResponse("hello").toStudentResponse());

		// The TDS Report items
		final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
				new ItemAttributeBuilder().bankKey(1).key(1).format("ER").toOpportunityItem(), 
				new ItemAttributeBuilder().bankKey(2).key(2).format("ER").toOpportunityItem());

		// The TDS Report responses
		final List<TDSReport.Opportunity.Item.Response> tdsReportItemResponses = Lists.newArrayList(
				new ItemResponseBuilder().content("").toIemResponse(), 
				new ItemResponseBuilder().content("hello").toIemResponse());

		// Add above TDS Report responses to its corresponding TDS Report items
		int index = 0;
		for (TDSReport.Opportunity.Item itemTmp : tdsReportItems) {
			itemTmp.setResponse(tdsReportItemResponses.get(index));
			index++;
		}

		final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems);

		// Represents list of StudentResponse for a particular studentID in IRP package (Excel)
		TestItemResponse testItemResponse = new TestItemResponse();
		testItemResponse.setStudentID(9999L);
		testItemResponse.setStudentResponses(irpPackageItems);

		when(studentResponseService.getTestItemResponseByStudentID(9999L)).thenReturn(testItemResponse);

		itemAttributesAnalysisAction.analyze(individualResponse);

		// IRP package (Excel) StudentResponse object
		StudentResponse StudentResponse1 = irpPackageItems.get(0);
		StudentResponse StudentResponse2 = irpPackageItems.get(1);

		when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "1", "1")).thenReturn(StudentResponse1);
		when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "2", "2")).thenReturn(StudentResponse2);

		// Item created like item retrieved item-xxx-xxx.xml from TrainingTestContent/Items/Item-xxx-xxx based on imsmanifest.xml
		org.cresst.sb.irp.domain.items.Itemrelease.Item item1 = new ItemreleaseItemBuilder("er", "1", "26", "1").toItem();
		
		// Add attriblist (list of attrib) into above mentioned item1 
		final List<org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib> attribs1 = Lists.newArrayList(
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("B").desc("").toAttrib(), 
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1 pt.").desc("").toAttrib());
		List<Itemrelease.Item.Attriblist> attribList1 = item1.getAttriblist();
		if (attribList1.size() == 0) {
			Itemrelease.Item.Attriblist attriblist = new Itemrelease.Item.Attriblist();
			attribList1.add(attriblist);
			attriblist.getAttrib().addAll(attribs1);
		}

		// Create another Item like item1 above
		org.cresst.sb.irp.domain.items.Itemrelease.Item item2 = new ItemreleaseItemBuilder("er", "2", "26", "2").toItem();
		
		// Add attriblist (list of attrib) into above mentioned item2
		final List<org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib> attribs2 = Lists.newArrayList(
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("hello").desc("").toAttrib(),
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1 pt.").desc("").toAttrib());
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
		// Attrib for name of "itm_att_Answer Key"
		when(itemService.getItemAttribValueFromIRPitemAttriblist(item2.getAttriblist().get(0), "itm_att_Answer Key")).thenReturn(
				attribs2.get(0));

		// Act
		underTest.analyze(individualResponse);

		// Assert
	    List<ItemCategory> itemCategories = individualResponse.getOpportunityCategory().getItemCategories();
	    ItemCategory itemCategory1 = itemCategories.get(0);
	    ResponseCategory responseCategory1 = itemCategory1.getResponseCategory();
	    assertEquals(false,responseCategory1.isResponseValid());
	 
        ItemCategory itemCategory2 = itemCategories.get(1);
	    ResponseCategory responseCategory2 = itemCategory2.getResponseCategory();
	    assertEquals(true,responseCategory2.isResponseValid());

	}
		
	/**
	 * test isValidStudentResponse method in ItemResponseAnalysisAction.java
	 * format type = MI
	 * when TDS report item exist (bankKey & key) in IRP package (Excel)
	 * and when the format match IRP Package (Excel)
	 * validate/parse item response 
	 * TDS report 1st item response format MI response response1, parse correct
	 * TDS report 2nd item response format MI response response2, parse error with <vXX> tag
	 */
	@Test
	public void isResponseValidTest_MI() {
		
		String response1 = "<itemResponse><response id=\"RESPONSE\"><value></value><value>2 b</value><value>3 a</value></response></itemResponse>";
		String response2 = "<itemResponse><response id=\"RESPONSE\"><vXX></value><value>2 b</value><value>3 a</value></response></itemResponse>";
		
		// Represents all the Items in the IRP package (Excel)
		List<StudentResponse> irpPackageItems = Lists.newArrayList(
				new StudentResponseBuilder(9999).bankKey(1).id(1).itemType("MI").excelResponse(response1).toStudentResponse(),
				new StudentResponseBuilder(9999).bankKey(2).id(2).itemType("MI").excelResponse(response2).toStudentResponse());

		// The TDS Report items
		final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
				new ItemAttributeBuilder().bankKey(1).key(1).format("MI").toOpportunityItem(), 
				new ItemAttributeBuilder().bankKey(2).key(2).format("MI").toOpportunityItem());

		// The TDS Report responses
		final List<TDSReport.Opportunity.Item.Response> tdsReportItemResponses = Lists.newArrayList(
				new ItemResponseBuilder().content(response1).toIemResponse(), 
				new ItemResponseBuilder().content(response2).toIemResponse());

		// Add above TDS Report responses to its corresponding TDS Report items
		int index = 0;
		for (TDSReport.Opportunity.Item itemTmp : tdsReportItems) {
			itemTmp.setResponse(tdsReportItemResponses.get(index));
			index++;
		}

		final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems);

		// Represents list of StudentResponse for a particular studentID in IRP package (Excel)
		TestItemResponse testItemResponse = new TestItemResponse();
		testItemResponse.setStudentID(9999L);
		testItemResponse.setStudentResponses(irpPackageItems);

		when(studentResponseService.getTestItemResponseByStudentID(9999L)).thenReturn(testItemResponse);

		itemAttributesAnalysisAction.analyze(individualResponse);

		// IRP package (Excel) StudentResponse object
		StudentResponse StudentResponse1 = irpPackageItems.get(0);
		StudentResponse StudentResponse2 = irpPackageItems.get(1);

		when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "1", "1")).thenReturn(StudentResponse1);
		when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "2", "2")).thenReturn(StudentResponse2);

		// Item created like item retrieved item-xxx-xxx.xml from TrainingTestContent/Items/Item-xxx-xxx based on imsmanifest.xml
		org.cresst.sb.irp.domain.items.Itemrelease.Item item1 = new ItemreleaseItemBuilder("mi", "1", "26", "1").toItem();
		
		// Add attriblist (list of attrib) into above mentioned item1 
		final List<org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib> attribs1 = Lists.newArrayList(
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("B").desc("").toAttrib(), 
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1 pt.").desc("").toAttrib());
		List<Itemrelease.Item.Attriblist> attribList1 = item1.getAttriblist();
		if (attribList1.size() == 0) {
			Itemrelease.Item.Attriblist attriblist = new Itemrelease.Item.Attriblist();
			attribList1.add(attriblist);
			attriblist.getAttrib().addAll(attribs1);
		}

		// Create another Item like item1 above
		org.cresst.sb.irp.domain.items.Itemrelease.Item item2 = new ItemreleaseItemBuilder("mi", "2", "26", "2").toItem();
		
		// Add attriblist (list of attrib) into above mentioned item2
		final List<org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib> attribs2 = Lists.newArrayList(
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("hello").desc("").toAttrib(),
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1 pt.").desc("").toAttrib());
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
		// Attrib for name of "itm_att_Answer Key"
		when(itemService.getItemAttribValueFromIRPitemAttriblist(item2.getAttriblist().get(0), "itm_att_Answer Key")).thenReturn(
				attribs2.get(0));

		// Act
		underTest.analyze(individualResponse);

		// Assert
	    List<ItemCategory> itemCategories = individualResponse.getOpportunityCategory().getItemCategories();
	    ItemCategory itemCategory1 = itemCategories.get(0);
	    ResponseCategory responseCategory1 = itemCategory1.getResponseCategory();
	    assertEquals(true,responseCategory1.isResponseValid());
	 
        ItemCategory itemCategory2 = itemCategories.get(1);
	    ResponseCategory responseCategory2 = itemCategory2.getResponseCategory();
	    assertEquals(false,responseCategory2.isResponseValid());

	}
			
	/**
	 * test isValidStudentResponse method in ItemResponseAnalysisAction.java
	 * format type = EQ
	 * when TDS report item exist (bankKey & key) in IRP package (Excel)
	 * and when the format match IRP Package (Excel)
	 * validate/parse item response 
	 * TDS report 1st item response format EQ response response1, parse correct
	 * TDS report 2nd item response format EQ response response2, parse error <XX> tag
	 */
	@Test
	public void isResponseValidTest_EQ() {
		
		String response1 = "<response><math xmlns=\"http://www.w3.org/1998/Math/MathML\"> " +
				"<mstyle displaystyle=\"true\"> " +
				"<mn>5</mn> " +
				"<mn>3</mn> " + 
				"<mo>.</mo> " +
				"<mn>1</mn> " +
				"<mn>3</mn> " +
				"</mstyle> " +
				"</math></response>";
		String response2 = "<response><math xmlns=\"http://www.w3.org/1998/Math/MathML\"> " +
				"<mstyle displaystyle=\"true\"> " +
				"<XX>5</mn> " +
				"<mn>3</mn> " + 
				"<mo>.</mo> " +
				"<mn>1</mn> " +
				"<mn>3</mn> " +
				"</mstyle> " +
				"</math></response>";	
		// Represents all the Items in the IRP package (Excel)
		List<StudentResponse> irpPackageItems = Lists.newArrayList(
				new StudentResponseBuilder(9999).bankKey(1).id(1).itemType("EQ").excelResponse(response1).toStudentResponse(),
				new StudentResponseBuilder(9999).bankKey(2).id(2).itemType("EQ").excelResponse(response2).toStudentResponse());

		// The TDS Report items
		final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
				new ItemAttributeBuilder().bankKey(1).key(1).format("EQ").toOpportunityItem(), 
				new ItemAttributeBuilder().bankKey(2).key(2).format("EQ").toOpportunityItem());

		// The TDS Report responses
		final List<TDSReport.Opportunity.Item.Response> tdsReportItemResponses = Lists.newArrayList(
				new ItemResponseBuilder().content(response1).toIemResponse(), 
				new ItemResponseBuilder().content(response2).toIemResponse());

		// Add above TDS Report responses to its corresponding TDS Report items
		int index = 0;
		for (TDSReport.Opportunity.Item itemTmp : tdsReportItems) {
			itemTmp.setResponse(tdsReportItemResponses.get(index));
			index++;
		}

		final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems);

		// Represents list of StudentResponse for a particular studentID in IRP package (Excel)
		TestItemResponse testItemResponse = new TestItemResponse();
		testItemResponse.setStudentID(9999L);
		testItemResponse.setStudentResponses(irpPackageItems);

		when(studentResponseService.getTestItemResponseByStudentID(9999L)).thenReturn(testItemResponse);

		itemAttributesAnalysisAction.analyze(individualResponse);

		// IRP package (Excel) StudentResponse object
		StudentResponse StudentResponse1 = irpPackageItems.get(0);
		StudentResponse StudentResponse2 = irpPackageItems.get(1);

		when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "1", "1")).thenReturn(StudentResponse1);
		when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "2", "2")).thenReturn(StudentResponse2);

		// Item created like item retrieved item-xxx-xxx.xml from TrainingTestContent/Items/Item-xxx-xxx based on imsmanifest.xml
		org.cresst.sb.irp.domain.items.Itemrelease.Item item1 = new ItemreleaseItemBuilder("eq", "1", "26", "1").toItem();
		
		// Add attriblist (list of attrib) into above mentioned item1 
		final List<org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib> attribs1 = Lists.newArrayList(
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("B").desc("").toAttrib(), 
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1 pt.").desc("").toAttrib());
		List<Itemrelease.Item.Attriblist> attribList1 = item1.getAttriblist();
		if (attribList1.size() == 0) {
			Itemrelease.Item.Attriblist attriblist = new Itemrelease.Item.Attriblist();
			attribList1.add(attriblist);
			attriblist.getAttrib().addAll(attribs1);
		}

		// Create another Item like item1 above
		org.cresst.sb.irp.domain.items.Itemrelease.Item item2 = new ItemreleaseItemBuilder("eq", "2", "26", "2").toItem();
		
		// Add attriblist (list of attrib) into above mentioned item2
		final List<org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib> attribs2 = Lists.newArrayList(
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("hello").desc("").toAttrib(),
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1 pt.").desc("").toAttrib());
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
		// Attrib for name of "itm_att_Answer Key"
		when(itemService.getItemAttribValueFromIRPitemAttriblist(item2.getAttriblist().get(0), "itm_att_Answer Key")).thenReturn(
				attribs2.get(0));

		// Act
		underTest.analyze(individualResponse);

		// Assert
	    List<ItemCategory> itemCategories = individualResponse.getOpportunityCategory().getItemCategories();
	    ItemCategory itemCategory1 = itemCategories.get(0);
	    ResponseCategory responseCategory1 = itemCategory1.getResponseCategory();
	    assertEquals(true,responseCategory1.isResponseValid());
	 
        ItemCategory itemCategory2 = itemCategories.get(1);
	    ResponseCategory responseCategory2 = itemCategory2.getResponseCategory();
	    assertEquals(false,responseCategory2.isResponseValid());

	}
	
	/**
	 * test isValidStudentResponse method in ItemResponseAnalysisAction.java
	 * format type = GI
	 * when TDS report item exist (bankKey & key) in IRP package (Excel)
	 * and when the format match IRP Package (Excel)
	 * validate/parse item response 
	 * TDS report 1st item response format GI response response1 - RegionGroupObject
	 * TDS report 2nd item response format GI response response2 - AtomicObject
	 * TDS report 2nd item response format GI response response2 - Object
	 */
	@Ignore("TODO: fix this")
	@Test
	public void isResponseValidTest_GI() {
		
		String response1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> " +
			"<!-- MACHINE GENERATED 8/5/14  18:10 PM. DO NOT EDIT --> " + 
			"<!DOCTYPE AnswerSet [ " + 
			"<!ELEMENT AnswerSet (Question+)> " + 
			"<!ELEMENT AtomicObject (#PCDATA)> " + 
			"<!ELEMENT EdgeVector (#PCDATA)> " + 
			"<!ELEMENT GridImageTestPoints (TestPoint*)> " + 
			"<!ELEMENT LabelList (#PCDATA)> " + 
			"<!ELEMENT Object (PointVector,EdgeVector,LabelList,ValueList)> " + 
			"<!ELEMENT ObjectSet (Object,AtomicObject+)> " + 
			"<!ELEMENT PointVector (#PCDATA)> " + 
			"<!ELEMENT Question (QuestionPart)> " + 
			"<!ATTLIST Question id NMTOKEN #REQUIRED> " + 
			"<!ELEMENT QuestionPart (LabelList,GridImageTestPoints,ObjectSet)> " + 
			"<!ATTLIST QuestionPart id NMTOKEN #REQUIRED> " + 
			"<!ELEMENT TestPoint (#PCDATA)> " + 
			"<!ELEMENT ValueList (#PCDATA)> " + 
			"]> " +
			"<AnswerSet><Question id=\"\"><QuestionPart id=\"1\"><ObjectSet><RegionGroupObject name=\"group\" numselected=\"2\"><RegionObject name=\"a1\" isselected=\"false\"/><RegionObject name=\"a2\" isselected=\"false\"/><RegionObject name=\"a3\" isselected=\"true\"/><RegionObject name=\"a4\" isselected=\"true\"/><RegionObject name=\"a5\" isselected=\"false\"/></RegionGroupObject></ObjectSet><SnapPoint></SnapPoint></QuestionPart></Question></AnswerSet>  ";
			
		String response2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> " +
				"<!-- MACHINE GENERATED 8/5/14  18:10 PM. DO NOT EDIT --> " + 
				"<!DOCTYPE AnswerSet [ " + 
				"<!ELEMENT AnswerSet (Question+)> " + 
				"<!ELEMENT AtomicObject (#PCDATA)> " + 
				"<!ELEMENT EdgeVector (#PCDATA)> " + 
				"<!ELEMENT GridImageTestPoints (TestPoint*)> " + 
				"<!ELEMENT LabelList (#PCDATA)> " + 
				"<!ELEMENT Object (PointVector,EdgeVector,LabelList,ValueList)> " + 
				"<!ELEMENT ObjectSet (Object,AtomicObject+)> " + 
				"<!ELEMENT PointVector (#PCDATA)> " + 
				"<!ELEMENT Question (QuestionPart)> " + 
				"<!ATTLIST Question id NMTOKEN #REQUIRED> " + 
				"<!ELEMENT QuestionPart (LabelList,GridImageTestPoints,ObjectSet)> " + 
				"<!ATTLIST QuestionPart id NMTOKEN #REQUIRED> " + 
				"<!ELEMENT TestPoint (#PCDATA)> " + 
				"<!ELEMENT ValueList (#PCDATA)> " + 
				"]> " +
				"<AnswerSet><Question id=\"\"><QuestionPart id=\"1\"><ObjectSet><AtomicObject>{2(105,378)}</AtomicObject><AtomicObject>{1(125,141)}</AtomicObject><AtomicObject>{1(168,93)}</AtomicObject><AtomicObject>{2(375,139)}</AtomicObject><AtomicObject>{8(419,93)}</AtomicObject><RegionGroupObject name=\"PartB\" numselected=\"0\"><RegionObject name=\"True\" isselected=\"false\"/><RegionObject name=\"False\" isselected=\"false\"/><RegionObject name=\"CBD\" isselected=\"false\"/></RegionGroupObject></ObjectSet><SnapPoint>30@125,269;168,317;403,317;186,317;433,317;419,317;375,271;151,317</SnapPoint></QuestionPart></Question></AnswerSet> ]]></Response> ";
				
		String response3 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> " +
				"<!-- MACHINE GENERATED 8/5/14  18:10 PM. DO NOT EDIT --> " + 
				"<!DOCTYPE AnswerSet [ " + 
				"<!ELEMENT AnswerSet (Question+)> " + 
				"<!ELEMENT AtomicObject (#PCDATA)> " + 
				"<!ELEMENT EdgeVector (#PCDATA)> " + 
				"<!ELEMENT GridImageTestPoints (TestPoint*)> " + 
				"<!ELEMENT LabelList (#PCDATA)> " + 
				"<!ELEMENT Object (PointVector,EdgeVector,LabelList,ValueList)> " + 
				"<!ELEMENT ObjectSet (Object,AtomicObject+)> " + 
				"<!ELEMENT PointVector (#PCDATA)> " + 
				"<!ELEMENT Question (QuestionPart)> " + 
				"<!ATTLIST Question id NMTOKEN #REQUIRED> " + 
				"<!ELEMENT QuestionPart (LabelList,GridImageTestPoints,ObjectSet)> " + 
				"<!ATTLIST QuestionPart id NMTOKEN #REQUIRED> " + 
				"<!ELEMENT TestPoint (#PCDATA)> " + 
				"<!ELEMENT ValueList (#PCDATA)> " + 
				"]> " +
				"<AnswerSet><Question id=\"\"><QuestionPart id=\"1\"><ObjectSet><Object><PointVector>{(237,227)}</PointVector><EdgeVector> {} </EdgeVector><LabelList> {} </LabelList><ValueList> {} </ValueList></Object></ObjectSet><SnapPoint></SnapPoint></QuestionPart></Question></AnswerSet> ]]></Response> ";

		String excelResponse1 = response1;
		String excelResponse2 = response3; // mismatch AtomicObject and Object
		String excelResponse3 = response3; 
		
		// Represents all the Items in the IRP package (Excel)
		List<StudentResponse> irpPackageItems = Lists.newArrayList(
				new StudentResponseBuilder(9999).bankKey(1).id(1).itemType("GI").excelResponse(excelResponse1).toStudentResponse(),
				new StudentResponseBuilder(9999).bankKey(2).id(2).itemType("GI").excelResponse(excelResponse2).toStudentResponse(),
				new StudentResponseBuilder(9999).bankKey(3).id(3).itemType("GI").excelResponse(excelResponse3).toStudentResponse());

		// The TDS Report items
		final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
				new ItemAttributeBuilder().bankKey(1).key(1).format("GI").toOpportunityItem(), 
				new ItemAttributeBuilder().bankKey(2).key(2).format("GI").toOpportunityItem(), 
				new ItemAttributeBuilder().bankKey(3).key(3).format("GI").toOpportunityItem());

		// The TDS Report responses
		final List<TDSReport.Opportunity.Item.Response> tdsReportItemResponses = Lists.newArrayList(
				new ItemResponseBuilder().content(response1).toIemResponse(), 
				new ItemResponseBuilder().content(response2).toIemResponse(), 
				new ItemResponseBuilder().content(response3).toIemResponse());

		// Add above TDS Report responses to its corresponding TDS Report items
		int index = 0;
		for (TDSReport.Opportunity.Item itemTmp : tdsReportItems) {
			itemTmp.setResponse(tdsReportItemResponses.get(index));
			index++;
		}

		final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems);

		// Represents list of StudentResponse for a particular studentID in IRP package (Excel)
		TestItemResponse testItemResponse = new TestItemResponse();
		testItemResponse.setStudentID(9999L);
		testItemResponse.setStudentResponses(irpPackageItems);

		when(studentResponseService.getTestItemResponseByStudentID(9999L)).thenReturn(testItemResponse);

		itemAttributesAnalysisAction.analyze(individualResponse);

		// IRP package (Excel) StudentResponse object
		StudentResponse StudentResponse1 = irpPackageItems.get(0);
		StudentResponse StudentResponse2 = irpPackageItems.get(1);
		StudentResponse StudentResponse3 = irpPackageItems.get(2);

		when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "1", "1")).thenReturn(StudentResponse1);
		when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "2", "2")).thenReturn(StudentResponse2);
		when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "3", "3")).thenReturn(StudentResponse3);

		// Item created like item retrieved item-xxx-xxx.xml from TrainingTestContent/Items/Item-xxx-xxx based on imsmanifest.xml
		org.cresst.sb.irp.domain.items.Itemrelease.Item item1 = new ItemreleaseItemBuilder("gi", "1", "26", "1").toItem();
		
		// Add attriblist (list of attrib) into above mentioned item1 
		final List<org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib> attribs1 = Lists.newArrayList(
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("B").desc("").toAttrib(), 
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1 pt.").desc("").toAttrib());
		List<Itemrelease.Item.Attriblist> attribList1 = item1.getAttriblist();
		if (attribList1.size() == 0) {
			Itemrelease.Item.Attriblist attriblist = new Itemrelease.Item.Attriblist();
			attribList1.add(attriblist);
			attriblist.getAttrib().addAll(attribs1);
		}

		// Create another Item like item1 above
		org.cresst.sb.irp.domain.items.Itemrelease.Item item2 = new ItemreleaseItemBuilder("gi", "2", "26", "2").toItem();
		
		// Add attriblist (list of attrib) into above mentioned item2
		final List<org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib> attribs2 = Lists.newArrayList(
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("hello").desc("").toAttrib(),
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1 pt.").desc("").toAttrib());
		List<Itemrelease.Item.Attriblist> attribList2 = item2.getAttriblist();
		if (attribList2.size() == 0) {
			Itemrelease.Item.Attriblist attriblist = new Itemrelease.Item.Attriblist();
			attribList2.add(attriblist);
			attriblist.getAttrib().addAll(attribs2);
		}

		// Create another Item like item1 above
		org.cresst.sb.irp.domain.items.Itemrelease.Item item3 = new ItemreleaseItemBuilder("gi", "3", "26", "3").toItem();
		
		// Add attriblist (list of attrib) into above mentioned item2
		final List<org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib> attribs3 = Lists.newArrayList(
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("hello").desc("").toAttrib(),
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1 pt.").desc("").toAttrib());
		List<Itemrelease.Item.Attriblist> attribList3 = item3.getAttriblist();
		if (attribList3.size() == 0) {
			Itemrelease.Item.Attriblist attriblist = new Itemrelease.Item.Attriblist();
			attribList3.add(attriblist);
			attriblist.getAttrib().addAll(attribs3);
		}
		
		when(itemService.getItemByIdentifier("item-1-1")).thenReturn(item1);
		when(itemService.getItemByIdentifier("item-2-2")).thenReturn(item2);
		when(itemService.getItemByIdentifier("item-3-3")).thenReturn(item3);

		when(itemService.getItemAttriblistFromIRPitem(item1)).thenReturn(item1.getAttriblist().get(0)); //attribs1
		when(itemService.getItemAttriblistFromIRPitem(item2)).thenReturn(item2.getAttriblist().get(0)); //attribs2
		when(itemService.getItemAttriblistFromIRPitem(item3)).thenReturn(item3.getAttriblist().get(0)); //attribs3

		// Attrib for name of "itm_att_Answer Key"
		when(itemService.getItemAttribValueFromIRPitemAttriblist(item1.getAttriblist().get(0), "itm_att_Answer Key")).thenReturn(
				attribs1.get(0));
		// Attrib for name of "itm_att_Answer Key"
		when(itemService.getItemAttribValueFromIRPitemAttriblist(item2.getAttriblist().get(0), "itm_att_Answer Key")).thenReturn(
				attribs2.get(0));
		when(itemService.getItemAttribValueFromIRPitemAttriblist(item3.getAttriblist().get(0), "itm_att_Answer Key")).thenReturn(
				attribs3.get(0));

		// Act
		underTest.analyze(individualResponse);

		// Assert
	    List<ItemCategory> itemCategories = individualResponse.getOpportunityCategory().getItemCategories();
	    ItemCategory itemCategory1 = itemCategories.get(0);
	    ResponseCategory responseCategory1 = itemCategory1.getResponseCategory();
	    assertEquals(true,responseCategory1.isResponseValid());
	 
        ItemCategory itemCategory2 = itemCategories.get(1);
	    ResponseCategory responseCategory2 = itemCategory2.getResponseCategory();
	    assertEquals(false,responseCategory2.isResponseValid());

	    ItemCategory itemCategory3= itemCategories.get(1);
		ResponseCategory responseCategory3 = itemCategory3.getResponseCategory();
		assertEquals(false,responseCategory3.isResponseValid());
	}
	
	/**
	 * test isValidStudentResponse method in ItemResponseAnalysisAction.java
	 * format type = TI
	 * when TDS report item exist (bankKey & key) in IRP package (Excel)
	 * and when the format match IRP Package (Excel)
	 * validate/parse item response 
	 * TDS report 1st item response format TI response response1, parse correct
	 * TDS report 2nd item response format TI response response2, parse error using EQ type
	 */
	@Test
	public void isResponseValidTest_TI() {
		
		String response1 = "<responseSpec><responseTable><tr><th id=\"col0\"/><th id=\"col1\"/></tr><tr><td/><td/></tr><tr><td/><td/></tr><tr><td/><td>0.12</td></tr><tr><td/><td/></tr><tr><td/><td/></tr><tr><td/><td/></tr><tr><td/><td/></tr><tr><td/><td>0.425</td></tr><tr><td/><td/></tr><tr><td/><td/></tr><tr><td/><td>0.0175</td></tr><tr><td/><td/></tr><tr><td/><td/></tr></responseTable></responseSpec>";
		String response2 = "<response><math xmlns=\"http://www.w3.org/1998/Math/MathML\"> " +
				"<mstyle displaystyle=\"true\"> " +
				"<XX>5</mn> " +
				"<mn>3</mn> " + 
				"<mo>.</mo> " +
				"<mn>1</mn> " +
				"<mn>3</mn> " +
				"</mstyle> " +
				"</math></response>";	
		// Represents all the Items in the IRP package (Excel)
		List<StudentResponse> irpPackageItems = Lists.newArrayList(
				new StudentResponseBuilder(9999).bankKey(1).id(1).itemType("TI").excelResponse(response1).toStudentResponse(),
				new StudentResponseBuilder(9999).bankKey(2).id(2).itemType("TI").excelResponse(response2).toStudentResponse());

		// The TDS Report items
		final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
				new ItemAttributeBuilder().bankKey(1).key(1).format("TI").toOpportunityItem(), 
				new ItemAttributeBuilder().bankKey(2).key(2).format("TI").toOpportunityItem());

		// The TDS Report responses
		final List<TDSReport.Opportunity.Item.Response> tdsReportItemResponses = Lists.newArrayList(
				new ItemResponseBuilder().content(response1).toIemResponse(), 
				new ItemResponseBuilder().content(response2).toIemResponse());

		// Add above TDS Report responses to its corresponding TDS Report items
		int index = 0;
		for (TDSReport.Opportunity.Item itemTmp : tdsReportItems) {
			itemTmp.setResponse(tdsReportItemResponses.get(index));
			index++;
		}

		final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems);

		// Represents list of StudentResponse for a particular studentID in IRP package (Excel)
		TestItemResponse testItemResponse = new TestItemResponse();
		testItemResponse.setStudentID(9999L);
		testItemResponse.setStudentResponses(irpPackageItems);

		when(studentResponseService.getTestItemResponseByStudentID(9999L)).thenReturn(testItemResponse);

		itemAttributesAnalysisAction.analyze(individualResponse);

		// IRP package (Excel) StudentResponse object
		StudentResponse StudentResponse1 = irpPackageItems.get(0);
		StudentResponse StudentResponse2 = irpPackageItems.get(1);

		when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "1", "1")).thenReturn(StudentResponse1);
		when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "2", "2")).thenReturn(StudentResponse2);

		// Item created like item retrieved item-xxx-xxx.xml from TrainingTestContent/Items/Item-xxx-xxx based on imsmanifest.xml
		org.cresst.sb.irp.domain.items.Itemrelease.Item item1 = new ItemreleaseItemBuilder("ti", "1", "26", "1").toItem();
		
		// Add attriblist (list of attrib) into above mentioned item1 
		final List<org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib> attribs1 = Lists.newArrayList(
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("B").desc("").toAttrib(), 
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1 pt.").desc("").toAttrib());
		List<Itemrelease.Item.Attriblist> attribList1 = item1.getAttriblist();
		if (attribList1.size() == 0) {
			Itemrelease.Item.Attriblist attriblist = new Itemrelease.Item.Attriblist();
			attribList1.add(attriblist);
			attriblist.getAttrib().addAll(attribs1);
		}

		// Create another Item like item1 above
		org.cresst.sb.irp.domain.items.Itemrelease.Item item2 = new ItemreleaseItemBuilder("ti", "2", "26", "2").toItem();
		
		// Add attriblist (list of attrib) into above mentioned item2
		final List<org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib> attribs2 = Lists.newArrayList(
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("hello").desc("").toAttrib(),
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1 pt.").desc("").toAttrib());
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
		// Attrib for name of "itm_att_Answer Key"
		when(itemService.getItemAttribValueFromIRPitemAttriblist(item2.getAttriblist().get(0), "itm_att_Answer Key")).thenReturn(
				attribs2.get(0));

		// Act
		underTest.analyze(individualResponse);

		// Assert
	    List<ItemCategory> itemCategories = individualResponse.getOpportunityCategory().getItemCategories();
	    ItemCategory itemCategory1 = itemCategories.get(0);
	    ResponseCategory responseCategory1 = itemCategory1.getResponseCategory();
	    assertEquals(true,responseCategory1.isResponseValid());
	 
        ItemCategory itemCategory2 = itemCategories.get(1);
	    ResponseCategory responseCategory2 = itemCategory2.getResponseCategory();
	    assertEquals(false,responseCategory2.isResponseValid());

	}
	
	/**
	 * test analysisItemResponseItemScoring method in ItemResponseAnalysisAction.java
	 * format type = MS
	 */
	@Test
	public void analysisItemResponseItemScoringTest_MS() {
		
		// Represents all the Items in the IRP package
		List<StudentResponse> irpPackageItems = Lists.newArrayList(
				new StudentResponseBuilder(9999).bankKey(1).id(1).itemType("MS").excelResponse("A,D").toStudentResponse(),
				new StudentResponseBuilder(9999).bankKey(2).id(2).itemType("MS").excelResponse("A,B,C,D,E,F").toStudentResponse());

		// The TDS Report items
		final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
				new ItemAttributeBuilder().bankKey(1).key(1).format("MS").toOpportunityItem(), 
				new ItemAttributeBuilder().bankKey(2).key(2).format("MS").toOpportunityItem());

		// The TDS Report responses
		final List<TDSReport.Opportunity.Item.Response> tdsReportItemResponses = Lists.newArrayList(
				new ItemResponseBuilder().content("A,D").toIemResponse(), 
				new ItemResponseBuilder().content("C,D").toIemResponse());

		// The TDS Report items with responses
		int index = 0;
		for (TDSReport.Opportunity.Item itemTmp : tdsReportItems) {
			itemTmp.setResponse(tdsReportItemResponses.get(index));
			index++;
		}

		final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems);

		TestItemResponse testItemResponse = new TestItemResponse();
		testItemResponse.setStudentID(9999L);
		testItemResponse.setStudentResponses(irpPackageItems);

		when(studentResponseService.getTestItemResponseByStudentID(9999L)).thenReturn(testItemResponse);

		itemAttributesAnalysisAction.analyze(individualResponse);

		StudentResponse StudentResponse1 = irpPackageItems.get(0);
		StudentResponse StudentResponse2 = irpPackageItems.get(1);

		when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "1", "1")).thenReturn(StudentResponse1);
		when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "2", "2")).thenReturn(StudentResponse2);

		org.cresst.sb.irp.domain.items.Itemrelease.Item item1 = new ItemreleaseItemBuilder("ms", "1", "26", "1").toItem();
		final List<org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib> attribs1 = Lists.newArrayList(
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("A,D").desc("").toAttrib(), 
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1 pt.").desc("").toAttrib());
		List<Itemrelease.Item.Attriblist> attribList1 = item1.getAttriblist();
		if (attribList1.size() == 0) {
			Itemrelease.Item.Attriblist attriblist = new Itemrelease.Item.Attriblist();
			attribList1.add(attriblist);
			attriblist.getAttrib().addAll(attribs1);
		}

		org.cresst.sb.irp.domain.items.Itemrelease.Item item2 = new ItemreleaseItemBuilder("ms", "2", "26", "2").toItem();
		final List<org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib> attribs2 = Lists.newArrayList(
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("B,C,D,E").desc("").toAttrib(),
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1 pt.").desc("").toAttrib());
		List<Itemrelease.Item.Attriblist> attribList2 = item2.getAttriblist();
		if (attribList2.size() == 0) {
			Itemrelease.Item.Attriblist attriblist = new Itemrelease.Item.Attriblist();
			attribList2.add(attriblist);
			attriblist.getAttrib().addAll(attribs2);
		}

		when(itemService.getItemByIdentifier("item-1-1")).thenReturn(item1);
		when(itemService.getItemByIdentifier("item-2-2")).thenReturn(item2);

		when(itemService.getItemAttriblistFromIRPitem(item1)).thenReturn(item1.getAttriblist().get(0));
		when(itemService.getItemAttriblistFromIRPitem(item2)).thenReturn(item2.getAttriblist().get(0));

		when(itemService.getItemAttribValueFromIRPitemAttriblist(item1.getAttriblist().get(0), "itm_att_Answer Key")).thenReturn(
				attribs1.get(0));
		when(itemService.getItemAttribValueFromIRPitemAttriblist(item2.getAttriblist().get(0), "itm_att_Answer Key")).thenReturn(
				attribs2.get(0));

		// Act
		underTest.analyze(individualResponse);

		// Assert
	    List<ItemCategory> itemCategories = individualResponse.getOpportunityCategory().getItemCategories();
	    ItemCategory itemCategory1 = itemCategories.get(0);
	    ResponseCategory responseCategory1 = itemCategory1.getResponseCategory();
	    FieldCheckType fieldCheckType1 = responseCategory1.getContentFieldCheckType();
        assertEquals(true, fieldCheckType1.isCorrectValue()); //attribs1 answer key "A,D" match "A,D" in tdsReportItemResponses
        
        ItemCategory itemCategory2 = itemCategories.get(1);
	    ResponseCategory responseCategory2 = itemCategory2.getResponseCategory();
	    FieldCheckType fieldCheckType2 = responseCategory2.getContentFieldCheckType();
	    assertEquals(false, fieldCheckType2.isCorrectValue()); //attribs2 answer key "B,C,D,E" vs "C,D" in tdsReportItemResponses

	}
	
	/**
	 * test analysisItemResponseItemScoring method in ItemResponseAnalysisAction.java
	 * format type = MC
	 */
	@Test
	public void analysisItemResponseItemScoringTest_MC() {
		
		// Represents all the Items in the IRP package
		List<StudentResponse> irpPackageItems = Lists.newArrayList(
				new StudentResponseBuilder(9999).bankKey(1).id(1).itemType("MC").excelResponse("A").toStudentResponse(),
				new StudentResponseBuilder(9999).bankKey(2).id(2).itemType("MC").excelResponse("F").toStudentResponse());

		// The TDS Report items
		final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
				new ItemAttributeBuilder().bankKey(1).key(1).format("MC").toOpportunityItem(), 
				new ItemAttributeBuilder().bankKey(2).key(2).format("MC").toOpportunityItem());

		// The TDS Report responses
		final List<TDSReport.Opportunity.Item.Response> tdsReportItemResponses = Lists.newArrayList(
				new ItemResponseBuilder().content("A").toIemResponse(), 
				new ItemResponseBuilder().content("F").toIemResponse());

		// The TDS Report items with responses
		int index = 0;
		for (TDSReport.Opportunity.Item itemTmp : tdsReportItems) {
			itemTmp.setResponse(tdsReportItemResponses.get(index));
			index++;
		}

		final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems);

		TestItemResponse testItemResponse = new TestItemResponse();
		testItemResponse.setStudentID(9999L);
		testItemResponse.setStudentResponses(irpPackageItems);

		when(studentResponseService.getTestItemResponseByStudentID(9999L)).thenReturn(testItemResponse);

		itemAttributesAnalysisAction.analyze(individualResponse);

		StudentResponse StudentResponse1 = irpPackageItems.get(0);
		StudentResponse StudentResponse2 = irpPackageItems.get(1);

		when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "1", "1")).thenReturn(StudentResponse1);
		when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "2", "2")).thenReturn(StudentResponse2);

		org.cresst.sb.irp.domain.items.Itemrelease.Item item1 = new ItemreleaseItemBuilder("mc", "1", "26", "1").toItem();
		final List<org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib> attribs1 = Lists.newArrayList(
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("C").desc("").toAttrib(), 
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1 pt.").desc("").toAttrib());
		List<Itemrelease.Item.Attriblist> attribList1 = item1.getAttriblist();
		if (attribList1.size() == 0) {
			Itemrelease.Item.Attriblist attriblist = new Itemrelease.Item.Attriblist();
			attribList1.add(attriblist);
			attriblist.getAttrib().addAll(attribs1);
		}

		org.cresst.sb.irp.domain.items.Itemrelease.Item item2 = new ItemreleaseItemBuilder("mc", "2", "26", "2").toItem();
		final List<org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib> attribs2 = Lists.newArrayList(
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("F").desc("").toAttrib(),
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1 pt.").desc("").toAttrib());
		List<Itemrelease.Item.Attriblist> attribList2 = item2.getAttriblist();
		if (attribList2.size() == 0) {
			Itemrelease.Item.Attriblist attriblist = new Itemrelease.Item.Attriblist();
			attribList2.add(attriblist);
			attriblist.getAttrib().addAll(attribs2);
		}

		when(itemService.getItemByIdentifier("item-1-1")).thenReturn(item1);
		when(itemService.getItemByIdentifier("item-2-2")).thenReturn(item2);

		when(itemService.getItemAttriblistFromIRPitem(item1)).thenReturn(item1.getAttriblist().get(0));
		when(itemService.getItemAttriblistFromIRPitem(item2)).thenReturn(item2.getAttriblist().get(0));

		when(itemService.getItemAttribValueFromIRPitemAttriblist(item1.getAttriblist().get(0), "itm_att_Answer Key")).thenReturn(
				attribs1.get(0));
		when(itemService.getItemAttribValueFromIRPitemAttriblist(item2.getAttriblist().get(0), "itm_att_Answer Key")).thenReturn(
				attribs2.get(0));

		// Act
		underTest.analyze(individualResponse);

		// Assert
	    List<ItemCategory> itemCategories = individualResponse.getOpportunityCategory().getItemCategories();
	    ItemCategory itemCategory1 = itemCategories.get(0);
	    ResponseCategory responseCategory1 = itemCategory1.getResponseCategory();
	    FieldCheckType fieldCheckType1 = responseCategory1.getContentFieldCheckType();
        assertEquals(false, fieldCheckType1.isCorrectValue()); //attribs1 answer key "C" match "A" in tdsReportItemResponses
        
        ItemCategory itemCategory2 = itemCategories.get(1);
	    ResponseCategory responseCategory2 = itemCategory2.getResponseCategory();
	    FieldCheckType fieldCheckType2 = responseCategory2.getContentFieldCheckType();
	    assertEquals(true, fieldCheckType2.isCorrectValue()); //attribs2 answer key "F" match "F" in tdsReportItemResponses

	}
	
	/**
	 * test analysisItemResponseItemScoring method in ItemResponseAnalysisAction.java
	 * format type = case "gi": case "ti": case "mi": case "eq": case "er":	
	 */
	@Test
	public void analysisItemResponseItemScoringTest_ItemScoring() {
		
		//TODO need to implement when receive data from AIR
		
		// Represents all the Items in the IRP package
		List<StudentResponse> irpPackageItems = Lists.newArrayList(
				new StudentResponseBuilder(9999).bankKey(1).id(1).itemType("MC").excelResponse("A").toStudentResponse(),
				new StudentResponseBuilder(9999).bankKey(2).id(2).itemType("MC").excelResponse("F").toStudentResponse());

		// The TDS Report items
		final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
				new ItemAttributeBuilder().bankKey(1).key(1).format("MC").toOpportunityItem(), 
				new ItemAttributeBuilder().bankKey(2).key(2).format("MC").toOpportunityItem());

		// The TDS Report responses
		final List<TDSReport.Opportunity.Item.Response> tdsReportItemResponses = Lists.newArrayList(
				new ItemResponseBuilder().content("A").toIemResponse(), 
				new ItemResponseBuilder().content("F").toIemResponse());

		// The TDS Report items with responses
		int index = 0;
		for (TDSReport.Opportunity.Item itemTmp : tdsReportItems) {
			itemTmp.setResponse(tdsReportItemResponses.get(index));
			index++;
		}

		final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems);

		TestItemResponse testItemResponse = new TestItemResponse();
		testItemResponse.setStudentID(9999L);
		testItemResponse.setStudentResponses(irpPackageItems);

		when(studentResponseService.getTestItemResponseByStudentID(9999L)).thenReturn(testItemResponse);

		itemAttributesAnalysisAction.analyze(individualResponse);

		StudentResponse StudentResponse1 = irpPackageItems.get(0);
		StudentResponse StudentResponse2 = irpPackageItems.get(1);

		when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "1", "1")).thenReturn(StudentResponse1);
		when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "2", "2")).thenReturn(StudentResponse2);

		org.cresst.sb.irp.domain.items.Itemrelease.Item item1 = new ItemreleaseItemBuilder("mc", "1", "26", "1").toItem();
		final List<org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib> attribs1 = Lists.newArrayList(
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("C").desc("").toAttrib(), 
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1 pt.").desc("").toAttrib());
		List<Itemrelease.Item.Attriblist> attribList1 = item1.getAttriblist();
		if (attribList1.size() == 0) {
			Itemrelease.Item.Attriblist attriblist = new Itemrelease.Item.Attriblist();
			attribList1.add(attriblist);
			attriblist.getAttrib().addAll(attribs1);
		}

		org.cresst.sb.irp.domain.items.Itemrelease.Item item2 = new ItemreleaseItemBuilder("mc", "2", "26", "2").toItem();
		final List<org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib> attribs2 = Lists.newArrayList(
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("F").desc("").toAttrib(),
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1 pt.").desc("").toAttrib());
		List<Itemrelease.Item.Attriblist> attribList2 = item2.getAttriblist();
		if (attribList2.size() == 0) {
			Itemrelease.Item.Attriblist attriblist = new Itemrelease.Item.Attriblist();
			attribList2.add(attriblist);
			attriblist.getAttrib().addAll(attribs2);
		}

		when(itemService.getItemByIdentifier("item-1-1")).thenReturn(item1);
		when(itemService.getItemByIdentifier("item-2-2")).thenReturn(item2);

		when(itemService.getItemAttriblistFromIRPitem(item1)).thenReturn(item1.getAttriblist().get(0));
		when(itemService.getItemAttriblistFromIRPitem(item2)).thenReturn(item2.getAttriblist().get(0));

		when(itemService.getItemAttribValueFromIRPitemAttriblist(item1.getAttriblist().get(0), "itm_att_Answer Key")).thenReturn(
				attribs1.get(0));
		when(itemService.getItemAttribValueFromIRPitemAttriblist(item2.getAttriblist().get(0), "itm_att_Answer Key")).thenReturn(
				attribs2.get(0));

		// Act
		underTest.analyze(individualResponse);

		// Assert
	    List<ItemCategory> itemCategories = individualResponse.getOpportunityCategory().getItemCategories();
	    ItemCategory itemCategory1 = itemCategories.get(0);
	    ResponseCategory responseCategory1 = itemCategory1.getResponseCategory();
	    FieldCheckType fieldCheckType1 = responseCategory1.getContentFieldCheckType();
        assertEquals(false, fieldCheckType1.isCorrectValue()); //attribs1 answer key "C" match "A" in tdsReportItemResponses
        
        ItemCategory itemCategory2 = itemCategories.get(1);
	    ResponseCategory responseCategory2 = itemCategory2.getResponseCategory();
	    FieldCheckType fieldCheckType2 = responseCategory2.getContentFieldCheckType();
	    assertEquals(true, fieldCheckType2.isCorrectValue()); //attribs2 answer key "F" match "F" in tdsReportItemResponses

	}
	
	/**
	 * test analysisItemResponseWithStudentReponse method in ItemResponseAnalysisAction.java
	 * format type = MC
	 */
	@Test
	public void analysisItemResponseWithStudentReponseTest_MC() {
		
		// Represents all the Items in the IRP package
		List<StudentResponse> irpPackageItems = Lists.newArrayList(
				new StudentResponseBuilder(9999).bankKey(1).id(1).itemType("MC").excelResponse("D").toStudentResponse(),
				new StudentResponseBuilder(9999).bankKey(2).id(2).itemType("MC").excelResponse("C").toStudentResponse());

		// The TDS Report items
		final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
				new ItemAttributeBuilder().bankKey(1).key(1).format("MC").toOpportunityItem(), 
				new ItemAttributeBuilder().bankKey(2).key(2).format("MC").toOpportunityItem());

		// The TDS Report responses
		final List<TDSReport.Opportunity.Item.Response> tdsReportItemResponses = Lists.newArrayList(
				new ItemResponseBuilder().content("A").toIemResponse(), 
				new ItemResponseBuilder().content("C").toIemResponse());

		// The TDS Report items with responses
		int index = 0;
		for (TDSReport.Opportunity.Item itemTmp : tdsReportItems) {
			itemTmp.setResponse(tdsReportItemResponses.get(index));
			index++;
		}

		final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems);

		TestItemResponse testItemResponse = new TestItemResponse();
		testItemResponse.setStudentID(9999L);
		testItemResponse.setStudentResponses(irpPackageItems);

		when(studentResponseService.getTestItemResponseByStudentID(9999L)).thenReturn(testItemResponse);

		itemAttributesAnalysisAction.analyze(individualResponse);

		StudentResponse StudentResponse1 = irpPackageItems.get(0);
		StudentResponse StudentResponse2 = irpPackageItems.get(1);

		when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "1", "1")).thenReturn(StudentResponse1);
		when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "2", "2")).thenReturn(StudentResponse2);

		org.cresst.sb.irp.domain.items.Itemrelease.Item item1 = new ItemreleaseItemBuilder("mc", "1", "26", "1").toItem();
		final List<org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib> attribs1 = Lists.newArrayList(
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("X").desc("").toAttrib(), 
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1 pt.").desc("").toAttrib());
		List<Itemrelease.Item.Attriblist> attribList1 = item1.getAttriblist();
		if (attribList1.size() == 0) {
			Itemrelease.Item.Attriblist attriblist = new Itemrelease.Item.Attriblist();
			attribList1.add(attriblist);
			attriblist.getAttrib().addAll(attribs1);
		}

		org.cresst.sb.irp.domain.items.Itemrelease.Item item2 = new ItemreleaseItemBuilder("mc", "2", "26", "2").toItem();
		final List<org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib> attribs2 = Lists.newArrayList(
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("Y").desc("").toAttrib(),
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1 pt.").desc("").toAttrib());
		List<Itemrelease.Item.Attriblist> attribList2 = item2.getAttriblist();
		if (attribList2.size() == 0) {
			Itemrelease.Item.Attriblist attriblist = new Itemrelease.Item.Attriblist();
			attribList2.add(attriblist);
			attriblist.getAttrib().addAll(attribs2);
		}

		when(itemService.getItemByIdentifier("item-1-1")).thenReturn(item1);
		when(itemService.getItemByIdentifier("item-2-2")).thenReturn(item2);

		when(itemService.getItemAttriblistFromIRPitem(item1)).thenReturn(item1.getAttriblist().get(0));
		when(itemService.getItemAttriblistFromIRPitem(item2)).thenReturn(item2.getAttriblist().get(0));

		when(itemService.getItemAttribValueFromIRPitemAttriblist(item1.getAttriblist().get(0), "itm_att_Answer Key")).thenReturn(
				attribs1.get(0));
		when(itemService.getItemAttribValueFromIRPitemAttriblist(item2.getAttriblist().get(0), "itm_att_Answer Key")).thenReturn(
				attribs2.get(0));

		// Act
		underTest.analyze(individualResponse);

		// Assert
	    List<ItemCategory> itemCategories = individualResponse.getOpportunityCategory().getItemCategories();
	    ItemCategory itemCategory1 = itemCategories.get(0);
	    ResponseCategory responseCategory1 = itemCategory1.getResponseCategory();
	    StudentResponse excelStudentResponse1 = responseCategory1.getStudentResponse();
	    assertEquals(false, excelStudentResponse1.getStatus()); //excel response "D" in irpPackageItems
		   														//does NOT match "A" in tdsReportItemResponses
        ItemCategory itemCategory2 = itemCategories.get(1);
	    ResponseCategory responseCategory2 = itemCategory2.getResponseCategory();
	    StudentResponse excelStudentResponse2 = responseCategory2.getStudentResponse();
	    assertEquals(true, excelStudentResponse2.getStatus()); //excel response "C" in irpPackageItems
	    													  //does match "C" in tdsReportItemResponses
	}
	
	/**
	 * test analysisItemResponseWithStudentReponse method in ItemResponseAnalysisAction.java
	 * format type = MS
	 */
	@Test
	public void analysisItemResponseWithStudentReponseTest_MS() {
		
		// Represents all the Items in the IRP package
		List<StudentResponse> irpPackageItems = Lists.newArrayList(
				new StudentResponseBuilder(9999).bankKey(1).id(1).itemType("MS").excelResponse("A,B,C").toStudentResponse(),
				new StudentResponseBuilder(9999).bankKey(2).id(2).itemType("MS").excelResponse("A,F,G,H").toStudentResponse());

		// The TDS Report items
		final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
				new ItemAttributeBuilder().bankKey(1).key(1).format("MS").toOpportunityItem(), 
				new ItemAttributeBuilder().bankKey(2).key(2).format("MS").toOpportunityItem());

		// The TDS Report responses
		final List<TDSReport.Opportunity.Item.Response> tdsReportItemResponses = Lists.newArrayList(
				new ItemResponseBuilder().content("A,C,B").toIemResponse(), 
				new ItemResponseBuilder().content("B").toIemResponse());

		// The TDS Report items with responses
		int index = 0;
		for (TDSReport.Opportunity.Item itemTmp : tdsReportItems) {
			itemTmp.setResponse(tdsReportItemResponses.get(index));
			index++;
		}

		final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems);

		TestItemResponse testItemResponse = new TestItemResponse();
		testItemResponse.setStudentID(9999L);
		testItemResponse.setStudentResponses(irpPackageItems);

		when(studentResponseService.getTestItemResponseByStudentID(9999L)).thenReturn(testItemResponse);

		itemAttributesAnalysisAction.analyze(individualResponse);

		StudentResponse StudentResponse1 = irpPackageItems.get(0);
		StudentResponse StudentResponse2 = irpPackageItems.get(1);

		when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "1", "1")).thenReturn(StudentResponse1);
		when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "2", "2")).thenReturn(StudentResponse2);

		org.cresst.sb.irp.domain.items.Itemrelease.Item item1 = new ItemreleaseItemBuilder("ms", "1", "26", "1").toItem();
		final List<org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib> attribs1 = Lists.newArrayList(
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("X").desc("").toAttrib(), 
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1 pt.").desc("").toAttrib());
		List<Itemrelease.Item.Attriblist> attribList1 = item1.getAttriblist();
		if (attribList1.size() == 0) {
			Itemrelease.Item.Attriblist attriblist = new Itemrelease.Item.Attriblist();
			attribList1.add(attriblist);
			attriblist.getAttrib().addAll(attribs1);
		}

		org.cresst.sb.irp.domain.items.Itemrelease.Item item2 = new ItemreleaseItemBuilder("ms", "2", "26", "2").toItem();
		final List<org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib> attribs2 = Lists.newArrayList(
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("Y").desc("").toAttrib(),
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1 pt.").desc("").toAttrib());
		List<Itemrelease.Item.Attriblist> attribList2 = item2.getAttriblist();
		if (attribList2.size() == 0) {
			Itemrelease.Item.Attriblist attriblist = new Itemrelease.Item.Attriblist();
			attribList2.add(attriblist);
			attriblist.getAttrib().addAll(attribs2);
		}

		when(itemService.getItemByIdentifier("item-1-1")).thenReturn(item1);
		when(itemService.getItemByIdentifier("item-2-2")).thenReturn(item2);

		when(itemService.getItemAttriblistFromIRPitem(item1)).thenReturn(item1.getAttriblist().get(0));
		when(itemService.getItemAttriblistFromIRPitem(item2)).thenReturn(item2.getAttriblist().get(0));

		when(itemService.getItemAttribValueFromIRPitemAttriblist(item1.getAttriblist().get(0), "itm_att_Answer Key")).thenReturn(
				attribs1.get(0));
		when(itemService.getItemAttribValueFromIRPitemAttriblist(item2.getAttriblist().get(0), "itm_att_Answer Key")).thenReturn(
				attribs2.get(0));

		// Act
		underTest.analyze(individualResponse);

		// Assert
	    List<ItemCategory> itemCategories = individualResponse.getOpportunityCategory().getItemCategories();
	    ItemCategory itemCategory1 = itemCategories.get(0);
	    ResponseCategory responseCategory1 = itemCategory1.getResponseCategory();
	    StudentResponse excelStudentResponse1 = responseCategory1.getStudentResponse();
	    assertEquals(true, excelStudentResponse1.getStatus()); //excel response "A,B,C" in irpPackageItems
		   														//does match "A,C,B" in tdsReportItemResponses
        ItemCategory itemCategory2 = itemCategories.get(1);
	    ResponseCategory responseCategory2 = itemCategory2.getResponseCategory();
	    StudentResponse excelStudentResponse2 = responseCategory2.getStudentResponse();
	    assertEquals(false, excelStudentResponse2.getStatus()); //excel response "A,F,G,H" in irpPackageItems
	    													    //does NOT match "B" in tdsReportItemResponses
	}
	
	/**
	 * test analysisItemResponseWithStudentReponse method in ItemResponseAnalysisAction.java
	 * format type = case "gi": case "ti": case "mi": case "eq": case "er":	
	 */
	//TODO need to implement once the update IRP package (Excel) with new student response column received 
	@Test
	public void analysisItemResponseWithStudentReponseTest_GI() {
		
		// Represents all the Items in the IRP package
		List<StudentResponse> irpPackageItems = Lists.newArrayList(
				new StudentResponseBuilder(9999).bankKey(1).id(1).itemType("MS").excelResponse("A,B,C").toStudentResponse(),
				new StudentResponseBuilder(9999).bankKey(2).id(2).itemType("MS").excelResponse("A,F,G,H").toStudentResponse());

		// The TDS Report items
		final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
				new ItemAttributeBuilder().bankKey(1).key(1).format("MS").toOpportunityItem(), 
				new ItemAttributeBuilder().bankKey(2).key(2).format("MS").toOpportunityItem());

		// The TDS Report responses
		final List<TDSReport.Opportunity.Item.Response> tdsReportItemResponses = Lists.newArrayList(
				new ItemResponseBuilder().content("A,C,B").toIemResponse(), 
				new ItemResponseBuilder().content("B").toIemResponse());

		// The TDS Report items with responses
		int index = 0;
		for (TDSReport.Opportunity.Item itemTmp : tdsReportItems) {
			itemTmp.setResponse(tdsReportItemResponses.get(index));
			index++;
		}

		final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems);

		TestItemResponse testItemResponse = new TestItemResponse();
		testItemResponse.setStudentID(9999L);
		testItemResponse.setStudentResponses(irpPackageItems);

		when(studentResponseService.getTestItemResponseByStudentID(9999L)).thenReturn(testItemResponse);

		itemAttributesAnalysisAction.analyze(individualResponse);

		StudentResponse StudentResponse1 = irpPackageItems.get(0);
		StudentResponse StudentResponse2 = irpPackageItems.get(1);

		when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "1", "1")).thenReturn(StudentResponse1);
		when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "2", "2")).thenReturn(StudentResponse2);

		org.cresst.sb.irp.domain.items.Itemrelease.Item item1 = new ItemreleaseItemBuilder("ms", "1", "26", "1").toItem();
		final List<org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib> attribs1 = Lists.newArrayList(
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("X").desc("").toAttrib(), 
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1 pt.").desc("").toAttrib());
		List<Itemrelease.Item.Attriblist> attribList1 = item1.getAttriblist();
		if (attribList1.size() == 0) {
			Itemrelease.Item.Attriblist attriblist = new Itemrelease.Item.Attriblist();
			attribList1.add(attriblist);
			attriblist.getAttrib().addAll(attribs1);
		}

		org.cresst.sb.irp.domain.items.Itemrelease.Item item2 = new ItemreleaseItemBuilder("ms", "2", "26", "2").toItem();
		final List<org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib> attribs2 = Lists.newArrayList(
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("Y").desc("").toAttrib(),
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1 pt.").desc("").toAttrib());
		List<Itemrelease.Item.Attriblist> attribList2 = item2.getAttriblist();
		if (attribList2.size() == 0) {
			Itemrelease.Item.Attriblist attriblist = new Itemrelease.Item.Attriblist();
			attribList2.add(attriblist);
			attriblist.getAttrib().addAll(attribs2);
		}

		when(itemService.getItemByIdentifier("item-1-1")).thenReturn(item1);
		when(itemService.getItemByIdentifier("item-2-2")).thenReturn(item2);

		when(itemService.getItemAttriblistFromIRPitem(item1)).thenReturn(item1.getAttriblist().get(0));
		when(itemService.getItemAttriblistFromIRPitem(item2)).thenReturn(item2.getAttriblist().get(0));

		when(itemService.getItemAttribValueFromIRPitemAttriblist(item1.getAttriblist().get(0), "itm_att_Answer Key")).thenReturn(
				attribs1.get(0));
		when(itemService.getItemAttribValueFromIRPitemAttriblist(item2.getAttriblist().get(0), "itm_att_Answer Key")).thenReturn(
				attribs2.get(0));

		// Act
		underTest.analyze(individualResponse);

		// Assert
	    List<ItemCategory> itemCategories = individualResponse.getOpportunityCategory().getItemCategories();
	    ItemCategory itemCategory1 = itemCategories.get(0);
	    ResponseCategory responseCategory1 = itemCategory1.getResponseCategory();
	    StudentResponse excelStudentResponse1 = responseCategory1.getStudentResponse();
	    assertEquals(true, excelStudentResponse1.getStatus()); //excel response "A,B,C" in irpPackageItems
		   														//does match "A,C,B" in tdsReportItemResponses
        ItemCategory itemCategory2 = itemCategories.get(1);
	    ResponseCategory responseCategory2 = itemCategory2.getResponseCategory();
	    StudentResponse excelStudentResponse2 = responseCategory2.getStudentResponse();
	    assertEquals(false, excelStudentResponse2.getStatus()); //excel response "A,F,G,H" in irpPackageItems
	    													    //does NOT match "B" in tdsReportItemResponses
	}
}
