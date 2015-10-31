package org.cresst.sb.irp.analysis.engine;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.EnumUtils;
import org.cresst.sb.irp.analysis.engine.ItemAttributesAnalysisAction.EnumFormatAcceptValues;
import org.cresst.sb.irp.analysis.engine.ItemResponseAnalysisAction.EnumItemResponseFieldName;
import org.cresst.sb.irp.domain.analysis.*;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.analysis.ItemCategory.ItemStatusEnum;
import org.cresst.sb.irp.domain.items.Itemrelease;
import org.cresst.sb.irp.domain.tdsreport.ScoreInfoType;
import org.cresst.sb.irp.domain.tdsreport.ScoreInfoType.ScoreRationale;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Item;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Item.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import tds.itemscoringengine.ScoringStatus;

import java.util.List;

@Service
public class ItemScoreInfoAnalysisAction extends AnalysisAction<ScoreInfoType, ItemScoreInfoAnalysisAction.EnumItemScoreInfoFieldName, ItemCategory> {
    private final static Logger logger = LoggerFactory.getLogger(ItemScoreInfoAnalysisAction.class);

    static public enum EnumItemScoreInfoFieldName {
        scorePoint, maxScore, scoreDimension, scoreStatus, confLevel
    }

    @Override
    public void analyze(IndividualResponse individualResponse) {
        try {
            TDSReport tdsReport = individualResponse.getTDSReport();
            
            OpportunityCategory opportunityCategory = individualResponse.getOpportunityCategory();
            List<ItemCategory> itemCategories = opportunityCategory.getItemCategories();
            
            Opportunity opportunity = tdsReport.getOpportunity();
            List<Item> tdsItems = opportunity.getItem();
            
            for (Item tdsItem : tdsItems) {
            	ItemCategory itemCategory = getItemCategoryByBankKeyKey(Long.toString(tdsItem.getBankKey()),
						Long.toString(tdsItem.getKey()), itemCategories, ItemStatusEnum.FOUND);
            	
            	if (itemCategory != null){
            		  analysisItemScoreInfo(itemCategory, tdsItem);
                      ScoreInfoCategory scoreInfoCategory = itemCategory.getScoreInfoCategory();
                      if (scoreInfoCategory != null)
                          analysisItemScoreInfoScoreRationale(scoreInfoCategory, tdsItem.getScoreInfo());
            	}
            }
        } catch (Exception e) {
            logger.error("analyze exception: ", e);
        }
    }

    private void analysisItemScoreInfo(ItemCategory itemCategory, Item tdsItem) {
        try {
            ScoreInfoCategory scoreInfoCategory = new ScoreInfoCategory();
            itemCategory.setScoreInfoCategory(scoreInfoCategory);
            
            ScoreInfoType scoreInfoType = tdsItem.getScoreInfo();
            if (scoreInfoType != null){
      
    			validate(scoreInfoCategory, scoreInfoType, scoreInfoType.getScorePoint(), EnumFieldCheckType.PC, EnumItemScoreInfoFieldName.scorePoint, itemCategory);
				validate(scoreInfoCategory, scoreInfoType, scoreInfoType.getMaxScore(), EnumFieldCheckType.P, EnumItemScoreInfoFieldName.maxScore, null);
				validate(scoreInfoCategory, scoreInfoType, scoreInfoType.getScoreDimension(), EnumFieldCheckType.P, EnumItemScoreInfoFieldName.scoreDimension, null);
				validate(scoreInfoCategory, scoreInfoType, scoreInfoType.getScoreStatus(), EnumFieldCheckType.PC, EnumItemScoreInfoFieldName.scoreStatus, itemCategory);
            }
        } catch (Exception e) {
            logger.error("analysisItemScoreInfo exception: ", e);
        }
    }

	@Override
	protected String expectedValue(ItemCategory itemCategory, EnumItemScoreInfoFieldName enumFieldName) {
		String strReturn = null;

		ResponseCategory responseCategory;
		try {
			switch (enumFieldName) {
			case scorePoint:
				responseCategory = itemCategory.getResponseCategory();
		    	int itemScore = responseCategory.getItemScore();
		    	strReturn = String.valueOf(itemScore);
				break;
			case maxScore:	
				break;
			case scoreDimension:
				break;
			case scoreStatus:
				String format = getTdsFieldNameValueByFieldName(itemCategory.getCellCategories(), "format").trim();
				responseCategory = itemCategory.getResponseCategory();
				if (StringUtils.equalsIgnoreCase(format, "MC") || StringUtils.equalsIgnoreCase(format, "MS") ){
					strReturn = "Scored";
				}else {
					ScoringStatus scoringStatus = responseCategory.getScoringStatus(); 
					if (scoringStatus != null){ //hand scoring items. TODO
						strReturn = scoringStatus.toString();
					}
				}			
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("expectedValue exception: ", e);
		}

		return strReturn;
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
                               EnumItemScoreInfoFieldName enumFieldName, FieldCheckType fieldCheckType) {
        try {
            switch (enumFieldCheckType) {
                case D:
                    break;
                case P:
                    checkP(scoreInfoType, enumFieldName, fieldCheckType);
                    break;
                case PC:
                    break;
            }
        } catch (Exception e) {
            logger.error("validateField exception: ", e);
        }
    }

    private void validateField(ScoreInfoType scoreInfoType, EnumFieldCheckType enumFieldCheckType,
                               EnumItemScoreInfoFieldName enumFieldName, FieldCheckType fieldCheckType, ItemCategory itemCategory, String itemFormat) {
        try {
            switch (enumFieldCheckType) {
                case D:
                    break;
                case P:
                    checkP(scoreInfoType, enumFieldName, fieldCheckType);
                    break;
                case PC:
                    checkP(scoreInfoType, enumFieldName, fieldCheckType);
                    checkC(scoreInfoType, enumFieldName, fieldCheckType, itemCategory, itemFormat);
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
                    // checkP(message, fieldCheckType);
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

    @Override
    protected void checkP(ScoreInfoType scoreInfoType, EnumItemScoreInfoFieldName enumFieldName, FieldCheckType fieldCheckType) {
        try {
            switch (enumFieldName) {
                case scorePoint:
                    // <xs:attribute name="scorePoint" type="UFloatAllowNegativeOne" />
                	if (scoreInfoType.getScorePoint() != null)
                		processP(scoreInfoType.getScorePoint(), fieldCheckType, false); // not required
                    break;
                case maxScore:
                    // <xs:attribute name="maxScore" type="UFloatAllowNegativeOne" />
                	if (scoreInfoType.getMaxScore() != null)
                		processP(scoreInfoType.getMaxScore(), fieldCheckType, false);
                    break;
                case scoreDimension:
                    // <xs:attribute name="scoreDimension" />
                	if (scoreInfoType.getScoreDimension() != null)
                		processP(scoreInfoType.getScoreDimension(), fieldCheckType, false);
                    break;
                case scoreStatus:
                    // <xs:attribute name="scoreStatus">
                    //      <xs:simpleType>
                    //        <xs:restriction base="xs:token">
                    //          <xs:enumeration value="Scored" />
                    //          <xs:enumeration value="NotScored" />
                    //          <xs:enumeration value="WaitingForMachineScore" />
                    //          <xs:enumeration value="ScoringError" />
                    //        </xs:restriction>
                    //      </xs:simpleType>
                	if (scoreInfoType.getScoreStatus() != null)
                		processP(scoreInfoType.getScoreStatus(), fieldCheckType, false);
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

    @Override
    protected void checkC(ScoreInfoType scoreInfoType, EnumItemScoreInfoFieldName enumFieldName,
                          FieldCheckType fieldCheckType, ItemCategory itemCategory) {
    	try {
			switch (enumFieldName) {
			case scorePoint:
				if (scoreInfoType.getScorePoint() != null)
					processC_scorePoint(scoreInfoType.getScorePoint(), fieldCheckType, itemCategory);
				break;
			case maxScore:
				break;
			case scoreDimension:
				break;
			case scoreStatus:
				if (scoreInfoType.getScoreStatus() != null)
					processC_scoreStatus(scoreInfoType.getScoreStatus(), fieldCheckType, itemCategory);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("checkC exception: ", e);
		}

    }
    
    private void processC_scorePoint(String scorePoint, FieldCheckType fieldCheckType, ItemCategory itemCategory){
    	try{
	    	ResponseCategory responseCategory = itemCategory.getResponseCategory();
	    	int itemScoreFromScoreEngine = responseCategory.getItemScore();
	    	
	    	int scorePointInt = Math.round(Float.parseFloat(scorePoint));
	    	
	    	if (itemScoreFromScoreEngine == scorePointInt)
	    		setCcorrect(fieldCheckType);
    	}catch (Exception e) {
			logger.error("processC_scorePoint exception: ", e);
		}
    }
    
    private void processC_scoreStatus(String scoreStatus, FieldCheckType fieldCheckType, ItemCategory itemCategory){
    	try{
	    	ResponseCategory responseCategory = itemCategory.getResponseCategory();
	    	
	    	String format = getTdsFieldNameValueByFieldName(itemCategory.getCellCategories(), "format").trim();
	    	
			if (StringUtils.equalsIgnoreCase(format, "MC") || StringUtils.equalsIgnoreCase(format, "MS") ){
				if (ScoringStatus.valueOf(scoreStatus) == ScoringStatus.Scored)
					setCcorrect(fieldCheckType);
			}else{
		    	ScoringStatus scoringStatusFromScoreEngine = responseCategory.getScoringStatus();  
		    	if (scoringStatusFromScoreEngine != null){ //hand scoring items. TODO 
					if(isAcceptableEnum(scoreStatus, ScoringStatus.class)){
						if(ScoringStatus.valueOf(scoreStatus).equals(scoringStatusFromScoreEngine)){
							setCcorrect(fieldCheckType);
						}
					}
		    	}
			}
    	}catch (Exception e) {
			logger.error("processC_scorePoint exception: ", e);
		}
    	
    }

	private boolean isAcceptableEnum(String fieldValue,  Class<ScoringStatus> class1) {
		try {
			if (fieldValue != null && !fieldValue.trim().isEmpty()) {
				if (EnumUtils.isValidEnum(class1, fieldValue)) {
					return true;
				}
			}
		} catch (Exception e) {
			logger.error("isAcceptableEnum exception: ", e);
		}
		return false;
	}
    
    protected void checkC(ScoreInfoType scoreInfoType, EnumItemScoreInfoFieldName enumFieldName,
                          FieldCheckType fieldCheckType, ItemCategory itemCategory, String itemFormat) {
        try {
            switch (enumFieldName) {
                case scorePoint:
                    processC(scoreInfoType, fieldCheckType, itemCategory, itemFormat);
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

    private void processC(ScoreInfoType scoreInfoType, FieldCheckType fieldCheckType, ItemCategory itemCategory, String itemFormat) {
        try {
            switch (itemFormat) {
            case "mc":
                scoreMC(scoreInfoType.getScorePoint(), fieldCheckType, itemCategory);
                break;
            case "ms": //scoreMS may differ from scoreMC if there is a partial point involved.
                scoreMS(scoreInfoType.getScorePoint(), fieldCheckType, itemCategory);
                break;
            default:
                break;
            }

        } catch (Exception e) {
            logger.error("processC exception: ", e);
        }
    }

    private void scoreMC(String scoreInfoTypeScorePoint, FieldCheckType fieldCheckType, ItemCategory itemCategory) {
        try {
            Itemrelease.Item.Attriblist attriblist = itemCategory.getAttriblist();
            Itemrelease.Item.Attriblist.Attrib attribAnswerKey = getItemAttribValueFromIRPitemAttriblist(attriblist,
                    "itm_att_Answer Key");
            Itemrelease.Item.Attriblist.Attrib attribItemPoint = getItemAttribValueFromIRPitemAttriblist(attriblist,
                    "itm_att_Item Point");
            ResponseCategory responseCategory = itemCategory.getResponseCategory();
           /* TODO
            String tdsResponseContent = responseCategory.getContent();
            String irpItemAnswerKey = attribAnswerKey.getVal();
            boolean blnCorrectAnswer = isCorrectValue(irpItemAnswerKey, tdsResponseContent);

            // <attrib attid="itm_att_Item Point">
            // <name>Item: Item Point</name>
            // <val>1 pt.</val>
            // </attrib>
            String irpItemItemPoint = attribItemPoint.getVal().replace("pt.", "").trim();
            boolean blnScorePoint = isCorrectValue(irpItemItemPoint, scoreInfoTypeScorePoint.trim());

            if (blnCorrectAnswer) {
                if (blnScorePoint)
                    setCcorrect(fieldCheckType);
            } else {
                if (scoreInfoTypeScorePoint.trim().equals(0))
                    setCcorrect(fieldCheckType);
            }*/

        } catch (Exception e) {
            logger.error("scoreMC exception: ", e);
        }

    }

    private void scoreMS(String scoreInfoTypeScorePoint, FieldCheckType fieldCheckType, ItemCategory itemCategory) {
        try {
            Itemrelease.Item.Attriblist attriblist = itemCategory.getAttriblist();
            Itemrelease.Item.Attriblist.Attrib attribAnswerKey = getItemAttribValueFromIRPitemAttriblist(attriblist,
                    "itm_att_Answer Key");
            Itemrelease.Item.Attriblist.Attrib attribItemPoint = getItemAttribValueFromIRPitemAttriblist(attriblist,
                    "itm_att_Item Point");
            ResponseCategory responseCategory = itemCategory.getResponseCategory();
           /* TODO
            String tdsResponseContent = responseCategory.getContent();
            String irpItemAnswerKey = attribAnswerKey.getVal();
            boolean blnCorrectAnswer = isCorrectValue(irpItemAnswerKey, tdsResponseContent);

            String irpItemItemPoint = attribItemPoint.getVal().replace("pt.", "").trim();
            boolean blnScorePoint = isCorrectValue(irpItemItemPoint, scoreInfoTypeScorePoint.trim());

            if (blnCorrectAnswer) {
                if (blnScorePoint)
                    setCcorrect(fieldCheckType);
            } else {
                if (scoreInfoTypeScorePoint.trim().equals(0))
                    setCcorrect(fieldCheckType);
            }*/
            
        } catch (Exception e) {
            logger.error("scoreMS exception: ", e);
        }
    }
}
