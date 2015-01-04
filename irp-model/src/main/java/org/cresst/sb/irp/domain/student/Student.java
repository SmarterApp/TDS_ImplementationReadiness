package org.cresst.sb.irp.domain.student;

import org.jsondoc.core.annotation.ApiObject;

import java.io.Serializable;

@ApiObject(name = "Student", description = "Student")
public class Student implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String StateAbbreviation;
	private String ResponsibleDistrictIdentifier;
	private String ResponsibleInstitutionIdentifier; //ResponsibleSchoolIdentifier
	private String LastOrSurname;
	private String FirstName;
	private String MiddleName;
	private String Birthdate;
	private long SSID;
	private String AlternateSSID;
	//private String StudentIdentifier;
	//private String ExternalSSID;
	//private String ConfirmationCode;
	private String GradeLevelWhenAssessed;
	private String Sex;
	private String HispanicOrLatinoEthnicity;
	private String AmericanIndianOrAlaskaNative;
	private String Asian;
	private String BlackOrAfricanAmerican;
	private String White;
	private String NativeHawaiianOrOtherPacificIslander;
	private String DemographicRaceTwoOrMoreRaces;
	private String IDEAIndicator;
	private String LEPStatus;
	private String Section504Status;
	private String EconomicDisadvantageStatus;
	private String LanguageCode;
	private String EnglishLanguageProficiencyLevel;
	private String MigrantStatus;
	private String FirstEntryDateIntoUSSchool;
	private String LimitedEnglishProficiencyEntryDate;
	private String LEPExitDate;
	private String TitleIIILanguageInstructionProgramType;
	private String PrimaryDisabilityType;
	private String Delete;

	public Student() {
	}
	
	public String getStateAbbreviation() {
		return StateAbbreviation;
	}

	public void setStateAbbreviation(String stateAbbreviation) {
		StateAbbreviation = stateAbbreviation;
	}

	public String getResponsibleDistrictIdentifier() {
		return ResponsibleDistrictIdentifier;
	}

	public void setResponsibleDistrictIdentifier(
			String responsibleDistrictIdentifier) {
		ResponsibleDistrictIdentifier = responsibleDistrictIdentifier;
	}

	/*
	public String getResponsibleSchoolIdentifier() {
		return ResponsibleSchoolIdentifier;
	}*/

	/*
	public void setResponsibleSchoolIdentifier(
			String responsibleSchoolIdentifier) {
		ResponsibleSchoolIdentifier = responsibleSchoolIdentifier;
	}*/
	
	public String getResponsibleInstitutionIdentifier() {
		return ResponsibleInstitutionIdentifier;
	}

	public void setResponsibleInstitutionIdentifier(
			String responsibleInstitutionIdentifier) {
		ResponsibleInstitutionIdentifier = responsibleInstitutionIdentifier;
	}

	public String getLastOrSurname() {
		return LastOrSurname;
	}

	public void setLastOrSurname(String lastOrSurname) {
		LastOrSurname = lastOrSurname;
	}

	public String getFirstName() {
		return FirstName;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	public String getMiddleName() {
		return MiddleName;
	}

	public void setMiddleName(String middleName) {
		MiddleName = middleName;
	}

	public String getBirthdate() {
		return Birthdate;
	}

	public void setBirthdate(String birthdate) {
		Birthdate = birthdate;
	}
	
	public long getSSID() {
		return SSID;
	}

	public void setSSID(long ssid) {
		SSID = ssid;
	}
	
	public String getAlternateSSID() {
		return AlternateSSID;
	}

	public void setAlternateSSID(String alternateSSID) {
		AlternateSSID = alternateSSID;
	}

	
	/*
	public String getStudentIdentifier() {
		return StudentIdentifier;
	}*/

	/*
	public void setStudentIdentifier(String studentIdentifier) {
		StudentIdentifier = studentIdentifier;
	}*/

	/*
	public String getExternalSSID() {
		return ExternalSSID;
	}*/

	/*
	public void setExternalSSID(String externalSSID) {
		ExternalSSID = externalSSID;
	}*/

	/*
	public String getConfirmationCode() {
		return ConfirmationCode;
	}*/

	/*
	public void setConfirmationCode(String confirmationCode) {
		ConfirmationCode = confirmationCode;
	}*/

	public String getGradeLevelWhenAssessed() {
		return GradeLevelWhenAssessed;
	}

	public void setGradeLevelWhenAssessed(String gradeLevelWhenAssessed) {
		GradeLevelWhenAssessed = gradeLevelWhenAssessed;
	}

	public String getSex() {
		return Sex;
	}

	public void setSex(String sex) {
		Sex = sex;
	}

	public String getHispanicOrLatinoEthnicity() {
		return HispanicOrLatinoEthnicity;
	}

	public void setHispanicOrLatinoEthnicity(String hispanicOrLatinoEthnicity) {
		HispanicOrLatinoEthnicity = hispanicOrLatinoEthnicity;
	}

	public String getAmericanIndianOrAlaskaNative() {
		return AmericanIndianOrAlaskaNative;
	}

	public void setAmericanIndianOrAlaskaNative(
			String americanIndianOrAlaskaNative) {
		AmericanIndianOrAlaskaNative = americanIndianOrAlaskaNative;
	}

	public String getAsian() {
		return Asian;
	}

	public void setAsian(String asian) {
		Asian = asian;
	}

	public String getBlackOrAfricanAmerican() {
		return BlackOrAfricanAmerican;
	}

	public void setBlackOrAfricanAmerican(String blackOrAfricanAmerican) {
		BlackOrAfricanAmerican = blackOrAfricanAmerican;
	}

	public String getWhite() {
		return White;
	}

	public void setWhite(String white) {
		White = white;
	}

	public String getNativeHawaiianOrOtherPacificIslander() {
		return NativeHawaiianOrOtherPacificIslander;
	}

	public void setNativeHawaiianOrOtherPacificIslander(
			String nativeHawaiianOrOtherPacificIslander) {
		NativeHawaiianOrOtherPacificIslander = nativeHawaiianOrOtherPacificIslander;
	}

	public String getDemographicRaceTwoOrMoreRaces() {
		return DemographicRaceTwoOrMoreRaces;
	}

	public void setDemographicRaceTwoOrMoreRaces(
			String demographicRaceTwoOrMoreRaces) {
		DemographicRaceTwoOrMoreRaces = demographicRaceTwoOrMoreRaces;
	}

	public String getIDEAIndicator() {
		return IDEAIndicator;
	}

	public void setIDEAIndicator(String iDEAIndicator) {
		IDEAIndicator = iDEAIndicator;
	}

	public String getLEPStatus() {
		return LEPStatus;
	}

	public void setLEPStatus(String lEPStatus) {
		LEPStatus = lEPStatus;
	}

	public String getSection504Status() {
		return Section504Status;
	}

	public void setSection504Status(String section504Status) {
		Section504Status = section504Status;
	}

	public String getEconomicDisadvantageStatus() {
		return EconomicDisadvantageStatus;
	}

	public void setEconomicDisadvantageStatus(String economicDisadvantageStatus) {
		EconomicDisadvantageStatus = economicDisadvantageStatus;
	}

	public String getLanguageCode() {
		return LanguageCode;
	}

	public void setLanguageCode(String languageCode) {
		LanguageCode = languageCode;
	}

	public String getEnglishLanguageProficiencyLevel() {
		return EnglishLanguageProficiencyLevel;
	}

	public void setEnglishLanguageProficiencyLevel(
			String englishLanguageProficiencyLevel) {
		EnglishLanguageProficiencyLevel = englishLanguageProficiencyLevel;
	}

	public String getMigrantStatus() {
		return MigrantStatus;
	}

	public void setMigrantStatus(String migrantStatus) {
		MigrantStatus = migrantStatus;
	}

	public String getFirstEntryDateIntoUSSchool() {
		return FirstEntryDateIntoUSSchool;
	}

	public void setFirstEntryDateIntoUSSchool(String firstEntryDateIntoUSSchool) {
		FirstEntryDateIntoUSSchool = firstEntryDateIntoUSSchool;
	}

	public String getLimitedEnglishProficiencyEntryDate() {
		return LimitedEnglishProficiencyEntryDate;
	}

	public void setLimitedEnglishProficiencyEntryDate(
			String limitedEnglishProficiencyEntryDate) {
		LimitedEnglishProficiencyEntryDate = limitedEnglishProficiencyEntryDate;
	}

	public String getLEPExitDate() {
		return LEPExitDate;
	}

	public void setLEPExitDate(String lEPExitDate) {
		LEPExitDate = lEPExitDate;
	}

	public String getTitleIIILanguageInstructionProgramType() {
		return TitleIIILanguageInstructionProgramType;
	}

	public void setTitleIIILanguageInstructionProgramType(
			String titleIIILanguageInstructionProgramType) {
		TitleIIILanguageInstructionProgramType = titleIIILanguageInstructionProgramType;
	}

	public String getPrimaryDisabilityType() {
		return PrimaryDisabilityType;
	}

	public void setPrimaryDisabilityType(String primaryDisabilityType) {
		PrimaryDisabilityType = primaryDisabilityType;
	}

	public String getDelete() {
		return Delete;
	}

	public void setDelete(String delete) {
		Delete = delete;
	}
	
	@Override
	public String toString() {
		return "Student [StateAbbreviation=" + StateAbbreviation
				+ ", ResponsibleDistrictIdentifier="
				+ ResponsibleDistrictIdentifier
				+ ", ResponsibleInstitutionIdentifier="
				+ ResponsibleInstitutionIdentifier + ", LastOrSurname="
				+ LastOrSurname + ", FirstName=" + FirstName + ", MiddleName="
				+ MiddleName + ", Birthdate=" + Birthdate + ", SSID=" + SSID
				+ ", AlternateSSID=" + AlternateSSID
				+ ", GradeLevelWhenAssessed=" + GradeLevelWhenAssessed
				+ ", Sex=" + Sex + ", HispanicOrLatinoEthnicity="
				+ HispanicOrLatinoEthnicity + ", AmericanIndianOrAlaskaNative="
				+ AmericanIndianOrAlaskaNative + ", Asian=" + Asian
				+ ", BlackOrAfricanAmerican=" + BlackOrAfricanAmerican
				+ ", White=" + White
				+ ", NativeHawaiianOrOtherPacificIslander="
				+ NativeHawaiianOrOtherPacificIslander
				+ ", DemographicRaceTwoOrMoreRaces="
				+ DemographicRaceTwoOrMoreRaces + ", IDEAIndicator="
				+ IDEAIndicator + ", LEPStatus=" + LEPStatus
				+ ", Section504Status=" + Section504Status
				+ ", EconomicDisadvantageStatus=" + EconomicDisadvantageStatus
				+ ", LanguageCode=" + LanguageCode
				+ ", EnglishLanguageProficiencyLevel="
				+ EnglishLanguageProficiencyLevel + ", MigrantStatus="
				+ MigrantStatus + ", FirstEntryDateIntoUSSchool="
				+ FirstEntryDateIntoUSSchool
				+ ", LimitedEnglishProficiencyEntryDate="
				+ LimitedEnglishProficiencyEntryDate + ", LEPExitDate="
				+ LEPExitDate + ", TitleIIILanguageInstructionProgramType="
				+ TitleIIILanguageInstructionProgramType
				+ ", PrimaryDisabilityType=" + PrimaryDisabilityType
				+ ", Delete=" + Delete + "]";
	}
	
	
}
