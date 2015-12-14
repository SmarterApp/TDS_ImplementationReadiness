package org.cresst.sb.irp.analysis.engine;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;
import org.cresst.sb.irp.domain.analysis.*;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.analysis.ItemCategory.ItemStatusEnum;
import org.cresst.sb.irp.domain.items.Itemrelease;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Item;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Item.Response;
import org.cresst.sb.irp.itemscoring.rubric.MachineRubricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tds.itemscoringengine.*;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class ItemResponseAnalysisAction extends AnalysisAction<Item, ItemResponseAnalysisAction.EnumItemResponseFieldName, ItemCategory> {
	private final static Logger logger = LoggerFactory.getLogger(ItemResponseAnalysisAction.class);

    static public enum EnumItemResponseFieldName {
		date, type, content
	}

    private IItemScorer itemScorer;
    private MachineRubricLoader machineRubricLoader;

    /*
     *  autowire constructor arguments (IItemScorer, MachineRubricLoader)
     *  <bean id="itemScorer" class="org.cresst.sb.irp.itemscoring.client.IrpProxyItemScorer">
	 * 		<constructor-arg ref="irpProxyRestTemplate" />
	 "		<constructor-arg value="${item.scoring.service.url}" />
	 * 	</bean>
     */
    @Autowired
    public ItemResponseAnalysisAction(IItemScorer itemScorer, MachineRubricLoader machineRubricLoader) {
        this.itemScorer = itemScorer;
        this.machineRubricLoader = machineRubricLoader;
    }

	@Override
	public void analyze(IndividualResponse individualResponse) {
		try {
			TDSReport tdsReport = individualResponse.getTDSReport();

            OpportunityCategory opportunityCategory = individualResponse.getOpportunityCategory();
			List<ItemCategory> itemCategories = opportunityCategory.getItemCategories(); // combination of FOUND, MISSING, EXTRA

            Opportunity opportunity = tdsReport.getOpportunity();
			List<Item> tdsItems = opportunity.getItem();

            for (Item tdsItem : tdsItems) {
				ItemCategory itemCategory = getItemCategoryByBankKeyKey(Long.toString(tdsItem.getBankKey()),
						Long.toString(tdsItem.getKey()), itemCategories, ItemStatusEnum.FOUND);

                if (itemCategory != null) {
                	org.cresst.sb.irp.domain.items.Itemrelease.Item irpItem = getItemByIdentifier(itemCategory.getItemBankKeyKey());
                	if (irpItem != null){
                		itemCategory.setIrpItem(irpItem);
        				Itemrelease.Item.Attriblist attriblist = getItemAttriblistFromIRPitem(irpItem);
        				itemCategory.setAttriblist(attriblist);
	               
						analyzeItemResponse(itemCategory, tdsItem);
                	}
				}
			}
		} catch (Exception e) {
			logger.error("analyze exception: ", e);
		}
	}

	private void analyzeItemResponse(ItemCategory itemCategory, Item tdsItem) {
		try {
			ResponseCategory responseCategory = new ResponseCategory();
			itemCategory.setResponseCategory(responseCategory);
			
			Response response = tdsItem.getResponse();
		
			if (response != null){
				validate(responseCategory, tdsItem, response.getDate(), EnumFieldCheckType.P, EnumItemResponseFieldName.date, null);
				validate(responseCategory, tdsItem, response.getType(), EnumFieldCheckType.PC, EnumItemResponseFieldName.type, itemCategory);
				validate(responseCategory, tdsItem, response.getContent(), EnumFieldCheckType.PC, EnumItemResponseFieldName.content, itemCategory);
			}			
		} catch (Exception e) {
			logger.error("analyzeItemResponse exception: ", e);
		}
	}
	
	/**
	 * Field Check Type (P) --> check that field is not empty, and field value is of correct data type and within acceptable
	 * values
	 *
	 * @param tdsItem
	 *            TDS Item with fields to check
	 * @param enumFieldName
	 *            Specifies the field to check
	 * @param fieldCheckType
	 *            This is where the results are stored
	 */
	@Override
	protected void checkP(Item tdsItem, EnumItemResponseFieldName enumFieldName, FieldCheckType fieldCheckType) {
		try {
			Response response = tdsItem.getResponse();
			switch (enumFieldName) {
			case date:
				// not required. xsd <xs:attribute name="date" type="xs:dateTime" />
				if (response.getDate() != null && response.getDate().toString().length() > 0)
					setPcorrect(fieldCheckType);
				break;
			case type:
				if (response.getType() != null)
					processP(response.getType(), fieldCheckType, false); //not required
				break;
			case content:
				processP(response.getContent(), fieldCheckType, false); 
				break;
			default:
				break;
			}
		
		} catch (Exception e) {
			logger.error("checkP exception: ", e);
		}
	}
	
	/**
	 * @param tdsItem
	 *            TDSReport Item with fields to check
	 * @param enumFieldName
	 *            Specifies the field to check
	 * @param fieldCheckType
	 *            This is where results are stored
	 * @param itemCategory
	 * 			  ItemCategory stores ResponseCategory, ScoreInfoCategory, itemBankKeyKey and domain.items.Itemrelease.Item
	 */
	@Override
	protected void checkC(Item tdsItem, EnumItemResponseFieldName enumFieldName, FieldCheckType fieldCheckType,
			ItemCategory itemCategory) {
		try {
			switch (enumFieldName) {
			case date:
				break;
			case type:
				Response response = tdsItem.getResponse();
				if (response.getType() != null)
					processC_type(response.getType(), fieldCheckType, itemCategory);
				break;
			case content:
				processC_content(tdsItem, fieldCheckType, itemCategory);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("checkC exception: ", e);
		}
	}

	
	@Override
	protected String expectedValue(ItemCategory itemCategory, EnumItemResponseFieldName enumFieldName) {
		String strReturn = null;

		try {
			switch (enumFieldName) {
			case date:
				break;
			case type:	
				break;
			case content:
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("expectedValue exception: ", e);
		}

		return strReturn;
	}	

	// <xs:attribute name="type">
	// <xs:simpleType>
	// 		<xs:restriction base="xs:token">
	// 		<xs:enumeration value="value" />
	// 		<xs:enumeration value="reference" />
	// 		<xs:enumeration value="" />
	// 		</xs:restriction>
	// </xs:simpleType>
	public void processC_type(String str, FieldCheckType fieldCheckType, ItemCategory itemCategory) {
		ResponseCategory responseCategory = itemCategory.getResponseCategory();
		CellCategory cellCategory = getCellCategoryByFieldName(responseCategory.getCellCategories(), "type");
		if (StringUtils.isBlank(str)) {
			cellCategory.setTdsExpectedValue(""); 
			setCcorrect(fieldCheckType);
		}else{
			if (StringUtils.equalsIgnoreCase(str, "value")) { 
				cellCategory.setTdsExpectedValue("value"); 
				setCcorrect(fieldCheckType);
			}else if (StringUtils.equalsIgnoreCase(str, "reference")){
				cellCategory.setTdsExpectedValue("reference"); 
				setCcorrect(fieldCheckType);
			}
		}
	}
	
	/**
	 * 
	 * @param tdsItem
	 *            TDS Item object stores Item attributes and Response data 
	 * @param fieldCheckType
	 *            This is where the results are stored
	 * @param itemCategory
	 *            itemCategory object stores ReponseCategory, ScoreInfoCategory and irpItem objects.
	 */
	protected void processC_content(Item tdsItem, FieldCheckType fieldCheckType, ItemCategory itemCategory){
		try {
			String format = tdsItem.getFormat().toLowerCase();
			switch (format) {
			case "ms":
				processCForMS(tdsItem, fieldCheckType, itemCategory);
				break;
			case "mc":
				processCForMC(tdsItem, fieldCheckType, itemCategory);
				break;
			case "mi":
			case "eq":
			case "gi":
			case "ti":
			case "htq":
			case "ebsr":
				processCForItemScoring(tdsItem,  fieldCheckType, itemCategory);
				break;
			case "wit": //wordlist
			case "tut":	//tutorial
			case "sa":	//short answer
			case "er":	//extended response
			case "wer":	//writing extended response
				//TODO
				//fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
				//responseCategory.setContentFieldCheckType(fieldCheckType);
				//validateField(response, EnumFieldCheckType.P, EnumItemResponseFieldName.content, fieldCheckType);
				break;
			default: 
				break;
			}
			
		} catch (Exception e) {
			logger.error("processC_content exception: ", e);
		}
	}
	
	private void processCForMS(Item tdsItem, FieldCheckType fieldCheckType, ItemCategory itemCategory) {
		try {
			if(validateMS(tdsItem.getResponse().getContent())){
				ResponseCategory responseCategory = itemCategory.getResponseCategory();
				responseCategory.setIsResponseValid(true);
				String itemFormat = tdsItem.getFormat();
				responseCategory.setTdsFormat(itemFormat);
				String tdsScore = tdsItem.getScore();
				responseCategory.setTdsItemScore(Integer.parseInt(tdsScore));
				
				Itemrelease.Item.Attriblist attriblist = itemCategory.getAttriblist();
				if (attriblist != null){
					Itemrelease.Item.Attriblist.Attrib attrib = getItemAttribValueFromIRPitemAttriblist(attriblist,
							"itm_att_Answer Key");
					Itemrelease.Item.Attriblist.Attrib attribItemPoint = getItemAttribValueFromIRPitemAttriblist(attriblist,
							"itm_att_Item Point");
					String itemPointValue = attribItemPoint.getVal();
					String num = itemPointValue.replaceAll("[^0-9]", "");
					responseCategory.setItemScore(Integer.parseInt(num));
					if (attrib != null){
						Response response = tdsItem.getResponse();
						String irpItemAnswerKey = attrib.getVal();
						CellCategory cellCategory = getCellCategoryByFieldName(responseCategory.getCellCategories(), "content");
						cellCategory.setTdsExpectedValue(irpItemAnswerKey);
						
						List<String> list1 = Lists.newArrayList(Splitter.on(",").trimResults().omitEmptyStrings()
								.splitToList(irpItemAnswerKey.toLowerCase()));
						List<String> list2 = Lists.newArrayList(Splitter.on(",").trimResults().omitEmptyStrings()
								.splitToList(response.getContent().toLowerCase()));
						if (compare(list1, list2)) {
							setCcorrect(fieldCheckType);
						}
					}
				}
			}
		}catch (Exception e) {
			logger.error("processCForMS exception: ", e);
		}
	}
	
	private void processCForMC(Item tdsItem, FieldCheckType fieldCheckType, ItemCategory itemCategory) {
		try {
			if(validateMC(tdsItem.getResponse().getContent())){
				ResponseCategory responseCategory = itemCategory.getResponseCategory();
				responseCategory.setIsResponseValid(true);
				String itemFormat = tdsItem.getFormat();
				responseCategory.setTdsFormat(itemFormat);
				String tdsScore = tdsItem.getScore();
				responseCategory.setTdsItemScore(Integer.parseInt(tdsScore));
				
				Itemrelease.Item.Attriblist attriblist = itemCategory.getAttriblist();
				if (attriblist != null){
					Itemrelease.Item.Attriblist.Attrib attrib = getItemAttribValueFromIRPitemAttriblist(attriblist,
							"itm_att_Answer Key");
					Itemrelease.Item.Attriblist.Attrib attribItemPoint = getItemAttribValueFromIRPitemAttriblist(attriblist,
							"itm_att_Item Point");
					String itemPointValue = attribItemPoint.getVal();
					String num = itemPointValue.replaceAll("[^0-9]", "");
					responseCategory.setItemScore(Integer.parseInt(num)); 
					if (attrib != null){
						Response response = tdsItem.getResponse();
						String irpItemAnswerKey = attrib.getVal();
						CellCategory cellCategory = getCellCategoryByFieldName(responseCategory.getCellCategories(), "content");
						cellCategory.setTdsExpectedValue(irpItemAnswerKey);
						
						boolean blnCorrectAnswer = isCorrectValue(irpItemAnswerKey, response.getContent());
						if (blnCorrectAnswer) {
							setCcorrect(fieldCheckType);
						}
					}
				}
			}
		}catch (Exception e) {
			logger.error("processCForMC exception: ", e);
		}
	}
	
	/**
	 * this function checks whether the Response content is valid or not
	 * 		if item scoring engine can give a score, then the response content is valid. it may also indicate 
	 * 		content matches the item format
	 * @param tdsItem
	 * @param fieldCheckType
	 * @param itemCategory
	 */
	private void processCForItemScoring(Item tdsItem, FieldCheckType fieldCheckType, ItemCategory itemCategory) {
		try {
			ResponseCategory responseCategory = itemCategory.getResponseCategory();
			if (responseCategory != null) {
				Long itemKey = tdsItem.getKey();
				Response response = tdsItem.getResponse();
				String itemFormat = tdsItem.getFormat();
				String tdsScore = tdsItem.getScore(); // <xs:attribute name="score" use="required" type="UFloatAllowNegativeOne" />
			
				String rubric = getMachineRubricContent(itemCategory, responseCategory);
				responseCategory.setTdsFormat(itemFormat);
				responseCategory.setTdsItemScore(Integer.parseInt(tdsScore));
				if (rubric != null) {
					responseCategory.setRubric(rubric);
					ResponseInfo responseInfo = new ResponseInfo(
						itemFormat,
						Long.toString(itemKey),
						response.getContent(),
						rubric,
						RubricContentType.ContentString,
						"",
						false);
					
					ItemScore itemScore = itemScorer.ScoreItem(responseInfo, null);
					ItemScoreInfo itemScoreInfo = itemScore.getScoreInfo();
					
					responseCategory.setItemScoreInfo(itemScoreInfo);
					
					ScoringStatus sStatus = itemScoreInfo.getStatus();
					responseCategory.setScoringStatus(sStatus);
					responseCategory.setItemScore(itemScoreInfo.getPoints());
					
					CellCategory cellCategory = getCellCategoryByFieldName(responseCategory.getCellCategories(), "content");
					cellCategory.setTdsExpectedValue(ScoringStatus.Scored.toString());
					if (sStatus == ScoringStatus.Scored) {
						responseCategory.setIsResponseValid(true);
						setCcorrect(fieldCheckType);
					}
				} else {
					responseCategory.setRubricMissing(true);
					logger.warn("Scoring rubric missing for " + itemCategory.getItemBankKeyKey());
				}
			}
		} catch (Exception e) {
			logger.error("processCForItemScoring exception: ", e);
		}
	}

	/**
	 * 
	 * @param tdsResponseContent
	 *            tdsResponseContent stores the student response for item type MS in tds report xml file
	 * @return
	 */
	protected boolean validateMS(String tdsResponseContent) {
		List<String> list = Lists.newArrayList(Splitter.on(",").trimResults().omitEmptyStrings()
				.splitToList(tdsResponseContent.toLowerCase()));
		if (list.size() > 0)
			return true;
		return false;
	}


	/**
	 * 
	 * @param tdsResponseContent
	 *            tdsStudentResponse stores the student response for item type MC in tds report xml file
	 * @return return true if tdsStudentResponse includes only one digit or only one letter (uppercase/lowercase)
	 */
	protected boolean validateMC(String tdsResponseContent) {
		boolean bln = Pattern.matches("[\\dA-Za-z]{1}", tdsResponseContent);
		if (bln)
			return true;
		return false;
	}

	/**
	 * This method gets the machine rubric content for the given item. If no machine rubric exists for the item
     * then null will be returned.
	 * 
	 * @param itemCategory 
	 * 		itemCategory stores ReponseCategory, ScoreInfoCategory, itemBankKeyKey and irpItem objects.
	 *
	 * @return Contents of the machine rubric if exists; otherwise, null
	 */
	String getMachineRubricContent(ItemCategory itemCategory, ResponseCategory responseCategory) {
		String rubric = null;
		try {
			Itemrelease.Item item = itemCategory.getIrpItem();
			String itemBankKeyKey = itemCategory.getItemBankKeyKey();
			List<Itemrelease.Item.MachineRubric> machineRubrics = item.getMachineRubric();
	
            if (machineRubrics != null && machineRubrics.size() > 0) {
                Itemrelease.Item.MachineRubric machineRubric = machineRubrics.get(0);
                String fileName = machineRubric.getFilename();
                responseCategory.setMachineRubricFileName(fileName);
                if (StringUtils.isNotBlank(fileName)){
                	rubric = machineRubricLoader.getContents(itemBankKeyKey.concat("/").concat(fileName));
                }
            }
		} catch (Exception exp) {
			logger.error("Unable to get rubric content", exp);
		}

		return rubric;
	}



}
