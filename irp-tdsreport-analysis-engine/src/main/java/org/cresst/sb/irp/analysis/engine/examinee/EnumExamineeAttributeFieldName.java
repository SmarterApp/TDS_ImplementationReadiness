package org.cresst.sb.irp.analysis.engine.examinee;

/**
 * Enum for the Examinee Attributes
 */
public enum EnumExamineeAttributeFieldName {
    LastOrSurname(35),
    FirstName(35),
    MiddleName(35, false),
    Birthdate(10),
    StudentIdentifier(40),
    AlternateSSID(50),
    GradeLevelWhenAssessed(2),
    Sex(6),
    HispanicOrLatinoEthnicity(3),
    AmericanIndianOrAlaskaNative(3),
    Asian(3),
    BlackOrAfricanAmerican(3),
    White(3),
    NativeHawaiianOrOtherPacificIslander(3),
    DemographicRaceTwoOrMoreRaces(3),
    IDEAIndicator(3),
    LEPStatus(3),
    Section504Status(22),
    EconomicDisadvantageStatus(3),
    LanguageCode(3, false),
    EnglishLanguageProficiencyLevel(20, false),
    EnglishLanguageProficiencLevel(20, false),
    MigrantStatus(3, false),
    FirstEntryDateIntoUSSchool(10, false),
    LimitedEnglishProficiencyEntryDate(10, false),
    LEPExitDate(10, false),
    TitleIIILanguageInstructionProgramType(27, false),
    PrimaryDisabilityType(3, false);

    private int maxWidth;
    private boolean isRequired;

    EnumExamineeAttributeFieldName(int maxWidth) {
        this.maxWidth = maxWidth;
        this.isRequired = true;
    }

    EnumExamineeAttributeFieldName(int maxWidth, boolean isRequired) {
        this.maxWidth = maxWidth;
        this.isRequired = isRequired;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public boolean isRequired() {
        return isRequired;
    }

}