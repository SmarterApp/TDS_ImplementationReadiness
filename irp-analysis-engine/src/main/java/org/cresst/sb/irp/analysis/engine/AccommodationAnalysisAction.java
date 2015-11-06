package org.cresst.sb.irp.analysis.engine;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.cresst.sb.irp.analysis.engine.examinee.ExamineeHelper;
import org.cresst.sb.irp.domain.analysis.AccommodationCategory;
import org.cresst.sb.irp.domain.analysis.CellCategory;
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
public class AccommodationAnalysisAction extends AnalysisAction<org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Accommodation, AccommodationAnalysisAction.EnumAccommodationFieldName, org.cresst.sb.irp.domain.accommodation.Accommodation> {
	private final static Logger logger = LoggerFactory.getLogger(AccommodationAnalysisAction.class);

	public enum EnumAccommodationFieldName{
		type, value, code, segment
	}
	
	public enum EnumAccommodationTypeAcceptableVaues {
		StudentIdentifier, StateAbbreviation, Subject, AmericanSignLanguage, ColorContrast, ClosedCaptioning, Language, Masking, PermissiveMode, PrintOnDemand, Zoom, PrintSize, StreamlinedInterface, TexttoSpeech, Translation, NonEmbeddedDesignatedSupports, NonEmbeddedAccommodations, Other
	}

	@Override
	public void analyze(IndividualResponse individualResponse) {
		try {
			TDSReport tdsReport = individualResponse.getTDSReport();

			Opportunity opportunity = tdsReport.getOpportunity();
			List<org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Accommodation> tdsAccommodations =
					opportunity.getAccommodation(); //<xs:element name="Accommodation" minOccurs="0" maxOccurs="unbounded">

			TDSReport.Examinee examinee = tdsReport.getExaminee();
			String studentIdentifier = ExamineeHelper.getStudentIdentifier(examinee);

			org.cresst.sb.irp.domain.accommodation.Accommodation accommodationExcel = getAccommodation(studentIdentifier);
			
			List<AccommodationCategory> accommodationCategories = new ArrayList<>();
			
			for (Accommodation accommodation : tdsAccommodations) {
				AccommodationCategory accommodationCategory = new AccommodationCategory();
				accommodationCategories.add(accommodationCategory);
				analyzeAccommodation(accommodationCategory, accommodation, accommodationExcel);	
			}
			
			OpportunityCategory opportunityCategory = individualResponse.getOpportunityCategory();
			opportunityCategory.setAccommodationCategories(accommodationCategories);
		} catch (Exception ex) {
			logger.error("Analyze exception", ex);
		}
	}

	private void analyzeAccommodation(AccommodationCategory accommodationCategory,
			org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Accommodation accommodation,
			org.cresst.sb.irp.domain.accommodation.Accommodation accommodationExcel) {
		
		validate(accommodationCategory, accommodation, accommodation.getType(), EnumFieldCheckType.P, EnumAccommodationFieldName.type, null);
		validate(accommodationCategory, accommodation, accommodation.getValue(), EnumFieldCheckType.P, EnumAccommodationFieldName.value, null);
		validate(accommodationCategory, accommodation, accommodation.getCode(), EnumFieldCheckType.PC, EnumAccommodationFieldName.code, accommodationExcel);
		setExpectedValue(accommodationCategory, accommodation, EnumAccommodationFieldName.code, accommodationExcel);
		validate(accommodationCategory, accommodation, accommodation.getSegment(), EnumFieldCheckType.P, EnumAccommodationFieldName.segment, null);
	}
	
	/**
	 * Field Check Type (P) --> check that field is not empty, and field value is of correct data type and within acceptable
	 * values
	 *
	 * @param accommodation
	 *            Accommodation with fields to check
	 * @param enumFieldName
	 *            Specifies the field to check
	 * @param fieldCheckType
	 *            This is where the results are stored
	 */
	@Override
	protected void checkP(org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Accommodation accommodation,
			EnumAccommodationFieldName enumFieldName, FieldCheckType fieldCheckType) {
		try {
			
			switch (enumFieldName) {
			case type:
				// <xs:attribute name="type" use="required" />
				EnumAccommodationTypeAcceptableVaues acceptableType = searchEnumObject(EnumAccommodationTypeAcceptableVaues.class, accommodation.getType());
				if (acceptableType != null)
					processP_PritableASCIIone(accommodation.getType(), fieldCheckType);
				break;
			case value:
				// <xs:attribute name="value" use="required"/>
				processP_PritableASCIIone(accommodation.getValue(), fieldCheckType);
				break;
			case code:
				// <xs:attribute name="code" use="required"/>
				processP_PritableASCIIone(accommodation.getCode(), fieldCheckType);
				break;
			case segment:
				//     <xs:attribute name="segment" use="required">
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
	 * @param accommodation
	 *            TDS Accommodation with fields to check
	 * @param enumFieldName
	 *            Specifies the field to check
	 * @param fieldCheckType
	 *            This is where the results are stored
	 * @param accommodationExcel
	 *            student accommodation to compare against TDS Accommodation
	 */
	@Override
	protected void checkC(org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Accommodation accommodation,
			EnumAccommodationFieldName enumFieldName, FieldCheckType fieldCheckType,
			org.cresst.sb.irp.domain.accommodation.Accommodation accommodationExcel) {
		if (accommodationExcel == null) {
			return;
		}
		
		switch (enumFieldName) {
		case type:
			break;
		case value:
			break;
		case code:
			EnumAccommodationTypeAcceptableVaues acceptableType = searchEnumObject(EnumAccommodationTypeAcceptableVaues.class, accommodation.getType());
			if (acceptableType != null){
				processC(accommodation.getCode(), acceptableType, fieldCheckType, accommodationExcel);
			}
			break;
		case segment:
			break;
		default:
			break;
		}
	}
	
	private void setExpectedValue(AccommodationCategory accommodationCategory, 
			org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Accommodation accommodation, 
			EnumAccommodationFieldName enumFieldName, 
			org.cresst.sb.irp.domain.accommodation.Accommodation accommodationExcel){
		switch (enumFieldName) {
			case type:
				break;
			case value:
				break;
			case code:
				EnumAccommodationTypeAcceptableVaues acceptableType = searchEnumObject(EnumAccommodationTypeAcceptableVaues.class, accommodation.getType());
				if (acceptableType != null){
					String expectedValue = getExpectedValue(accommodation.getCode(), acceptableType, accommodationExcel);
					if (expectedValue != null){
						CellCategory cellCategory = getCellCategoryByFieldName(accommodationCategory.getCellCategories(), 
							EnumAccommodationFieldName.code.toString());
						cellCategory.setTdsExpectedValue(expectedValue);
					}
				}
				break;
			case segment:
				break;
			default:
				break;
		}	
	}
	
	protected String getExpectedValue(String tdsValue,
			EnumAccommodationTypeAcceptableVaues acceptableValue,
			org.cresst.sb.irp.domain.accommodation.Accommodation accommodationExcel){
		if (accommodationExcel == null) {
			return null;
		}
		try {
			// Dynamically lookup the Accommodation's property that matches the Attribute enumFieldName
			BeanInfo info = Introspector.getBeanInfo(accommodationExcel.getClass());
			PropertyDescriptor[] properties = info.getPropertyDescriptors();
			for (PropertyDescriptor descriptor : properties) {
				  // Compares the property name to the enum field name in order to perform a proper field check
				if (StringUtils.equalsIgnoreCase(descriptor.getName(), acceptableValue.name()) ||
					(descriptor.getName().equalsIgnoreCase("zoom") && acceptableValue.name().equalsIgnoreCase("PrintSize"))){
					Method getter = descriptor.getReadMethod();
                    if (getter != null) {
                        String value = (String) getter.invoke(accommodationExcel);
                        if (StringUtils.isNotBlank(value))
                        	return value;
                    }
				}
			}
		} catch (Exception e) {
			logger.error("checkC exception: ", e);
		}
		return null;
	}
	
	/**
	 * Checks if the field has correct value
	 *
	 * @param tdsValue
	 *            TDS Accommodation value of fields to check
	 * @param enumFieldName
	 *            Specifies the field to check
	 * @param fieldCheckType
	 *            This is where the results are stored
	 * @param accommodationExcel
	 *            student accommodation to compare against TDS Accommodation
	 */
	protected void processC(String tdsValue,
			EnumAccommodationTypeAcceptableVaues acceptableValue, FieldCheckType fieldCheckType,
			org.cresst.sb.irp.domain.accommodation.Accommodation accommodationExcel){
		if (accommodationExcel == null) {
			return;
		}
		try {
			// Dynamically lookup the Accommodation's property that matches the Attribute enumFieldName
			BeanInfo info = Introspector.getBeanInfo(accommodationExcel.getClass());
			PropertyDescriptor[] properties = info.getPropertyDescriptors();
			for (PropertyDescriptor descriptor : properties) {
				  // Compares the property name to the enum field name in order to perform a proper field check
				if (StringUtils.equalsIgnoreCase(descriptor.getName(), acceptableValue.name()) ||
					(descriptor.getName().equalsIgnoreCase("zoom") && acceptableValue.name().equalsIgnoreCase("PrintSize"))){
					Method getter = descriptor.getReadMethod();
                    if (getter != null) {
                        String value = (String) getter.invoke(accommodationExcel);
                        processSameValue(value, tdsValue, fieldCheckType);
                    }
				}
			}
		} catch (Exception e) {
			logger.error("checkC exception: ", e);
		}
	}
	
	@Override
	protected String expectedValue(org.cresst.sb.irp.domain.accommodation.Accommodation accommodationExcel, EnumAccommodationFieldName enumFieldName) {
		if (accommodationExcel == null) {
			return null;
		}

		String strReturn = null;
		switch (enumFieldName) {
			case type:
				break;
			case value:
				break;
			case code:
				break;
			case segment:
				break;
			default:
				break;
		}
		
		return strReturn;
	}

	private <T extends Enum<?>> T searchEnumObject(Class<T> enumeration, String search) {
		search = StringUtils.deleteWhitespace(search);
		for (T each : enumeration.getEnumConstants()) {
			if (each.name().compareToIgnoreCase(search) == 0) {
				return each;
			}
		}
		return null;
	}

	private void processSameValue(String first, String second, FieldCheckType fieldCheckType) {
		if (StringUtils.isNotBlank(first) || StringUtils.isNotBlank(second)) {
			if (StringUtils.equalsIgnoreCase(first, second)) {
				setCcorrect(fieldCheckType);
			}
		}
	}

}
