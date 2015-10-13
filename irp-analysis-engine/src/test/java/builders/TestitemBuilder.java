package builders;

import org.cresst.sb.irp.domain.testpackage.Identifier;
import org.cresst.sb.irp.domain.testpackage.Testitem; ;

public class TestitemBuilder {

	private Testitem testitem = new Testitem();
	private Identifier identifier = new Identifier();
	
	public TestitemBuilder filename(String filename){
		testitem.setFilename(filename);
		return this;
	}
	
	public TestitemBuilder itemtype(String itemtype){
		testitem.setItemtype(itemtype);
		return this;
	}
	
	public TestitemBuilder uniqueid(String uniqueid){
		identifier.setUniqueid(uniqueid);
		return this;
	}
	
	public Testitem toTestitem(){
		testitem.setIdentifier(identifier);
		return testitem;
	}
		
}
