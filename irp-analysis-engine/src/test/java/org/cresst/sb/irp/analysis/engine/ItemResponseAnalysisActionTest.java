package org.cresst.sb.irp.analysis.engine;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.List;

import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.ExamineeCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.ItemCategory;
import org.cresst.sb.irp.domain.analysis.OpportunityCategory;
import org.cresst.sb.irp.domain.analysis.ResponseCategory;
import org.cresst.sb.irp.domain.items.Itemrelease;
import org.cresst.sb.irp.domain.studentresponse.StudentResponse;
import org.cresst.sb.irp.domain.studentresponse.TestItemResponse;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.service.ItemService;
import org.cresst.sb.irp.service.StudentResponseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import builders.AttribBuilder;
import builders.ItemAttributeBuilder;
import builders.ItemResponseBuilder;
import builders.ItemreleaseItemBuilder;
import builders.StudentResponseBuilder;

import com.google.common.collect.Lists;

/**
 * Test class to verify that Response are analyzed correctly by ItemResponseAnalysisAction
 */
@RunWith(MockitoJUnitRunner.class)
public class ItemResponseAnalysisActionTest {

	@Mock
	private StudentResponseService studentResponseService;

	@Mock
	private ItemService itemService;

	@InjectMocks
	private ItemAttributesAnalysisAction itemAttributesAnalysisAction = new ItemAttributesAnalysisAction();

	@InjectMocks
	private ItemResponseAnalysisAction underTest = new ItemResponseAnalysisAction();

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

	@Test
	public void whenTDSReportItemResponseSameAsIRPPackage_ItemResponse() {
		
		// Represents all the Items in the IRP package
		List<StudentResponse> irpPackageItems = Lists
				.newArrayList(new StudentResponseBuilder(9999).bankKey(1).id(1).itemType("MC").excelResponse("A")
						.toStudentResponse(),
						new StudentResponseBuilder(9999).bankKey(2).id(2).itemType("MS").excelResponse("A,B,C,D,E,F")
								.toStudentResponse());

		// The TDS Report items
		final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(new ItemAttributeBuilder().bankKey(1).key(1)
				.format("MC").toOpportunityItem(), new ItemAttributeBuilder().bankKey(2).key(2).format("MS").toOpportunityItem());

		// The TDS Report responses
		final List<TDSReport.Opportunity.Item.Response> tdsReportItemResponses = Lists.newArrayList(new ItemResponseBuilder()
				.content("A").toIemResponse(), new ItemResponseBuilder().content("A,B,C,D,E,F").toIemResponse());

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
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("A").desc("").toAttrib(), new AttribBuilder(
						"itm_att_Item Point").name("Item: Item Point").val("1 pt.").desc("").toAttrib());
		List<Itemrelease.Item.Attriblist> attribList1 = item1.getAttriblist();
		if (attribList1.size() == 0) {
			Itemrelease.Item.Attriblist attriblist = new Itemrelease.Item.Attriblist();
			attribList1.add(attriblist);
			attriblist.getAttrib().addAll(attribs1);
		}

		org.cresst.sb.irp.domain.items.Itemrelease.Item item2 = new ItemreleaseItemBuilder("ms", "2", "26", "2").toItem();
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
        assertEquals(true, fieldCheckType1.isCorrectValue());
        ItemCategory itemCategory2 = itemCategories.get(1);
	    ResponseCategory responseCategory2 = itemCategory2.getResponseCategory();
	    FieldCheckType fieldCheckType2 = responseCategory2.getContentFieldCheckType();
	    assertEquals(true, fieldCheckType2.isCorrectValue());

	}
	
	@Test
	public void whenTDSReportItemResponseDiffIRPPackage_ItemResponse() {
		
		// Represents all the Items in the IRP package
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

		org.cresst.sb.irp.domain.items.Itemrelease.Item item1 = new ItemreleaseItemBuilder("mc", "1", "26", "1").toItem();
		final List<org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib> attribs1 = Lists.newArrayList(
				new AttribBuilder("itm_att_Answer Key").name("Item: Answer Key").val("A").desc("").toAttrib(), 
				new AttribBuilder("itm_att_Item Point").name("Item: Item Point").val("1 pt.").desc("").toAttrib());
		List<Itemrelease.Item.Attriblist> attribList1 = item1.getAttriblist();
		if (attribList1.size() == 0) {
			Itemrelease.Item.Attriblist attriblist = new Itemrelease.Item.Attriblist();
			attribList1.add(attriblist);
			attriblist.getAttrib().addAll(attribs1);
		}

		org.cresst.sb.irp.domain.items.Itemrelease.Item item2 = new ItemreleaseItemBuilder("ms", "2", "26", "2").toItem();
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
        assertEquals(true, fieldCheckType1.isCorrectValue());
        ItemCategory itemCategory2 = itemCategories.get(1);
	    ResponseCategory responseCategory2 = itemCategory2.getResponseCategory();
	    FieldCheckType fieldCheckType2 = responseCategory2.getContentFieldCheckType();
	    assertEquals(false, fieldCheckType2.isCorrectValue());

	}
}
