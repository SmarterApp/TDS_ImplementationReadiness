package org.cresst.sb.irp.analysis.engine;

import org.apache.commons.lang3.EnumUtils;
import org.cresst.sb.irp.domain.analysis.*;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.analysis.ItemCategory.ItemStatusEnum;
import org.cresst.sb.irp.domain.studentresponse.StudentResponse;
import org.cresst.sb.irp.domain.studentresponse.TestItemResponse;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ItemAttributesAnalysisAction extends AnalysisAction<Item, ItemAttributesAnalysisAction.EnumItemFieldName, Object> {
	private final static Logger logger = LoggerFactory.getLogger(ItemAttributesAnalysisAction.class);

	static public enum EnumItemFieldName {
		position, segmentId, bankKey, key, operational, isSelected, format, score, scoreStatus, adminDate, numberVisits, mimeType, clientId, strand, contentLevel, pageNumber, pageVisits, pageTime, dropped
	}

	static public enum EnumFormatAcceptValues {
		associateInteraction, choiceInteraction, customInteraction, drawingInteraction, endAttemptInteraction, extendedTextInteraction, gapMatchInteraction, graphicAssociateInteraction, graphicGapMatchInteraction, graphicOrderInteraction, hotspotInteraction, hottextInteraction, inlineChoiceInteraction, matchInteraction, mediaInteraction, orderInteraction, positionObjectInteraction, selectPointInteraction, sliderInteraction, textEntryInteraction, uploadInteraction, EBSR, EQ, ER, GI, HT, HTQ, MC, MI, MS, NL, SA, TI, TUT, WER, WORDLIST, Stimulus
	}

	@Override
	public void analyze(IndividualResponse individualResponse) {
		try {
			TDSReport tdsReport = individualResponse.getTDSReport();
			ExamineeCategory examineeCategory = individualResponse.getExamineeCategory();
			String examineeKey = getTdsFieldNameValueByFieldName(examineeCategory.getCellCategories(), "key");

			Opportunity opportunity = tdsReport.getOpportunity();
			List<Item> listItem = opportunity.getItem();

			TestItemResponse testItemResponse = getTestItemResponseByStudentID(Long.parseLong(examineeKey));
			List<StudentResponse> listStudentResponse = testItemResponse.getStudentResponses();
			
			List<Item> removedList = new ArrayList<Item>();
			
			List<ItemCategory> itemCategories = new ArrayList<>();
			for(StudentResponse sr: listStudentResponse){
				ItemCategory itemCategory = new ItemCategory();
				itemCategories.add(itemCategory);
				logger.info("bankkey {} and id {} listItem. size {} ", sr.getBankKey(), sr.getId(), listItem.size());
				Item item = itemExistByIRPpackageBankKeyKey(sr.getBankKey(), sr.getId(), listItem);
				if(item != null){
					analyzeItemAttributes(itemCategory, item);
					itemCategory.setStatus(ItemStatusEnum.FOUND);
					//listItem.remove(item);
					removedList.add(item);
					logger.info("removedList size --->{} ", removedList.size());
				}else {
					logger.info("else else bankKey {} abd ID {}.", sr.getBankKey(), sr.getId());
					analyzeItemAttributes(itemCategory, sr);
					itemCategory.setStatus(ItemStatusEnum.MISSING);
				}
			}
			
			for (Item item : listItem) {
				logger.info("BBkey {} abd Key....", item.getBankKey(), item.getKey());
				ItemCategory itemCategory = new ItemCategory();
				itemCategories.add(itemCategory);
				analyzeItemAttributes2(itemCategory, item);
				itemCategory.setStatus(ItemStatusEnum.EXTRA);
			}
			
			OpportunityCategory opportunityCategory = individualResponse.getOpportunityCategory();
			opportunityCategory.setItemCategories(itemCategories);
		} catch (Exception ex) {
			logger.error("Analyze exception", ex);
		}
	}

	protected Item itemExistByIRPpackageBankKeyKey(String bankKey, String key, List<Item> listItem){
		for (Item item : listItem) {
			String itemBankKey = Long.toString(item.getBankKey()).trim();
			String itemKey = Long.toString(item.getKey()).trim();
			if (itemBankKey.equalsIgnoreCase(bankKey) && itemKey.equalsIgnoreCase(key)){
				return item;
			}
		}
		return null;
	}
	
	private void analyzeItemAttributes(Category itemCategory, StudentResponse sr) {
		FieldCheckType fieldCheckType = new FieldCheckType();
		fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
		
		CellCategory cellCategory = new CellCategory();
		cellCategory.setTdsFieldName(EnumItemFieldName.bankKey.toString());
		cellCategory.setTdsFieldNameValue(sr.getBankKey());
		cellCategory.setTdsExpectedValue(expectedValue(null, EnumItemFieldName.bankKey));
		cellCategory.setFieldCheckType(fieldCheckType);
		itemCategory.addCellCategory(cellCategory);

		fieldCheckType = new FieldCheckType();
		fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
		
		cellCategory = new CellCategory();
		cellCategory.setTdsFieldName(EnumItemFieldName.key.toString());
		cellCategory.setTdsFieldNameValue(sr.getId());
		cellCategory.setTdsExpectedValue(expectedValue(null, EnumItemFieldName.key));
		cellCategory.setFieldCheckType(fieldCheckType);
		itemCategory.addCellCategory(cellCategory);
	}
	
	/**
	 * Analyzes each of the given item's fields.
	 *
	 * @param itemCategory
	 *            The ItemCategory to store the results of the analyze
	 * @param item
	 *            The item to analyze
	 */
	private void analyzeItemAttributes(Category itemCategory, Item item) {
		validate(itemCategory, item, item.getPosition(), EnumFieldCheckType.P, EnumItemFieldName.position, null);
		validate(itemCategory, item, item.getSegmentId(), EnumFieldCheckType.P, EnumItemFieldName.segmentId, null);
		validate(itemCategory, item, item.getBankKey(), EnumFieldCheckType.P, EnumItemFieldName.bankKey, null);
		validate(itemCategory, item, item.getKey(), EnumFieldCheckType.P, EnumItemFieldName.key, null);
		validate(itemCategory, item, item.getClientId(), EnumFieldCheckType.D, EnumItemFieldName.clientId, null);
		validate(itemCategory, item, item.getOperational(), EnumFieldCheckType.P, EnumItemFieldName.operational, null);
		validate(itemCategory, item, item.getIsSelected(), EnumFieldCheckType.PC, EnumItemFieldName.isSelected, null);
		validate(itemCategory, item, item.getFormat(), EnumFieldCheckType.PC, EnumItemFieldName.format, null);
		validate(itemCategory, item, item.getScore(), EnumFieldCheckType.PC, EnumItemFieldName.score, null);
		validate(itemCategory, item, item.getScoreStatus(), EnumFieldCheckType.D, EnumItemFieldName.scoreStatus, null);
		validate(itemCategory, item, item.getAdminDate(), EnumFieldCheckType.P, EnumItemFieldName.adminDate, null);
		validate(itemCategory, item, item.getNumberVisits(), EnumFieldCheckType.P, EnumItemFieldName.numberVisits, null);
		validate(itemCategory, item, item.getMimeType(), EnumFieldCheckType.P, EnumItemFieldName.mimeType, null);
		validate(itemCategory, item, item.getStrand(), EnumFieldCheckType.P, EnumItemFieldName.strand, null);
		validate(itemCategory, item, item.getContentLevel(), EnumFieldCheckType.P, EnumItemFieldName.contentLevel, null);
		validate(itemCategory, item, item.getPageNumber(), EnumFieldCheckType.P, EnumItemFieldName.pageNumber, null);
		validate(itemCategory, item, item.getPageVisits(), EnumFieldCheckType.P, EnumItemFieldName.pageVisits, null);
		validate(itemCategory, item, item.getPageTime(), EnumFieldCheckType.P, EnumItemFieldName.pageTime, null);
		validate(itemCategory, item, item.getDropped(), EnumFieldCheckType.P, EnumItemFieldName.dropped, null);
	}

	/**
	 * Analyzes each of the given item's fields.
	 *
	 * @param itemCategory
	 *            The ItemCategory to store the results of the analyze
	 * @param item
	 *            The item to analyze
	 */
	private void analyzeItemAttributes2(Category itemCategory, Item item) {
		validate(itemCategory, item, item.getBankKey(), EnumFieldCheckType.P, EnumItemFieldName.bankKey, null);
		validate(itemCategory, item, item.getKey(), EnumFieldCheckType.P, EnumItemFieldName.key, null);
	}
	
	/**
	 * Field Check Type (P) --> check that field is not empty, and field value is of correct data type and within acceptable
	 * values
	 *
	 * @param item
	 *            Item with fields to check
	 * @param enumFieldName
	 *            Specifies the field to check
	 * @param fieldCheckType
	 *            This is where results are stored
	 */
	@Override
	protected void checkP(Item item, EnumItemFieldName enumFieldName, FieldCheckType fieldCheckType) {
		try {
			switch (enumFieldName) {
			case position:
				// <xs:attribute name="position" use="required" type="xs:unsignedInt" />
				processP_Positive32bit(Long.toString(item.getPosition()), fieldCheckType);
				break;
			case segmentId:
				// <xs:attribute name="segmentId" use="required" />
				processP_PritableASCIIone(item.getSegmentId(), fieldCheckType);
				break;
			case bankKey:
				// <xs:attribute name="bankKey" use="required" type="xs:unsignedInt" /> vs dox, need to check
				processP_PritableASCIIone(Long.toString(item.getBankKey()), fieldCheckType);
				break;
			case key:
				// <xs:attribute name="key" use="required" type="xs:unsignedInt" />
				processP_Positive64bit(item.getKey(), fieldCheckType);
				break;
			case operational:
				// <xs:attribute name="operational" use="required" type="Bit" />
				processP_AcceptValue(item.getOperational(), fieldCheckType, 0, 1);
				break;
			case isSelected:
				// <xs:attribute name="isSelected" use="required" type="Bit" />
				processP_AcceptValue(item.getIsSelected(), fieldCheckType, 0, 1);
				break;
			case format:
				// <xs:attribute name="format" use="required" />
				processAcceptableEnum(item.getFormat(), fieldCheckType, EnumFormatAcceptValues.class);
				break;
			case score:
				// <xs:attribute name="score" use="required" type="UFloatAllowNegativeOne" />
				validateUnsignedFloat(item.getScore(), fieldCheckType, -1);
				break;
			case scoreStatus:
				// <xs:attribute name="scoreStatus">
				// <xs:simpleType>
				// <xs:restriction base="xs:token">
				// <xs:enumeration value="NOTSCORED" />
				// <xs:enumeration value="SCORED" />
				// <xs:enumeration value="WAITINGFORMACHINESCORE" />
				// <xs:enumeration value="SCORINGERROR" />
				// <!-- future -->
				// <xs:enumeration value="APPEALED" />
				// </xs:restriction>
				// </xs:simpleType>
				processP(item.getScoreStatus(), fieldCheckType, false); // last param: required: N
				break;
			case adminDate:
				// <xs:attribute name="adminDate" use="required" type="xs:dateTime" />
				processP(item.getAdminDate().toString(), fieldCheckType, true);
				break;
			case numberVisits:
				// <xs:attribute name="numberVisits" use="required" type="xs:unsignedInt" />
				processP_Positive32bit(Long.toString(item.getNumberVisits()), fieldCheckType);
				break;
			case mimeType:
				// <xs:attribute name="mimeType" use="required">
				// <xs:simpleType>
				// <xs:restriction base="xs:token">
				// <xs:enumeration value="text/plain" />
				// <xs:enumeration value="text/xml" />
				// <xs:enumeration value="text/html" />
				// <xs:enumeration value="audio/ogg" />
				// </xs:restriction>
				// </xs:simpleType>
				processP_PritableASCIIone(item.getMimeType(), fieldCheckType);
				break;
			case strand:
				// <xs:attribute name="strand" use="required" />
				processP_PritableASCIIone(item.getStrand(), fieldCheckType);
				break;
			case contentLevel:
				// <xs:attribute name="contentLevel" use="required" />
				processP_PritableASCIIone(item.getContentLevel(), fieldCheckType);
				break;
			case pageNumber:
				// <xs:attribute name="pageNumber" use="required" type="xs:unsignedInt" />
				processP_Positive32bit(Long.toString(item.getPageNumber()), fieldCheckType);
				break;
			case pageVisits:
				// <xs:attribute name="pageVisits" use="required" type="xs:unsignedInt" />
				processP_Positive32bit(Long.toString(item.getPageVisits()), fieldCheckType);
				break;
			case pageTime:
				// <xs:attribute name="pageTime" use="required" type="xs:int" />
				processP_Positive32bit(Integer.toString(item.getPageTime()), fieldCheckType);
				break;
			case dropped:
				// <xs:attribute name="dropped" use="required" type="Bit" />
				// <xs:simpleType name="Bit">
				// <xs:restriction base="xs:unsignedByte">
				// <xs:minInclusive value="0" />
				// <xs:maxInclusive value="1" />
				// </xs:restriction>
				// </xs:simpleType>
				processP(Short.toString(item.getDropped()), fieldCheckType, true); // last param: required Y
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("checkP exception: ", e);
		}
	}

	/**
	 * No op for now
	 *
	 * @param item
	 *            Item with fields to check
	 * @param enumFieldName
	 *            Specifies the field to check
	 * @param fieldCheckType
	 *            This is where results are stored
	 * @param unused
	 *            Unused parameter
	 */
	@Override
	protected void checkC(Item item, EnumItemFieldName enumFieldName, FieldCheckType fieldCheckType, Object unused) {
	}

	private void processAcceptableEnum(String fieldValue, FieldCheckType fieldCheckType, Class<EnumFormatAcceptValues> class1) {
		try {
			if (fieldValue != null && !fieldValue.trim().isEmpty()) {
				if (EnumUtils.isValidEnum(class1, fieldValue)) {
					setPcorrect(fieldCheckType);
				}
			}
		} catch (Exception e) {
			logger.error("processAcceptableEnum exception: ", e);
		}
	}
	
}
