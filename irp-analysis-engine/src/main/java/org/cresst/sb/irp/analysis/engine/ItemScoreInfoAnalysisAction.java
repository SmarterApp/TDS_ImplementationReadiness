package org.cresst.sb.irp.analysis.engine;

import org.cresst.sb.irp.domain.analysis.*;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.items.Itemrelease;
import org.cresst.sb.irp.domain.tdsreport.ScoreInfoType;
import org.cresst.sb.irp.domain.tdsreport.ScoreInfoType.ScoreRationale;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
            
            int indexOfItemCategory = 0;
            for (Item tdsItem : tdsItems) {
                ItemCategory itemCategory = itemCategories.get(indexOfItemCategory);
                analysisItemScoreInfo(itemCategory, tdsItem);
                ScoreInfoCategory scoreInfoCategory = itemCategory.getScoreInfoCategory();
                if (scoreInfoCategory != null)
                    analysisItemScoreInfoScoreRationale(scoreInfoCategory, tdsItem.getScoreInfo());
                indexOfItemCategory++;
            }
        } catch (Exception e) {
            logger.error("analyze exception: ", e);
        }
    }

    private void analysisItemScoreInfo(ItemCategory itemCategory, Item tdsItem) {
        try {
            String itemFormat = tdsItem.getFormat().trim();
            ScoreInfoCategory scoreInfoCategory = new ScoreInfoCategory();
            itemCategory.setScoreInfoCategory(scoreInfoCategory);
            ScoreInfoType scoreInfoType = tdsItem.getScoreInfo();
            FieldCheckType fieldCheckType;

            scoreInfoCategory.setScorePoint(scoreInfoType.getScorePoint());
            fieldCheckType = new FieldCheckType();
            if (itemFormat.toLowerCase().equals("mc") || itemFormat.toLowerCase().equals("ms")) { // handle MC, MS
                fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
                scoreInfoCategory.setScorePointFieldCheckType(fieldCheckType);
                validateField(scoreInfoType, EnumFieldCheckType.PC, EnumItemScoreInfoFieldName.scorePoint, fieldCheckType,
                        itemCategory, itemFormat);
            } else {
                fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
                scoreInfoCategory.setScorePointFieldCheckType(fieldCheckType);
                validateField(scoreInfoType, EnumFieldCheckType.P, EnumItemScoreInfoFieldName.scorePoint, fieldCheckType,
                        itemCategory, itemFormat);
            }

            // need to double check UPDATE dox with AIR as "old" version does NOT have this value
            scoreInfoCategory.setMaxScore(scoreInfoType.getMaxScore());
            fieldCheckType = new FieldCheckType();
            fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
            scoreInfoCategory.setMaxScoreFieldCheckType(fieldCheckType);
            validateField(scoreInfoType, EnumFieldCheckType.P, EnumItemScoreInfoFieldName.maxScore, fieldCheckType);

            scoreInfoCategory.setMaxScore(scoreInfoType.getScoreDimension());
            fieldCheckType = new FieldCheckType();
            fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
            scoreInfoCategory.setScoreDimensionFieldCheckType(fieldCheckType);
            validateField(scoreInfoType, EnumFieldCheckType.P, EnumItemScoreInfoFieldName.scoreDimension, fieldCheckType);

            scoreInfoCategory.setMaxScore(scoreInfoType.getScoreStatus());
            fieldCheckType = new FieldCheckType();
            fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
            scoreInfoCategory.setScoreStatusFieldCheckType(fieldCheckType);
            validateField(scoreInfoType, EnumFieldCheckType.P, EnumItemScoreInfoFieldName.scoreStatus, fieldCheckType);

            scoreInfoCategory.setMaxScore(scoreInfoType.getConfLevel());
            fieldCheckType = new FieldCheckType();
            fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
            scoreInfoCategory.setConfLevelFieldCheckType(fieldCheckType);
            validateField(scoreInfoType, EnumFieldCheckType.P, EnumItemScoreInfoFieldName.confLevel, fieldCheckType);

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
                    // <xs:attribute name="scoreStatus">
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

    @Override
    protected void checkC(ScoreInfoType scoreInfoType, EnumItemScoreInfoFieldName enumFieldName,
                          FieldCheckType fieldCheckType, ItemCategory itemCategory) {

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
