package org.cresst.sb.irp.dao;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.EnumUtils;
import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.analysis.*;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Item;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class ItemAttributesAnalysisAction extends AnalysisAction {
	private static Logger logger = Logger.getLogger(ItemAttributesAnalysisAction.class);

	public enum EnumItemFieldName {
		position, segmentId, bankKey, key, operational, isSelected, format, score, scoreStatus, adminDate, numberVisits, mimeType, clientId, strand, contentLevel, pageNumber, pageVisits, pageTime, dropped;
	}

	public enum EnumFormatAcceptValues {
		associateInteraction, choiceInteraction, customInteraction, drawingInteraction, endAttemptInteraction, extendedTextInteraction, gapMatchInteraction, graphicAssociateInteraction, graphicGapMatchInteraction, graphicOrderInteraction, hotspotInteraction, hottextInteraction, inlineChoiceInteraction, matchInteraction, mediaInteraction, orderInteraction, positionObjectInteraction, selectPointInteraction, sliderInteraction, textEntryInteraction, uploadInteraction, EBSR, EQ, ER, GI, HT, HTQ, MC, MI, MS, NL, SA, TI, TUT, WER, WORDLIST, Stimulus
	}

	@Override
	public void analysis() throws IOException {
		IndividualResponse individualResponse = getIndividualResponse();
		TDSReport tdsReport = individualResponse.getTDSReport();
		OpportunityCategory opportunityCategory = individualResponse.getOpportunityCategory();
		List<ItemCategory> listItemCategory = opportunityCategory.getItemCategories();
		ItemCategory itemCategory;
		Opportunity opportunity = getOpportunity(tdsReport);
		List<Item> listItem = opportunity.getItem();
		for (Item i : listItem) {
			itemCategory = new ItemCategory();
			listItemCategory.add(itemCategory);
			analysisItemAttributes(itemCategory, i);
		}
	}

	/**
	 * Analyzes each of the given item's fields.
	 * @param itemCategory The ItemCategory to store the results of the analysis
	 * @param item The item to analyze
	 */
	private void analysisItemAttributes(ItemCategory itemCategory, Item item) {
		validateItem(itemCategory, item, item.getPosition(), EnumFieldCheckType.P, EnumItemFieldName.position);
		validateItem(itemCategory, item, item.getSegmentId(), EnumFieldCheckType.P, EnumItemFieldName.segmentId);
		validateItem(itemCategory, item, item.getBankKey(), EnumFieldCheckType.P, EnumItemFieldName.bankKey);
		validateItem(itemCategory, item, item.getKey(), EnumFieldCheckType.P, EnumItemFieldName.key);
		validateItem(itemCategory, item, item.getClientId(), EnumFieldCheckType.D, EnumItemFieldName.clientId);
		validateItem(itemCategory, item, item.getOperational(), EnumFieldCheckType.P, EnumItemFieldName.operational);
		validateItem(itemCategory, item, item.getIsSelected(), EnumFieldCheckType.PC, EnumItemFieldName.isSelected);
		validateItem(itemCategory, item, item.getFormat(), EnumFieldCheckType.PC, EnumItemFieldName.format);
		validateItem(itemCategory, item, item.getScore(), EnumFieldCheckType.PC, EnumItemFieldName.score);
		validateItem(itemCategory, item, item.getScoreStatus(), EnumFieldCheckType.D, EnumItemFieldName.scoreStatus);
		validateItem(itemCategory, item, item.getAdminDate(), EnumFieldCheckType.P, EnumItemFieldName.adminDate);
		validateItem(itemCategory, item, item.getNumberVisits(), EnumFieldCheckType.P, EnumItemFieldName.numberVisits);
		validateItem(itemCategory, item, item.getMimeType(), EnumFieldCheckType.P, EnumItemFieldName.mimeType);
		validateItem(itemCategory, item, item.getStrand(), EnumFieldCheckType.P, EnumItemFieldName.strand);
		validateItem(itemCategory, item, item.getContentLevel(), EnumFieldCheckType.P, EnumItemFieldName.contentLevel);
		validateItem(itemCategory, item, item.getPageNumber(), EnumFieldCheckType.P, EnumItemFieldName.pageNumber);
		validateItem(itemCategory, item, item.getPageVisits(), EnumFieldCheckType.P, EnumItemFieldName.pageVisits);
		validateItem(itemCategory, item, item.getPageTime(), EnumFieldCheckType.P, EnumItemFieldName.pageTime);
		validateItem(itemCategory, item, item.getDropped(), EnumFieldCheckType.P, EnumItemFieldName.dropped);
	}

	/**
	 * Sets up the FieldCheckType and CellCategory so the item's field can be evaluated.
	 * @param itemCategory The ItemCategory that the CellCategory is stored
	 * @param item The Item being evaluated
	 * @param value The value of the item's field that is being checked
	 * @param enumFieldCheckType Specifies the type of field check to perform
	 * @param enumItemFieldName The name of the field being analyzed
	 * @param <T> Since item has many different types of values this generic parameter was introduced to account for the differences.
	 */
	private <T> void validateItem(ItemCategory itemCategory, Item item, T value, EnumFieldCheckType enumFieldCheckType, EnumItemFieldName enumItemFieldName) {
		final FieldCheckType fieldCheckType = new FieldCheckType();
		fieldCheckType.setEnumfieldCheckType(enumFieldCheckType);

		final CellCategory cellCategory = new CellCategory();
		cellCategory.setTdsFieldName(enumItemFieldName.toString());
		cellCategory.setTdsFieldNameValue(Objects.toString(value, ""));
		cellCategory.setFieldCheckType(fieldCheckType);

		itemCategory.addCellCategory(cellCategory);

		validateField(item, enumFieldCheckType, enumItemFieldName, fieldCheckType);
	}

	private void validateField(Item item, EnumFieldCheckType enumFieldCheckType, EnumItemFieldName enumFieldName,
			FieldCheckType fieldCheckType) {

		switch (enumFieldCheckType) {
			case D:
				break;
			case P:
				checkP(item, enumFieldName, fieldCheckType);
				break;
			case PC:
				checkP(item, enumFieldName, fieldCheckType);
				// checkC(accommodation, enumFieldName, fieldCheckType);
				break;
		}
	}

	// Field Check Type (P) --> check that field is not empty, and field value is of correct data type
	// and within acceptable values
	private void checkP(Item item, EnumItemFieldName enumFieldName, FieldCheckType fieldCheckType) {
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
				processP(item.getScoreStatus(), fieldCheckType, false); //last param: required: N
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
				// 	<xs:attribute name="contentLevel" use="required" />
				processP_PritableASCIIone(item.getContentLevel(), fieldCheckType);
				break;
			case pageNumber:
				// <xs:attribute name="pageNumber" use="required" type="xs:unsignedInt" />
				processP_Positive32bit(Long.toString(item.getPageNumber()), fieldCheckType);
				break;
			case pageVisits:
				// 	<xs:attribute name="pageVisits" use="required" type="xs:unsignedInt" />
				processP_Positive32bit(Long.toString(item.getPageVisits()), fieldCheckType);
				break;
			case pageTime:
				// <xs:attribute name="pageTime" use="required" type="xs:int" />
				processP_Positive32bit(Integer.toString(item.getPageTime()), fieldCheckType);
				break;
			case dropped:
				// <xs:attribute name="dropped" use="required" type="Bit" />
				//	<xs:simpleType name="Bit">
				//<xs:restriction base="xs:unsignedByte">
				//<xs:minInclusive value="0" />
				//<xs:maxInclusive value="1" />
				//</xs:restriction>
				//</xs:simpleType>
				processP(Short.toString(item.getDropped()), fieldCheckType, true); //last param: required Y
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("checkP exception: ", e);
		}
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
