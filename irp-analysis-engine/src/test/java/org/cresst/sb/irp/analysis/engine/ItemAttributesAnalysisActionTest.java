package org.cresst.sb.irp.analysis.engine;

import builders.CellCategoryBuilder;
import builders.ItemAttributeBuilder;
import builders.StudentResponseBuilder;

import com.google.common.collect.Lists;

import org.cresst.sb.irp.domain.analysis.*;
import org.cresst.sb.irp.domain.analysis.ItemCategory.ItemStatusEnum;
import org.cresst.sb.irp.domain.studentresponse.StudentResponse;
import org.cresst.sb.irp.domain.studentresponse.TestItemResponse;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.service.StudentResponseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Test class to verify that Items are analyzed correctly by ItemAttributeAnalysisAction
 */
@RunWith(MockitoJUnitRunner.class)
public class ItemAttributesAnalysisActionTest {

    @Mock
    private StudentResponseService studentResponseService;

    @InjectMocks
    private ItemAttributesAnalysisAction underTest = new ItemAttributesAnalysisAction();

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
     * When there are equal Items in the given TDS Report and in the IRP Package
     * then mark the items from the IRP package as found.
     */
    @Test
    public void whenTDSReportContainsSameItemsAsIRPPackage_ItemsMarkedAsFound() {
        // Arrange
        // Represents all the Items in the IRP package
        List<StudentResponse> irpPackageItems = Lists.newArrayList(
                new StudentResponseBuilder(9999).bankKey(1).id(1).toStudentResponse(),
                new StudentResponseBuilder(9999).bankKey(2).id(2).toStudentResponse()
        );

        // The TDS Report has same Items than the IRP package
        final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
                new ItemAttributeBuilder().bankKey(1).key(1).toOpportunityItem(),
                new ItemAttributeBuilder().bankKey(2).key(2).toOpportunityItem()
        );

        final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems);

        TestItemResponse testItemResponse = new TestItemResponse();
        testItemResponse.setStudentID(9999L);
        testItemResponse.setStudentResponses(irpPackageItems);

        when(studentResponseService.getTestItemResponseByStudentID(9999L)).thenReturn(testItemResponse);

        // Act
        underTest.analyze(individualResponse);

        // Assert
        List<ItemCategory> itemCategories = individualResponse.getOpportunityCategory().getItemCategories();
        assertEquals(ItemStatusEnum.FOUND, itemCategories.get(0).getStatus());
        assertEquals(ItemStatusEnum.FOUND, itemCategories.get(1).getStatus());
    }

    /**
     * When there are more Items in the given TDS Report than exist in the IRP Package
     * then mark the extraneous Items found in the TDS Report as being invalid.
     */
    @Test
    public void whenTDSReportContainsMoreItemsThanIRPPackage_ExtraneousTDSReportItemsMarkedAsExtra() {
        // Arrange
        // Represents all the Items in the IRP package
        List<StudentResponse> irpPackageItems = Lists.newArrayList(
                new StudentResponseBuilder(9999).bankKey(1).id(1).toStudentResponse()
        );

        // The TDS Report has more items than the IRP Package
        final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
                new ItemAttributeBuilder().bankKey(1).key(1).toOpportunityItem(),
                new ItemAttributeBuilder().bankKey(2).key(2).toOpportunityItem()
        );

        final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems);

        TestItemResponse testItemResponse = new TestItemResponse();
        testItemResponse.setStudentID(9999L);
        testItemResponse.setStudentResponses(irpPackageItems);

        when(studentResponseService.getTestItemResponseByStudentID(9999L)).thenReturn(testItemResponse);

        // Act
        underTest.analyze(individualResponse);

        // Assert
        List<ItemCategory> itemCategories = individualResponse.getOpportunityCategory().getItemCategories();
        assertEquals(ItemStatusEnum.FOUND, itemCategories.get(0).getStatus());
        assertEquals(ItemStatusEnum.EXTRA, itemCategories.get(1).getStatus());
    }

    /**
     * When there are less Items in the given TDS Report than exist in the IRP Package
     * then mark the missing items from the IRP package as missing.
     */
    @Test
    public void whenTDSReportContainsFewerItemsThanIRPPackage_MissingItemsMarkedAsMissing() {
        // Arrange
        // Represents all the Items in the IRP package
        List<StudentResponse> irpPackageItems = Lists.newArrayList(
                new StudentResponseBuilder(9999).bankKey(1).id(1).toStudentResponse(),
                new StudentResponseBuilder(9999).bankKey(2).id(2).toStudentResponse()
        );

        // The TDS Report has fewer Items than the IRP package
        final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
                new ItemAttributeBuilder().bankKey(1).key(1).toOpportunityItem()
        );

        final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems);

        TestItemResponse testItemResponse = new TestItemResponse();
        testItemResponse.setStudentID(9999L);
        testItemResponse.setStudentResponses(irpPackageItems);

        when(studentResponseService.getTestItemResponseByStudentID(9999L)).thenReturn(testItemResponse);

        // Act
        underTest.analyze(individualResponse);

        // Assert
        List<ItemCategory> itemCategories = individualResponse.getOpportunityCategory().getItemCategories();
        assertEquals(ItemStatusEnum.FOUND, itemCategories.get(0).getStatus());
        assertEquals(ItemStatusEnum.MISSING, itemCategories.get(1).getStatus());
    }

    /**
     * When there are equal Items in the given TDS Report and in the IRP Package
     * but a TDS Report Item does not exist in the IRP package then mark the item
     * from the TDS Report as extra.
     */
    @Test
    public void whenTDSReportAndIRPPackageContainsSameNumberOfItemsButTDSReportItemNotFound_ItemMarkedAsExtra() {
        // Arrange
        // Represents all the Items in the IRP package
        List<StudentResponse> irpPackageItems = Lists.newArrayList(
                new StudentResponseBuilder(9999).bankKey(1).id(1).toStudentResponse(),
                new StudentResponseBuilder(9999).bankKey(2).id(2).toStudentResponse()
        );

        // The TDS Report has same Items than the IRP package but one of the TDS Report Items
        // does not exist in the IRP package
        final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
                new ItemAttributeBuilder().bankKey(1).key(1).toOpportunityItem(),
                new ItemAttributeBuilder().bankKey(3).key(3).toOpportunityItem()
        );

        final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems);

        TestItemResponse testItemResponse = new TestItemResponse();
        testItemResponse.setStudentID(9999L);
        testItemResponse.setStudentResponses(irpPackageItems);

        when(studentResponseService.getTestItemResponseByStudentID(9999L)).thenReturn(testItemResponse);

        // Act
        underTest.analyze(individualResponse);

        // Assert
        List<ItemCategory> itemCategories = individualResponse.getOpportunityCategory().getItemCategories();
        assertEquals(ItemStatusEnum.FOUND, itemCategories.get(0).getStatus());
        assertEquals(ItemStatusEnum.MISSING, itemCategories.get(1).getStatus());
        assertEquals(ItemStatusEnum.EXTRA, itemCategories.get(3).getStatus());
    }
}