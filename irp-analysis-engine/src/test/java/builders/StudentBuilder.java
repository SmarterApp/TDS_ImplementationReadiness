package builders;

import org.cresst.sb.irp.domain.student.Student;

/**
 * Builder to create IRP Student objects to support unit tests
 */
public class StudentBuilder {

    private Student student = new Student();

    public StudentBuilder(long ssid) {
        student.setSSID(ssid);
    }

    public StudentBuilder alternateSSID(String alternateSSID) {
        student.setAlternateSSID(alternateSSID);
        return this;
    }

    public StudentBuilder americanIndianOrAlaskaNative(String americanIndianOrAlaskaNative) { 
        student.setAmericanIndianOrAlaskaNative(americanIndianOrAlaskaNative); 
        return this; 
    }

    public StudentBuilder asian(String asian) {
        student.setAsian(asian);
        return this;
    }

    public StudentBuilder birthdate(String birthdate) {
        student.setBirthdate(birthdate);
        return this;
    }

    public StudentBuilder blackOrAfricanAmerican(String blackOrAfricanAmerican) {
        student.setBlackOrAfricanAmerican(blackOrAfricanAmerican);
        return this;
    }

    public StudentBuilder demographicRaceTwoOrMoreRaces(String demographicRaceTwoOrMoreRaces) {
        student.setDemographicRaceTwoOrMoreRaces(demographicRaceTwoOrMoreRaces);
        return this;
    }

    public StudentBuilder economicDisadvantageStatus(String economicDisadvantageStatus) {
        student.setEconomicDisadvantageStatus(economicDisadvantageStatus);
        return this;
    }

    public StudentBuilder englishLanguageProficiencyLevel(String englishLanguageProficiencyLevel) {
        student.setEnglishLanguageProficiencyLevel(englishLanguageProficiencyLevel);
        return this;
    }

    public StudentBuilder firstEntryDateIntoUSSchool(String firstEntryDateIntoUSSchool) {
        student.setFirstEntryDateIntoUSSchool(firstEntryDateIntoUSSchool);
        return this;
    }

    public StudentBuilder firstName(String firstName) {
        student.setFirstName(firstName);
        return this;
    }

    public StudentBuilder gradeLevelWhenAssessed(String gradeLevelWhenAssessed) {
        student.setGradeLevelWhenAssessed(gradeLevelWhenAssessed);
        return this;
    }

    public StudentBuilder hispanicOrLatinoEthnicity(String hispanicOrLatinoEthnicity) {
        student.setHispanicOrLatinoEthnicity(hispanicOrLatinoEthnicity);
        return this;
    }

    public StudentBuilder ideaIndicator(String ideaIndicator) {
        student.setIDEAIndicator(ideaIndicator);
        return this;
    }

    public StudentBuilder languageCode(String languageCode) {
        student.setLanguageCode(languageCode);
        return this;
    }

    public StudentBuilder lastOrSurname(String lastOrSurname) {
        student.setLastOrSurname(lastOrSurname);
        return this;
    }

    public StudentBuilder lepExitDate(String lepExitDate) {
        student.setLEPExitDate(lepExitDate);
        return this;
    }

    public StudentBuilder lepStatus(String lepStatus) {
        student.setLEPStatus(lepStatus);
        return this;
    }

    public StudentBuilder limitedEnglishProficiencyEntryDate(String limitedEnglishProficiencyEntryDate) {
        student.setLimitedEnglishProficiencyEntryDate(limitedEnglishProficiencyEntryDate);
        return this;
    }

    public StudentBuilder middleName(String middleName) {
        student.setMiddleName(middleName);
        return this;
    }

    public StudentBuilder migrantStatus(String migrantStatus) {
        student.setMigrantStatus(migrantStatus);
        return this;
    }

    public StudentBuilder nativeHawaiianOrOtherPacificIslander(String nativeHawaiianOrOtherPacificIslander) {
        student.setNativeHawaiianOrOtherPacificIslander(nativeHawaiianOrOtherPacificIslander);
        return this;
    }

    public StudentBuilder primaryDisabilityType(String primaryDisabilityType) {
        student.setPrimaryDisabilityType(primaryDisabilityType);
        return this;
    }

    public StudentBuilder section504Status(String section504Status) {
        student.setSection504Status(section504Status);
        return this;
    }

    public StudentBuilder sex(String sex) {
        student.setSex(sex);
        return this;
    }

    public StudentBuilder titleIIILanguageInstructionProgramType(String titleIIILanguageInstructionProgramType) {
        student.setTitleIIILanguageInstructionProgramType(titleIIILanguageInstructionProgramType);
        return this;
    }

    public StudentBuilder white(String white) {
        student.setWhite(white);
        return this;
    }

    public StudentBuilder responsibleDistrictIdentifier(String responsibleDistrictIdentifier) {
        student.setResponsibleDistrictIdentifier(responsibleDistrictIdentifier);
        return this;
    }

    public StudentBuilder responsibleInstitutionIdentifier(String responsibleInstitutionIdentifier) {
        student.setResponsibleInstitutionIdentifier(responsibleInstitutionIdentifier);
        return this;
    }

    public StudentBuilder stateAbbreviation(String stateAbbreviation) {
        student.setStateAbbreviation(stateAbbreviation);
        return this;
    }

    public Student toStudent() {
        return student;
    }
}
