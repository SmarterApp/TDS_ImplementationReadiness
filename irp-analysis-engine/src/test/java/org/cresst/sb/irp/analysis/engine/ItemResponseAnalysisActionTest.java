package org.cresst.sb.irp.analysis.engine;

import builders.*;
import com.google.common.collect.Lists;
import org.cresst.sb.irp.domain.analysis.*;
import org.cresst.sb.irp.domain.items.Itemrelease;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.itemscoring.rubric.MachineRubricLoader;
import org.cresst.sb.irp.service.ItemService;
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
	/*	List<StudentResponse> irpPackageItems = Lists.newArrayList(
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

		//when(studentResponseService.getTestItemResponseByStudentID(9999L)).thenReturn(testItemResponse);

		itemAttributesAnalysisAction.analyze(individualResponse);

		// IRP package (Excel) StudentResponse object
		StudentResponse StudentResponse1 = irpPackageItems.get(0);
		StudentResponse StudentResponse2 = irpPackageItems.get(1);

	//	when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "1", "1")).thenReturn(StudentResponse1);
	//	when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "2", "2")).thenReturn(StudentResponse2);

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
	    assertEquals(true,responseCategory2.isResponseValid());*/

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
/*		List<StudentResponse> irpPackageItems = Lists.newArrayList(
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

	//	when(studentResponseService.getTestItemResponseByStudentID(9999L)).thenReturn(testItemResponse);

		itemAttributesAnalysisAction.analyze(individualResponse);

		// IRP package (Excel) StudentResponse object
		StudentResponse StudentResponse1 = irpPackageItems.get(0);
		StudentResponse StudentResponse2 = irpPackageItems.get(1);

	//	when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "1", "1")).thenReturn(StudentResponse1);
	//	when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "2", "2")).thenReturn(StudentResponse2);

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
*/
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
/*		List<StudentResponse> irpPackageItems = Lists.newArrayList(
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

	//	when(studentResponseService.getTestItemResponseByStudentID(9999L)).thenReturn(testItemResponse);

		itemAttributesAnalysisAction.analyze(individualResponse);

		// IRP package (Excel) StudentResponse object
		StudentResponse StudentResponse1 = irpPackageItems.get(0);
		StudentResponse StudentResponse2 = irpPackageItems.get(1);

	//	when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "1", "1")).thenReturn(StudentResponse1);
	//	when(studentResponseService.getStudentResponseByStudentIDandBankKeyID(9999L, "2", "2")).thenReturn(StudentResponse2);

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
*/
	}
		
}
