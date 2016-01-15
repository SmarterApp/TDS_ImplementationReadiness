package org.cresst.sb.irp.analysis.engine;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.List;

import com.google.common.collect.Lists;

import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.ItemCategory;
import org.cresst.sb.irp.domain.analysis.OpItemsData;
import org.cresst.sb.irp.domain.analysis.OpportunityCategory;
import org.cresst.sb.irp.domain.analysis.TestPropertiesCategory;
import org.cresst.sb.irp.domain.analysis.ItemCategory.ItemStatusEnum;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.testpackage.Administration;
import org.cresst.sb.irp.domain.testpackage.Bpelement;
import org.cresst.sb.irp.domain.testpackage.Itempool;
import org.cresst.sb.irp.domain.testpackage.Testblueprint;
import org.cresst.sb.irp.domain.testpackage.Testitem;
import org.cresst.sb.irp.domain.teststudentmapping.TestStudentMapping;
import org.cresst.sb.irp.service.TestPackageService;
import org.cresst.sb.irp.service.TestStudentMappingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import builders.BpelementBuilder;
import builders.ItemAttributeBuilder;
import builders.OpItemsDataBuilder;
import builders.TestStudentMappingBuilder;
import builders.TestitemBuilder;
import builders.TestspecificationBuilder;

/**
 * Test class to verify that Items are analyzed correctly by ItemAttributeAnalysisAction
 */
@RunWith(MockitoJUnitRunner.class)
public class ItemAttributesAnalysisActionTest {

	@Mock
	public TestPackageService testPackageService;
	
	@Mock
	public TestStudentMappingService testStudentMappingService;

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
	
	private Administration generateTestPackageAdministration(List<Testitem> irptestpackageTestitems, List<Bpelement> bpelements) {
	
		final Administration administration = new Administration();
		
		final Testblueprint testblueprint = new Testblueprint();
		administration.setTestblueprint(testblueprint);
		if (bpelements != null)
			testblueprint.getBpelement().addAll(bpelements);
		
		Itempool itempool = new Itempool();
		administration.setItempool(itempool);
		if (irptestpackageTestitems != null) {
			itempool.getTestitem().addAll(irptestpackageTestitems);
		}
		
		return administration;
	}

	private List<Bpelement> generateBpelements() {
		final List<Bpelement> bpelements = Lists.newArrayList(
				new BpelementBuilder()
				.elementtype("test")
				.minopitems("2")
				.maxopitems("2")
				.toBpelement(),
				new BpelementBuilder()
				.elementtype("segment")
				.minopitems("1")
				.maxopitems("1")
				.toBpelement()); 
		return bpelements;
	}
	
	private List<Testitem> generateTestItemForTestPackage() {
		final List<Testitem> testitems = Lists.newArrayList(
				new TestitemBuilder()
					.filename("item-100-1000.xml")
					.itemtype("EQ")
					.uniqueid("100-1000")
					.toTestitem()
			);
		return testitems;
	}
	
	private List<Testitem> generateTestItemsForTestPackage() {
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
	
	private List<TestStudentMapping> generateTestStudentMappings(){
		final List<TestStudentMapping> testStudentMappings = Lists.newArrayList(
				new TestStudentMappingBuilder()
					.test("test")
					.testType("Combined")
					.componentTestName("testPerf")
					.toTestStudentMapping(), 
				new TestStudentMappingBuilder()
					.test("test")
					.testType("Combined")
					.componentTestName("testCat")
					.isCAT(true)
					.toTestStudentMapping());
		return testStudentMappings;
	}
	
	/**
	 * TestType - Single
	 * isCAT - false
	 * When there are equal Items in the given TDS Report and in the IRP TestPackage then mark the items from the IRP package as
	 * found.
	 * 
	 */
	@Test
	public void whenTDSReportContainsSameItemsAsIRPTestPackage_ItemsMarkedAsFound_Single_Perf() {

		// The TDS Report has same Items than the IRP package
		final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
				new ItemAttributeBuilder().bankKey(100).key(1000).format("EQ").toOpportunityItem(),
				new ItemAttributeBuilder().bankKey(200).key(2000).format("TI").toOpportunityItem());

		final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems);

		final Administration administration =
				generateTestPackageAdministration(generateTestItemsForTestPackage(), generateBpelements());

		when(testPackageService.getTestpackageByIdentifierUniqueid("test")).thenReturn(new TestspecificationBuilder("test")
				.setAdministration(administration)
				.toTestspecification()
				);
		
		when(testStudentMappingService.getTestStudentMappingsByTestName("test")).thenReturn(null);
		
	    // Act
        underTest.analyze(individualResponse);
        
        // Assert
        OpportunityCategory returnedOpportunityCategory = individualResponse.getOpportunityCategory();
        OpItemsData actualOpItemsData = returnedOpportunityCategory.getOpItemsData();
		OpItemsData expectedOpItemsData = new OpItemsDataBuilder()
			.isCombo(false)
			.elementtype("test")
			.minopitems(2)
			.maxopitems(2)
			.catItems(0)
			.perfItems(2)
			.isMinopitems(true)
			.isMaxopitems(true)
			.toOpItemsData();

        List<ItemCategory> itemCategories = returnedOpportunityCategory.getItemCategories();
        assertEquals(ItemStatusEnum.FOUND, itemCategories.get(0).getStatus());
        assertEquals(ItemStatusEnum.FOUND, itemCategories.get(1).getStatus());
        assertEquals(actualOpItemsData.getPerfItems(), 2);
        assertEquals(actualOpItemsData.getCatItems(), 0);
        assertFalse(actualOpItemsData.isCombo());
        assertTrue(actualOpItemsData.isMinopitems());
        assertTrue(actualOpItemsData.isMaxopitems());
       // assertEquals(expectedOpItemsData, actualOpItemsData);
	}
	
	/**
	 * TestType - Single
	 * isCAT - true
	 * When there are equal Items in the given TDS Report and in the IRP TestPackage then mark the items from the IRP package as
	 * found.
	 * 
	 */
	@Test
	public void whenTDSReportContainsSameItemsAsIRPTestPackage_ItemsMarkedAsFound_Single_CAT() {

		// The TDS Report has same Items than the IRP package
		final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
				new ItemAttributeBuilder().bankKey(100).key(1000).format("EQ").toOpportunityItem(),
				new ItemAttributeBuilder().bankKey(200).key(2000).format("TI").toOpportunityItem());

		final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems);
		individualResponse.setCAT(true);;

		final Administration administration =
				generateTestPackageAdministration(generateTestItemsForTestPackage(), generateBpelements());

		when(testPackageService.getTestpackageByIdentifierUniqueid("test")).thenReturn(new TestspecificationBuilder("test")
				.setAdministration(administration)
				.toTestspecification()
				);
		
		when(testStudentMappingService.getTestStudentMappingsByTestName("test")).thenReturn(null);
		
	    // Act
        underTest.analyze(individualResponse);
        
        // Assert
        OpportunityCategory returnedOpportunityCategory = individualResponse.getOpportunityCategory();
        OpItemsData actualOpItemsData = returnedOpportunityCategory.getOpItemsData();
		OpItemsData expectedOpItemsData = new OpItemsDataBuilder()
			.isCombo(false)
			.elementtype("test")
			.minopitems(2)
			.maxopitems(2)
			.catItems(2)
			.perfItems(0)
			.isMinopitems(true)
			.isMaxopitems(true)
			.toOpItemsData();
        List<ItemCategory> itemCategories = returnedOpportunityCategory.getItemCategories();
        assertEquals(ItemStatusEnum.FOUND, itemCategories.get(0).getStatus());
        assertEquals(ItemStatusEnum.FOUND, itemCategories.get(1).getStatus());
        assertEquals(actualOpItemsData.getPerfItems(), 0);
        assertEquals(actualOpItemsData.getCatItems(), 2);
        assertFalse(actualOpItemsData.isCombo());
        assertTrue(actualOpItemsData.isMinopitems());
        assertTrue(actualOpItemsData.isMaxopitems());
        //assertEquals(expectedOpItemsData, actualOpItemsData);
	}
	
	
	  /**
     * When there are more Items in the given TDS Report than exist in the IRP TestPackage
     * then mark the extraneous Items found in the TDS Report as being invalid.
     */
    @Test
    public void whenTDSReportContainsMoreItemsThanIRPTestPackage_ExtraneousTDSReportItemsMarkedAsExtra() {
    	
        // The TDS Report has more items than the IRP Package
        final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
                new ItemAttributeBuilder().bankKey(100).key(1000).format("EQ").toOpportunityItem(),
                new ItemAttributeBuilder().bankKey(200).key(2000).format("TI").toOpportunityItem()
        );
    	
    	final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems);
    	
		final Administration administration =
				generateTestPackageAdministration(generateTestItemForTestPackage(), generateBpelements());

		when(testPackageService.getTestpackageByIdentifierUniqueid("test")).thenReturn(new TestspecificationBuilder("test")
				.setAdministration(administration)
				.toTestspecification()
				);

		when(testStudentMappingService.getTestStudentMappingsByTestName("test")).thenReturn(generateTestStudentMappings());
		
	    // Act
        underTest.analyze(individualResponse);
        
        // Assert
        List<ItemCategory> itemCategories = individualResponse.getOpportunityCategory().getItemCategories();
        
        assertEquals(ItemStatusEnum.FOUND, itemCategories.get(0).getStatus());
        assertEquals(ItemStatusEnum.EXTRA, itemCategories.get(1).getStatus());
    }

    /**
     * for TestType - Single
     * When there are less Items in the given TDS Report than exist in the IRP TestPackage
     * then mark the missing items from the IRP package as missing.
     */
    @Test
    public void whenTDSReportContainsFewerItemsThanIRPTestPackage_MissingItemsMarkedAsMissing_Single() {
    	
        // The TDS Report has fewer Items than the IRP package
        final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
                new ItemAttributeBuilder().bankKey(100).key(1000).toOpportunityItem()
        );

        final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems);

		final Administration administration =
				generateTestPackageAdministration(generateTestItemsForTestPackage(), generateBpelements());
		
		when(testPackageService.getTestpackageByIdentifierUniqueid("test")).thenReturn(new TestspecificationBuilder("test")
				.setAdministration(administration)
				.toTestspecification()
				);
		
		when(testStudentMappingService.getTestStudentMappingsByTestName("test")).thenReturn(generateTestStudentMappings());

		// Act
		underTest.analyze(individualResponse);
		
        // Assert
        List<ItemCategory> itemCategories = individualResponse.getOpportunityCategory().getItemCategories();
        
        assertEquals(ItemStatusEnum.FOUND, itemCategories.get(0).getStatus());
        assertEquals(ItemStatusEnum.MISSING, itemCategories.get(1).getStatus());
    }
    
    /**
     * for TestType - Single
     * When there are less Items in the given TDS Report than exist in the IRP TestPackage
     * and It is a CAT test package
     * then mark the missing items from the IRP package as NOTUSED.
     */
    @Test
    public void whenTDSReportContainsFewerItemsThanIRPTestPackage_NOTUSEDItemsMarkedAsNOTUSED_Single() {
    	
        // The TDS Report has fewer Items than the IRP package
        final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
                new ItemAttributeBuilder().bankKey(100).key(1000).toOpportunityItem()
        );

        final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems);
        individualResponse.setCAT(true);

		final Administration administration =
				generateTestPackageAdministration(generateTestItemsForTestPackage(), generateBpelements());
	
		when(testPackageService.getTestpackageByIdentifierUniqueid("test")).thenReturn(new TestspecificationBuilder("test")
				.setAdministration(administration)
				.toTestspecification()
				);

		when(testStudentMappingService.getTestStudentMappingsByTestName("test")).thenReturn(null);
		
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
                new ItemAttributeBuilder().bankKey(100).key(1000).format("EQ").toOpportunityItem(),
                new ItemAttributeBuilder().bankKey(300).key(3000).format("GI").toOpportunityItem()
        );

        final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems);
    	
		final Administration administration =
				generateTestPackageAdministration(generateTestItemsForTestPackage(), generateBpelements());
		
		when(testPackageService.getTestpackageByIdentifierUniqueid("test")).thenReturn(new TestspecificationBuilder("test")
				.setAdministration(administration)
				.toTestspecification()
				);

		when(testStudentMappingService.getTestStudentMappingsByTestName("test")).thenReturn(null);
		
		// Act
		underTest.analyze(individualResponse);
		
	    // Assert
        List<ItemCategory> itemCategories = individualResponse.getOpportunityCategory().getItemCategories();
        
        assertEquals(ItemStatusEnum.FOUND, itemCategories.get(0).getStatus());
        assertEquals(ItemStatusEnum.MISSING, itemCategories.get(1).getStatus());
        assertEquals(ItemStatusEnum.EXTRA, itemCategories.get(2).getStatus());
        
    }
    
    /**
     * for TestType - Combo
     * CAT - true because tds Items key 200-2000 matches uniqueid in administrationCat
     * When there are less Items in the given TDS Report than exist in the IRP TestPackage
     * then mark the missing items from the IRP package as missing.
     */
    @Test
    public void whenTDSReportContainsFewerItemsThanIRPTestPackage_MissingItemsMarkedAsMissing_Combo() {
    	
        // The TDS Report has fewer Items than the IRP package
        final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
                new ItemAttributeBuilder().bankKey(200).key(2000).format("TI").toOpportunityItem()
        );

        final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems);
        individualResponse.setCombo(true);

		final Administration administration =
				generateTestPackageAdministration(generateTestItemsForTestPackage(), generateBpelements());
		
		when(testPackageService.getTestpackageByIdentifierUniqueid("test")).thenReturn(new TestspecificationBuilder("test")
				.setAdministration(administration)
				.toTestspecification()
				);
		
		when(testStudentMappingService.getTestStudentMappingsByTestName("test")).thenReturn(generateTestStudentMappings());

		final Administration administrationPerf = generateTestPackageAdministration(Lists.newArrayList(
				new TestitemBuilder()
					.filename("item-100-1000.xml")
					.itemtype("EQ")
					.uniqueid("100-1000")
					.toTestitem()), null);
		when(testPackageService.getTestpackageByIdentifierUniqueid("testPerf")).thenReturn(new TestspecificationBuilder("testPerf")
			.setAdministration(administrationPerf)
			.toTestspecification()
		);
		
		final Administration administrationCat = generateTestPackageAdministration(Lists.newArrayList(
				new TestitemBuilder()
					.filename("item-500-4000.xml")
					.itemtype("EQ")
					.uniqueid("500-4000")
					.toTestitem(),
				new TestitemBuilder()
					.filename("item-200-2000.xml")
					.itemtype("TI")
					.uniqueid("200-2000")
					.toTestitem()), null);
		when(testPackageService.getTestpackageByIdentifierUniqueid("testCat")).thenReturn(new TestspecificationBuilder("testCat")
			.setAdministration(administrationCat)
			.toTestspecification()
		);
		
		// Act
		underTest.analyze(individualResponse);
		
        // Assert
	    OpportunityCategory returnedOpportunityCategory = individualResponse.getOpportunityCategory();
	    OpItemsData actualOpItemsData = returnedOpportunityCategory.getOpItemsData();
		OpItemsData expectedOpItemsData = new OpItemsDataBuilder()
				.isCombo(true)
				.elementtype("test")
				.minopitems(2)
				.maxopitems(2)
				.catItems(1)
				.perfItems(0)
				.isMinopitems(false)
				.isMaxopitems(true)
				.toOpItemsData();
        List<ItemCategory> itemCategories = returnedOpportunityCategory.getItemCategories();
        
        assertEquals(ItemStatusEnum.MISSING, itemCategories.get(0).getStatus());
        assertEquals(ItemStatusEnum.FOUND, itemCategories.get(1).getStatus());
        assertEquals(actualOpItemsData.getPerfItems(), 0);
        assertEquals(actualOpItemsData.getCatItems(), 1);
        assertTrue(actualOpItemsData.isCombo());
        assertFalse(actualOpItemsData.isMinopitems());
        assertTrue(actualOpItemsData.isMaxopitems());
        //assertEquals(expectedOpItemsData, actualOpItemsData);
    }
    
    /**
     * for TestType - Combo
     * CAT - false because tds Items key 100-1000 matches uniqueid in administrationPerf
     * When there are less Items in the given TDS Report than exist in the IRP TestPackage
     * and It is a CAT test package
     * then mark the missing items from the IRP package as NOTUSED.
     */
    @Test
    public void whenTDSReportContainsFewerItemsThanIRPTestPackage_NOTUSEDItemsMarkedAsNOTUSED_Combo() {
    	
        // The TDS Report has fewer Items than the IRP package
        final List<TDSReport.Opportunity.Item> tdsReportItems = Lists.newArrayList(
                new ItemAttributeBuilder().bankKey(100).key(1000).format("EQ").toOpportunityItem()
        );

        final IndividualResponse individualResponse = generateIndividualResponse(tdsReportItems);
        individualResponse.setCombo(true);

		final Administration administration =
				generateTestPackageAdministration(generateTestItemsForTestPackage(), generateBpelements());
	
		when(testPackageService.getTestpackageByIdentifierUniqueid("test")).thenReturn(new TestspecificationBuilder("test")
				.setAdministration(administration)
				.toTestspecification()
				);
		
		when(testStudentMappingService.getTestStudentMappingsByTestName("test")).thenReturn(generateTestStudentMappings());

		final Administration administrationPerf = generateTestPackageAdministration(Lists.newArrayList(
				new TestitemBuilder()
					.filename("item-100-1000.xml")
					.itemtype("EQ")
					.uniqueid("100-1000")
					.toTestitem()), generateBpelements());
		when(testPackageService.getTestpackageByIdentifierUniqueid("testPerf")).thenReturn(new TestspecificationBuilder("testPerf")
			.setAdministration(administrationPerf)
			.toTestspecification()
		);
		
		final Administration administrationCat = generateTestPackageAdministration(Lists.newArrayList(
				new TestitemBuilder()
					.filename("item-500-4000.xml")
					.itemtype("EQ")
					.uniqueid("500-4000")
					.toTestitem(),
				new TestitemBuilder()
					.filename("item-200-2000.xml")
					.itemtype("TI")
					.uniqueid("200-2000")
					.toTestitem()), generateBpelements());
		when(testPackageService.getTestpackageByIdentifierUniqueid("testCat")).thenReturn(new TestspecificationBuilder("testCat")
			.setAdministration(administrationCat)
			.toTestspecification()
		);
		
		// Act
		underTest.analyze(individualResponse);
		
        // Assert
	    OpportunityCategory returnedOpportunityCategory = individualResponse.getOpportunityCategory();
	    OpItemsData actualOpItemsData = returnedOpportunityCategory.getOpItemsData();
		OpItemsData expectedOpItemsData = new OpItemsDataBuilder()
				.isCombo(true)
				.elementtype("test")
				.minopitems(2)
				.maxopitems(2)
				.catItems(0)
				.perfItems(1)
				.isMinopitems(false)
				.isMaxopitems(true)
				.toOpItemsData();
        List<ItemCategory> itemCategories = returnedOpportunityCategory.getItemCategories();
        
        assertEquals(ItemStatusEnum.FOUND, itemCategories.get(0).getStatus());
        assertEquals(ItemStatusEnum.NOTUSED, itemCategories.get(1).getStatus());
        assertEquals(actualOpItemsData.getPerfItems(), 1);
        assertEquals(actualOpItemsData.getCatItems(), 0);
        assertTrue(actualOpItemsData.isCombo());
        assertFalse(actualOpItemsData.isMinopitems());
        assertTrue(actualOpItemsData.isMaxopitems());
        //assertEquals(expectedOpItemsData, actualOpItemsData);
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
        
		final Administration administration =
				generateTestPackageAdministration(generateTestItemsForTestPackage(), generateBpelements());
		
		when(testPackageService.getTestpackageByIdentifierUniqueid("test")).thenReturn(new TestspecificationBuilder("test")
				.setAdministration(administration)
				.toTestspecification()
				);
		
		when(testStudentMappingService.getTestStudentMappingsByTestName("test")).thenReturn(null);
		
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
    
}
