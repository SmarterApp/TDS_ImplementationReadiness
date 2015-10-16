package builders;

import org.cresst.sb.irp.domain.accommodation.Accommodation;


/**
 * Builder for Accommodation objects
 */
public class AccommodationExcelBuilder {

	private Accommodation accommodation = new Accommodation();

	public AccommodationExcelBuilder(long ssid) {
		accommodation.setStudentIdentifier(ssid);
	}

	public AccommodationExcelBuilder studentIdentifier(long studentIdentifier){
		accommodation.setStudentIdentifier(studentIdentifier);
		return this;
	}
	
	public AccommodationExcelBuilder stateAbbreviation(String stateAbbreviation) {
		accommodation.setStateAbbreviation(stateAbbreviation);
		return this;
	}
	
	public AccommodationExcelBuilder subject(String subject){
		accommodation.setSubject(subject);
		return this;
	}
	
	public AccommodationExcelBuilder americanSignLanguage(String americanSignLanguage){
		accommodation.setAmericanSignLanguage(americanSignLanguage);
		return this;
	}

	public AccommodationExcelBuilder colorContrast(String colorContrast){
		accommodation.setColorContrast(colorContrast);
		return this;
	}
	
	public AccommodationExcelBuilder closedCaptioning(String closedCaptioning) {
		accommodation.setClosedCaptioning(closedCaptioning);
		return this;
	}
	
	public AccommodationExcelBuilder language(String language){
		accommodation.setLanguage(language);
		return this;
	}
	
	public AccommodationExcelBuilder masking(String masking){
		accommodation.setMasking(masking);
		return this;
	}

	public AccommodationExcelBuilder permissiveMode(String permissiveMode){
		accommodation.setPermissiveMode(permissiveMode);
		return this;
	}
	
	public AccommodationExcelBuilder printOnDemand(String printOnDemand) {
		accommodation.setPrintOnDemand(printOnDemand);
		return this;
	}
	
	public AccommodationExcelBuilder zoom(String zoom){
		accommodation.setZoom(zoom);
		return this;
	}
	
	public AccommodationExcelBuilder streamlinedInterface(String streamlinedInterface){
		accommodation.setStreamlinedInterface(streamlinedInterface);
		return this;
	}

	public AccommodationExcelBuilder texttoSpeech(String texttoSpeech){
		accommodation.setTexttoSpeech(texttoSpeech);
		return this;
	}
	
	public AccommodationExcelBuilder translation(String translation) {
		accommodation.setTranslation(translation);
		return this;
	}
	
	public AccommodationExcelBuilder nonEmbeddedDesignatedSupports(String nonEmbeddedDesignatedSupports){
		accommodation.setNonEmbeddedDesignatedSupports(nonEmbeddedDesignatedSupports);
		return this;
	}
	
	public AccommodationExcelBuilder nonEmbeddedAccommodations(String nonEmbeddedAccommodations){
		accommodation.setNonEmbeddedAccommodations(nonEmbeddedAccommodations);
		return this;
	}

	public AccommodationExcelBuilder other(String other){
		accommodation.setOther(other);
		return this;
	}
	
	public Accommodation toAccommodation(){
		return accommodation;
	}
	
}
