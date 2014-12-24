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
import org.cresst.sb.irp.domain.analysis.ScoreRationaleCategory;
import org.cresst.sb.irp.domain.items.Itemrelease;
import org.cresst.sb.irp.domain.tdsreport.ScoreInfoType;
import org.cresst.sb.irp.domain.tdsreport.ScoreInfoType.ScoreRationale;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Item;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
public class ItemScoreInfoAnalysisAction extends AnalysisAction {
	private static Logger logger = Logger.getLogger(ItemScoreInfoAnalysisAction.class);

	public enum EnumItemScoreInfoFieldName {
		scorePoint, maxScore, scoreDimension, scoreStatus, confLevel;
	}

	@Override
	public void analysis(IndividualResponse individualResponse) throws IOException {
		try {
			TDSReport tdsReport = individualResponse.getTDSReport();
			OpportunityCategory opportunityCategory = individualResponse.getOpportunityCategory();
			List<ItemCategory> listItemCategory = opportunityCategory.getItemCategories();
			Opportunity opportunity = getOpportunity(tdsReport);
			List<Item> listItem = opportunity.getItem();
			int indexOfItemCategory = 0;
			for (Item i : listItem) {
				ItemCategory itemCategory = listItemCategory.get(indexOfItemCategory);
				analysisItemScoreInfo(itemCategory, i);
				ScoreInfoCategory scoreInfoCategory = itemCategory.getScoreInfoCategory();
				if (scoreInfoCategory != null)
					analysisItemScoreInfoScoreRationale(scoreInfoCategory, i.getScoreInfo());
				indexOfItemCategory++;
			}
		} catch (Exception e) {
			logger.error("analysis exception: ", e);
		}
	}

	private void analysisItemScoreInfo(ItemCategory itemCategory, Item tdsItem) {
		try {
			String itemFormat = tdsItem.getFormat();
			ScoreInfoCategory scoreInfoCategory = new ScoreInfoCategory();
			itemCategory.setScoreInfoCategory(scoreInfoCategory);
			ScoreInfoType scoreInfoType = tdsItem.getScoreInfo();
			FieldCheckType fieldCheckType;

			scoreInfoCategory.setScorePoint(scoreInfoType.getScorePoint());
			fieldCheckType = new FieldCheckType();
			if (itemFormat.trim().toLowerCase().equals("mc")) { // handle multiple choice only
				fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
				scoreInfoCategory.setScorePointFieldCheckType(fieldCheckType);
				validateField(scoreInfoType, EnumFieldCheckType.PC, EnumItemScoreInfoFieldName.scorePoint, fieldCheckType,
						itemCategory);
			} else 	if (itemFormat.trim().toLowerCase().equals("ms")) { // handle MS 
				fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
				scoreInfoCategory.setScorePointFieldCheckType(fieldCheckType);
				validateField(scoreInfoType, EnumFieldCheckType.PC, EnumItemScoreInfoFieldName.scorePoint, fieldCheckType,
						itemCategory);
			}
			else {
				fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
				scoreInfoCategory.setScorePointFieldCheckType(fieldCheckType);
				validateField(scoreInfoType, EnumFieldCheckType.P, EnumItemScoreInfoFieldName.scorePoint, fieldCheckType,
						itemCategory);
			}

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

	private void analysisItemScoreInfoScoreRationale(ScoreInfoCategory scoreInfoCategory, ScoreInfoType scoreInfoType) {
		try {
			ScoreRationaleCategory scoreRationaleCategory = new ScoreRationaleCategory();
			scoreInfoCategory.setScoreRationaleCategory(scoreRationaleCategory);
			ScoreRationale scoreRationale = scoreInfoType.getScoreRationale();
			scoreRationaleCategory.setMessage(scoreRationale.getMessage().toString());
			FieldCheckType fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			scoreRationaleCategory.setMessageFieldCheckType(fieldCheckType);
			validateField(scoreRationale.getMessage().toString(), EnumFieldCheckType.P, fieldCheckType);
			/****** scoreRationale.getMessage() needs to double check. there is no getContent method ****/
		} catch (Exception e) {
			logger.error("analysisItemScoreInfoScoreRationale exception: ", e);
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
	
	private void validateField(String message, EnumFieldCheckType enumFieldCheckType, FieldCheckType fieldCheckType) {
		try {
			switch (enumFieldCheckType) {
			case D:
				break;
			case P:
				//checkP(message, fieldCheckType);
				// temporarely set P to true. No getContent method. need to check it out
				setPcorrect(fieldCheckType); 
				break;
			case PC:
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
				//<xs:attribute name="scorePoint" type="UFloatAllowNegativeOne" />
				processP(scoreInfoType.getScorePoint(), fieldCheckType, false); // Required N.
				break;
			case maxScore:
				// <xs:attribute name="maxScore" type="UFloatAllowNegativeOne" />
				processP(scoreInfoType.getMaxScore(), fieldCheckType, false);// Required N.
				break;
			case scoreDimension:
				// <xs:attribute name="scoreDimension" />
				processP(scoreInfoType.getScoreDimension(), fieldCheckType, false);// Required N.
				break;
			case scoreStatus:
				//<xs:attribute name="scoreStatus">
				//      <xs:simpleType>
				//        <xs:restriction base="xs:token">
				//          <xs:enumeration value="Scored" />
				//          <xs:enumeration value="NotScored" />
				//          <xs:enumeration value="WaitingForMachineScore" />
				//          <xs:enumeration value="ScoringError" />
				//        </xs:restriction>
				//      </xs:simpleType>
				processP(scoreInfoType.getScoreStatus(), fieldCheckType, false);// Required N.
				break;
			case confLevel:
				// <xs:attribute name="confLevel" />
				processP(scoreInfoType.getConfLevel(), fieldCheckType, false);
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
		try {
			//need to modify this function to handle MC and MS . . .
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
			String irpItemItemPoint = attribItemPoint.getVal().replace("pt.", "").trim();
			boolean blnScorePoint = isCorrectValue(irpItemItemPoint, scoreInfoTypeScorePoint.trim());
			/*if (blnCorrectAnswer && blnScorePoint) {
				setCcorrect(fieldCheckType);
			}*/
			
			if (blnCorrectAnswer){
				if (scoreInfoTypeScorePoint.trim().equals(irpItemItemPoint))
					setCcorrect(fieldCheckType);
			}else
			{
				if (scoreInfoTypeScorePoint.trim().equals(0))
					setCcorrect(fieldCheckType);
			}
			
		} catch (Exception e) {
			logger.error("processC exception: ", e);
		}

	}

}
