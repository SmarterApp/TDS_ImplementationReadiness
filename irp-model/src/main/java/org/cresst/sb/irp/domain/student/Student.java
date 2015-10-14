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
	private String ResponsibleInstitutionIdentifier;
	private String LastOrSurname;
	private String FirstName;
	private String MiddleName;
	private String Birthdate;
	private String StudentIdentifier;
	private long SSID;
	private String AlternateSSID;
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
	private String Subject;
	private String AmericanSignLanguage;
	private String ColorContrast;
	private String ClosedCaptioning;
	private String Language;
	private String Masking;
	private String PermissiveMode;
	private String PrintOnDemand;
	private String PrintSize;
	private String StreamlinedInterface;
	private String TextToSpeech;
	private String Translation;
	private String NonEmbeddedDesignatedSupports;
	private String NonEmbeddedAccommodations;
	private String Other;


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
	
	public String getStudentIdentifier() {
		return StudentIdentifier;
	}

	public void setStudentIdentifier(String studentIdentifier) {
		StudentIdentifier = studentIdentifier;
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
	
	public String getSubject() {
		return Subject;
	}

	public void setSubject(String subject) {
		Subject = subject;
	}
	
	public String getAmericanSignLanguage() {
		return AmericanSignLanguage;
	}

	public void setAmericanSignLanguage(String americanSignLanguage) {
		AmericanSignLanguage = americanSignLanguage;
	}

	public String getColorContrast() {
		return ColorContrast;
	}

	public void setColorContrast(String colorContrast) {
		ColorContrast = colorContrast;
	}

	public String getClosedCaptioning() {
		return ClosedCaptioning;
	}

	public void setClosedCaptioning(String closedCaptioning) {
		ClosedCaptioning = closedCaptioning;
	}
	
	public String getLanguage() {
		return Language;
	}

	public void setLanguage(String language) {
		Language = language;
	}

	public String getMasking() {
		return Masking;
	}

	public void setMasking(String masking) {
		Masking = masking;
	}

	public String getPermissiveMode() {
		return PermissiveMode;
	}

	public void setPermissiveMode(String permissiveMode) {
		PermissiveMode = permissiveMode;
	}
	
	public String getPrintOnDemand() {
		return PrintOnDemand;
	}

	public void setPrintOnDemand(String printOnDemand) {
		PrintOnDemand = printOnDemand;
	}
	
	public String getPrintSize() {
		return PrintSize;
	}

	public void setPrintSize(String printSize) {
		PrintSize = printSize;
	}
	
	public String getStreamlinedInterface() {
		return StreamlinedInterface;
	}

	public void setStreamlinedInterface(String streamlinedInterface) {
		StreamlinedInterface = streamlinedInterface;
	}

	public String getTextToSpeech() {
		return TextToSpeech;
	}

	public void setTextToSpeech(String textToSpeech) {
		TextToSpeech = textToSpeech;
	}

	public String getTranslation() {
		return Translation;
	}

	public void setTranslation(String translation) {
		Translation = translation;
	}

	public String getNonEmbeddedDesignatedSupports() {
		return NonEmbeddedDesignatedSupports;
	}

	public void setNonEmbeddedDesignatedSupports(String nonEmbeddedDesignatedSupports) {
		NonEmbeddedDesignatedSupports = nonEmbeddedDesignatedSupports;
	}

	public String getNonEmbeddedAccommodations() {
		return NonEmbeddedAccommodations;
	}

	public void setNonEmbeddedAccommodations(String nonEmbeddedAccommodations) {
		NonEmbeddedAccommodations = nonEmbeddedAccommodations;
	}

	public String getOther() {
		return Other;
	}

	public void setOther(String other) {
		Other = other;
	}

	@Override
	public String toString() {
		return "Student [StateAbbreviation=" + StateAbbreviation + ", ResponsibleDistrictIdentifier="
				+ ResponsibleDistrictIdentifier + ", ResponsibleInstitutionIdentifier="
				+ ResponsibleInstitutionIdentifier + ", LastOrSurname=" + LastOrSurname + ", FirstName=" + FirstName
				+ ", MiddleName=" + MiddleName + ", Birthdate=" + Birthdate + ", StudentIdentifier="
				+ StudentIdentifier + ", SSID=" + SSID + ", AlternateSSID=" + AlternateSSID
				+ ", GradeLevelWhenAssessed=" + GradeLevelWhenAssessed + ", Sex=" + Sex
				+ ", HispanicOrLatinoEthnicity=" + HispanicOrLatinoEthnicity + ", AmericanIndianOrAlaskaNative="
				+ AmericanIndianOrAlaskaNative + ", Asian=" + Asian + ", BlackOrAfricanAmerican="
				+ BlackOrAfricanAmerican + ", White=" + White + ", NativeHawaiianOrOtherPacificIslander="
				+ NativeHawaiianOrOtherPacificIslander + ", DemographicRaceTwoOrMoreRaces="
				+ DemographicRaceTwoOrMoreRaces + ", IDEAIndicator=" + IDEAIndicator + ", LEPStatus=" + LEPStatus
				+ ", Section504Status=" + Section504Status + ", EconomicDisadvantageStatus="
				+ EconomicDisadvantageStatus + ", LanguageCode=" + LanguageCode + ", EnglishLanguageProficiencyLevel="
				+ EnglishLanguageProficiencyLevel + ", MigrantStatus=" + MigrantStatus
				+ ", FirstEntryDateIntoUSSchool=" + FirstEntryDateIntoUSSchool
				+ ", LimitedEnglishProficiencyEntryDate=" + LimitedEnglishProficiencyEntryDate + ", LEPExitDate="
				+ LEPExitDate + ", TitleIIILanguageInstructionProgramType=" + TitleIIILanguageInstructionProgramType
				+ ", PrimaryDisabilityType=" + PrimaryDisabilityType + ", Subject=" + Subject
				+ ", AmericanSignLanguage=" + AmericanSignLanguage + ", ColorContrast=" + ColorContrast
				+ ", ClosedCaptioning=" + ClosedCaptioning + ", Language=" + Language + ", Masking=" + Masking
				+ ", PermissiveMode=" + PermissiveMode + ", PrintOnDemand=" + PrintOnDemand + ", PrintSize="
				+ PrintSize + ", StreamlinedInterface=" + StreamlinedInterface + ", TextToSpeech=" + TextToSpeech
				+ ", Translation=" + Translation + ", NonEmbeddedDesignatedSupports=" + NonEmbeddedDesignatedSupports
				+ ", NonEmbeddedAccommodations=" + NonEmbeddedAccommodations + ", Other=" + Other + "]";
	}

	
}
