package builders;

import org.cresst.sb.irp.domain.testpackage.Administration;
import org.cresst.sb.irp.domain.testpackage.Identifier;
import org.cresst.sb.irp.domain.testpackage.Testspecification;

public class TestspecificationBuilder {
	
	private Testspecification testspecification = new Testspecification();
	private Administration administration;
	private Identifier identifier = new Identifier();
	
	public TestspecificationBuilder(String uniqueid){
		identifier.setUniqueid(uniqueid);
	}
	
	public TestspecificationBuilder uniqueid(String uniqueid){
		identifier.setUniqueid(uniqueid);
		return this;
	}
	
	public TestspecificationBuilder setAdministration(Administration admin){
		administration = admin;
		return this;
	}
	
	public Testspecification toTestspecification(){
		testspecification.setIdentifier(identifier);
		testspecification.setAdministration(administration);
		return testspecification;
	}
	

}
