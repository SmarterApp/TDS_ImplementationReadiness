package org.cresst.sb.irp.analysis.engine;

import org.apache.commons.lang3.StringUtils;
import org.cresst.sb.irp.analysis.engine.examinee.EnumExamineeAttributeFieldName;
import org.cresst.sb.irp.analysis.engine.examinee.ExamineeHelper;
import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.ExamineeAttributeCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.student.Student;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee.ExamineeAttribute;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;

@Service
public class ExamineeAttributeAnalysisAction extends AnalysisAction<ExamineeAttribute, EnumExamineeAttributeFieldName, Student> {
    private final static Logger logger = LoggerFactory.getLogger(ExamineeAttributeAnalysisAction.class);

    static private String[] gradeLevelWhenAssessedAcceptableValues = {
            "IT", "PR", "PK", "TK", "KG", "01", "02", "03", "04", "05", "06", "07", "08", "09",
            "10", "11", "12", "13", "PS", "UG"
    };

    @Override
    public void analyze(IndividualResponse individualResponse) {
        TDSReport tdsReport = individualResponse.getTDSReport();
        Examinee tdsReportExaminee = tdsReport.getExaminee(); //<xs:element name="Examinee" minOccurs="1" maxOccurs="1">

		String studentIdentifier = ExamineeHelper.getStudentIdentifier(tdsReportExaminee);
	
        Student student = null;
        try {
            student = getStudent(studentIdentifier);
        } catch (NotFoundException ex) {
            logger.info(String.format("TDS Report contains an Student Identifier (%s) that does not match an IRP Student", studentIdentifier));
        }

        List<ExamineeAttribute> tdsExamineeAttributes = ExamineeHelper.getFinalExamineeAttributes(tdsReportExaminee);

        // Analyze all the ExamineeAttributes that have a FINAL context
        for (EnumExamineeAttributeFieldName enumExamineeAttributeFieldName : EnumExamineeAttributeFieldName.values()) {

            ExamineeAttributeCategory examineeAttributeCategory = new ExamineeAttributeCategory();
            individualResponse.addExamineeAttributeCategory(examineeAttributeCategory);

            ExamineeAttribute examineeAttribute = ExamineeHelper.getFinalExamineeAttribute(tdsReportExaminee, enumExamineeAttributeFieldName);

            analyzeExamineeAttribute(examineeAttributeCategory, enumExamineeAttributeFieldName, examineeAttribute, student);

            if (examineeAttribute != null) {
                // Remove ExamineeAttributes from tdsExamineeAttributes so that extraneous attributes can be marked as such
                for (int i = tdsExamineeAttributes.size() - 1; i >= 0; i--) {
                    ExamineeAttribute toRemove = tdsExamineeAttributes.get(i);
                    if (ExamineeHelper.convertToExamineeAttributeEnum(toRemove.getName()) == enumExamineeAttributeFieldName) {
                        tdsExamineeAttributes.remove(i);
                    }
                }
            }
        }

        // The remaining ExamineeAttributes are not defined in the Open System spec
        for (ExamineeAttribute extraExamineeAttribute : tdsExamineeAttributes) {

            ExamineeAttributeCategory examineeAttributeCategory = new ExamineeAttributeCategory();
            individualResponse.addExamineeAttributeCategory(examineeAttributeCategory);

            // For unknown ExamineeAttributes, let user know that it is incorrect
            final FieldCheckType fieldCheckType = new FieldCheckType();
            fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
            fieldCheckType.setFieldValueEmpty(false);
            fieldCheckType.setCorrectDataType(true);

            final CellCategory cellCategory = new CellCategory();
            cellCategory.setTdsFieldName("name");
            cellCategory.setTdsFieldNameValue(extraExamineeAttribute.getName());
            cellCategory.setFieldCheckType(fieldCheckType);

            examineeAttributeCategory.addCellCategory(cellCategory);
        }
    }

    private void analyzeExamineeAttribute(ExamineeAttributeCategory examineeAttributeCategory,
                                          EnumExamineeAttributeFieldName enumExamineeAttributeFieldName,
                                          ExamineeAttribute examineeAttribute, Student student) {

        if (examineeAttribute != null) {
            validate(examineeAttributeCategory, examineeAttribute, examineeAttribute.getValue(), EnumFieldCheckType.PC,
                    enumExamineeAttributeFieldName, student);
        } else if (enumExamineeAttributeFieldName.isRequired()){

            final FieldCheckType fieldCheckType = new FieldCheckType();
            fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
            fieldCheckType.setFieldValueEmpty(true);
            fieldCheckType.setRequiredFieldMissing(true);
            fieldCheckType.setOptionalValue(false);

            final CellCategory cellCategory = new CellCategory();
            cellCategory.setTdsFieldName(enumExamineeAttributeFieldName.name());
            cellCategory.setTdsExpectedValue(expectedValue(student, enumExamineeAttributeFieldName));
            cellCategory.setFieldCheckType(fieldCheckType);

            examineeAttributeCategory.addCellCategory(cellCategory);
        }
    }

    /**
     * Checks the given ExamineeAttribute for appropriate field definitions
     *
     * @param examineeAttribute
     * @param enumFieldName     Specifies the field to check
     * @param fieldCheckType    This is where the results are stored
     */
    @Override
    protected void checkP(ExamineeAttribute examineeAttribute, EnumExamineeAttributeFieldName enumFieldName, FieldCheckType fieldCheckType) {
        switch (enumFieldName) {
            case MiddleName:
            case AlternateSSID:
                fieldCheckType.setOptionalValue(true);
                processP_PrintableASCIIzeroMaxWidth(examineeAttribute.getValue(), fieldCheckType, enumFieldName.getMaxWidth());
            case Birthdate:
                processDate(examineeAttribute.getValue(), fieldCheckType);
                break;
            case GradeLevelWhenAssessed:
                final String inputValue = examineeAttribute.getValue();
                fieldCheckType.setCorrectDataType(StringUtils.isNotBlank(inputValue) && StringUtils.isAsciiPrintable(inputValue));
                fieldCheckType.setCorrectWidth(inputValue != null && inputValue.length() == enumFieldName.getMaxWidth());

                for (int i = 0; i < gradeLevelWhenAssessedAcceptableValues.length; i++) {
                    if (gradeLevelWhenAssessedAcceptableValues[i] == inputValue) {
                        fieldCheckType.setAcceptableValue(true);
                        break;
                    }
                }
                break;
            default:
                processP_PrintableASCIIoneMaxWidth(examineeAttribute.getValue(), fieldCheckType, enumFieldName.getMaxWidth());
                break;
        }

        if (StringUtils.isNotBlank(examineeAttribute.getValue())) {
            fieldCheckType.setFieldValueEmpty(false);
        }
    }

    /**
     * Checks the correctness of the given ExamineeAttribute against the IRP Student
     *
     * @param examineeAttribute ExamineeAttribute with fields to check
     * @param enumFieldName     Specifies the field to check
     * @param fieldCheckType    This is where the results are stored
     * @param student           Student that will be compared against ExamineeAttribute
     */
    @Override
    protected void checkC(ExamineeAttribute examineeAttribute, EnumExamineeAttributeFieldName enumFieldName, FieldCheckType fieldCheckType, Student student) {
        if (student == null) {
            return;
        }

        try {
            // Dynamically lookup the Student's property that matches the Attribute enumFieldName
            BeanInfo info = Introspector.getBeanInfo(student.getClass());
            PropertyDescriptor[] properties = info.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : properties) {
                // Compares the property name to the enum field name in order to perform a proper field check
				// There's a typo in the documentation and Open Test System code for EnglishLanguageProficiencyLevel
				// Both the correct and erroneous spellings are accepted.
            	if (StringUtils.equalsIgnoreCase(descriptor.getName(), enumFieldName.name())
                	|| (StringUtils.equalsIgnoreCase(descriptor.getName(), EnumExamineeAttributeFieldName.EnglishLanguageProficiencyLevel.name())
        			&& enumFieldName.name().equalsIgnoreCase(EnumExamineeAttributeFieldName.EnglishLanguageProficiencLevel.name()))) {
                    Method getter = descriptor.getReadMethod();
                    if (getter != null) {
                        String value = (String) getter.invoke(student);
                        switch(enumFieldName){
	                    	default:
	                    		processSameValue(value, examineeAttribute.getValue(), fieldCheckType);
	                    		break;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            logger.info(String.format("Accessor method for Student's %s method could not be invoked", enumFieldName.name()), ex);
        }
    }

    private void processSameValue(String first, String second, FieldCheckType fieldCheckType) {
        if (StringUtils.equalsIgnoreCase(first, second)) {
            setCcorrect(fieldCheckType);
        }
    }

    /**
     * Uses the IRP Student object to populate the expected value of the field being analyzed
     *
     * @param student       IRP Student with the expected values
     * @param enumFieldName Specifies the field to check
     * @return The value of the Student object that is expected for the given EnumTestFieldName
     */
	@Override
	protected String expectedValue(Student student, EnumExamineeAttributeFieldName enumFieldName) {

        if (student == null) {
            return null;
        }

		String strReturn = null;

        switch (enumFieldName) {
			case LastOrSurname:
				strReturn = student.getLastOrSurname();
				break;
			case FirstName:
				strReturn = student.getFirstName();
				break;
			case MiddleName:
				strReturn = student.getMiddleName();
				break;
			case Birthdate:
				strReturn = student.getBirthdate();
				break;
			case StudentIdentifier:
				strReturn = student.getStudentIdentifier();
				break;	
			case AlternateSSID:
				strReturn = student.getAlternateSSID();
				break;
			case GradeLevelWhenAssessed:
				strReturn = student.getGradeLevelWhenAssessed();
				break;
			case Sex:
				strReturn = student.getSex();
				break;
			case HispanicOrLatinoEthnicity:
				strReturn = student.getHispanicOrLatinoEthnicity();
				break;
			case AmericanIndianOrAlaskaNative:
				strReturn = student.getAmericanIndianOrAlaskaNative();
				break;
			case Asian:
				strReturn = student.getAsian();
				break;
			case BlackOrAfricanAmerican:
				strReturn = student.getBlackOrAfricanAmerican();
				break;
			case White:
				strReturn = student.getWhite();
				break;	
			case NativeHawaiianOrOtherPacificIslander:
				strReturn = student.getNativeHawaiianOrOtherPacificIslander();
				break;
			case DemographicRaceTwoOrMoreRaces:
				strReturn = student.getDemographicRaceTwoOrMoreRaces();
				break;
			case IDEAIndicator:
				strReturn = student.getIDEAIndicator();
				break;
			case LEPStatus:
				strReturn = student.getLEPStatus();
				break;
			case Section504Status:
				strReturn = student.getSection504Status();
				break;
			case EconomicDisadvantageStatus:
				strReturn = student.getEconomicDisadvantageStatus();
				break;
			case LanguageCode:
				strReturn = student.getLanguageCode();
				break;				
			case EnglishLanguageProficiencyLevel:
			case EnglishLanguageProficiencLevel:
				strReturn = student.getEnglishLanguageProficiencyLevel();
				break;
			case MigrantStatus:
				strReturn = student.getMigrantStatus();
				break;
			case FirstEntryDateIntoUSSchool:
				strReturn = student.getFirstEntryDateIntoUSSchool();
				break;
			case LimitedEnglishProficiencyEntryDate:
				strReturn = student.getLimitedEnglishProficiencyEntryDate();
				break;				
			case LEPExitDate:
				strReturn = student.getLEPExitDate();
				break;
			case TitleIIILanguageInstructionProgramType:
				strReturn = student.getTitleIIILanguageInstructionProgramType();
				break;
			case PrimaryDisabilityType:
				strReturn = student.getPrimaryDisabilityType();
				break;				
			default:
				break;
		}
 
		return strReturn;
	}
    
    
}
