package org.cresst.sb.irp.analysis.engine;

import AIR.Common.xml.XmlElement;
import AIR.Common.xml.XmlReader;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
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
import org.jdom2.Document;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tds.itemscoringengine.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class ItemResponseAnalysisAction extends AnalysisAction<Response, ItemResponseAnalysisAction.EnumItemResponseFieldName, Itemrelease.Item.Attriblist> {
	private final static Logger logger = LoggerFactory.getLogger(ItemResponseAnalysisAction.class);

    static public enum EnumItemResponseFieldName {
		date, type, content
	}

    private IItemScorer itemScorer;
    private MachineRubricLoader machineRubricLoader;

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
					analyzeItemResponse(itemCategory, tdsItem);
					//analyzeItemResponseWithStudentReponse(itemCategory, studentResponse);
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
			FieldCheckType fieldCheckType;

			responseCategory.setDate((response.getDate() != null) ? response.getDate().toString() : "");
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			responseCategory.setDateFieldCheckType(fieldCheckType);
			validateField(response, EnumFieldCheckType.P, EnumItemResponseFieldName.date, fieldCheckType);
			
			responseCategory.setType(response.getType());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P); // changed to P only. if add C, need to implement
			responseCategory.setTypeFieldCheckType(fieldCheckType);
			validateField(response, EnumFieldCheckType.P, EnumItemResponseFieldName.type, fieldCheckType);
			
			responseCategory.setContent(response.getContent());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			responseCategory.setContentFieldCheckType(fieldCheckType);
			analysisItemResponseItemScoring(tdsItem, fieldCheckType, responseCategory, itemCategory);
			//responseCategory.setIsResponseValid(true); TODO 
		} catch (Exception e) {
			logger.error("analyzeItemResponse exception: ", e);
		}
	}

	private void validateField(Response response, EnumFieldCheckType enumFieldCheckType, EnumItemResponseFieldName enumFieldName,
			FieldCheckType fieldCheckType) {
		try {
			switch (enumFieldCheckType) {
			case D:
				break;
			case P:
				checkP(response, enumFieldName, fieldCheckType);
				break;
			case PC:
				checkP(response, enumFieldName, fieldCheckType);
				checkC(response, enumFieldName, fieldCheckType, null); //TODO 
				break;
			}
		} catch (Exception e) {
			logger.error("validateField exception: ", e);
		}
	}

	/**
	 * 
	 * @param tdsItem
	 *            TDS Item object stores Item attributes and Response data 
	 * @param fieldCheckType
	 *            This is where the results are stored
	 * @param responseCategory
	 *            ResponseCategory object stores date, type, content from TDS Response. It also stores ItemScoreInfo itemScoreInfo
	 * @param itemCategory
	 *            itemCategory object stores ReponseCategory, ScoreInfoCategory and irpItem objects.
	 */
	protected void analysisItemResponseItemScoring(Item tdsItem, FieldCheckType fieldCheckType,
			ResponseCategory responseCategory, ItemCategory itemCategory) {
		try {
			org.cresst.sb.irp.domain.items.Itemrelease.Item irpItem = getItemByIdentifier(itemCategory.getItemBankKeyKey());
			if (irpItem != null) {
				itemCategory.setIrpItem(irpItem);
				Itemrelease.Item.Attriblist attriblist = getItemAttriblistFromIRPitem(irpItem);
				itemCategory.setAttriblist(attriblist);

				String format = tdsItem.getFormat().toLowerCase();
				Response response = tdsItem.getResponse();
				switch (format) {
				case "ms":
					validateFieldMS(response, EnumFieldCheckType.PC, EnumItemResponseFieldName.content, fieldCheckType, attriblist);
					break;
				case "mc":
					validateFieldMC(response, EnumFieldCheckType.PC, EnumItemResponseFieldName.content, fieldCheckType, attriblist);
					break;
				case "gi":
				case "ti":
				case "mi":
				case "eq":
					validateFieldItemScoring(tdsItem, EnumFieldCheckType.PC, EnumItemResponseFieldName.content, fieldCheckType,
							responseCategory, itemCategory);
					break;
				default: //hand scored items: wit, tut, sa, er, wer
					fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
					responseCategory.setContentFieldCheckType(fieldCheckType);
					validateField(response, EnumFieldCheckType.P, EnumItemResponseFieldName.content, fieldCheckType);
					break;
				}
			}
		} catch (Exception e) {
			logger.error("analysisItemResponseItemScoring exception: ", e);
		}
	}

	/**
	 * 
	 * @param tdsItem
	 *            TDS Item object stores Item attributes and Response data 
	 * @param enumFieldCheckType 
	 * 			 
	 * @param enumFieldName 
	 *            Specifies the field to check
	 * @param fieldCheckType
	 *            This is where the results are stored
	 * @param responseCategory
	 *            ResponseCategory object stores date, type, content from TDS Response. It also stores ItemScoreInfo itemScoreInfo
	 * @param itemCategory
	 *            itemCategory object stores ReponseCategory, ScoreInfoCategory and irpItem objects.
	 */
	private void validateFieldItemScoring(Item tdsItem, EnumFieldCheckType enumFieldCheckType, EnumItemResponseFieldName enumFieldName, 
			FieldCheckType fieldCheckType, ResponseCategory responseCategory, ItemCategory itemCategory) {
		try {
			switch (enumFieldCheckType) {
			case D:
				break;
			case P:
				checkP(tdsItem.getResponse(), enumFieldName, fieldCheckType);
				break;
			case PC:
				checkP(tdsItem.getResponse(), enumFieldName, fieldCheckType);
				checkCForItemScoring(tdsItem, enumFieldName, fieldCheckType, responseCategory, itemCategory);
				break;
			}
		} catch (Exception e) {
			logger.error("validateFieldItemScoring exception: ", e);
		}
	}

	private void validateFieldMC(Response response, EnumFieldCheckType enumFieldCheckType,
			EnumItemResponseFieldName enumFieldName, FieldCheckType fieldCheckType, Itemrelease.Item.Attriblist attriblist) {
		try {
			switch (enumFieldCheckType) {
			case D:
				break;
			case P:
				checkP(response, enumFieldName, fieldCheckType);
				break;
			case PC:
				checkP(response, enumFieldName, fieldCheckType);
				checkCForMC(response, enumFieldName, fieldCheckType, attriblist);
				break;
			}
		} catch (Exception e) {
			logger.error("validateFieldMC exception: ", e);
		}
	}

	private void validateFieldMS(Response response, EnumFieldCheckType enumFieldCheckType,
			EnumItemResponseFieldName enumFieldName, FieldCheckType fieldCheckType, Itemrelease.Item.Attriblist attriblist) {
		try {
			switch (enumFieldCheckType) {
			case D:
				break;
			case P:
				checkP(response, enumFieldName, fieldCheckType);
				break;
			case PC:
				checkP(response, enumFieldName, fieldCheckType);
				checkCForMS(response, enumFieldName, fieldCheckType, attriblist);
				break;
			}
		} catch (Exception e) {
			logger.error("validateFieldMS exception: ", e);
		}
	}

	/**
	 * Field Check Type (P) --> check that field is not empty, and field value is of correct data type and within acceptable
	 * values
	 *
	 * @param response
	 *            Response with fields to check
	 * @param enumFieldName
	 *            Specifies the field to check
	 * @param fieldCheckType
	 *            This is where the results are stored
	 */
	@Override
	protected void checkP(Response response, EnumItemResponseFieldName enumFieldName, FieldCheckType fieldCheckType) {
		try {
			switch (enumFieldName) {
			case date:
				// Required N. xsd <xs:attribute name="date" type="xs:dateTime" />
				if (response.getDate() != null && response.getDate().toString().length() > 0)
					setPcorrect(fieldCheckType);
				break;
			case type:
				// <xs:attribute name="type">
				// <xs:simpleType>
				// <xs:restriction base="xs:token">
				// <xs:enumeration value="value" />
				// <xs:enumeration value="reference" />
				// <xs:enumeration value="" />
				// </xs:restriction>
				// </xs:simpleType>
				processP(response.getType(), fieldCheckType, false); // Required N
				break;
			case content:
				processP(response.getContent(), fieldCheckType, false); // Required N
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("checkP exception: ", e);
		}
	}

	/**
	 * Checks if the field has the correct value
	 *
	 * @param response
	 *            Response object with field to check
	 * @param enumFieldName
	 *            enum EnumItemResponseFieldName
	 * @param fieldCheckType
	 *            This is where the results are stored
	 * @param attriblist
	 *            Attriblist to compare against Response
	 */
	@Override
	protected void checkC(Response response, EnumItemResponseFieldName enumFieldName, FieldCheckType fieldCheckType,
			Itemrelease.Item.Attriblist attriblist) {
		try {
			switch (enumFieldName) {
			case date:
				break;
			case type:
				setCcorrect(fieldCheckType);
				break;
			case content:
				processC(response.getContent(), fieldCheckType, attriblist);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("checkC exception: ", e);
		}
	}

	protected void checkCForItemScoring(Item tdsItem, EnumItemResponseFieldName enumFieldName, FieldCheckType fieldCheckType,
                                        ResponseCategory responseCategory, ItemCategory itemCategory) {
		try {
			switch (enumFieldName) {
			case date:
				break;
			case type:
				setCcorrect(fieldCheckType);
				break;
			case content:
				processCForItemScoring(tdsItem, fieldCheckType, responseCategory, itemCategory);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("checkCForItemScoring exception: ", e);
		}
	}

	protected void checkCForMC(Response response, EnumItemResponseFieldName enumFieldName, FieldCheckType fieldCheckType,
                               Itemrelease.Item.Attriblist attriblist) {
		try {
			switch (enumFieldName) {
			case date:
				break;
			case type:
				setCcorrect(fieldCheckType);
				break;
			case content:
				processCForMC(response.getContent(), fieldCheckType, attriblist);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("checkCForMC exception: ", e);
		}
	}

	protected void checkCForMS(Response response, EnumItemResponseFieldName enumFieldName, FieldCheckType fieldCheckType,
                               Itemrelease.Item.Attriblist attriblist) {
		try {
			switch (enumFieldName) {
			case date:
				break;
			case type:
				setCcorrect(fieldCheckType);
				break;
			case content:
				processCForMS(response.getContent(), fieldCheckType, attriblist);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("checkCForMS exception: ", e);
		}
	}

	private void processC(String tdsResponseContent, FieldCheckType fieldCheckType, Itemrelease.Item.Attriblist attriblist) {
		try {
			if (attriblist != null) {
				Itemrelease.Item.Attriblist.Attrib attrib = getItemAttribValueFromIRPitemAttriblist(attriblist,
						"itm_att_Answer Key");
				String irpItemAnswerKey = attrib.getVal();
				boolean blnCorrectAnswer = isCorrectValue(irpItemAnswerKey, tdsResponseContent);
				if (blnCorrectAnswer) {
					setCcorrect(fieldCheckType);
				}
			}
		} catch (Exception e) {
			logger.error("processC exception: ", e);
		}
	}

	private void processCForItemScoring(Item tdsItem, FieldCheckType fieldCheckType, ResponseCategory responseCategory,
                                        ItemCategory itemCategory) {
		try {
			Long itemKey = tdsItem.getKey();
			Response response = tdsItem.getResponse();
			String itemFormat = tdsItem.getFormat();

			String rubric = getMachineRubricContent(itemCategory);
			System.out.println("rubric ->" + rubric);
            if (rubric != null) {
                ResponseInfo responseInfo = new ResponseInfo(
                        itemFormat,
                        Long.toString(itemKey),
                        response.getContent(),
                        rubric,
                        RubricContentType.ContentString,
                        "",
                        false);

                //TODO need to get itemScorer bean
                /*
                 *  <bean id="itemScorer" class="org.cresst.sb.irp.itemscoring.client.IrpProxyItemScorer">
        		 * 		<constructor-arg ref="irpProxyRestTemplate" />
        		 "		<constructor-arg value="${item.scoring.service.url}" />
    			 * 	</bean>
                 */
                
                ItemScore itemScore = itemScorer.ScoreItem(responseInfo, null);
                ItemScoreInfo itemScoreInfo = itemScore.getScoreInfo();

                responseCategory.setItemScoreInfo(itemScoreInfo);

                ScoringStatus sStatus = itemScoreInfo.getStatus();
                if (sStatus.Scored == ScoringStatus.Scored) {
                    setCcorrect(fieldCheckType);
                }
            }
		} catch (Exception e) {
			logger.error("processCForItemScoring exception: ", e);
		}
	}

	private void processCForMC(String tdsResponseContent, FieldCheckType fieldCheckType, Itemrelease.Item.Attriblist attriblist) {
		try {
			if (attriblist != null) {
				Itemrelease.Item.Attriblist.Attrib attrib = getItemAttribValueFromIRPitemAttriblist(attriblist,
						"itm_att_Answer Key");
				String irpItemAnswerKey = attrib.getVal();
				boolean blnCorrectAnswer = isCorrectValue(irpItemAnswerKey, tdsResponseContent);
				if (blnCorrectAnswer) {
					setCcorrect(fieldCheckType);
				}
			}
		} catch (Exception e) {
			logger.error("processCForMC exception: ", e);
		}
	}

	private void processCForMS(String tdsResponseContent, FieldCheckType fieldCheckType, Itemrelease.Item.Attriblist attriblist) {
		try {
			if (attriblist != null) {
				Itemrelease.Item.Attriblist.Attrib attrib = getItemAttribValueFromIRPitemAttriblist(attriblist,
						"itm_att_Answer Key");
				String irpItemAnswerKey = attrib.getVal();
				List<String> list1 = Lists.newArrayList(Splitter.on(",").trimResults().omitEmptyStrings()
						.splitToList(irpItemAnswerKey.toLowerCase()));
				List<String> list2 = Lists.newArrayList(Splitter.on(",").trimResults().omitEmptyStrings()
						.splitToList(tdsResponseContent.toLowerCase()));
				if (compare(list1, list2)) {
					setCcorrect(fieldCheckType);
				}
			}
		} catch (Exception e) {
			logger.error("processCForMS exception: ", e);
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
	 * This method created/modified based on AIR open source QTIItemScorer.java in item-scoring-engine
	 * 
	 * @param studentResponse
	 *            The CDATA content from tds Report xml file
	 * @return Map<String, String> Store response id and value result separatored by ','
	 */
	protected Map<String, String> retrieveItemResponse(String studentResponse) {
		Map<String, String> identifiersAndResponses = new HashMap<>();
		// first try to retrieve the item response, and the identifier
		try {
			XmlReader reader = new XmlReader(new StringReader(studentResponse));
			Document doc = reader.getDocument();
			List<Element> responseNodes = new XmlElement(doc.getRootElement()).selectNodes("//itemResponse/response");
			for (Element elem : responseNodes) {
				String identifier = elem.getAttributeValue("id");
				List<String> responses = new ArrayList<String>();
				List<Element> valueNodes = new XmlElement(elem).selectNodes("value");
				for (Element valElem : valueNodes) {
					responses.add(valElem.getText());
				}

				// if (!identifiersAndResponses.containsKey(identifier)) {
				identifiersAndResponses.put(identifier, StringUtils.join(responses, ','));
				// } else {
				// identifiersAndResponses.put (identifier, StringUtils.join(",",
				// responses));
				// }
			}
		} catch (final Exception e) {
			logger.info("Error loading response");
		}

		if (identifiersAndResponses.size() == 0) {
			logger.info("No responses found");
			return null;
		}
		return identifiersAndResponses;
	}


	protected ItemCategory getItemCategoryByBankKeyKey(String bankKey, String key, List<ItemCategory> listItemCategory,
			ItemCategory.ItemStatusEnum itemStatusEnum) {

		for (ItemCategory itemCategory : listItemCategory) {
			ImmutableList<CellCategory> cellCategories = itemCategory.getCellCategories();
			String _bankKey = getTdsFieldNameValueByFieldName(cellCategories, "bankKey");
			String _key = getTdsFieldNameValueByFieldName(cellCategories, "key");

			if (bankKey.equalsIgnoreCase(_bankKey)
                    && key.equalsIgnoreCase(_key)
                    && itemCategory.getStatus() == itemStatusEnum
					&& itemCategory.isItemFormatCorrect()) {
				return itemCategory;
			}
		}

		return null;
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
	String getMachineRubricContent(ItemCategory itemCategory) {
		String rubric = null;
		try {
			Itemrelease.Item item = itemCategory.getIrpItem();
			String itemBankKeyKey = itemCategory.getItemBankKeyKey();
			List<Itemrelease.Item.MachineRubric> machineRubrics = item.getMachineRubric();
			
            if (machineRubrics != null && machineRubrics.size() > 0) {
                Itemrelease.Item.MachineRubric machineRubric = machineRubrics.get(0);
                String fileName = machineRubric.getFilename();
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
