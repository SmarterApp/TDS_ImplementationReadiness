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
import org.cresst.sb.irp.domain.testpackage.Testpackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestPackageServiceImpl implements TestPackageService {
	private static Logger logger = Logger.getLogger(TestPackageServiceImpl.class);

	@Autowired
	private TestpackageDao testpackageDao;
	
	@Override
	public Testpackage getTestpackage() {
		return testpackageDao.getTestpackage();
	}

	@Override
	public Identifier getIdentifier() {
		return testpackageDao.getIdentifier();
	}

	@Override
	public List<Property> getListProperty() {
		return testpackageDao.getListProperty();
	}

	@Override
	public Administration getAdministration() {
		return testpackageDao.getAdministration();
	}
	
	@Override
	public String getPublisher() {
		return testpackageDao.getPublisher();
	}
	
	@Override
	public String getPublishdate() {
		return testpackageDao.getPublishdate();
	}

	@Override
	public String getVersion() {
		return testpackageDao.getVersion();
	}

	@Override
	public String getPurpose() {
		return testpackageDao.getPurpose();
	}

	@Override
	public Testblueprint getTestblueprint() {
		return testpackageDao.getTestblueprint();
	}

	@Override
	public List<Poolproperty> getListPoolproperty() {
		return testpackageDao.getListPoolproperty();
	}

	@Override
	public Itempool getItempool() {
		return testpackageDao.getItempool();
	}

	@Override
	public List<Testform> getTestform() {
		return testpackageDao.getTestform();
	}

	@Override
	public List<Adminsegment> getAdminsegment() {
		return testpackageDao.getAdminsegment();
	}

}
