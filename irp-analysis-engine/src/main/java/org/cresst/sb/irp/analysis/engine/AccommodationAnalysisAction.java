package org.cresst.sb.irp.analysis.engine;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.cresst.sb.irp.domain.analysis.AccommodationCategory;
import org.cresst.sb.irp.domain.analysis.ExamineeCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.OpportunityCategory;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Accommodation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AccommodationAnalysisAction extends AnalysisAction<org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Accommodation, 
	AccommodationAnalysisAction.EnumAccommodationFieldName, org.cresst.sb.irp.domain.accommodation.Accommodation> {
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
            List<org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Accommodation> listAccommodation 
            	= opportunity.getAccommodation();

            ExamineeCategory examineeCategory = individualResponse.getExamineeCategory();
        	String examineeKey = getTdsFieldNameValueByFieldName(examineeCategory.getCellCategories(), "key");
        	org.cresst.sb.irp.domain.accommodation.Accommodation accommodationExcel = getAccommodation(Long.parseLong(examineeKey));
        	System.out.println("accommodationExcel ==>" + accommodationExcel.toString());
        	
            List<AccommodationCategory> listAccommodationCategory = new ArrayList<>();

            for (Accommodation accommodation : listAccommodation) {
                AccommodationCategory accommodationCategory = new AccommodationCategory();
                listAccommodationCategory.add(accommodationCategory);
                analyzeAccommodation(accommodationCategory, accommodation, accommodationExcel);
            }

            OpportunityCategory opportunityCategory = individualResponse.getOpportunityCategory();
            opportunityCategory.setAccommodationCategories(listAccommodationCategory);
        } catch (Exception ex) {
            logger.error("Analyze exception", ex);
        }
    }

    private void analyzeAccommodation(AccommodationCategory accommodationCategory, 
    		org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Accommodation accommodation, 
    		org.cresst.sb.irp.domain.accommodation.Accommodation accommodationExcel) {
        validate(accommodationCategory, accommodation, accommodation.getType(), EnumFieldCheckType.PC, EnumAccommodationFieldName.type, accommodationExcel);
        validate(accommodationCategory, accommodation, accommodation.getValue(), EnumFieldCheckType.PC, EnumAccommodationFieldName.value, accommodationExcel);
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
    protected void checkP(org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Accommodation accommodation, 
    		EnumAccommodationFieldName enumFieldName, 
    		FieldCheckType fieldCheckType) {
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
    protected void checkC(org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Accommodation accommodation, 
    		EnumAccommodationFieldName enumFieldName, 
    		FieldCheckType fieldCheckType, 
    		org.cresst.sb.irp.domain.accommodation.Accommodation accommodationExcel) 
    {
    	   if (accommodationExcel == null) {
               return;
           }
    	   try {
	    	   switch (enumFieldName) {
	           	case type:
	           		processTypeC(accommodation, accommodationExcel, fieldCheckType);
	           		break;
	            case value:
	            	processValueC(accommodation, accommodationExcel, fieldCheckType);
	            	break;
	            default:
	                break;
		        }
		    } catch (Exception e) {
		        logger.error("checkC exception: ", e);
		    }
    }
    
    private void processTypeC(org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Accommodation accommodation, 
    		org.cresst.sb.irp.domain.accommodation.Accommodation accommodationExcel,
    		FieldCheckType fieldCheckType)
    {
        try {
        	String type = StringUtils.deleteWhitespace(accommodation.getType());
            // Dynamically lookup the Accommodation's property that matches the Attribute enumFieldName
            BeanInfo info = Introspector.getBeanInfo(accommodationExcel.getClass());
            PropertyDescriptor[] properties = info.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : properties) {
                // Compares the property name to the type field name in order to perform a proper field check
         	 	
            	//TODO need to implement "Print Size" vs "Zoom"
            	if (StringUtils.equalsIgnoreCase(descriptor.getName(), type))
 	 			{
         	 		 setCcorrect(fieldCheckType);
 	 			}
            }
        } catch (Exception ex) {
            logger.info(String.format("processTypeC for Accommodation type $s method could not be invoked", accommodation.getType()), ex);
        }
    }
    
    private void processValueC(org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Accommodation accommodation, 
    		org.cresst.sb.irp.domain.accommodation.Accommodation accommodationExcel,
    		FieldCheckType fieldCheckType)
    {
        try {
        	String type = StringUtils.deleteWhitespace(accommodation.getType());
            // Dynamically lookup the Accommodation's property that matches the Attribute enumFieldName
            BeanInfo info = Introspector.getBeanInfo(accommodationExcel.getClass());
            PropertyDescriptor[] properties = info.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : properties) {
                // Compares the property name to the type field name in order to perform a proper field check
         	 	if (StringUtils.equalsIgnoreCase(descriptor.getName(), type))
 	 			{
         	 	    Method getter = descriptor.getReadMethod();
                    if (getter != null) {
                        String value = (String) getter.invoke(accommodationExcel);
                        processSameValue(value, accommodation.getValue(), fieldCheckType);
                    }
 	 			}
            }
        } catch (Exception ex) {
            logger.info(String.format("processValueC for Accommodation type $s method could not be invoked", accommodation.getType()), ex);
        }
    }

    @Override
	protected String expectedValue(org.cresst.sb.irp.domain.accommodation.Accommodation accommodationExcel, 
			EnumAccommodationFieldName enumFieldName) {
    	//TODO need to implement to get ExpectedValue
        if (accommodationExcel == null) {
            return null;
        }

		String strReturn = null;

        switch (enumFieldName) {
			case type:
				//TODO
				//EnumAccommodationAcceptValues ev = searchEnumObject(EnumAccommodationAcceptValues.class, )
				//strReturn = student.getLastOrSurname();
				break;
			case value:
				//TODO
				break;
			default:
				break;
		}

		return strReturn;
    }
    
    private void processSameValue(String first, String second, FieldCheckType fieldCheckType) {
    	if(StringUtils.isNotBlank(first) || StringUtils.isNotBlank(second)){
	        if (StringUtils.equalsIgnoreCase(first, second)) {
	            setCcorrect(fieldCheckType);
	        }
    	}
    }
    
    private void processAcceptableEnum(String fieldValue, FieldCheckType fieldCheckType,
                                       Class<EnumAccommodationAcceptValues> class1, String parenthesesValue) {
        try {
            if (fieldValue != null && !fieldValue.trim().isEmpty()) {
            	if (searchEnum(class1, fieldValue) || fieldValue.equalsIgnoreCase(parenthesesValue)) {
            		 setPcorrect(fieldCheckType);
            	}
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
    	search = StringUtils.deleteWhitespace(search);
        for (T each : enumeration.getEnumConstants()) {
            if (each.name().compareToIgnoreCase(search) == 0) {
                return true;
            }
        }
        return false;
    }
    
    private <T extends Enum<?>> T searchEnumObject(Class<T> enumeration, String search) {
    	search = StringUtils.deleteWhitespace(search);
    	System.out.println("search ===>" + search);
        for (T each : enumeration.getEnumConstants()) {
        	System.out.println("each ==>" + each.toString());
            if (each.name().compareToIgnoreCase(search) == 0) {
                return each;
            }
        }
        return null;
    }
    
}
