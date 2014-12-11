package org.cresst.sb.irp.dao;

import java.io.IOException;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.ItemCategory;
import org.cresst.sb.irp.domain.analysis.OpportunityCategory;
import org.cresst.sb.irp.domain.analysis.ResponseCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.items.Itemrelease;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Item;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Item.Response;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class ItemResponseAnalysisAction extends AnalysisAction {
	private static Logger logger = Logger.getLogger(ItemResponseAnalysisAction.class);

	public enum EnumItemResponseFieldName {
		date, type, content;
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
				analysisItemResponse(itemCategory, i);
				indexOfItemCategory++;
			}
		} catch (Exception e) {
			logger.error("analysis exception: ", e);
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

			List<CellCategory> listItemAttribute = itemCategory.getListItemAttribute();
			String itemBankKeyKey = getItemBankKeyKeyFromItemAttribute(listItemAttribute);
			String itemIdentifier = "Item-".concat(itemBankKeyKey);
			itemCategory.setItemBankKeyKey(itemIdentifier);
			responseCategory.setContent(response.getContent());
			fieldCheckType = new FieldCheckType();
			boolean blnFormat = isItemFormatByValue(listItemAttribute, "MC");
			if(blnFormat){ //handle MC only 
				fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
				responseCategory.setContentFieldCheckType(fieldCheckType);
				org.cresst.sb.irp.domain.items.Itemrelease.Item irpItem = getItemByIdentifier(itemIdentifier);
				validateField(response, EnumFieldCheckType.PC, EnumItemResponseFieldName.content, fieldCheckType, irpItem, itemCategory);
			}else{
				fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
				responseCategory.setContentFieldCheckType(fieldCheckType);
				validateField(response, EnumFieldCheckType.P, EnumItemResponseFieldName.content, fieldCheckType);
			}
		} catch (Exception e) {
			logger.error("analysisItemResponse exception: ", e);
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
				checkC(response, enumFieldName, fieldCheckType);
				break;
			}
		} catch (Exception e) {
			logger.error("validateField exception: ", e);
		}
	}
	
	private void validateField(Response response, EnumFieldCheckType enumFieldCheckType, EnumItemResponseFieldName enumFieldName,
			FieldCheckType fieldCheckType, org.cresst.sb.irp.domain.items.Itemrelease.Item irpItem, ItemCategory itemCategory) {
		try {
			switch (enumFieldCheckType) {
			case D:
				break;
			case P:
				checkP(response, enumFieldName, fieldCheckType);
				break;
			case PC:
				checkP(response, enumFieldName, fieldCheckType);
				checkC(response, enumFieldName, fieldCheckType, irpItem, itemCategory);
				break;
			}
		} catch (Exception e) {
			logger.error("validateField exception: ", e);
		}
	}

	private void checkP(Response response, EnumItemResponseFieldName enumFieldName, FieldCheckType fieldCheckType) {
		try {
			switch (enumFieldName) {
			case date:
				// check field is not empty. xsd <xs:attribute name="date" type="xs:dateTime" />
				processP(response.getDate(), fieldCheckType);
				break;
			case type:
				//accept values -> value reference <blank allowed>
				/*<xs:attribute name="type">
                   <xs:simpleType>
                     <xs:restriction base="xs:token">
                       <xs:enumeration value="value" />
                       <xs:enumeration value="reference" />
                       <xs:enumeration value="" />
                     </xs:restriction>
                   </xs:simpleType>
                 </xs:attribute>*/
				//xsd already validated
				setPcorrect(fieldCheckType);
				break;
			case content:
				if (response.getContent().length() > 0)
					setPcorrect(fieldCheckType);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("checkP exception: ", e);
		}
	}

	private void checkC(Response response, EnumItemResponseFieldName enumFieldName, FieldCheckType fieldCheckType){
		try {
			switch (enumFieldName) {
			case date:
				break;
			case type:
				setCcorrect(fieldCheckType);
				break;
			case content:
				processC(response.getContent(), fieldCheckType);
				break;
			default:
				break;
			}
		}catch (Exception e) {
			logger.error("checkC exception: ", e);
		}
	}
	
	private void checkC(Response response, EnumItemResponseFieldName enumFieldName, FieldCheckType fieldCheckType, 
			org.cresst.sb.irp.domain.items.Itemrelease.Item irpItem, ItemCategory itemCategory){
		try {
			switch (enumFieldName) {
			case date:
				break;
			case type:
				setCcorrect(fieldCheckType);
				break;
			case content:
				processC(response.getContent(), fieldCheckType, irpItem, itemCategory);
				break;
			default:
				break;
			}
		}catch (Exception e) {
			logger.error("checkC exception: ", e);
		}
	}
	
	private void processP(XMLGregorianCalendar xmlGregorianCalendar, FieldCheckType fieldCheckType) {
		if (xmlGregorianCalendar != null) {
			setPcorrect(fieldCheckType);
		}
	}
	
	private void processC(String responseContent, FieldCheckType fieldCheckType){
		try {
		}catch (Exception e) {
			logger.error("processC exception: ", e);
		}
	}
	
	private void processC(String responseContent, FieldCheckType fieldCheckType, 
			org.cresst.sb.irp.domain.items.Itemrelease.Item irpItem, ItemCategory itemCategory){
		try {
			Itemrelease.Item.Attriblist.Attrib attrib = getItemAttribValueFromIRPitem(irpItem, "itm_att_Answer Key");
			itemCategory.setAttrib(attrib);
			String answerKey = attrib.getVal();
			if (answerKey.trim().toLowerCase().equals(responseContent.trim().toLowerCase()))
				setCcorrect(fieldCheckType);
		}catch (Exception e) {
			logger.error("processC exception: ", e);
		}
	}

}
