package org.cresst.sb.irp.dao;

import java.util.HashMap;
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
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.cresst.sb.irp.utils.RetrieveFileUtil;
import org.cresst.sb.irp.utils.XMLValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class TestpackageDaoImpl implements TestpackageDao, InitializingBean {
	private final static Logger logger = LoggerFactory.getLogger(TestpackageDaoImpl.class);
	private Map<String, Testspecification> mapTestpackage = new HashMap<>();

    @Value("${irp.package.location}/TestPackages/TDS/Administration")
	private Resource tdsTestPackagePath;

	@Value("${irp.package.location}/TestPackages/TIS/Administration")
	private Resource tisTestPackagePath;

	@Value("classpath:testspecification.official.xsd")
	private Resource testspecificationXSDResource;

	@Autowired
	private XMLValidate xmlValidate;

	@Autowired
	private RetrieveFileUtil retrieveFileUtil;

	@Override
	public Testspecification getTestpackage(String uniqueid) {
		Testspecification testpackage = mapTestpackage.get(uniqueid);
		if (testpackage == null) {
			throw new NotFoundException("Could not find testpackage for TestpackageDaoImpl.getTestpackage");
		}
		return testpackage;
	}

	@Override
	public Testspecification getTestpackageByIdentifierUniqueid(String uniqueid) {
		Testspecification testpackage = mapTestpackage.get(uniqueid);
		if (testpackage == null) {
			return null;
		}
		return testpackage;
	}

	@Override
	public Map<String, Testspecification> getMapTestpackage() {
		return mapTestpackage;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			retrieveFileUtil.walk(tdsTestPackagePath.getURI().getPath(), mapTestpackage, testspecificationXSDResource,
					xmlValidate);
			retrieveFileUtil.walk(tisTestPackagePath.getURI().getPath(), mapTestpackage, testspecificationXSDResource,
                    xmlValidate);
		} catch (Exception e) {
			logger.error("afterPropertiesSet exception: ", e);
		}
	}

	@Override
	public Identifier getIdentifier(String testpackageUniqueid) {
		Testspecification testpackage = mapTestpackage.get(testpackageUniqueid);
		if (testpackage == null) {
			throw new NotFoundException("Could not find testpackage for TestpackageDaoImpl.getIdentifier");
		}
		Identifier identifier = testpackage.getIdentifier();
		if (identifier == null) {
			throw new NotFoundException("Could not find Identifier for TestpackageDaoImpl.getIdentifier");
		}
		return identifier;
	}

	@Override
	public List<Property> getListProperty(String testpackageUniqueid) {
		Testspecification testpackage = mapTestpackage.get(testpackageUniqueid);
		if (testpackage == null) {
			throw new NotFoundException("Could not find testpackage for TestpackageDaoImpl.getListProperty");
		}
		List<Property> listProperty = testpackage.getProperty();
		if (listProperty == null) {
			throw new NotFoundException("Could not find listProperty for TestpackageDaoImpl.getListProperty");
		}
		return listProperty;
	}

	@Override
	public Administration getAdministration(String testpackageUniqueid) {
		Testspecification testpackage = mapTestpackage.get(testpackageUniqueid);
		if (testpackage == null) {
			throw new NotFoundException("Could not find testpackage for TestpackageDaoImpl.getAdministration");
		}

		Administration administration;
		administration = testpackage.getAdministration();
		/*
		 * List<Object> objects = testpackage.getAdministrationOrRegistrationOrReportingOrScoringOrComplete();
		 * 
		 * // First element is checked because there should only be one element in the list if (objects.size() > 0 &&
		 * objects.get(0) instanceof Administration) { administration = (Administration)objects.get(0); } else { throw new
		 * NotFoundException( "Could not find administration for TestpackageDaoImpl.getAdministration"); }
		 */

		return administration;
	}

	@Override
	public String getPublisher(String uniqueid) {
		Testspecification testpackage = mapTestpackage.get(uniqueid);
		if (testpackage == null) {
			throw new NotFoundException("Could not find testpackage for TestpackageDaoImpl.getPublisher");
		}
		String publisher = testpackage.getPublisher();
		if (publisher == null) {
			throw new NotFoundException("Could not find publisher for TestpackageDaoImpl.getPublisher");
		}
		return publisher;
	}

	@Override
	public String getPublishdate(String uniqueid) {
		Testspecification testpackage = mapTestpackage.get(uniqueid);
		if (testpackage == null) {
			throw new NotFoundException("Could not find testpackage for TestpackageDaoImpl.getPublishdate");
		}
		String publishdate = testpackage.getPublishdate();
		if (publishdate == null) {
			throw new NotFoundException("Could not find publishdate for TestpackageDaoImpl.getPublishdate");
		}
		return publishdate;
	}

	@Override
	public String getVersion(String uniqueid) {
		Testspecification testpackage = mapTestpackage.get(uniqueid);
		if (testpackage == null) {
			throw new NotFoundException("Could not find testpackage for TestpackageDaoImpl.getVersion");
		}
		String version = testpackage.getVersion();
		if (version == null) {
			throw new NotFoundException("Could not find version for TestpackageDaoImpl.getVersion");
		}
		return version;
	}

	@Override
	public String getPurpose(String uniqueid) {
		Testspecification testpackage = mapTestpackage.get(uniqueid);
		if (testpackage == null) {
			throw new NotFoundException("Could not find testpackage for TestpackageDaoImpl.getPurpose");
		}
		String purpose = testpackage.getPurpose();
		if (purpose == null) {
			throw new NotFoundException("Could not find purpose for TestpackageDaoImpl.getPurpose");
		}
		return purpose;
	}

	@Override
	public Testblueprint getTestblueprint(String testpackageUniqueid) {
		Testspecification testpackage = mapTestpackage.get(testpackageUniqueid);
		if (testpackage == null) {
			throw new NotFoundException("Could not find testpackage for TestpackageDaoImpl.getTestblueprint");
		}
		Administration administration = getAdministration(testpackageUniqueid);
		if (administration == null) {
			throw new NotFoundException("Could not find administration for TestpackageDaoImpl.getTestblueprint");
		}
		Testblueprint testblueprint = administration.getTestblueprint();
		if (testblueprint == null) {
			throw new NotFoundException("Could not find testblueprint for TestpackageDaoImpl.getTestblueprint");
		}
		return testblueprint;
	}

	@Override
	public List<Poolproperty> getListPoolproperty(String testpackageUniqueid) {
		Testspecification testpackage = mapTestpackage.get(testpackageUniqueid);
		if (testpackage == null) {
			throw new NotFoundException("Could not find testpackage for TestpackageDaoImpl.getListPoolproperty");
		}
		Administration administration = getAdministration(testpackageUniqueid);
		if (administration == null) {
			throw new NotFoundException("Could not find administration for TestpackageDaoImpl.getListPoolproperty");
		}
		List<Poolproperty> listPoolproperty = administration.getPoolproperty();
		if (listPoolproperty == null) {
			throw new NotFoundException("Could not find listPoolproperty for TestpackageDaoImpl.getListPoolproperty");
		}
		return listPoolproperty;
	}

	@Override
	public Itempool getItempool(String testpackageUniqueid) {
		Testspecification testpackage = mapTestpackage.get(testpackageUniqueid);
		if (testpackage == null) {
			throw new NotFoundException("Could not find testpackage for TestpackageDaoImpl.getItempool");
		}
		Administration administration = getAdministration(testpackageUniqueid);
		if (administration == null) {
			throw new NotFoundException("Could not find administration for TestpackageDaoImpl.getItempool");
		}
		Itempool itempool = administration.getItempool();
		if (itempool == null) {
			throw new NotFoundException("Could not find itempool for TestpackageDaoImpl.getItempool");
		}
		return itempool;
	}

	@Override
	public List<Testform> getTestform(String testpackageUniqueid) {
		Testspecification testpackage = mapTestpackage.get(testpackageUniqueid);
		if (testpackage == null) {
			throw new NotFoundException("Could not find testpackage for TestpackageDaoImpl.getTestform");
		}
		Administration administration = getAdministration(testpackageUniqueid);
		if (administration == null) {
			throw new NotFoundException("Could not find administration for TestpackageDaoImpl.getTestform");
		}
		List<Testform> listTestform = administration.getTestform();
		if (listTestform == null) {
			throw new NotFoundException("Could not find listTestform for TestpackageDaoImpl.getTestform");
		}
		return listTestform;
	}

	@Override
	public List<Adminsegment> getAdminsegment(String testpackageUniqueid) {
		Testspecification testpackage = mapTestpackage.get(testpackageUniqueid);
		if (testpackage == null) {
			throw new NotFoundException("Could not find testpackage for TestpackageDaoImpl.getAdminsegment");
		}
		Administration administration = getAdministration(testpackageUniqueid);
		if (administration == null) {
			throw new NotFoundException("Could not find administration for TestpackageDaoImpl.getAdminsegment");
		}
		List<Adminsegment> listAdminsegment = administration.getAdminsegment();
		if (listAdminsegment == null) {
			throw new NotFoundException("Could not find listAdminsegment for TestpackageDaoImpl.getAdminsegment");
		}
		return listAdminsegment;
	}
}
