package org.cresst.sb.irp.analysis.engine;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.EnumUtils;
import org.cresst.sb.irp.domain.analysis.*;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.analysis.ItemCategory.ItemStatusEnum;
import org.cresst.sb.irp.domain.tdsreport.ScoreInfoType;
import org.cresst.sb.irp.domain.tdsreport.ScoreInfoType.ScoreRationale;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Item;
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
    
    public enum EnumItemScoreInfoScoreRationaleFieldName{
    	message
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
				responseCategory = itemCategory.getResponseCategory();
				String format = getTdsFieldNameValueByFieldName(itemCategory.getCellCategories(), "format").trim();
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
    
    /**
     * This function compares the scorePoint with itemScore in responseCategory.getItemScore()
     * 		The itemScore in ReponseCategory includes the IRP item score for MC/MS or score from item scoring engine
     * @param scorePoint
     * 		The value of scorePoint from TDS Report ScoreInfo element 
     * @param fieldCheckType
     * @param itemCategory
     */
    private void processC_scorePoint(String scorePoint, FieldCheckType fieldCheckType, ItemCategory itemCategory){
    	try{
	    	ResponseCategory responseCategory = itemCategory.getResponseCategory();
	    	ScoreInfoCategory scoreInfoCategory = itemCategory.getScoreInfoCategory();
	    	
	    	int itemScore = responseCategory.getItemScore(); //IRP item score (MC, MS) or score from itemScoreInfo.getPoints()
	    	scoreInfoCategory.setItemScore(itemScore);
	    	
	    	int scorePointInt = Math.round(Float.parseFloat(scorePoint)); //<xs:attribute name="scorePoint" type="UFloatAllowNegativeOne"/>
	    	
	    	if (itemScore == scorePointInt)
	    		setCcorrect(fieldCheckType);
    	}catch (Exception e) {
			logger.error("processC_scorePoint exception: ", e);
		}
    }
    
    private void processC_scoreStatus(String scoreStatus, FieldCheckType fieldCheckType, ItemCategory itemCategory){
    	try{
	    	ResponseCategory responseCategory = itemCategory.getResponseCategory();
	    	ScoreInfoCategory scoreInfoCategory = itemCategory.getScoreInfoCategory();
	    	
	    	String format = getTdsFieldNameValueByFieldName(itemCategory.getCellCategories(), "format").trim();
	    	scoreInfoCategory.setTdsFormat(format);
	    	
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
    
    /**
     * TDSReport -> Opportunity -> Item -> ScoreInfo -> ScoreRational
     * @param scoreInfoCategory
     * @param scoreInfoType
     */
    private void analysisItemScoreInfoScoreRationale(ScoreInfoCategory scoreInfoCategory, ScoreInfoType scoreInfoType) {
        try {
        	 ScoreRationale scoreRationale = scoreInfoType.getScoreRationale();
        	 if (scoreRationale != null){ //<xs:element name="ScoreRationale" minOccurs="0" maxOccurs="1">
        		 ScoreRationaleCategory scoreRationaleCategory = new ScoreRationaleCategory();
                 scoreInfoCategory.setScoreRationaleCategory(scoreRationaleCategory);

                 //TODO class org.apache.xerces.dom.ElementNSImpl, not able to access value of Message
                 if (scoreRationale.getMessage() != null){
                	 FieldCheckType fieldCheckType = new FieldCheckType();
                     fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);

                     final CellCategory cellCategory = new CellCategory();
                     cellCategory.setTdsFieldName(EnumItemScoreInfoScoreRationaleFieldName.message.toString());
                     cellCategory.setTdsFieldNameValue(scoreRationale.getMessage().toString());
                     cellCategory.setFieldCheckType(fieldCheckType);

                     scoreRationaleCategory.addCellCategory(cellCategory);
                     validateField(scoreRationale.getMessage().toString(), EnumFieldCheckType.P, fieldCheckType);
                 }
        	 }
        } catch (Exception e) {
            logger.error("analysisItemScoreInfoScoreRationale exception: ", e);
        }
    }

    private void validateField(String message, EnumFieldCheckType enumFieldCheckType, FieldCheckType fieldCheckType) {
        try {
            switch (enumFieldCheckType) {
                case D:
                    break;
                case P:
                	if (message != null) //TODO not able to access message value
                		setPcorrect(fieldCheckType);
                    break;
                case PC:
                    break;
            }
        } catch (Exception e) {
            logger.error("validateField exception: ", e);
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
    
}
