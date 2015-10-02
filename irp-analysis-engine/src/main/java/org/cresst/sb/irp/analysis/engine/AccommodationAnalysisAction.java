package org.cresst.sb.irp.analysis.engine;

import org.apache.commons.lang3.EnumUtils;
import org.cresst.sb.irp.domain.analysis.AccommodationCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.OpportunityCategory;
import org.cresst.sb.irp.domain.student.Student;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Accommodation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccommodationAnalysisAction extends AnalysisAction<Accommodation, AccommodationAnalysisAction.EnumAccommodationFieldName, Student> {
    private final static Logger logger = LoggerFactory.getLogger(AccommodationAnalysisAction.class);

    static public enum EnumAccommodationFieldName {
        type, value, code, segment
    }

    static public enum EnumAccommodationAcceptValues {
        AmericanSignLanguage, ColorContrast, ClosedCaptioning, Language, Masking, PermissiveMode,
        PrintOnDemand, PrintSize, StreamlinedInterface, TexttoSpeech,
        NonEmbeddedDesignatedSupports, NonEmbeddedAccommodations, Other
    }

    @Override
    public void analyze(IndividualResponse individualResponse) {
        try {
            TDSReport tdsReport = individualResponse.getTDSReport();

            Opportunity opportunity = tdsReport.getOpportunity();
            List<Accommodation> listAccommodation = opportunity.getAccommodation();

            List<AccommodationCategory> listAccommodationCategory = new ArrayList<>();

            for (Accommodation accommodation : listAccommodation) {
                AccommodationCategory accommodationCategory = new AccommodationCategory();
                listAccommodationCategory.add(accommodationCategory);
                analyzeAccommodation(accommodationCategory, accommodation);
            }

            OpportunityCategory opportunityCategory = individualResponse.getOpportunityCategory();
            opportunityCategory.setAccommodationCategories(listAccommodationCategory);
        } catch (Exception ex) {
            logger.error("Analyze exception", ex);
        }
    }

    private void analyzeAccommodation(AccommodationCategory accommodationCategory, Accommodation accommodation) {
        validate(accommodationCategory, accommodation, accommodation.getType(), EnumFieldCheckType.PC, EnumAccommodationFieldName.type, null);
        validate(accommodationCategory, accommodation, accommodation.getValue(), EnumFieldCheckType.PC, EnumAccommodationFieldName.value, null);
        validate(accommodationCategory, accommodation, accommodation.getCode(), EnumFieldCheckType.P, EnumAccommodationFieldName.code, null);
        validate(accommodationCategory, accommodation, accommodation.getSegment(), EnumFieldCheckType.P, EnumAccommodationFieldName.segment, null);
    }

    /**
     * Field Check Type (P) --> check that field is not empty, and field value is of correct data type
     * and within acceptable values
     *
     * @param accommodation  Accommodation with fields to check
     * @param enumFieldName  Specifies the field to check
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
     * @param accommodation  TDS Accommodation with fields to check
     * @param enumFieldName  Specifies the field to check
     * @param fieldCheckType This is where the results are stored
     * @param student        Student accommodation to compare against TDS Accommodation
     */
    @Override
    protected void checkC(Accommodation accommodation, EnumAccommodationFieldName enumFieldName, FieldCheckType fieldCheckType, Student student) {
        //TODO Need to get student accommodation file from AIR in order to checkC
    }

    private void processAcceptableEnum(String fieldValue, FieldCheckType fieldCheckType,
                                       Class<EnumAccommodationAcceptValues> class1, String parenthesesValue) {
        try {
            if (fieldValue != null && !fieldValue.trim().isEmpty()) {
            	if (searchEnum(class1, fieldValue) || fieldValue.equalsIgnoreCase(parenthesesValue)) {
            		 setPcorrect(fieldCheckType);
            	}
                /*if (EnumUtils.isValidEnum(class1, fieldValue) || fieldValue.equalsIgnoreCase(parenthesesValue)) {
                    setPcorrect(fieldCheckType);
                }*/
            }
        } catch (Exception e) {
            logger.error("processAcceptableEnum exception: ", e);
        }
    }
    
    /**
     * ignoreCase and removes all whitespaces for search value e.g type="language" type="Print Size"
     * @param enumeration
     * @param fieldValue 
     * @return 
     */
    private <T extends Enum<?>> boolean searchEnum(Class<T> enumeration, String search) {
    	search = search.replaceAll("\\s+","");
        for (T each : enumeration.getEnumConstants()) {
            if (each.name().compareToIgnoreCase(search) == 0) {
                return true;
            }
        }
        return false;
    }
    
}
