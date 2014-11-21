package org.cresst.sb.irp.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
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
import org.cresst.sb.irp.utils.XMLValidate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TestpackageDaoImpl implements TestpackageDao, InitializingBean {
	private static Logger logger = Logger.getLogger(TestpackageDaoImpl.class);
	private String rootResourceFolderName = "SampleAssessmentItemPackage";
	private String testpackageAdminMathFileName = "testpackageAdminMath.xml";
	private String testpackageAdminXSDFileName = "testpackageAdmin.xsd";
	private Testpackage testpackage; 
	
	//use this list if parse multiple testpackage xml files. e.g: Math, English
	//the following methods need to be modified too to make sure which testpackage object required
	private List<Testpackage> listTestpackage = new ArrayList<Testpackage>(); 
	
	@Autowired
	private XMLValidate xMLValidate;

	public TestpackageDaoImpl() {
		logger.info("initializing");
	}

	@Override
	public Testpackage getTestpackage() {
		return testpackage;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			String xsdPath = rootResourceFolderName + "/"
					+ testpackageAdminXSDFileName;
			String xmlPath = rootResourceFolderName + "/"
					+ testpackageAdminMathFileName;
			// TODO Auto-generated method stub
			/****** there is a problem to validate xml vs xsd. need to be check it out *****/
			//boolean bln = xMLValidate.validateXMLSchema(xsdPath, xmlPath);
			
			JAXBContext ctx = JAXBContext
					.newInstance(ObjectFactory.class);
			Unmarshaller unmarshaller = ctx.createUnmarshaller();
			testpackage = (Testpackage)unmarshaller.unmarshal(new File(
					rootResourceFolderName + "/" + testpackageAdminMathFileName));
			
		} catch (Exception e) {
			logger.error("afterPropertiesSet exception: ", e);
		}
	}

	@Override
	public Identifier getIdentifier() {
		Identifier identifier = testpackage.getIdentifier();
		if (identifier == null){
			throw new NotFoundException("Could not find Identifier for TestpackageDaoImpl.getIdentifier");
		}
		return identifier;
	}

	@Override
	public List<Property> getListProperty() {
		List<Property> listProperty = testpackage.getProperty();
		if (listProperty == null){
			throw new NotFoundException("Could not find listProperty for TestpackageDaoImpl.getListProperty");
		}
		return listProperty;
	}

	@Override
	public Administration getAdministration() {
		Administration administration = testpackage.getAdministration();
		if (administration == null){
			throw new NotFoundException("Could not find administration for TestpackageDaoImpl.getAdministration");
		}
		return administration;
	}

	@Override
	public String getPublisher() {
		String publisher = testpackage.getPublisher();
		if (publisher == null){
			throw new NotFoundException("Could not find publisher for TestpackageDaoImpl.getPublisher");
		}
		return publisher;
	}

	@Override
	public String getPublishdate() {
		String publishdate = testpackage.getPublishdate();
		if (publishdate == null){
			throw new NotFoundException("Could not find publishdate for TestpackageDaoImpl.getPublishdate");
		}
		return publishdate;
	}

	@Override
	public String getVersion() {
		String version = testpackage.getVersion();
		if (version == null){
			throw new NotFoundException("Could not find version for TestpackageDaoImpl.getVersion");
		}
		return version;
	}

	@Override
	public String getPurpose() {
		String purpose = testpackage.getVersion();
		if (purpose == null){
			throw new NotFoundException("Could not find purpose for TestpackageDaoImpl.getPurpose");
		}
		return purpose;
	}

	@Override
	public Testblueprint getTestblueprint() {
		Administration administration = testpackage.getAdministration();
		if (administration == null){
			throw new NotFoundException("Could not find administration for TestpackageDaoImpl.getTestblueprint");
		}
		Testblueprint testblueprint = administration.getTestblueprint();
		if (testblueprint == null){
			throw new NotFoundException("Could not find testblueprint for TestpackageDaoImpl.getTestblueprint");
		}
		return testblueprint;
	}

	@Override
	public List<Poolproperty> getListPoolproperty() {
		Administration administration = testpackage.getAdministration();
		if (administration == null){
			throw new NotFoundException("Could not find administration for TestpackageDaoImpl.getListPoolproperty");
		}
		List<Poolproperty> listPoolproperty = administration.getPoolproperty();
		if (listPoolproperty == null){
			throw new NotFoundException("Could not find listPoolproperty for TestpackageDaoImpl.getListPoolproperty");
		}
		return listPoolproperty;
	}

	@Override
	public Itempool getItempool() {
		Administration administration = testpackage.getAdministration();
		if (administration == null){
			throw new NotFoundException("Could not find administration for TestpackageDaoImpl.getItempool");
		}
		Itempool itempool = administration.getItempool();
		if (itempool == null){
			throw new NotFoundException("Could not find itempool for TestpackageDaoImpl.getItempool");
		}
		return itempool;
	}

	@Override
	public List<Testform> getTestform() {
		Administration administration = testpackage.getAdministration();
		if (administration == null){
			throw new NotFoundException("Could not find administration for TestpackageDaoImpl.getTestform");
		}
		List<Testform> listTestform = administration.getTestform();
		if (listTestform == null){
			throw new NotFoundException("Could not find listTestform for TestpackageDaoImpl.getTestform");
		}
		return listTestform;
	}

	@Override
	public List<Adminsegment> getAdminsegment() {
		Administration administration = testpackage.getAdministration();
		if (administration == null){
			throw new NotFoundException("Could not find administration for TestpackageDaoImpl.getAdminsegment");
		}
		List<Adminsegment> listAdminsegment = administration.getAdminsegment();
		if (listAdminsegment == null){
			throw new NotFoundException("Could not find listAdminsegment for TestpackageDaoImpl.getAdminsegment");
		}
		return listAdminsegment;
	}
	
	

}
