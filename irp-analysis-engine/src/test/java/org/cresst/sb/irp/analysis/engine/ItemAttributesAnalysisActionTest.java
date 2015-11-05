package org.cresst.sb.irp.analysis.engine;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.List;

import com.google.common.collect.Lists;

import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.ItemCategory;
import org.cresst.sb.irp.domain.analysis.OpportunityCategory;
import org.cresst.sb.irp.domain.analysis.TestPropertiesCategory;
import org.cresst.sb.irp.domain.analysis.ItemCategory.ItemStatusEnum;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.testpackage.Administration;
import org.cresst.sb.irp.domain.testpackage.Itempool;
import org.cresst.sb.irp.domain.testpackage.Testitem;
import org.cresst.sb.irp.service.TestPackageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import builders.ItemAttributeBuilder;
import builders.TestitemBuilder;
import builders.TestspecificationBuilder;

/**
 * Test class to verify that Items are analyzed correctly by ItemAttributeAnalysisAction
 */
@RunWith(MockitoJUnitRunner.class)
public class ItemAttributesAnalysisActionTest {

	@Mock
	public TestPackageService testPackageService;

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
		cellCategory.setTdsFieldName("name");
		cellCategory.setTdsFieldNameValue("test");

		final TestPropertiesCategory testPropertiesCategory = new TestPropertiesCategory();
		testPropertiesCategory.addCellCategory(cellCategory);
		individualResponse.setTestPropertiesCategory(testPropertiesCategory);

		final OpportunityCategory opportunityCategory = new OpportunityCategory();
		individualResponse.setOpportunityCategory(opportunityCategory);

		return individualResponse;
	}
	
	private Administration generateTestPackageAdministration(List<Testitem> irptestpackageTestitems){
		
		final Administration administration = new Administration();
		
		Itempool itempool = new Itempool();
		administration.setItempool(itempool);

		if (irptestpackageTestitems != null) {
			itempool.getTestitem().addAll(irptestpackageTestitems);
		}
		
		return administration;
	}

	/**
	 * When there are equal Items in the given TDS Report and in the IRP TestPackage then mark the items from the IRP package as
	 * found.
	 * 
	 */
	@Test
	public void whenTDSReportContainsSameItemsAsIRPTestPackage_ItemsMarkedAsFound() {

		// The TDS Report has same Items than the IRP package
		final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
				new ItemAttributeBuilder().bankKey(100).key(1000).toOpportunityItem(),
				new ItemAttributeBuilder().bankKey(200).key(2000).toOpportunityItem());

		final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems);

		final Administration administration = generateTestPackageAdministration(generateTestItems2());

		when(testPackageService.getTestpackageByIdentifierUniqueid("test")).thenReturn(new TestspecificationBuilder("test")
				.setAdministration(administration)
				.toTestspecification()
				);

	    // Act
        underTest.analyze(individualResponse);
        
        // Assert
        List<ItemCategory> itemCategories = individualResponse.getOpportunityCategory().getItemCategories();
        
        assertEquals(ItemStatusEnum.FOUND, itemCategories.get(0).getStatus());
        assertEquals(ItemStatusEnum.FOUND, itemCategories.get(1).getStatus());
	}
	
	  /**
     * When there are more Items in the given TDS Report than exist in the IRP TestPackage
     * then mark the extraneous Items found in the TDS Report as being invalid.
     */
    @Test
    public void whenTDSReportContainsMoreItemsThanIRPTestPackage_ExtraneousTDSReportItemsMarkedAsExtra() {
    	
        // The TDS Report has more items than the IRP Package
        final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
                new ItemAttributeBuilder().bankKey(100).key(1000).toOpportunityItem(),
                new ItemAttributeBuilder().bankKey(200).key(2000).toOpportunityItem()
        );
    	
    	final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems);
    	
    	final Administration administration = generateTestPackageAdministration(generateTestItems1());

		when(testPackageService.getTestpackageByIdentifierUniqueid("test")).thenReturn(new TestspecificationBuilder("test")
				.setAdministration(administration)
				.toTestspecification()
				);

	    // Act
        underTest.analyze(individualResponse);
        
        // Assert
        List<ItemCategory> itemCategories = individualResponse.getOpportunityCategory().getItemCategories();
        
        assertEquals(ItemStatusEnum.FOUND, itemCategories.get(0).getStatus());
        assertEquals(ItemStatusEnum.EXTRA, itemCategories.get(1).getStatus());
    }

    /**
     * When there are less Items in the given TDS Report than exist in the IRP TestPackage
     * then mark the missing items from the IRP package as missing.
     */
    @Test
    public void whenTDSReportContainsFewerItemsThanIRPTestPackage_MissingItemsMarkedAsMissing() {
    	
        // The TDS Report has fewer Items than the IRP package
        final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
                new ItemAttributeBuilder().bankKey(100).key(1000).toOpportunityItem()
        );

        final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems);

    	final Administration administration = generateTestPackageAdministration(generateTestItems2());
		
		when(testPackageService.getTestpackageByIdentifierUniqueid("test")).thenReturn(new TestspecificationBuilder("test")
				.setAdministration(administration)
				.toTestspecification()
				);

		// Act
		underTest.analyze(individualResponse);
		
        // Assert
        List<ItemCategory> itemCategories = individualResponse.getOpportunityCategory().getItemCategories();
        
        assertEquals(ItemStatusEnum.FOUND, itemCategories.get(0).getStatus());
        assertEquals(ItemStatusEnum.MISSING, itemCategories.get(1).getStatus());
    }
    
    /**
     * When there are less Items in the given TDS Report than exist in the IRP TestPackage
     * and It is a CAT test package
     * then mark the missing items from the IRP package as NOTUSED.
     */
    @Test
    public void whenTDSReportContainsFewerItemsThanIRPTestPackage_NOTUSEDItemsMarkedAsNOTUSED() {
    	
        // The TDS Report has fewer Items than the IRP package
        final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
                new ItemAttributeBuilder().bankKey(100).key(1000).toOpportunityItem()
        );

        final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems);
        individualResponse.setCAT(true);

    	final Administration administration = generateTestPackageAdministration(generateTestItems2());
		
		when(testPackageService.getTestpackageByIdentifierUniqueid("test")).thenReturn(new TestspecificationBuilder("test")
				.setAdministration(administration)
				.toTestspecification()
				);

		// Act
		underTest.analyze(individualResponse);
		
        // Assert
        List<ItemCategory> itemCategories = individualResponse.getOpportunityCategory().getItemCategories();
        
        assertEquals(ItemStatusEnum.FOUND, itemCategories.get(0).getStatus());
        assertEquals(ItemStatusEnum.NOTUSED, itemCategories.get(1).getStatus());
    }
    
    /**
     * When there are equal Items in the given TDS Report and in the IRP Package
     * but a TDS Report Item does not exist in the IRP package then mark the item
     * from the TDS Report as extra.
     */
    @Test
    public void whenTDSReportAndIRPTestPackageContainsSameNumberOfItemsButTDSReportItemNotFound_ItemMarkedAsExtra() {
    
    	   // The TDS Report has same Items than the IRP package but one of the TDS Report Items
        // does not exist in the IRP package
        final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
                new ItemAttributeBuilder().bankKey(100).key(1000).toOpportunityItem(),
                new ItemAttributeBuilder().bankKey(300).key(3000).toOpportunityItem()
        );

        final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems);
    	
    	final Administration administration = generateTestPackageAdministration(generateTestItems2());
		
		when(testPackageService.getTestpackageByIdentifierUniqueid("test")).thenReturn(new TestspecificationBuilder("test")
				.setAdministration(administration)
				.toTestspecification()
				);

		// Act
		underTest.analyze(individualResponse);
		
	    // Assert
        List<ItemCategory> itemCategories = individualResponse.getOpportunityCategory().getItemCategories();
        
        assertEquals(ItemStatusEnum.FOUND, itemCategories.get(0).getStatus());
        assertEquals(ItemStatusEnum.MISSING, itemCategories.get(1).getStatus());
        assertEquals(ItemStatusEnum.EXTRA, itemCategories.get(2).getStatus());
        
    }
    
    /**
     * TDS Report Item does exist in the IRP TestPackage then mark the item
     * from the TDS Report as FOUND.
     * TDS Report Item format matches the IRP package format then mark the item
     * isItemFormatCorrect to true
     */
    @Test
    public void tdsReportItemFoundItemMarkedAsFound_FormatMatchIRPPackage_isItemFormatCorrectAsTrue() {
        // The TDS Report has same Items than the IRP package but one of the TDS Report Items
        // does not exist in the IRP package
        final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
                new ItemAttributeBuilder().bankKey(100).key(1000).format("EQ").toOpportunityItem(),
                new ItemAttributeBuilder().bankKey(200).key(2000).format("MS").toOpportunityItem(),
                new ItemAttributeBuilder().bankKey(300).key(3000).format("GI").toOpportunityItem()
        );

        final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems);
        
       	final Administration administration = generateTestPackageAdministration(generateTestItems2());
		
		when(testPackageService.getTestpackageByIdentifierUniqueid("test")).thenReturn(new TestspecificationBuilder("test")
				.setAdministration(administration)
				.toTestspecification()
				);
		
		// Act
		underTest.analyze(individualResponse);
	
	    // Assert
        List<ItemCategory> itemCategories = individualResponse.getOpportunityCategory().getItemCategories();
        assertEquals(ItemStatusEnum.FOUND, itemCategories.get(0).getStatus());
        assertEquals(true, itemCategories.get(0).isItemFormatCorrect());
        assertEquals(ItemStatusEnum.FOUND, itemCategories.get(1).getStatus());
        assertEquals(false, itemCategories.get(1).isItemFormatCorrect());
        assertEquals(ItemStatusEnum.EXTRA, itemCategories.get(2).getStatus());
        assertEquals(false, itemCategories.get(2).isItemFormatCorrect());

    }
    
	private List<Testitem> generateTestItems2() {
		final List<Testitem> testitems = Lists.newArrayList(
				new TestitemBuilder()
					.filename("item-100-1000.xml")
					.itemtype("EQ")
					.uniqueid("100-1000")
					.toTestitem(), 
				new TestitemBuilder()
					.filename("item-200-2000.xml")
					.itemtype("TI")
					.uniqueid("200-2000")
					.toTestitem());
		return testitems;
	}
	
	private List<Testitem> generateTestItems1() {
		final List<Testitem> testitems = Lists.newArrayList(
				new TestitemBuilder()
					.filename("item-100-1000.xml")
					.itemtype("EQ")
					.uniqueid("100-1000")
					.toTestitem()
			);
		return testitems;
	}
}
