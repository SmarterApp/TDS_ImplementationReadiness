package builders;

import org.cresst.sb.irp.domain.testpackage.Bpelement;

public class BpelementBuilder {
	
	private Bpelement bpelement = new Bpelement();
	
	public BpelementBuilder elementtype(String elementtype){
		bpelement.setElementtype(elementtype);
		return this;
	}
	
	public BpelementBuilder minopitems(String minopitems){
		bpelement.setMinopitems(minopitems);
		return this;
	}
	
	public BpelementBuilder maxopitems(String maxopitems){
		bpelement.setMaxopitems(maxopitems);
		return this;
	}
	
	public Bpelement toBpelement(){
		return bpelement;
	}
	

}
