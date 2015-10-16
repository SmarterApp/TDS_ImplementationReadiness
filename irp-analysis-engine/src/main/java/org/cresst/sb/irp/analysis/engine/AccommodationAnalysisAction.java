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
public class AccommodationAnalysisAction extends AnalysisAction<org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Accommodation, AccommodationAnalysisAction.EnumAccommodationFieldName, org.cresst.sb.irp.domain.accommodation.Accommodation> {
	private final static Logger logger = LoggerFactory.getLogger(AccommodationAnalysisAction.class);

	static public enum EnumAccommodationFieldName {
		StudentIdentifier, StateAbbreviation, Subject, AmericanSignLanguage, ColorContrast, ClosedCaptioning, Language, Masking, PermissiveMode, PrintOnDemand, Zoom, PrintSize, StreamlinedInterface, TexttoSpeech, Translation, NonEmbeddedDesignatedSupports, NonEmbeddedAccommodations, Other
	}

	@Override
	public void analyze(IndividualResponse individualResponse) {
		try {
			TDSReport tdsReport = individualResponse.getTDSReport();

			Opportunity opportunity = tdsReport.getOpportunity();
			List<org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Accommodation> listAccommodation =
					opportunity.getAccommodation();

			ExamineeCategory examineeCategory = individualResponse.getExamineeCategory();
			Long examineeKey = Long.parseLong(getTdsFieldNameValueByFieldName(examineeCategory.getCellCategories(), "key"));
			org.cresst.sb.irp.domain.accommodation.Accommodation accommodationExcel = getAccommodation(examineeKey);

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
		// <xs:attribute name="type" use="required" />
		EnumAccommodationFieldName fieldName = searchEnumObject(EnumAccommodationFieldName.class, accommodation.getType());
		validate(accommodationCategory, accommodation, accommodation.getValue(), EnumFieldCheckType.PC, fieldName, accommodationExcel);
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
			case StudentIdentifier:
			case StateAbbreviation:
			case Subject:
			case AmericanSignLanguage:
			case ColorContrast:
			case ClosedCaptioning:
			case Language:
			case Masking:
			case PermissiveMode:
			case PrintOnDemand:
				processP_PritableASCIIone(accommodation.getValue(), fieldCheckType);
				break;
			case Zoom:
			case PrintSize:
				processP_PritableASCIIone(accommodation.getValue(), fieldCheckType);
				break;
			case StreamlinedInterface:
			case TexttoSpeech:
			case Translation:
			case NonEmbeddedDesignatedSupports:
			case NonEmbeddedAccommodations:
			case Other:
				processP_PritableASCIIone(accommodation.getValue(), fieldCheckType);
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
		try {
			// Dynamically lookup the Accommodation's property that matches the Attribute enumFieldName
			BeanInfo info = Introspector.getBeanInfo(accommodationExcel.getClass());
			PropertyDescriptor[] properties = info.getPropertyDescriptors();
			for (PropertyDescriptor descriptor : properties) {
				  // Compares the property name to the enum field name in order to perform a proper field check
				if (StringUtils.equalsIgnoreCase(descriptor.getName(), enumFieldName.name()) ||
					(descriptor.getName().equalsIgnoreCase("zoom") && enumFieldName.name().equalsIgnoreCase("PrintSize"))){
					Method getter = descriptor.getReadMethod();
                    if (getter != null) {
                        String value = (String) getter.invoke(accommodationExcel);
                        processSameValue(value, accommodation.getValue(), fieldCheckType);
                    }
				}
			}
		} catch (Exception e) {
			logger.error("checkC exception: ", e);
		}
	}

	@Override
	protected String expectedValue(org.cresst.sb.irp.domain.accommodation.Accommodation accommodationExcel,
			EnumAccommodationFieldName enumFieldName) {
		if (accommodationExcel == null) {
			return null;
		}

		String strReturn = null;

		switch (enumFieldName) {
		case StudentIdentifier:
			strReturn = Long.toString(accommodationExcel.getStudentIdentifier());
			break;
		case StateAbbreviation:
			strReturn = accommodationExcel.getStateAbbreviation();
			break;
		case Subject:
			strReturn = accommodationExcel.getSubject();
			break;
		case AmericanSignLanguage:
			strReturn = accommodationExcel.getAmericanSignLanguage();
			break;
		case ColorContrast:
			strReturn = accommodationExcel.getColorContrast();
			break;
		case ClosedCaptioning:
			strReturn = accommodationExcel.getClosedCaptioning();
			break;
		case Language:
			strReturn = accommodationExcel.getLanguage();
			break;
		case Masking:
			strReturn = accommodationExcel.getMasking();
			break;
		case PermissiveMode:
			strReturn = accommodationExcel.getPermissiveMode();
			break;
		case PrintOnDemand:
			strReturn = accommodationExcel.getPrintOnDemand();
			break;
		case Zoom:
		case PrintSize:
			strReturn = accommodationExcel.getZoom();
			break;
		case StreamlinedInterface:
			strReturn = accommodationExcel.getStreamlinedInterface();
			break;
		case TexttoSpeech:
			strReturn = accommodationExcel.getTexttoSpeech();
			break;
		case Translation:
			strReturn = accommodationExcel.getTranslation();
			break;
		case NonEmbeddedDesignatedSupports:
			strReturn = accommodationExcel.getNonEmbeddedDesignatedSupports();
			break;
		case NonEmbeddedAccommodations:
			strReturn = accommodationExcel.getNonEmbeddedAccommodations();
			break;
		case Other:
			strReturn = accommodationExcel.getOther();
			break;
		default:
			logger.info("Accommodation object does not have corresponding field of $s", enumFieldName);
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
