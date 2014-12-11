package org.cresst.sb.irp.dao;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.ItemCategory;
import org.cresst.sb.irp.domain.analysis.OpportunityCategory;
import org.cresst.sb.irp.domain.analysis.ResponseCategory;
import org.cresst.sb.irp.domain.analysis.ScoreInfoCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.items.Itemrelease;
import org.cresst.sb.irp.domain.tdsreport.ScoreInfoType;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Item;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class ItemScoreInfoAnalysisAction extends AnalysisAction {
	private static Logger logger = Logger.getLogger(ItemScoreInfoAnalysisAction.class);

	public enum EnumItemScoreInfoFieldName {
		scorePoint, maxScore, scoreDimension, scoreStatus, confLevel;
	}

	@Override
	public void analysis() throws IOException {
		try {
			IndividualResponse individualResponse = getIndividualResponse();
			TDSReport tdsReport = individualResponse.getTDSReport();
			OpportunityCategory opportunityCategory = individualResponse.getOpportunityCategory();
			List<ItemCategory> listItemCategory = opportunityCategory.getListItemCategory();
			Opportunity opportunity = getOpportunity(tdsReport);
			List<Item> listItem = opportunity.getItem();
			int indexOfItemCategory = 0;
			for (Item i : listItem) {
				ItemCategory itemCategory = listItemCategory.get(indexOfItemCategory);
				analysisItemScoreInfo(itemCategory, i);
				indexOfItemCategory++;
			}
		} catch (Exception e) {
			logger.error("analysis exception: ", e);
		}
	}

	private void analysisItemScoreInfo(ItemCategory itemCategory, Item tdsItem) {
		try {
			ScoreInfoCategory scoreInfoCategory = new ScoreInfoCategory();
			itemCategory.setScoreInfoCategory(scoreInfoCategory);
			ScoreInfoType scoreInfoType = tdsItem.getScoreInfo();
			FieldCheckType fieldCheckType;

			scoreInfoCategory.setScorePoint(scoreInfoType.getScorePoint());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			scoreInfoCategory.setScorePointFieldCheckType(fieldCheckType);
			validateField(scoreInfoType, EnumFieldCheckType.PC, EnumItemScoreInfoFieldName.scorePoint, fieldCheckType,
					itemCategory);

			// need to double check UPDATE dox with AIR as "old" version does NOT have this value
			scoreInfoCategory.setMaxScore(scoreInfoType.getMaxScore());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			scoreInfoCategory.setMaxScoreFieldCheckType(fieldCheckType);
			validateField(scoreInfoType, EnumFieldCheckType.P, EnumItemScoreInfoFieldName.maxScore, fieldCheckType, itemCategory);

			scoreInfoCategory.setMaxScore(scoreInfoType.getScoreDimension());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			scoreInfoCategory.setScoreDimensionFieldCheckType(fieldCheckType);
			validateField(scoreInfoType, EnumFieldCheckType.P, EnumItemScoreInfoFieldName.scoreDimension, fieldCheckType,
					itemCategory);

			scoreInfoCategory.setMaxScore(scoreInfoType.getScoreStatus());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			scoreInfoCategory.setScoreStatusFieldCheckType(fieldCheckType);
			validateField(scoreInfoType, EnumFieldCheckType.P, EnumItemScoreInfoFieldName.scoreStatus, fieldCheckType,
					itemCategory);

			scoreInfoCategory.setMaxScore(scoreInfoType.getConfLevel());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			scoreInfoCategory.setConfLevelFieldCheckType(fieldCheckType);
			validateField(scoreInfoType, EnumFieldCheckType.P, EnumItemScoreInfoFieldName.confLevel, fieldCheckType, itemCategory);

		} catch (Exception e) {
			logger.error("analysisItemScoreInfo exception: ", e);
		}
	}

	private void validateField(ScoreInfoType scoreInfoType, EnumFieldCheckType enumFieldCheckType,
			EnumItemScoreInfoFieldName enumFieldName, FieldCheckType fieldCheckType, ItemCategory itemCategory) {
		try {
			switch (enumFieldCheckType) {
			case D:
				break;
			case P:
				checkP(scoreInfoType, enumFieldName, fieldCheckType);
				break;
			case PC:
				checkP(scoreInfoType, enumFieldName, fieldCheckType);
				checkC(scoreInfoType, enumFieldName, fieldCheckType, itemCategory);
				break;
			}
		} catch (Exception e) {
			logger.error("validateField exception: ", e);
		}
	}

	private void checkP(ScoreInfoType scoreInfoType, EnumItemScoreInfoFieldName enumFieldName, FieldCheckType fieldCheckType) {
		try {
			switch (enumFieldName) {
			case scorePoint:
				// Required N. xsd <xs:attribute name="scorePoint" type="UFloatAllowNegativeOne" />
				processP(scoreInfoType.getScorePoint(), fieldCheckType);
				break;
			case maxScore:
				// Required N. xsd <xs:attribute name="maxScore" type="UFloatAllowNegativeOne" />
				processP(scoreInfoType.getMaxScore(), fieldCheckType);
				break;
			case scoreDimension:
				// Required N. xsd <xs:attribute name="scoreDimension" />
				if (scoreInfoType.getScoreDimension() != null) {
					validateToken(scoreInfoType.getScoreDimension(), fieldCheckType);
					validatePritableASCIIone(scoreInfoType.getScoreDimension(), fieldCheckType);
				}
				break;
			case scoreStatus:
				// Required N.
				/*
				 * xsd: <xs:attribute name="scoreStatus"> <xs:simpleType> <xs:restriction base="xs:token"> <xs:enumeration
				 * value="Scored" /> <xs:enumeration value="NotScored" /> <xs:enumeration value="WaitingForMachineScore" />
				 * <xs:enumeration value="ScoringError" /> </xs:restriction> </xs:simpleType> </xs:attribute>
				 */
				processP(scoreInfoType.getScoreStatus(), fieldCheckType);
				break;
			case confLevel:
				// <xs:attribute name="confLevel" />
				processP(scoreInfoType.getConfLevel(), fieldCheckType);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("checkP exception: ", e);
		}
	}
	
	private void checkC(ScoreInfoType scoreInfoType, EnumItemScoreInfoFieldName enumFieldName, FieldCheckType fieldCheckType,
			ItemCategory itemCategory) {
		try {
			switch (enumFieldName) {
			case scorePoint:
				processC(scoreInfoType.getScorePoint(), fieldCheckType, itemCategory);
				break;
			case maxScore:
				break;
			case scoreDimension:
				break;
			case scoreStatus:
				break;
			case confLevel:
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("checkC exception: ", e);
		}
	}

	private void processC(String scoreInfoTypeScorePoint, FieldCheckType fieldCheckType, ItemCategory itemCategory) {
		Itemrelease.Item.Attriblist attriblist = itemCategory.getAttriblist();
		Itemrelease.Item.Attriblist.Attrib attribAnswerKey = getItemAttribValueFromIRPitemAttriblist(attriblist,
				"itm_att_Answer Key");
		Itemrelease.Item.Attriblist.Attrib attribItemPoint = getItemAttribValueFromIRPitemAttriblist(attriblist,
				"itm_att_Item Point");

		ResponseCategory responseCategory = itemCategory.getResponseCategory();
		String tdsResponseContent = responseCategory.getContent();
		String irpItemAnswerKey = attribAnswerKey.getVal();
		boolean blnCorrectAnswer = isCorrectValue(irpItemAnswerKey, tdsResponseContent);
		
		// <attrib attid="itm_att_Item Point"> <name>Item: Item Point</name> <val>1 pt.</val> <desc>1 Point</desc> </attrib>
		String irpItemItemPoint = attribItemPoint.getVal().replace("pt", "").trim();
		boolean blnScorePoint = isCorrectValue(irpItemItemPoint, scoreInfoTypeScorePoint.trim());
		if (blnCorrectAnswer && blnScorePoint){
			setCcorrect(fieldCheckType);
		}

	}

}
