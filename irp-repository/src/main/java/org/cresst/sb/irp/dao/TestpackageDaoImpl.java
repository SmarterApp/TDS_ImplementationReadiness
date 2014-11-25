package org.cresst.sb.irp.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.testpackage.Administration;
import org.cresst.sb.irp.domain.testpackage.Adminsegment;
import org.cresst.sb.irp.domain.testpackage.Identifier;
import org.cresst.sb.irp.domain.testpackage.Itempool;
import org.cresst.sb.irp.domain.testpackage.ObjectFactory;
import org.cresst.sb.irp.domain.testpackage.Poolproperty;
import org.cresst.sb.irp.domain.testpackage.Property;
import org.cresst.sb.irp.domain.testpackage.Testblueprint;
import org.cresst.sb.irp.domain.testpackage.Testform;
import org.cresst.sb.irp.domain.testpackage.Testpackage;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.cresst.sb.irp.utils.RetrieveFileUtil;
import org.cresst.sb.irp.utils.XMLValidate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class TestpackageDaoImpl implements TestpackageDao, InitializingBean {
	private static Logger logger = Logger.getLogger(TestpackageDaoImpl.class);
	private Map<String, Testpackage> mapTestpackage = new HashMap<String, Testpackage>();

	//@Value("classpath:irp-package/TestPackages/Practice-ELA-11-Spring-2013-2015.xml")
	//private Resource testPackageAdminMathResource;
	
	@Value("classpath:irp-package/TestPackages")
	private Resource testPackagePath;

	@Value("classpath:irp-package/testpackageAdmin.xsd")
	private Resource testPackageAdminResource;

	@Autowired
	private XMLValidate xMLValidate;
	
	@Autowired
	private RetrieveFileUtil retrieveFileUtil;

	public TestpackageDaoImpl() {
		logger.info("initializing");
	}

	@Override
	public Testpackage getTestpackage(String uniqueid) {
		Testpackage testpackage = mapTestpackage.get(uniqueid);
		if (testpackage == null) {
			throw new NotFoundException(
					"Could not find testpackage for TestpackageDaoImpl.getTestpackage");
		}
		return testpackage;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			// TODO Auto-generated method stub
			/******
			 * there is a problem to validate xml vs xsd. need to be check it
			 * out
			 *****/
			// boolean bln = xMLValidate.validateXMLSchema(xsdPath, xmlPath);
			
			retrieveFileUtil.walk(testPackagePath.getURI().getPath(), mapTestpackage);
			
			/*JAXBContext ctx = JAXBContext.newInstance(ObjectFactory.class);
			Unmarshaller unmarshaller = ctx.createUnmarshaller();
			
			Testpackage testpackage = (Testpackage) unmarshaller
					.unmarshal(testPackageAdminMathResource.getInputStream());
			String uniqueid = testpackage.getIdentifier().getUniqueid();
			mapTestpackage.put(uniqueid, testpackage);*/
			
		} catch (Exception e) {
			logger.error("afterPropertiesSet exception: ", e);
		}
	}

	@Override
	public Identifier getIdentifier(String testpackageUniqueid) {
		Testpackage testpackage = mapTestpackage.get(testpackageUniqueid);
		if (testpackage == null) {
			throw new NotFoundException(
					"Could not find testpackage for TestpackageDaoImpl.getIdentifier");
		}
		Identifier identifier = testpackage.getIdentifier();
		if (identifier == null) {
			throw new NotFoundException(
					"Could not find Identifier for TestpackageDaoImpl.getIdentifier");
		}
		return identifier;
	}

	@Override
	public List<Property> getListProperty(String testpackageUniqueid) {
		Testpackage testpackage = mapTestpackage.get(testpackageUniqueid);
		if (testpackage == null) {
			throw new NotFoundException(
					"Could not find testpackage for TestpackageDaoImpl.getListProperty");
		}
		List<Property> listProperty = testpackage.getProperty();
		if (listProperty == null) {
			throw new NotFoundException(
					"Could not find listProperty for TestpackageDaoImpl.getListProperty");
		}
		return listProperty;
	}

	@Override
	public Administration getAdministration(String testpackageUniqueid) {
		Testpackage testpackage = mapTestpackage.get(testpackageUniqueid);
		if (testpackage == null) {
			throw new NotFoundException(
					"Could not find testpackage for TestpackageDaoImpl.getAdministration");
		}
		Administration administration = testpackage.getAdministration();
		if (administration == null) {
			throw new NotFoundException(
					"Could not find administration for TestpackageDaoImpl.getAdministration");
		}
		return administration;
	}

	@Override
	public String getPublisher(String uniqueid) {
		Testpackage testpackage = mapTestpackage.get(uniqueid);
		if (testpackage == null) {
			throw new NotFoundException(
					"Could not find testpackage for TestpackageDaoImpl.getPublisher");
		}
		String publisher = testpackage.getPublisher();
		if (publisher == null) {
			throw new NotFoundException(
					"Could not find publisher for TestpackageDaoImpl.getPublisher");
		}
		return publisher;
	}

	@Override
	public String getPublishdate(String uniqueid) {
		Testpackage testpackage = mapTestpackage.get(uniqueid);
		if (testpackage == null) {
			throw new NotFoundException(
					"Could not find testpackage for TestpackageDaoImpl.getPublishdate");
		}
		String publishdate = testpackage.getPublishdate();
		if (publishdate == null) {
			throw new NotFoundException(
					"Could not find publishdate for TestpackageDaoImpl.getPublishdate");
		}
		return publishdate;
	}

	@Override
	public String getVersion(String uniqueid) {
		Testpackage testpackage = mapTestpackage.get(uniqueid);
		if (testpackage == null) {
			throw new NotFoundException(
					"Could not find testpackage for TestpackageDaoImpl.getVersion");
		}
		String version = testpackage.getVersion();
		if (version == null) {
			throw new NotFoundException(
					"Could not find version for TestpackageDaoImpl.getVersion");
		}
		return version;
	}

	@Override
	public String getPurpose(String uniqueid) {
		Testpackage testpackage = mapTestpackage.get(uniqueid);
		if (testpackage == null) {
			throw new NotFoundException(
					"Could not find testpackage for TestpackageDaoImpl.getPurpose");
		}
		String purpose = testpackage.getPurpose();
		if (purpose == null) {
			throw new NotFoundException(
					"Could not find purpose for TestpackageDaoImpl.getPurpose");
		}
		return purpose;
	}

	@Override
	public Testblueprint getTestblueprint(String testpackageUniqueid) {
		Testpackage testpackage = mapTestpackage.get(testpackageUniqueid);
		if (testpackage == null) {
			throw new NotFoundException(
					"Could not find testpackage for TestpackageDaoImpl.getTestblueprint");
		}
		Administration administration = testpackage.getAdministration();
		if (administration == null) {
			throw new NotFoundException(
					"Could not find administration for TestpackageDaoImpl.getTestblueprint");
		}
		Testblueprint testblueprint = administration.getTestblueprint();
		if (testblueprint == null) {
			throw new NotFoundException(
					"Could not find testblueprint for TestpackageDaoImpl.getTestblueprint");
		}
		return testblueprint;
	}

	@Override
	public List<Poolproperty> getListPoolproperty(String testpackageUniqueid) {
		Testpackage testpackage = mapTestpackage.get(testpackageUniqueid);
		if (testpackage == null) {
			throw new NotFoundException(
					"Could not find testpackage for TestpackageDaoImpl.getListPoolproperty");
		}
		Administration administration = testpackage.getAdministration();
		if (administration == null) {
			throw new NotFoundException(
					"Could not find administration for TestpackageDaoImpl.getListPoolproperty");
		}
		List<Poolproperty> listPoolproperty = administration.getPoolproperty();
		if (listPoolproperty == null) {
			throw new NotFoundException(
					"Could not find listPoolproperty for TestpackageDaoImpl.getListPoolproperty");
		}
		return listPoolproperty;
	}

	@Override
	public Itempool getItempool(String testpackageUniqueid) {
		Testpackage testpackage = mapTestpackage.get(testpackageUniqueid);
		if (testpackage == null) {
			throw new NotFoundException(
					"Could not find testpackage for TestpackageDaoImpl.getItempool");
		}
		Administration administration = testpackage.getAdministration();
		if (administration == null) {
			throw new NotFoundException(
					"Could not find administration for TestpackageDaoImpl.getItempool");
		}
		Itempool itempool = administration.getItempool();
		if (itempool == null) {
			throw new NotFoundException(
					"Could not find itempool for TestpackageDaoImpl.getItempool");
		}
		return itempool;
	}

	@Override
	public List<Testform> getTestform(String testpackageUniqueid) {
		Testpackage testpackage = mapTestpackage.get(testpackageUniqueid);
		if (testpackage == null) {
			throw new NotFoundException(
					"Could not find testpackage for TestpackageDaoImpl.getTestform");
		}
		Administration administration = testpackage.getAdministration();
		if (administration == null) {
			throw new NotFoundException(
					"Could not find administration for TestpackageDaoImpl.getTestform");
		}
		List<Testform> listTestform = administration.getTestform();
		if (listTestform == null) {
			throw new NotFoundException(
					"Could not find listTestform for TestpackageDaoImpl.getTestform");
		}
		return listTestform;
	}

	@Override
	public List<Adminsegment> getAdminsegment(String testpackageUniqueid) {
		Testpackage testpackage = mapTestpackage.get(testpackageUniqueid);
		if (testpackage == null) {
			throw new NotFoundException(
					"Could not find testpackage for TestpackageDaoImpl.getAdminsegment");
		}
		Administration administration = testpackage.getAdministration();
		if (administration == null) {
			throw new NotFoundException(
					"Could not find administration for TestpackageDaoImpl.getAdminsegment");
		}
		List<Adminsegment> listAdminsegment = administration.getAdminsegment();
		if (listAdminsegment == null) {
			throw new NotFoundException(
					"Could not find listAdminsegment for TestpackageDaoImpl.getAdminsegment");
		}
		return listAdminsegment;
	}

}
