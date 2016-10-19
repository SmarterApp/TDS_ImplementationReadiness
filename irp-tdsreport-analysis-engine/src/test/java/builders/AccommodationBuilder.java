package builders;

import org.cresst.sb.irp.domain.tdsreport.TDSReport;

public class AccommodationBuilder {

	private TDSReport.Opportunity.Accommodation accommodation = new TDSReport.Opportunity.Accommodation();

	public AccommodationBuilder() {
	}

	public AccommodationBuilder type(String type) {
		accommodation.setType(type);
		return this;
	}
	
	public AccommodationBuilder value(String value){
		accommodation.setValue(value);
		return this;
	}
	
	public AccommodationBuilder code(String code){
		accommodation.setCode(code);
		return this;
	}

	public AccommodationBuilder segment(Long segment){
		accommodation.setSegment(segment);
		return this;
	}
	
	public TDSReport.Opportunity.Accommodation toAccommodation(){
		return accommodation;
	}
	
}
