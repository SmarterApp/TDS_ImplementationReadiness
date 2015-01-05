package org.cresst.sb.irp.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.dao.TestpackageDao;
import org.cresst.sb.irp.domain.testpackage.Administration;
import org.cresst.sb.irp.domain.testpackage.Adminsegment;
import org.cresst.sb.irp.domain.testpackage.Identifier;
import org.cresst.sb.irp.domain.testpackage.Itempool;
import org.cresst.sb.irp.domain.testpackage.Poolproperty;
import org.cresst.sb.irp.domain.testpackage.Property;
import org.cresst.sb.irp.domain.testpackage.Testblueprint;
import org.cresst.sb.irp.domain.testpackage.Testform;
import org.cresst.sb.irp.domain.testpackage.Testspecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestPackageServiceImpl implements TestPackageService {
	private static Logger logger = Logger.getLogger(TestPackageServiceImpl.class);

	@Autowired
	private TestpackageDao testpackageDao;
	
	@Override
	public Testspecification getTestpackage(String uniqueid) {
		return testpackageDao.getTestpackage(uniqueid);
	}

	@Override
	public Testspecification getTestpackageByIdentifierUniqueid(String uniqueid) {
		return testpackageDao.getTestpackageByIdentifierUniqueid(uniqueid);
	}
	
	@Override
	public Identifier getIdentifier(String testpackageUniqueid) {
		return testpackageDao.getIdentifier(testpackageUniqueid);
	}

	@Override
	public List<Property> getListProperty(String testpackageUniqueid) {
		return testpackageDao.getListProperty(testpackageUniqueid);
	}

	@Override
	public Administration getAdministration(String testpackageUniqueid) {
		return testpackageDao.getAdministration(testpackageUniqueid);
	}
	
	@Override
	public String getPublisher(String uniqueid) {
		return testpackageDao.getPublisher(uniqueid);
	}
	
	@Override
	public String getPublishdate(String uniqueid) {
		return testpackageDao.getPublishdate(uniqueid);
	}

	@Override
	public String getVersion(String uniqueid) {
		return testpackageDao.getVersion(uniqueid);
	}

	@Override
	public String getPurpose(String uniqueid) {
		return testpackageDao.getPurpose(uniqueid);
	}

	@Override
	public Testblueprint getTestblueprint(String testpackageUniqueid) {
		return testpackageDao.getTestblueprint(testpackageUniqueid);
	}

	@Override
	public List<Poolproperty> getListPoolproperty(String testpackageUniqueid) {
		return testpackageDao.getListPoolproperty(testpackageUniqueid);
	}

	@Override
	public Itempool getItempool(String testpackageUniqueid) {
		return testpackageDao.getItempool(testpackageUniqueid);
	}

	@Override
	public List<Testform> getTestform(String testpackageUniqueid) {
		return testpackageDao.getTestform(testpackageUniqueid);
	}

	@Override
	public List<Adminsegment> getAdminsegment(String testpackageUniqueid) {
		return testpackageDao.getAdminsegment(testpackageUniqueid);
	}

	@Override
	public String getSubjectPropertyValueFromListProperty(List<Property> listProperty) {
		return testpackageDao.getSubjectPropertyValueFromListProperty(listProperty);
	}

	@Override
	public String getGradePropertyValueFromListProperty(List<Property> listProperty) {
		return testpackageDao.getGradePropertyValueFromListProperty(listProperty);
	}


}
