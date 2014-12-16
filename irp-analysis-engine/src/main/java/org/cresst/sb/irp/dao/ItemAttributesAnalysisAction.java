package org.cresst.sb.irp.dao;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.EnumUtils;
import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.ItemCategory;
import org.cresst.sb.irp.domain.analysis.OpportunityCategory;
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
		try {
			IndividualResponse individualResponse = getIndividualResponse();
			TDSReport tdsReport = individualResponse.getTDSReport();
			OpportunityCategory opportunityCategory = individualResponse.getOpportunityCategory();
			List<ItemCategory> listItemCategory = opportunityCategory.getListItemCategory();
			ItemCategory itemCategory;
			Opportunity opportunity = getOpportunity(tdsReport);
			List<Item> listItem = opportunity.getItem();
			for (Item i : listItem) {
				itemCategory = new ItemCategory();
				listItemCategory.add(itemCategory);
				analysisItemAttributes(itemCategory, i);
			}
		} catch (Exception e) {
			logger.error("analysis exception: ", e);
		}
	}

	private void analysisItemAttributes(ItemCategory itemCategory, Item item) {
		try {
			FieldCheckType fieldCheckType;

			itemCategory.setPosition(item.getPosition());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			itemCategory.setPositionFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.P, EnumItemFieldName.position, fieldCheckType);

			itemCategory.setSegmentId(item.getSegmentId());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			itemCategory.setSegmentIdFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.P, EnumItemFieldName.segmentId, fieldCheckType);

			itemCategory.setBankKey(item.getBankKey());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			itemCategory.setBankKeyFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.P, EnumItemFieldName.bankKey, fieldCheckType);

			itemCategory.setKey(item.getKey());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			itemCategory.setKeyFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.P, EnumItemFieldName.key, fieldCheckType);

			itemCategory.setOperational(item.getOperational());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			itemCategory.setOperationalFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.P, EnumItemFieldName.operational, fieldCheckType);

			itemCategory.setIsSelected(item.getIsSelected());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			itemCategory.setIsSelectedFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.PC, EnumItemFieldName.isSelected, fieldCheckType);

			itemCategory.setFormat(item.getFormat());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			itemCategory.setFormatFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.PC, EnumItemFieldName.format, fieldCheckType);

			itemCategory.setScore(item.getScore());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			itemCategory.setScoreFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.PC, EnumItemFieldName.score, fieldCheckType);

			itemCategory.setScoreStatus(item.getScoreStatus());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			itemCategory.setScoreStatusFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.P, EnumItemFieldName.scoreStatus, fieldCheckType);

			itemCategory.setAdminDate(item.getAdminDate().toString());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			itemCategory.setAdminDateFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.P, EnumItemFieldName.adminDate, fieldCheckType);

			itemCategory.setNumberVisits(item.getNumberVisits());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			itemCategory.setNumberVisitsFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.P, EnumItemFieldName.numberVisits, fieldCheckType);

			itemCategory.setMimeType(item.getMimeType());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			itemCategory.setMimeTypeFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.P, EnumItemFieldName.mimeType, fieldCheckType);

			itemCategory.setStrand(item.getStrand());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			itemCategory.setStrandFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.P, EnumItemFieldName.strand, fieldCheckType);
			
			itemCategory.setContentLevel(item.getContentLevel());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			itemCategory.setContentLevelFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.P, EnumItemFieldName.contentLevel, fieldCheckType);

			itemCategory.setPageNumber(item.getPageNumber());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			itemCategory.setPageNumberFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.P, EnumItemFieldName.pageNumber, fieldCheckType);
			
			itemCategory.setPageVisits(item.getPageVisits());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			itemCategory.setPageVisitsFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.P, EnumItemFieldName.pageVisits, fieldCheckType);
			
			itemCategory.setPageTime(item.getPageTime());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			itemCategory.setPageTimeFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.P, EnumItemFieldName.pageTime, fieldCheckType);
			
			itemCategory.setDropped(item.getDropped());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			itemCategory.setDroppedFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.P, EnumItemFieldName.dropped, fieldCheckType);

		} catch (Exception e) {
			logger.error("analysisItemAttributes exception: ", e);
		}
	}

	private void validateField(Item item, EnumFieldCheckType enumFieldCheckType, EnumItemFieldName enumFieldName,
			FieldCheckType fieldCheckType) {
		try {
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
		} catch (Exception e) {
			logger.error("validateField exception: ", e);
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
