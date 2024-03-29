package org.cresst.sb.irp.dao;

import java.util.List;
import java.util.Map;

import org.cresst.sb.irp.domain.testpackage.Administration;
import org.cresst.sb.irp.domain.testpackage.Adminsegment;
import org.cresst.sb.irp.domain.testpackage.Identifier;
import org.cresst.sb.irp.domain.testpackage.Itempool;
import org.cresst.sb.irp.domain.testpackage.Poolproperty;
import org.cresst.sb.irp.domain.testpackage.Property;
import org.cresst.sb.irp.domain.testpackage.Testblueprint;
import org.cresst.sb.irp.domain.testpackage.Testform;
import org.cresst.sb.irp.domain.testpackage.Testspecification;

/**
 * Test package data repository for accessing irp-package data for TDS, TIS, and
 * classpath:testspecification.official.xsd
 */
public interface TestpackageDao {
	
	Map<String, Testspecification> getMapTestpackage();

	Testspecification getTestpackage(String uniqueid);

	Testspecification getTestpackageByIdentifierUniqueid(String uniqueid);
	
	Identifier getIdentifier(String testpackageUniqueid);
	
	List<Property> getListProperty(String testpackageUniqueid);
	
	Administration getAdministration(String testpackageUniqueid);
	
	String getPublisher(String uniqueid);
	
	String getPublishdate(String uniqueid);
	
	String getVersion(String uniqueid);
	
	String getPurpose(String uniqueid);
	
	Testblueprint getTestblueprint(String testpackageUniqueid);
	
	List<Poolproperty> getListPoolproperty(String testpackageUniqueid);
	
	Itempool getItempool(String testpackageUniqueid);
	
	List<Testform> getTestform(String testpackageUniqueid);
	
	List<Adminsegment> getAdminsegment(String testpackageUniqueid);

}
