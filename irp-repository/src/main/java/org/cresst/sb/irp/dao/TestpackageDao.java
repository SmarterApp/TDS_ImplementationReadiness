package org.cresst.sb.irp.dao;

import java.util.List;

import org.cresst.sb.irp.domain.testpackage.Administration;
import org.cresst.sb.irp.domain.testpackage.Adminsegment;
import org.cresst.sb.irp.domain.testpackage.Identifier;
import org.cresst.sb.irp.domain.testpackage.Itempool;
import org.cresst.sb.irp.domain.testpackage.Poolproperty;
import org.cresst.sb.irp.domain.testpackage.Property;
import org.cresst.sb.irp.domain.testpackage.Testblueprint;
import org.cresst.sb.irp.domain.testpackage.Testform;
import org.cresst.sb.irp.domain.testpackage.Testpackage;

public interface TestpackageDao {
	
	Testpackage getTestpackage();
	
	Identifier getIdentifier();
	
	List<Property> getListProperty();
	
	Administration getAdministration();
	
	String getPublisher();
	
	String getPublishdate();
	
	String getVersion();
	
	String getPurpose();
	
	Testblueprint getTestblueprint();
	
	List<Poolproperty> getListPoolproperty();
	
	Itempool getItempool();
	
	List<Testform> getTestform();
	
	List<Adminsegment> getAdminsegment();

}
