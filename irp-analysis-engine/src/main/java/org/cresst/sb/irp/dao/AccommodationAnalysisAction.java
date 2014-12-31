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
public class AccommodationAnalysisAction extends AnalysisAction<Accommodation, AccommodationAnalysisAction.EnumAccommodationFieldName> {
	private static Logger logger = Logger.getLogger(AccommodationAnalysisAction.class);

	static public enum EnumAccommodationFieldName {
		type, value, code, segment
	}
	
	public enum EnumAccommodationAcceptValues {
		AmericanSignLanguage, ColorContrast, ClosedCaptioning, Language, Masking, PermissiveMode,	
		PrintOnDemand, PrintSize, StreamlinedInterface,	TexttoSpeech,	
		NonEmbeddedDesignatedSupports, NonEmbeddedAccommodations, Other
	}
	
	@Override
	public void analyze(IndividualResponse individualResponse) throws IOException {
		TDSReport tdsReport = individualResponse.getTDSReport();
		OpportunityCategory opportunityCategory = individualResponse.getOpportunityCategory();
		List<AccommodationCategory> listAccommodationCategory = opportunityCategory.getAccommodationCategories();

		AccommodationCategory accommodationCategory;
		Opportunity opportunity = tdsReport.getOpportunity();
		List<Accommodation> listAccommodation = opportunity.getAccommodation();
		for (Accommodation a : listAccommodation) {
			accommodationCategory = new AccommodationCategory();
			listAccommodationCategory.add(accommodationCategory);
			analysisEachAccommodation(accommodationCategory, a);
		}
	}

	private void analysisEachAccommodation(AccommodationCategory accommodationCategory, Accommodation accommodation){
		validate(accommodationCategory, accommodation, accommodation.getType(), EnumFieldCheckType.PC, EnumAccommodationFieldName.type);
		validate(accommodationCategory, accommodation, accommodation.getValue(), EnumFieldCheckType.PC, EnumAccommodationFieldName.value);
		validate(accommodationCategory, accommodation, accommodation.getCode(), EnumFieldCheckType.P, EnumAccommodationFieldName.code);
		validate(accommodationCategory, accommodation, accommodation.getSegment(), EnumFieldCheckType.P, EnumAccommodationFieldName.segment);
	}

	/**
	 * Field Check Type (P) --> check that field is not empty, and field value is of correct data type
	 * and within acceptable values
	 * @param accommodation Accommodation with fields to check
	 * @param enumFieldName Specifies the field to check
	 * @param fieldCheckType This is where the results are stored
	 */
	@Override
	protected void checkP(Accommodation accommodation, EnumAccommodationFieldName enumFieldName, FieldCheckType fieldCheckType) {
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

	/**
	 * Checks if the field has correct value
	 *
	 * @param checkObj       Object with fields to check
	 * @param enumFieldName  Specifies the field to check
	 * @param fieldCheckType This is where the results are stored
	 */
	@Override
	protected void checkC(Accommodation checkObj, EnumAccommodationFieldName enumFieldName, FieldCheckType fieldCheckType) {
		// Need to get student accommodation file from AIR in order to checkC
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
