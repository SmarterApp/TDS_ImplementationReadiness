package org.cresst.sb.irp.dao;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.EnumUtils;
import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.analysis.AccommodationCategory;
import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.OpportunityCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Accommodation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class AccommodationAnalysisAction extends AnalysisAction {
	private static Logger logger = Logger.getLogger(AccommodationAnalysisAction.class);

	public enum EnumAccommodationFieldName {
		type, value, code, segment;
	}
	
	public enum EnumAccommodationAcceptValues {
		AmericanSignLanguage, ColorContrast, ClosedCaptioning, Language, Masking, PermissiveMode,	
		PrintOnDemand, PrintSize, StreamlinedInterface,	TexttoSpeech,	
		NonEmbeddedDesignatedSupports, NonEmbeddedAccommodations, Other;	
	}
	
	@Override
	public void analysis() throws IOException {
		try {
			IndividualResponse individualResponse = getIndividualResponse();
			TDSReport tdsReport = individualResponse.getTDSReport();
			OpportunityCategory opportunityCategory = individualResponse.getOpportunityCategory();
			List<AccommodationCategory> listAccommodationCategory = opportunityCategory.getAccommodationCategories();
			
			AccommodationCategory accommodationCategory;
			Opportunity opportunity = getOpportunity(tdsReport);
			List<Accommodation> listAccommodation = opportunity.getAccommodation();
			for (Accommodation a : listAccommodation) {
				accommodationCategory = new AccommodationCategory();
				listAccommodationCategory.add(accommodationCategory);
				analysisEachAccommodation(accommodationCategory, a);
			}
			
		} catch (Exception e) {
			logger.error("analysis exception: ", e);
		}
	}

	private void analysisEachAccommodation(AccommodationCategory accommodationCategory, Accommodation accommodation){
		try {
			List<CellCategory> listCellCategory = accommodationCategory.getCellCategories();
			CellCategory cellCategory;
			FieldCheckType fieldCheckType;

			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumAccommodationFieldName.type.toString());
			cellCategory.setTdsFieldNameValue(accommodation.getType());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(accommodation, EnumFieldCheckType.PC, EnumAccommodationFieldName.type, fieldCheckType);
			
			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumAccommodationFieldName.value.toString());
			cellCategory.setTdsFieldNameValue(accommodation.getValue());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(accommodation, EnumFieldCheckType.PC, EnumAccommodationFieldName.value, fieldCheckType);
			
			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumAccommodationFieldName.code.toString());
			cellCategory.setTdsFieldNameValue(accommodation.getCode());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(accommodation, EnumFieldCheckType.P, EnumAccommodationFieldName.code, fieldCheckType);
			
			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumAccommodationFieldName.segment.toString());
			cellCategory.setTdsFieldNameValue(Long.toString(accommodation.getSegment()));
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(accommodation, EnumFieldCheckType.P, EnumAccommodationFieldName.segment, fieldCheckType);
			
		} catch (Exception e) {
			logger.error("analysisEachAccommodation exception: ", e);
		}
	}
	
	private void validateField(Accommodation accommodation, EnumFieldCheckType enumFieldCheckType,
			EnumAccommodationFieldName enumFieldName, FieldCheckType fieldCheckType) {
		try {
			switch (enumFieldCheckType) {
			case D:
				break;
			case P:
				checkP(accommodation, enumFieldName, fieldCheckType);
				break;
			case PC:
				checkP(accommodation, enumFieldName, fieldCheckType);
				//need to get student accommodation file from AIR in order to checkC
				//checkC(accommodation, enumFieldName, fieldCheckType);
				break;
			}
		} catch (Exception e) {
			logger.error("validateField exception: ", e);
		}
	}
	
	// Field Check Type (P) --> check that field is not empty, and field value is of correct data type
	// and within acceptable values
	private void checkP(Accommodation accommodation, EnumAccommodationFieldName enumFieldName, FieldCheckType fieldCheckType) {
		try {
			switch (enumFieldName) {
			case type:
				//	<xs:attribute name="type" use="required" />
				processAcceptableEnum(accommodation.getType(), fieldCheckType, EnumAccommodationAcceptValues.class, "Translation(Glossary)");
				break;
			case value:
				//<xs:attribute name="value" use="required" />
				processP_PritableASCIIone(accommodation.getValue(), fieldCheckType);
				break;
			case code:
				//<xs:attribute name="code" use="required" />
				processP_PritableASCIIone(accommodation.getCode(), fieldCheckType);
				break;
			case segment:
				//  <xs:attribute name="segment" use="required">
                //<xs:simpleType>
                //<xs:restriction base="xs:unsignedInt">
                //  <xs:minInclusive value="0" />
                //</xs:restriction>
				//</xs:simpleType>
				//</xs:attribute>
				processP_Positive32bit(Long.toString(accommodation.getSegment()), fieldCheckType);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("checkP exception: ", e);
		}
	}
	
	private void processAcceptableEnum(String fieldValue, FieldCheckType fieldCheckType,
			Class<EnumAccommodationAcceptValues> class1, String parenthesesValue) {
		try {
			if (fieldValue != null && !fieldValue.trim().isEmpty()) {
				if (EnumUtils.isValidEnum(class1, fieldValue) || fieldValue.toLowerCase().trim().equals(parenthesesValue.toLowerCase())) {
					setPcorrect(fieldCheckType);
				} 
			}
		} catch (Exception e) {
			logger.error("processAcceptableEnum exception: ", e);
		}
	}
	
}
