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
import org.cresst.sb.irp.domain.tinytablescoringengine.Table2;
import org.cresst.sb.irp.domain.tinytablescoringengine.TableCell2;
import org.cresst.sb.irp.domain.tinytablescoringengine.TableVector2;
import org.jdom2.Document;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import qtiscoringengine.cs2java.StringHelper;
import tinygrscoringengine.GRObject;
import tinygrscoringengine.GeoObject;
import tinygrscoringengine.Point;
import tinygrscoringengine.TinyGRException;
import tinygrscoringengine.Vector;
import tinytablescoringengine.Table;
import tinytablescoringengine.TableCell;
import tinytablescoringengine.TableVector;
import AIR.Common.xml.XmlElement;
import AIR.Common.xml.XmlReader;

import com.google.common.collect.ImmutableList;

import java.io.StringReader;
import java.io.StringWriter;
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
			for (Item item : listItem) {
				ItemCategory itemCategory = listItemCategory.get(indexOfItemCategory);
				analysisItemResponse(itemCategory, item);
				analysisItemResponseWithStudentReponse(itemCategory, item, Long.parseLong(examineeKey));
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

		String tdsResponseContent = studentResponse.getTdsResponseContent();
		Map<String, String> identifiersAndResponses = retrieveItemResponse(tdsResponseContent);
		if (studentResponse.getTraningTestItem().equals("3") && identifiersAndResponses != null) {
			if (strYesValue.equals(identifiersAndResponses.get("RESPONSE"))) {
				studentResponse.setStatus(true);
			} else {
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

		// need methods or classes to parse tdsResponseContent (CDATA content)
		// provoided by AIR

		if (studentResponse.getTraningTestItem().equals("4")) {
			// TODO

		} else if (studentResponse.getTraningTestItem().equals("not present")) {
			// TODO
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
		System.out.println("responseContent -->" + responseContent);
		try {
			System.out.println("tdsResponseContent -->" + tdsResponseContent);
			// still need to implement by tomorrow for
			// <AnswerSet><Question id=""><QuestionPart id="1"><ObjectSet><AtomicObject>{curve(325,239)}
			List<GRObject> listGRObject = getObjectStrings(tdsResponseContent);
			if (listGRObject != null) {
				// TODO
				for (GRObject go : listGRObject) {
					logger.info(String.format("strxml -->%s", go.getXmlString()));
					logger.info(String.format("typeofObject -->%s", go.getTypeOfObject()));

				}

				if (studentResponse.getTraningTestItem().equals("1")) {
					// TODO
				} else if (studentResponse.getTraningTestItem().equals("5")) {
					// TODO
				} else if (studentResponse.getTraningTestItem().equals("8")) {
					// TODO
				}
				studentResponse.setStatus(false);
			}
		} catch (Exception e) {
			logger.error("validateGI exception: ", e);
		}
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
		logger.info("responseContent -->" + responseContent);
		try {
			logger.info("tdsResponseContent -->" + tdsResponseContent);
			Table2 table = getTableObject(tdsResponseContent);
			if (table != null) {
				for (int i = 0; i < table.getRowCount(); i++) {
					TableVector2 tableVector2 = table.getRowIndex(i);
					// boolean isHeader = tableVector2.isHeader;
					// int elemCount = tableVector2.getElementCount();
					TableCell2[] tc2Array = tableVector2.getElements();
					for (int j = 0; j < tc2Array.length; j++) {
						TableCell2 tcTmp = tc2Array[j];
						String context = tcTmp.getContext();
						logger.info(String.format("context -->%s and isHead %s", context, tcTmp.getIsHeader()));
					}
				}
				if (studentResponse.getTraningTestItem().equals("7")) {
					// TODO
				}
			}
		} catch (Exception e) {
			logger.error("validateTI exception: ", e);
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

	/**
	 * This method create/modified based on AIR related classes in package tinytablescoringengine
	 * 
	 * @param responseContent
	 *            The student response from tds report xml file (item type : TI)
	 * @return Table2 object
	 * 			  Table 2 is subclass of TableObject2
	 */
	protected Table2 getTableObject(String responseContent) {
		Table2 table2 = null;
		try {
			StringReader sr = new StringReader(responseContent);
			XmlReader reader = new XmlReader(sr);
			Document doc = new Document();
			doc = reader.getDocument();
			List<Element> responseSpec = new XmlElement(doc.getRootElement()).selectNodes("//responseSpec");
			for (Element child : responseSpec) {
				if (child.getName().equals("responseSpec")) {
					return Table2.fromXml(child.getChild("responseTable"));
				}
			}
		} catch (Exception exp) {
			logger.error("getTableObject exception: ", exp);
		}

		return table2;
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
			objects = new ArrayList<GRObject>();
			StringWriter sw = new StringWriter();
			// StringBuilder sb = null; //throws none pointer exception
			StringBuilder sb = new StringBuilder();
			sb.append(sw);
			// XmlWriter xw = new XmlWriter(null); //IllegalArgumentException: Null OutputStream is not a valid argument
			StringReader sr = new StringReader(answerSet);
			XmlReader reader = new XmlReader(sr);
			Document doc = new Document();
			doc = reader.getDocument();
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
		int endOfPoint = 0;
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
		int endOfPoint = 0;
		List<Point> points = new ArrayList<Point>();
		while (pointText.contains("(")) {
			points.add(Point.getPointObj(pointText));
			endOfPoint = pointText.indexOf(")");
			pointText = pointText.substring(endOfPoint + 1);
		}
		return points;
	}

	/**
	 * this method is from TinyGR.java
	 * 
	 * @param child
	 * @param sw
	 * @param objects
	 * @param sb
	 */
	private static void outputObjectString(Element child, StringWriter sw, List<String> objects, StringBuilder sb) {
		String objString = sw.toString();
		if (objString != null) {

			objects.add(sw.toString());
		}
		sb.delete(0, sb.length());
	}

}
