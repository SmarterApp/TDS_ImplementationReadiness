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
import org.cresst.sb.irp.domain.studentresponse.StudentResponse;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Item;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Item.Response;
import org.cresst.sb.irp.domain.tinytablescoringengine.Table;
import org.cresst.sb.irp.domain.tinytablescoringengine.TableCell;
import org.cresst.sb.irp.domain.tinytablescoringengine.TableVector;
import org.cresst.sb.irp.itemscoring.rubric.MachineRubricLoader;
import org.jdom2.Document;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qtiscoringengine.cs2java.StringHelper;
import tds.itemscoringengine.*;
import tinyequationscoringengine.MathExpression;
import tinyequationscoringengine.MathExpressionInfo;
import tinyequationscoringengine.MathExpressionSet;
import tinyequationscoringengine.MathMLParser;
import tinygrscoringengine.*;
import tinygrscoringengine.GRObject.ObjectType;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class ItemResponseAnalysisAction extends
		AnalysisAction<Response, ItemResponseAnalysisAction.EnumItemResponseFieldName, Itemrelease.Item.Attriblist> {
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

			ExamineeCategory examineeCategory = individualResponse.getExamineeCategory();
			String examineeKey = getTdsFieldNameValueByFieldName(examineeCategory.getCellCategories(), "key");

            OpportunityCategory opportunityCategory = individualResponse.getOpportunityCategory();
			List<ItemCategory> itemCategories = opportunityCategory.getItemCategories(); // combination of FOUND, MISSING, EXTRA

            Opportunity opportunity = tdsReport.getOpportunity();
			List<Item> tdsItems = opportunity.getItem();

            for (Item item : tdsItems) {
				ItemCategory itemCategory = getItemCategoryByBankKeyKey(Long.toString(item.getBankKey()),
						Long.toString(item.getKey()), itemCategories, ItemStatusEnum.FOUND);

                if (itemCategory != null) {
					StudentResponse studentResponse = getStudentResponseByStudentIDandBankKeyID(Long.parseLong(examineeKey),
							Long.toString(item.getBankKey()), Long.toString(item.getKey()));

					if (studentResponse != null) {
						analyzeItemResponse(itemCategory, item, studentResponse);
						analyzeItemResponseWithStudentReponse(itemCategory, studentResponse);
					}
				}
			}
		} catch (Exception e) {
			logger.error("analyze exception: ", e);
		}
	}

	private void analyzeItemResponse(ItemCategory itemCategory, Item tdsItem, StudentResponse studentResponse) {
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
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			responseCategory.setTypeFieldCheckType(fieldCheckType);
			validateField(response, EnumFieldCheckType.PC, EnumItemResponseFieldName.type, fieldCheckType);

			responseCategory.setContent(response.getContent());
			if (isValidStudentResponse(tdsItem, studentResponse)) {
				responseCategory.setIsResponseValid(true);
				fieldCheckType = new FieldCheckType();
				fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
				responseCategory.setContentFieldCheckType(fieldCheckType);
				analysisItemResponseItemScoring(tdsItem, fieldCheckType, responseCategory, itemCategory);
			}
		} catch (Exception e) {
			logger.error("analyzeItemResponse exception: ", e);
		}
	}

	private void analyzeItemResponseWithStudentReponse(ItemCategory itemCategory, StudentResponse studentResponse) {
		try {
			if (studentResponse != null) {
				ResponseCategory responseCategory = itemCategory.getResponseCategory();
				responseCategory.setStudentResponse(studentResponse);
				studentResponse.setTdsResponseContent(responseCategory.getContent());
				validateStudentResponse(studentResponse);
			}
		} catch (Exception e) {
			logger.error("analyzeItemResponseWithStudentResponse exception: ", e);
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
				checkC(response, enumFieldName, fieldCheckType, null);
				break;
			}
		} catch (Exception e) {
			logger.error("validateField exception: ", e);
		}
	}

	/**
	 * 
	 * @param tdsItem
	 *            Item object stores Item attributes data for tds report xml item tag
	 * @param fieldCheckType
	 *            This is where the results are stored
	 * @param responseCategory
	 *            ResponseCategory object stores ItemScoreInfo itemScoreInfo
	 * @param itemCategory
	 *            itemCategory object
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
					validateFieldMS(response, EnumFieldCheckType.PC, EnumItemResponseFieldName.content, fieldCheckType,
							attriblist);
					break;
				case "mc":
					validateFieldMC(response, EnumFieldCheckType.PC, EnumItemResponseFieldName.content, fieldCheckType,
							attriblist);
					break;
				case "gi":
				case "ti":
				case "mi":
				case "eq":
				case "er":
					validateFieldItemScoring(tdsItem, EnumFieldCheckType.PC, EnumItemResponseFieldName.content, fieldCheckType,
							responseCategory, itemCategory);
					break;
				default:
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
	 *            Item object stores Item attributes data for tds report xml item tag
	 * @param enumFieldCheckType
	 *            enum EnumFieldCheckType
	 * @param enumFieldName
	 *            Specifies the field to check
	 * @param fieldCheckType
	 *            This is where the results are stored
	 * @param responseCategory
	 *            ResponseCategory object stores ItemScoreInfo itemScoreInfo
	 * @param itemCategory
	 *            itemCategory object
	 */
	private void validateFieldItemScoring(Item tdsItem, EnumFieldCheckType enumFieldCheckType,
			EnumItemResponseFieldName enumFieldName, FieldCheckType fieldCheckType, ResponseCategory responseCategory,
			ItemCategory itemCategory) {
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

			String rubric = getMachineRubricContent(itemCategory.getIrpItem());
            if (rubric != null) {
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

	private void validateStudentResponse(StudentResponse studentResponse) {
		try {
			String format = studentResponse.getItemType().toLowerCase();
			switch (format) {
			case "er":
				validateER(studentResponse);
				break;
			case "mi":
				validateMI(studentResponse);
				break;
			case "eq":
				validateEQ(studentResponse);
				break;
			case "gi":
				validateGI(studentResponse);
				break;
			case "ms":
				validateMS(studentResponse);
				break;
			case "mc":
				validateMC(studentResponse);
				break;
			case "ti":
				validateTI(studentResponse);
				break;
			}
		} catch (Exception e) {
			logger.error("validateStudentResponse exception: ", e);
		}
	}

	/**
	 * Parsing student response for item type ER and match its corresponding student's response in excel file
	 * 
	 * @param studentResponse
	 *            StudentResponse stores the student response for item type ER in tds report xml file
	 */
	private void validateER(StudentResponse studentResponse) {
		// TODO need to get excelResponseContent from another column which in NOT exist now in excel file
		String excelResponseContent = studentResponse.getResponseContent();
		String tdsResponseContent = studentResponse.getTdsResponseContent();

		// TODO - need to modify following code after above excelResponseContent update
		if (excelResponseContent.equals("any text") && tdsResponseContent.length() > 0) {
			studentResponse.setStatus(true);
		}

	}

	protected boolean validateER(String studentResponse) {
		return StringUtils.isNotBlank(studentResponse);
	}

	/**
	 * Parsing student response for item type MI and match its corresponding student's response in excel file
	 * 
	 * @param studentResponse
	 *            StudentResponse stores the student response for item type MI in tds report xml file
	 */
	private void validateMI(StudentResponse studentResponse) {
		// TODO need to get excelResponseContent from another column which in NOT exist now in excel file
		String excelResponseContent = studentResponse.getResponseContent();

		// TODO This part will be removed when an additional column added in student responses file
		// This column store CDATA content.
		String[] parts = excelResponseContent.split(";");
		String strYesValue = "";
		for (int i = 0; i < parts.length; i++) {
			String strTmp = parts[i];
			if (strTmp.contains("YES")) {
				String strRemoveYESfor = strTmp.replace("YES for", "");
				String[] choiceArr = strRemoveYESfor.split("and");
				for (int j = 0; j < choiceArr.length; j++) {
					strYesValue += choiceArr[j].trim();
					if (j < choiceArr.length - 1)
						strYesValue += ",";
				}
				break;
			}
		}

		// TODO need to update following if (strYesValue.equals(identifiersAndResponses.get("RESPONSE"))) {
		String tdsResponseContent = studentResponse.getTdsResponseContent();
		Map<String, String> identifiersAndResponses = retrieveItemResponse(tdsResponseContent);
		if (identifiersAndResponses != null && identifiersAndResponses.size() > 0) {
			logger.info(String.format("identifiersAndResponses.get(RESPONSE) ->%s", identifiersAndResponses.get("RESPONSE")));
			if (strYesValue.equals(identifiersAndResponses.get("RESPONSE"))) {
				studentResponse.setStatus(true);
			}
		}
	}

	protected boolean validateMI(String tdsStudentResponse) {
		Map<String, String> identifiersAndResponses = retrieveItemResponse(tdsStudentResponse);
		if (identifiersAndResponses != null && identifiersAndResponses.size() > 0)
			return true;
		return false;
	}

	/**
	 * Parsing student response for item type EQ and match its corresponding student's response in excel file
	 * 
	 * @param studentResponse
	 *            StudentResponse stores the student response for item type EQ in tds report xml file
	 */
	private void validateEQ(StudentResponse studentResponse) {
		// TODO need to get excelResponseContent from another column which in NOT exist now in excel file
		String excelResponseContent = studentResponse.getResponseContent();
		String tdsResponseContent = studentResponse.getTdsResponseContent();

		try {
			logger.info(String.format("tdsResponseContent -->%s", tdsResponseContent));
			MathExpressionSet mathExpressionSet = MathMLParser.processMathMLData(tdsResponseContent.trim());
			if (mathExpressionSet.size() > 0) {
				// TODO update following code to validate excelResponseContent
				for (MathExpression me : mathExpressionSet) {
					System.out.println("me.toString() ->" + me.toString());
					MathExpressionInfo meinfo = me.toMathExpressionInfo();
					System.out.println("AppliedCorrection ->" + meinfo.getAppliedCorrection());
					List<String> lsOvercorrectedSympyResponse = meinfo.getOvercorrectedSympyResponse();
					if (lsOvercorrectedSympyResponse != null)
						for (String s : meinfo.getOvercorrectedSympyResponse())
							System.out.println("OvercorrectedSympyResponse ->" + s);
					for (String s : meinfo.getSympyResponseNotSimplified())
						System.out.println("SympyResponseNotSimplified ->" + s);
					System.out.println("TriedToApplyCorrection ->" + meinfo.getTriedToApplyCorrection());
					for (String s : meinfo.getSympyResponse())
						System.out.println("SympyResponse  ->" + s);
				}

			} else if (!tdsResponseContent.trim().startsWith("<response>") && tdsResponseContent.trim().length() > 0) {
				// TODO need to update once excelResponseContent retrieved from another column in excel file
				if (tdsResponseContent.trim().equals(excelResponseContent)) {
					studentResponse.setStatus(true);
				}

			}
		} catch (Exception e) {
			logger.error("validateEQ exception: ", e);
		}

	}

	protected boolean validateEQ(String studentResponse) {
		try {
			MathExpressionSet mathExpressionSet = MathMLParser.processMathMLData(studentResponse.trim());
			if (mathExpressionSet.size() > 0) {
				return true;
			}
		} catch (Exception e) {
			logger.error("validateEQ exception: ", e);
		}
		return false;
	}

	/**
	 * Parsing student response for item type GI and match its corresponding student's response in excel file
	 * 
	 * @param studentResponse
	 *            StudentResponse stores the student response for item type GI in tds report xml file
	 */
	private void validateGI(StudentResponse studentResponse) {
		// TODO need to get excelResponseContent from another column which in NOT exist now in excel file
		String excelResponseContent = studentResponse.getResponseContent();
		String tdsResponseContent = studentResponse.getTdsResponseContent();

		try {
			logger.info(String.format("tdsResponseContent -->%s", tdsResponseContent));
			List<GRObject> listGRObject = getObjectStrings(tdsResponseContent);
			if (listGRObject != null) {
				// TODO need to update following to validate excelResponseContent
				ObjectType objectType = null;
				for (GRObject go : listGRObject) {
					logger.info(String.format("strxml -->%s", go.getXmlString()));
					logger.info(String.format("typeofObject -->%s", go.getTypeOfObject()));
					objectType = go.getTypeOfObject();
					break;
				}

				switch (objectType) {
				case Atomic:
					logger.info("Atomic");
					// studentResponse.setStatus(false);
					break;
				case Point:
					logger.info("Point");
					// studentResponse.setStatus(false);
					break;
				case Geometric:
					logger.info("Geometric");
					// studentResponse.setStatus(false);
					break;
				case RegionGroup:
					logger.info("RegionGroup");
					// studentResponse.setStatus(false);
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			logger.error("validateGI exception: ", e);
		}
	}

	/**
	 * this method parse student response in studentResponse to get sub format (ObjectType like RegionGroup then parse tds student
	 * response in tdsStudentResponse to get ObjectType
	 * 
	 * @param tdsResponseContent
	 *            - student response in tds report xml file
	 * @param studentResponse
	 *            - student response in IRP package (Excel)
	 * 
	 * @return
	 */
	protected boolean validateGI(String tdsResponseContent, StudentResponse studentResponse) {

		List<GRObject> listGRObject = null;
		ObjectType excelObjectType = null;
		// TODO
		// parse excelStudentResponse to get the sub format like RegionGroupObject, AtomicObject, Object
		// String excelStudentResponse = studentResponse.getStudentResponse() //the real student response column does not EXIST
		// now
		/*
		 * listGRObject = getObjectStrings(excelStudentResponse); if (listGRObject != null) { for (GRObject go : listGRObject) {
		 * logger.info(String.format("strxml -->%s", go.getXmlString())); logger.info(String.format("typeofObject -->%s",
		 * go.getTypeOfObject())); excelObjectType = go.getTypeOfObject(); break; } }
		 */

		ObjectType tdsObjectType = null;
		listGRObject = getObjectStrings(tdsResponseContent);
		if (listGRObject != null) {
			for (GRObject go : listGRObject) {
				logger.info(String.format("strxml -->%s", go.getXmlString()));
				logger.info(String.format("typeofObject -->%s", go.getTypeOfObject()));
				tdsObjectType = go.getTypeOfObject();
				break;
			}
		}

		if (excelObjectType != null && tdsObjectType != null && excelObjectType.equals(tdsObjectType))
			return true;
		return false;
	}

	/**
	 * Parsing student response for item type MS and match its corresponding student's response in excel file
	 * 
	 * @param studentResponse
	 *            StudentResponse stores the student response for item type MS in tds report xml file
	 * 
	 */
	private void validateMS(StudentResponse studentResponse) {
		// TODO need to get excelResponseContent from another column which in NOT exist now in excel file
		String excelResponseContent = studentResponse.getResponseContent();
		String tdsResponseContent = studentResponse.getTdsResponseContent();

		List<String> list1 = Lists.newArrayList(Splitter.on(",").trimResults().omitEmptyStrings()
				.splitToList(excelResponseContent.toLowerCase()));
		List<String> list2 = Lists.newArrayList(Splitter.on(",").trimResults().omitEmptyStrings()
				.splitToList(tdsResponseContent.toLowerCase()));
		if (compare(list1, list2)) {
			studentResponse.setStatus(true);
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
	 * Parsing student response for item type MC and match its corresponding student's response in excel file
	 * 
	 * @param studentResponse
	 *            StudentResponse stores the student response for item type MC in tds report xml file
	 * 
	 */
	private void validateMC(StudentResponse studentResponse) {
		// TODO need to get excelResponseContent from another column which in NOT exist now in excel file
		String excelResponseContent = studentResponse.getResponseContent();
		String tdsResponseContent = studentResponse.getTdsResponseContent();

		if (excelResponseContent.equalsIgnoreCase(tdsResponseContent))
			studentResponse.setStatus(true);

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
	 * Parsing student response for item type TI and match its corresponding student's response in excel file
	 * 
	 * @param studentResponse
	 *            StudentResponse stores the student response for item type TI in tds report xml file
	 * 
	 */
	private void validateTI(StudentResponse studentResponse) {
		// TODO need to get excelResponseContent from another column which in NOT exist now in excel file
		String excelResponseContent = studentResponse.getResponseContent();
		String tdsResponseContent = studentResponse.getTdsResponseContent();
		try {
			logger.info("tdsResponseContent -->" + tdsResponseContent);
			Table table = getTableObject(tdsResponseContent);
			if (table != null) {
				// TODO
				if (matchTI(table, excelResponseContent))
					studentResponse.setStatus(true);
			}
		} catch (Exception e) {
			logger.error("validateTI exception: ", e);
		}
	}

	protected boolean validateTI(String tdsResponseContent) {
		Table table = getTableObject(tdsResponseContent);
		if (table != null)
			return true;
		return false;
	}

	protected boolean matchTI(Table table, String studentResponse) {
		int rowCountWithHeader = table.getRowCount() - 1;
		int rowCountStudentResponse = 2; // need to discuss how to get row count for student response
		int columnCountStudentResponse = 2; // need to discuss how to get col count for student response
		if ((rowCountWithHeader != rowCountStudentResponse) || (table.getColumnCount() != columnCountStudentResponse))
			return false;

		for (int i = 0; i < table.getRowCount(); i++) {
			TableVector tableVector = table.getRowIndex(i);
			if (!tableVector.isHeader) { // skip header
				TableCell[] tcArray = tableVector.getElements();
				for (int j = 0; j < tcArray.length; j++) {
					TableCell tcTmp = tcArray[j];
					String context = tcTmp.getContext();
					logger.info("context -->" + context);
					// TODO -- need to match cell value in student response
				}
			}
		}
		return true;
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

	/**
	 * This method create/modified based on AIR related classes in package tinytablescoringengine
	 * 
	 * @param responseContent
	 *            The student response from tds report xml file (item type : TI)
	 * @return Table object Table is subclass of TableObject
	 */
	protected Table getTableObject(String responseContent) {
		// Table table = null;
		try {
			StringReader sr = new StringReader(responseContent);
			XmlReader reader = new XmlReader(sr);
			Document doc = reader.getDocument();
			List<Element> responseSpec = new XmlElement(doc.getRootElement()).selectNodes("//responseSpec");
			for (Element child : responseSpec) {
				if (child.getName().equals("responseSpec")) {
					return Table.fromXml(child.getChild("responseTable"));
				}
			}
		} catch (Exception exp) {
			logger.error("getTableObject exception: ", exp);
		}

		return null;
	}

	/**
	 * This method created/modified based on AIR open source TinyGR.java in item-scoring-eingine
	 * 
	 * @param answerSet
	 *            The student response from tds report xml file (item format: GI )
	 * @return List<String> objects stores xml elements data
	 */
	protected List<GRObject> getObjectStrings(String answerSet) {
		List<GRObject> objects = null;
		try {
			objects = new ArrayList<>();
			StringWriter sw = new StringWriter();
			StringBuilder sb = new StringBuilder();
			sb.append(sw);
			StringReader sr = new StringReader(answerSet);
			XmlReader reader = new XmlReader(sr);
			Document doc = reader.getDocument();
			List<Element> objectSet = new XmlElement(doc.getRootElement())
					.selectNodes("//AnswerSet//Question//QuestionPart//ObjectSet");
			for (Element child : objectSet) {
				for (Element childOfObjectSet : child.getChildren()) {
					if (childOfObjectSet.getName().equals("Object")) {
						// not able to use GRObject.createFromNode(childOfObjectSet); due to bugs metioned
						// in http://forum.opentestsystem.org/viewtopic.php?f=9&t=3276&sid=695b38a99415bf126009cdf998c43a49
						GRObject obj = createFromNode(childOfObjectSet);
						// GRObject obj = GRObject.createFromNode(childOfObjectSet);
						objects.add(obj);
					} else if (childOfObjectSet.getName().equals("RegionGroupObject")) {
						GRObject obj = GRObject.createFromNode(childOfObjectSet);
						// objects.add(obj.getXmlString());
						objects.add(obj);
						for (Element region : childOfObjectSet.getChildren()) {
							GRObject objRegion = GRObject.createFromNode(region);
							objects.add(objRegion);
							// outputObjectString(region, sw, objects, sb);
						}
					} else if (childOfObjectSet.getName().equals("AtomicObject")) {
						GRObject obj = GRObject.createFromNode(childOfObjectSet);
						objects.add(obj);
					}
				}
			}

		} catch (Exception exp) {
			logger.error("getObjectStrings exception: ", exp);
		}
		return objects;
	}

	/**
	 * This method created/modified based on AIR open source TinyGR.java and GRObject.java. It handles the item format = GI with
	 * <AnswerSet><Question id=""><QuestionPart id="1"><ObjectSet><Object><PointVector>{(
	 * 
	 * @param node
	 * @return GRObject or null
	 */
	private GRObject createFromNode(Element node) throws TinyGRException {
		switch (node.getName()) {
		case "Object":
			return GeoObjectFromXml(node);
		default:
			return null;
		}
	}

	/**
	 * This method created/modified based on AIR open source GeoObject.java
	 * 
	 * @param node
	 * @return
	 */
	private GRObject GeoObjectFromXml(Element node) throws TinyGRException {
		char[] toTrim = { ' ', '{', '}' };
		Element vectorNode = node.getChild("EdgeVector");
		Element pointNode = node.getChild("PointVector");
		String pointText = StringHelper.trim(pointNode.getText(), toTrim);
		String vectorText = vectorNode.getText().trim();

		List<Point> points = getPointObjects(pointText);
		int start = vectorText.indexOf('{');
		int end = vectorText.lastIndexOf('}');

		List<Vector> vectors = null;

		if ((start >= 0) && (end > 0)) {
			vectorText = vectorText.substring(start + 1, end - start);
			vectors = getVectorObjects(vectorText);
		}

		switch (vectors.size()) {
		case 0:
			if (points.size() == 1)
				return points.get(0);
			else {
				throw new TinyGRException(3, "What kind of object has multiple points but no vectors?");
			}
		case 1:
			return vectors.get(0);
		default:
			return new GeoObject(points, vectors);
		}
	}

	/**
	 * This method is from GeoObject.java provided by AIR
	 * 
	 * @param vectorText
	 * @return
	 */
	private List<Vector> getVectorObjects(String vectorText) {
		int endOfPoint;
		List<Vector> vectors = new ArrayList<Vector>();
		while (vectorText.contains("{")) // wrong thing to check...
		{
			vectors.add(Vector.getVectorObj(vectorText));
			endOfPoint = vectorText.indexOf("}");
			vectorText = vectorText.substring(endOfPoint + 1);
		}
		return vectors;
	}

	/**
	 * This method is from GeoObject.java provided by AIR
	 * 
	 * @param pointText
	 * @return
	 */
	private List<Point> getPointObjects(String pointText) {
		int endOfPoint;
		List<Point> points = new ArrayList<Point>();
		while (pointText.contains("(")) {
			points.add(Point.getPointObj(pointText));
			endOfPoint = pointText.indexOf(")");
			pointText = pointText.substring(endOfPoint + 1);
		}
		return points;
	}

	/**
	 * this method using student response in studentResponse (IRP package in Excel) including format and sub format to validate
	 * item.getResponse().getContent() is valid or not e.g format GI includes sub format RegionGroupObject, AtomicObject, Object
	 * 
	 * @param item
	 *            - item.getResponse().getContent() - tds report xml student response
	 * @param studentResponse
	 *            - IRP package (Excel) student response
	 * 
	 * @return boolean
	 */
	protected boolean isValidStudentResponse(Item item, StudentResponse studentResponse) {
		String response = item.getResponse().getContent();
		String format = item.getFormat().toLowerCase();
		boolean bln = false;
		switch (format) {
		case "er":
			bln = validateER(response);
			break;
		case "mi":
			bln = validateMI(response);
			break;
		case "eq":
			bln = validateEQ(response);
			break;
		case "gi":
			bln = validateGI(response, studentResponse); // need to hand sub format like RegionGroupObject, AtomicObject
			break;
		case "ms":
			bln = validateMS(response);
			break;
		case "mc":
			bln = validateMC(response);
			break;
		case "ti":
			bln = validateTI(response);
			break;
		default:
			break;
		}
		return bln;
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
	 * @param item The item to get the machine rubric from
	 *
	 * @return Contents of the machine rubric if exists; otherwise, null
	 */
	String getMachineRubricContent(Itemrelease.Item item) {
		String rubric = null;
		try {
			List<Itemrelease.Item.MachineRubric> machineRubrics = item.getMachineRubric();

            if (machineRubrics != null && machineRubrics.size() > 1) {
                Itemrelease.Item.MachineRubric machineRubric = machineRubrics.get(0);
                rubric = machineRubricLoader.getContents(machineRubric.getFilename());
            }
		} catch (Exception exp) {
			logger.error("Unable to get rubric content", exp);
		}

		return rubric;
	}

}
