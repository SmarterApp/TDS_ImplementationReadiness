package org.cresst.sb.irp.analysis.engine;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.ExamineeAttributeCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.student.Student;
import org.cresst.sb.irp.domain.tdsreport.Context;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class ExamineeAttributeAnalysisAction extends AnalysisAction<ExamineeAttribute, ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName, Student> {
    private final static Logger logger = LoggerFactory.getLogger(ExamineeAttributeAnalysisAction.class);

    static public enum EnumExamineeAttributeFieldName {
        LastOrSurname, FirstName, MiddleName, Birthdate, StudentIdentifier, AlternateSSID, GradeLevelWhenAssessed, Sex, HispanicOrLatinoEthnicity, AmericanIndianOrAlaskaNative, Asian, BlackOrAfricanAmerican, White, NativeHawaiianOrOtherPacificIslander, DemographicRaceTwoOrMoreRaces, IDEAIndicator, LEPStatus, Section504Status, EconomicDisadvantageStatus, LanguageCode, EnglishLanguageProficiencyLevel, EnglishLanguageProficiencLevel, MigrantStatus, FirstEntryDateIntoUSSchool, LimitedEnglishProficiencyEntryDate, LEPExitDate, TitleIIILanguageInstructionProgramType, PrimaryDisabilityType
    }

    @Override
    public void analyze(IndividualResponse individualResponse) {
        TDSReport tdsReport = individualResponse.getTDSReport();
        Examinee examinee = tdsReport.getExaminee(); //<xs:element name="Examinee" minOccurs="1" maxOccurs="1">
        Long examineeKey = examinee.getKey(); //<xs:attribute name="key" type="xs:long" use="required"/>
        
        Student student = null;
  
        try {
            student = getStudent(examineeKey);
        } catch (NotFoundException ex) {
            logger.info(String.format("TDS Report contains an Examinee Key (%d) that does not match an IRP Student", examineeKey));
        }
     
        // Analyze all the ExamineeAttributes that have a FINAL context
        List<Examinee.ExamineeAttribute> examineeAttributes = getFinalExamineeAttributes(examinee);
        for (Examinee.ExamineeAttribute examineeAttribute : examineeAttributes) {
            ExamineeAttributeCategory examineeAttributeCategory = new ExamineeAttributeCategory();
            individualResponse.addExamineeAttributeCategory(examineeAttributeCategory);

            analyzeExamineeAttribute(examineeAttributeCategory, examineeAttribute, student);
        }
    }

    /**
     * Gets all of the ExamineeAttributes that have the FINAL context attribute
     *
     * @param examinee Examinee containing the ExamineeAttributes
     * @return List of ExamineeAttributes marked with FINAL context attribute. Never null.
     */
    public List<Examinee.ExamineeAttribute> getFinalExamineeAttributes(Examinee examinee) {
        List<Examinee.ExamineeAttribute> examineeAttributes = new ArrayList<>();

        if (examinee != null) {
            List<Object> listObject = examinee.getExamineeAttributeOrExamineeRelationship();
            for (Object object : listObject) {
                if (object instanceof Examinee.ExamineeAttribute) {
                    Examinee.ExamineeAttribute examineeAttribute = (Examinee.ExamineeAttribute) object;
                    if (examineeAttribute.getContext() == Context.FINAL) {
                        examineeAttributes.add(examineeAttribute);
                    }
                }
            }
        }

        return examineeAttributes;
    }

    private void analyzeExamineeAttribute(ExamineeAttributeCategory examineeAttributeCategory,
                                          ExamineeAttribute examineeAttribute, Student student) {
        EnumExamineeAttributeFieldName fieldName = convertToFieldName(examineeAttribute.getName());
        if (fieldName != null) {
            validate(examineeAttributeCategory, examineeAttribute, examineeAttribute.getValue(), EnumFieldCheckType.PC, fieldName, student);
//			validate(examineeAttributeCategory, examineeAttribute, examineeAttribute.getContextDate(), EnumFieldCheckType.P, EnumExamineeAttributeFieldName.contextDate, null);
        } else {
            // For unknown ExamineeAttributes, let user know that it is incorrect
            final FieldCheckType fieldCheckType = new FieldCheckType();
            fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
            fieldCheckType.setFieldEmpty(false);
            fieldCheckType.setCorrectDataType(true);

            final CellCategory cellCategory = new CellCategory();
            cellCategory.setTdsFieldName("name");
            cellCategory.setTdsFieldNameValue(examineeAttribute.getName());
            cellCategory.setFieldCheckType(fieldCheckType);

            examineeAttributeCategory.addCellCategory(cellCategory);
        }
    }

    /**
     * Converts the given string to an {@link ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName}.
     * If it can't convert, then null is returned.
     *
     * @param nameFieldValue
     * @return The enum if string represents a valid enum. Null otherwise.
     */
    private EnumExamineeAttributeFieldName convertToFieldName(String nameFieldValue) {

        if (EnumUtils.isValidEnum(EnumExamineeAttributeFieldName.class, nameFieldValue)) {
            return EnumExamineeAttributeFieldName.valueOf(nameFieldValue);
        }

        return null;
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
        processP_PritableASCIIone(examineeAttribute.getValue(), fieldCheckType);
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
                /* email 10/6/2015
                 * No resolution on the "EnglishLanguageProficiencLevel" typo in the Open System. If they decide
                 * to fix the code and databases, I'll have to spend time updating our system. 
                 * If they fix the documentation and keep the typo, then we'll be OK as we can handle the typo 
                 * in IRP. In fact, we could probably support the typo and non-typo in IRP by allowing 
                 * either of them to be valid entries in the TDS report.
                 */
            	if (StringUtils.equalsIgnoreCase(descriptor.getName(), enumFieldName.name())
                	|| (StringUtils.equalsIgnoreCase(descriptor.getName(), EnumExamineeAttributeFieldName.EnglishLanguageProficiencyLevel.name())
        			&& enumFieldName.name().equalsIgnoreCase(EnumExamineeAttributeFieldName.EnglishLanguageProficiencLevel.name()))) {
                    Method getter = descriptor.getReadMethod();
                    if (getter != null) {
                        String value = (String) getter.invoke(student);
                        processSameValue(value, examineeAttribute.getValue(), fieldCheckType);
                    }
                }
            }
        } catch (Exception ex) {
            logger.info(String.format("Accessor method for Student's %s method could not be invoked", enumFieldName.name()), ex);
        }
    }

    private void processSameValue(String first, String second, FieldCheckType fieldCheckType) {
    	if(StringUtils.isNotBlank(first) || StringUtils.isNotBlank(second)){
	        if (StringUtils.equalsIgnoreCase(first, StringUtils.stripStart(second, "0"))) {
	            setCcorrect(fieldCheckType);
	        }
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
