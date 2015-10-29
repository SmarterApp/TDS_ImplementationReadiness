package builders;

import org.cresst.sb.irp.domain.tdsreport.TDSReport;

public class ItemResponseBuilder {

	private TDSReport.Opportunity.Item.Response itemResponse = new TDSReport.Opportunity.Item.Response();
	
	public ItemResponseBuilder() {}
	
	public ItemResponseBuilder type(String value){
		itemResponse.setType(value);
		return this;
	}
	
	public ItemResponseBuilder content(String value){
		itemResponse.setContent(value);
		return this;
	}
	
	public TDSReport.Opportunity.Item.Response toIemResponse(){
		return itemResponse;
	}
	
}
