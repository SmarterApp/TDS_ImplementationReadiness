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
import org.cresst.sb.irp.domain.analysis.ResponseCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Item;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Item.Response;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class ItemAnalysisAction extends AnalysisAction {
	private static Logger logger = Logger.getLogger(ItemAnalysisAction.class);

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
				System.out.println("item ------->" + i.getPosition());
				itemCategory = new ItemCategory();
				listItemCategory.add(itemCategory);
				analysisItemAttributes(itemCategory, i);
				analysisItemResponse(itemCategory, i);
			}

		} catch (Exception e) {
			logger.error("analysis exception: ", e);
		}
	}

	private void analysisItemAttributes(ItemCategory itemCategory, Item item) {
		try {
			List<CellCategory> listCellCategory = itemCategory.getListItemAttribute();

			CellCategory cellCategory;
			FieldCheckType fieldCheckType;

			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumItemFieldName.position.toString());
			cellCategory.setTdsFieldNameValue(Long.toString(item.getPosition()));
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.P, EnumItemFieldName.position, fieldCheckType);

			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumItemFieldName.segmentId.toString());
			cellCategory.setTdsFieldNameValue(item.getSegmentId());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.P, EnumItemFieldName.segmentId, fieldCheckType);

			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumItemFieldName.bankKey.toString());
			cellCategory.setTdsFieldNameValue(Long.toString(item.getBankKey()));
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.P, EnumItemFieldName.bankKey, fieldCheckType);

			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumItemFieldName.key.toString());
			cellCategory.setTdsFieldNameValue(Long.toString(item.getKey()));
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.P, EnumItemFieldName.key, fieldCheckType);

			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumItemFieldName.operational.toString());
			cellCategory.setTdsFieldNameValue(Short.toString(item.getOperational()));
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.P, EnumItemFieldName.operational, fieldCheckType);

			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumItemFieldName.isSelected.toString());
			cellCategory.setTdsFieldNameValue(Short.toString(item.getIsSelected()));
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.PC, EnumItemFieldName.isSelected, fieldCheckType);

			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumItemFieldName.format.toString());
			cellCategory.setTdsFieldNameValue(item.getFormat());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.PC, EnumItemFieldName.format, fieldCheckType);

			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumItemFieldName.score.toString());
			cellCategory.setTdsFieldNameValue(item.getScore());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.PC, EnumItemFieldName.score, fieldCheckType);

			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumItemFieldName.scoreStatus.toString());
			cellCategory.setTdsFieldNameValue(item.getScoreStatus());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.D);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.D, EnumItemFieldName.scoreStatus, fieldCheckType);

			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumItemFieldName.adminDate.toString());
			cellCategory.setTdsFieldNameValue(item.getAdminDate().toString());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.P, EnumItemFieldName.adminDate, fieldCheckType);

			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumItemFieldName.numberVisits.toString());
			cellCategory.setTdsFieldNameValue(Long.toString(item.getNumberVisits()));
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.P, EnumItemFieldName.numberVisits, fieldCheckType);

			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumItemFieldName.mimeType.toString());
			cellCategory.setTdsFieldNameValue(item.getMimeType());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.P, EnumItemFieldName.mimeType, fieldCheckType);

			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumItemFieldName.strand.toString());
			cellCategory.setTdsFieldNameValue(item.getStrand());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.P, EnumItemFieldName.strand, fieldCheckType);

			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumItemFieldName.contentLevel.toString());
			cellCategory.setTdsFieldNameValue(item.getContentLevel());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.P, EnumItemFieldName.contentLevel, fieldCheckType);

			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumItemFieldName.pageNumber.toString());
			cellCategory.setTdsFieldNameValue(Long.toString(item.getPageNumber()));
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.P, EnumItemFieldName.pageNumber, fieldCheckType);

			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumItemFieldName.dropped.toString());
			cellCategory.setTdsFieldNameValue(Short.toString(item.getDropped()));
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(item, EnumFieldCheckType.P, EnumItemFieldName.dropped, fieldCheckType);

		} catch (Exception e) {
			logger.error("analysisItemAttributes exception: ", e);
		}
	}

	private void analysisItemResponse(ItemCategory itemCategory, Item item) {
		try {
			ResponseCategory responseCategory = new ResponseCategory();
			itemCategory.setResponseCategory(responseCategory);
			Response response = item.getResponse();
			String strDate = response.getDate().toString();
			responseCategory.setDate(strDate);
			
			FieldCheckType fieldCheckType;
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			responseCategory.setDateFieldCheckType(fieldCheckType);
			//validateField(response.getDate(), EnumFieldCheckType.P, EnumItemFieldName.position, fieldCheckType);
			
			String strType = response.getType();
			responseCategory.setType(strType);
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			responseCategory.setTypeFieldCheckType(fieldCheckType);
			//validateField(response.getType(), EnumFieldCheckType.P, EnumItemFieldName.position, fieldCheckType);
			
			String strNodeText = response.getContent();
			responseCategory.setContent(strNodeText);
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			responseCategory.setContentFieldCheckType(fieldCheckType);
			//validateField(response.getContent(), EnumFieldCheckType.P, EnumItemFieldName.position, fieldCheckType);
			
			
		} catch (Exception e) {
			logger.error("analysisItemResponse exception: ", e);
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
				validateUnsignedIntPositive32bit(Long.toString(item.getPosition()), fieldCheckType);
				break;
			case segmentId:
				validateToken(item.getSegmentId(), fieldCheckType);
				validatePritableASCIIone(item.getSegmentId(), fieldCheckType);
				break;
			case bankKey:
				validateToken(Long.toString(item.getBankKey()), fieldCheckType);
				validatePritableASCIIone(Long.toString(item.getBankKey()), fieldCheckType);
				break;
			case key:
				validateUnsignedLongPositive64bit(item.getKey(), fieldCheckType);
				break;
			case operational:
				validateUnsignedInt(Short.toString(item.getOperational()), fieldCheckType, 0, 1);
				break;
			case isSelected:
				validateUnsignedInt(Short.toString(item.getIsSelected()), fieldCheckType, 0, 1);
				break;
			case format:
				validateToken(item.getFormat(), fieldCheckType);
				processAcceptableEnum(item.getFormat(), fieldCheckType, EnumFormatAcceptValues.class);
				break;
			case score:
				validateUnsignedFloat(item.getScore(), fieldCheckType, -1);
				break;
			case adminDate:
				// validateUnsignedFloat(item.getScore(), fieldCheckType, -1);
				break;
			case numberVisits:
				validateUnsignedIntPositive32bit(Long.toString(item.getNumberVisits()), fieldCheckType);
				break;
			case mimeType:
				validatePritableASCIIone(item.getMimeType(), fieldCheckType);
				break;
			case strand:
				validatePritableASCIIone(item.getStrand(), fieldCheckType);
				break;
			case contentLevel:
				validatePritableASCIIone(item.getContentLevel(), fieldCheckType);
				break;
			case pageNumber:
				validateUnsignedIntPositive32bit(Long.toString(item.getPageNumber()), fieldCheckType);
				break;
			case dropped:
				validateUnsignedInt(Short.toString(item.getDropped()), fieldCheckType, 0, 1);
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
			System.out.println("fieldValue ->" + fieldValue);
			if (fieldValue != null && !fieldValue.trim().isEmpty()) {
				if (EnumUtils.isValidEnum(class1, fieldValue)) {
					fieldCheckType.setAcceptableValue(true);
				}
			}
		} catch (Exception e) {
			logger.error("processAcceptableEnum exception: ", e);
		}
	}

}
