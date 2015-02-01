package org.cresst.sb.irp.analysis.engine;

import org.apache.commons.lang3.StringUtils;
import org.cresst.sb.irp.domain.analysis.*;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.items.Itemrelease;
import org.cresst.sb.irp.domain.studentresponse.StudentResponse;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Item;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Item.Response;
import org.jdom2.Document;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import AIR.Common.xml.XmlElement;
import AIR.Common.xml.XmlReader;

import com.google.common.collect.ImmutableList;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemResponseAnalysisAction extends
		AnalysisAction<Response, ItemResponseAnalysisAction.EnumItemResponseFieldName, Itemrelease.Item.Attriblist> {
	private final static Logger logger = LoggerFactory.getLogger(ItemResponseAnalysisAction.class);

	static public enum EnumItemResponseFieldName {
		date, type, content
	}

	@Override
	public void analyze(IndividualResponse individualResponse) {
		try {
			TDSReport tdsReport = individualResponse.getTDSReport();
			ExamineeCategory examineeCategory = individualResponse.getExamineeCategory();
			String examineeKey = getTdsFieldNameValueByFieldName(examineeCategory.getCellCategories(), "key");
			OpportunityCategory opportunityCategory = individualResponse.getOpportunityCategory();
			List<ItemCategory> listItemCategory = opportunityCategory.getItemCategories();
			Opportunity opportunity = tdsReport.getOpportunity();
			List<Item> listItem = opportunity.getItem();
			int indexOfItemCategory = 0;
			for (Item i : listItem) {
				ItemCategory itemCategory = listItemCategory.get(indexOfItemCategory);
				analysisItemResponse(itemCategory, i);
				analysisItemResponseWithStudentReponse(itemCategory, i, Long.parseLong(examineeKey));
				indexOfItemCategory++;
			}
		} catch (Exception e) {
			logger.error("analyze exception: ", e);
		}
	}

	private void analysisItemResponse(ItemCategory itemCategory, Item tdsItem) {
		try {
			ResponseCategory responseCategory = new ResponseCategory();
			itemCategory.setResponseCategory(responseCategory);
			Response response = tdsItem.getResponse();
			FieldCheckType fieldCheckType;

			responseCategory.setDate(response.getDate().toString());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			responseCategory.setDateFieldCheckType(fieldCheckType);
			validateField(response, EnumFieldCheckType.P, EnumItemResponseFieldName.date, fieldCheckType);

			responseCategory.setType(response.getType());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			responseCategory.setTypeFieldCheckType(fieldCheckType);
			validateField(response, EnumFieldCheckType.PC, EnumItemResponseFieldName.type, fieldCheckType);

			StringBuilder itemIdentifier = new StringBuilder();
			itemIdentifier.append("item-").append(tdsItem.getBankKey()).append("-").append(tdsItem.getKey());
			itemCategory.setItemBankKeyKey(itemIdentifier.toString().trim());

			responseCategory.setContent(response.getContent());
			fieldCheckType = new FieldCheckType();
			String format = tdsItem.getFormat();
			if (format.trim().toLowerCase().equals("mc") || format.trim().toLowerCase().equals("ms")) {// handle MC, MS
				fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
				responseCategory.setContentFieldCheckType(fieldCheckType);
				org.cresst.sb.irp.domain.items.Itemrelease.Item irpItem = getItemByIdentifier(itemCategory.getItemBankKeyKey());
				itemCategory.setIrpItem(irpItem);
				Itemrelease.Item.Attriblist attriblist = getItemAttriblistFromIRPitem(irpItem);
				itemCategory.setAttriblist(attriblist);
				validateField(response, EnumFieldCheckType.PC, EnumItemResponseFieldName.content, fieldCheckType, attriblist);
			} else {
				fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
				responseCategory.setContentFieldCheckType(fieldCheckType);
				validateField(response, EnumFieldCheckType.P, EnumItemResponseFieldName.content, fieldCheckType);
			}
		} catch (Exception e) {
			logger.error("analysisItemResponse exception: ", e);
		}
	}

	private void analysisItemResponseWithStudentReponse(ItemCategory itemCategory, Item tdsItem, Long examineeKey) {
		try {
			ImmutableList<CellCategory> iList = itemCategory.getCellCategories();
			String format = getTdsFieldNameValueByFieldName(iList, "format");
			// String bankKey = getTdsFieldNameValueByFieldName(iList, "bankKey");
			String key = getTdsFieldNameValueByFieldName(iList, "key");
			StudentResponse studentResponse = getStudentResponseByStudentIDandBankKeyID(examineeKey, key);
			if (studentResponse != null && format.toLowerCase().equals(studentResponse.getItemType().toLowerCase())) {
				System.out.println("format ---->" + format + " key ===>" + key);
				ResponseCategory responseCategory = itemCategory.getResponseCategory();
				responseCategory.setStudentResponse(studentResponse);
				studentResponse.setTdsResponseContent(responseCategory.getContent());
				validateStudentResponse(studentResponse);
			}

		} catch (Exception e) {
			logger.error("analysisItemResponseWithStudentReponse exception: ", e);
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
	 * Static method against specific student response file. No generic method implementation for the time being
	 * 
	 * @param studentResponse
	 *            StudentResponse stores data for a student's item information of a test
	 */
	private void validateER(StudentResponse studentResponse) {
		String responseContent = studentResponse.getResponseContent(); 
		String tdsResponseContent = studentResponse.getTdsResponseContent();
		if (studentResponse.getTraningTestItem().equals("2")) {
			if (responseContent.equals("any text") && tdsResponseContent.length() > 0)
				studentResponse.setStatus(true);
		}
	}

	/**
	 * Static method against specific student response file. No generic method implementation for the time being
	 * 
	 * @param studentResponse
	 *            StudentResponse stores data for a student's item information of a test
	 */
	private void validateMI(StudentResponse studentResponse) {
		String responseContent = studentResponse.getResponseContent(); 
		
		// This part will be removed when an additional column added in student responses file
		// This column store CDATA content.
		String[] parts = responseContent.split(";");
		String strYesValue ="";
		for(int i=0; i< parts.length; i++){
			String strTmp = parts[i];
			if (strTmp.contains("YES"))
			{
				String strRemoveYESfor = strTmp.replace("YES for", "");
				String[] choiceArr = strRemoveYESfor.split("and");
				for(int j=0; j<choiceArr.length; j++){
					strYesValue += choiceArr[j].trim();
					if (j < choiceArr.length - 1)
						strYesValue += ",";
				}
				break;
			}
		}
		
		String tdsResponseContent = studentResponse.getTdsResponseContent();
		Map<String, String> identifiersAndResponses = retrieveItemResponse(tdsResponseContent);
		if (studentResponse.getTraningTestItem().equals("3") && identifiersAndResponses != null) {
				if(strYesValue.equals(identifiersAndResponses.get("RESPONSE"))){
					studentResponse.setStatus(true);
				}
				else{
					studentResponse.setStatus(false);
				}
		}
	}

	/**
	 * Static method against specific student response file. No generic method implementation for the time being
	 * 
	 * @param studentResponse
	 *            StudentResponse stores data for a student's item information of a test
	 */
	private void validateEQ(StudentResponse studentResponse) {
		String responseContent = studentResponse.getResponseContent(); 
		String tdsResponseContent = studentResponse.getTdsResponseContent();
		
		//need methods or classes to parse tdsResponseContent (CDATA content)
		//provoided by AIR
		
		if (studentResponse.getTraningTestItem().equals("4")) {
			//TODO
		
		} else if (studentResponse.getTraningTestItem().equals("not present")) {
			//TODO
		}
		studentResponse.setStatus(false);
	}

	/**
	 * Static method against specific student response file. No generic method implementation for the time being
	 * 
	 * @param studentResponse
	 *            StudentResponse stores data for a student's item information of a test
	 */
	private void validateGI(StudentResponse studentResponse) {
		String responseContent = studentResponse.getResponseContent(); 
		String tdsResponseContent = studentResponse.getTdsResponseContent();
		
		//need methods or classes to parse tdsResponseContent (CDATA content)
		//provoided by AIR
		
		if (studentResponse.getTraningTestItem().equals("1")) {
			//TODO
		} else if (studentResponse.getTraningTestItem().equals("5")) {
			//TODO
		} else if (studentResponse.getTraningTestItem().equals("8")) {
			//TODO
		}
		studentResponse.setStatus(false);
	}

	/**
	 * Static method against specific student response file. No generic method implementation for the time being
	 * 
	 * @param studentResponse
	 *            StudentResponse stores data for a student's item information of a test
	 * 
	 */
	private void validateMS(StudentResponse studentResponse) {
		String responseContent = studentResponse.getResponseContent(); 
		String tdsResponseContent = studentResponse.getTdsResponseContent();
		if (studentResponse.getTraningTestItem().equals("6")) {
			if (responseContent.equals(tdsResponseContent))
				studentResponse.setStatus(true);
			else
				studentResponse.setStatus(false);
		}
	}
	
	/**
	 * Static method against specific student response file. No generic method implementation for the time being
	 * 
	 * @param studentResponse
	 *            StudentResponse stores data for a student's item information of a test
	 * 
	 */
	private void validateMC(StudentResponse studentResponse) {
		String responseContent = studentResponse.getResponseContent(); 
		System.out.println("responseContent --->" + responseContent);
		String tdsResponseContent = studentResponse.getTdsResponseContent();
		System.out.println("tdsResponseContent --->" + tdsResponseContent);
		if (studentResponse.getTraningTestItem().equals("not present")) {
			if (responseContent.equals(tdsResponseContent))
				studentResponse.setStatus(true);
			else
				studentResponse.setStatus(false);
		}
	}
	
	/**
	 * Static method against specific student response file. No generic method implementation for the time being
	 * 
	 * @param studentResponse
	 *            StudentResponse stores data for a student's item information of a test
	 * 
	 */
	private void validateTI(StudentResponse studentResponse) {
		String responseContent = studentResponse.getResponseContent(); 
		String tdsResponseContent = studentResponse.getTdsResponseContent();
		
		//need methods or classes to parse tdsResponseContent (CDATA content)
		//provoided by AIR
		if (studentResponse.getTraningTestItem().equals("7")) {
			//TODO
		}
		studentResponse.setStatus(false);
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

	private void validateField(Response response, EnumFieldCheckType enumFieldCheckType, EnumItemResponseFieldName enumFieldName,
			FieldCheckType fieldCheckType, Itemrelease.Item.Attriblist attriblist) {
		try {
			switch (enumFieldCheckType) {
			case D:
				break;
			case P:
				checkP(response, enumFieldName, fieldCheckType);
				break;
			case PC:
				checkP(response, enumFieldName, fieldCheckType);
				checkC(response, enumFieldName, fieldCheckType, attriblist);
				break;
			}
		} catch (Exception e) {
			logger.error("validateField exception: ", e);
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
	 *            Object with field to check
	 * @param enumFieldName
	 *            Specifies the field to check
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

	
	
}
