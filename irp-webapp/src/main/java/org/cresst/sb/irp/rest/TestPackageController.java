package org.cresst.sb.irp.rest;

import java.util.List;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.testpackage.Administration;
import org.cresst.sb.irp.domain.testpackage.Adminsegment;
import org.cresst.sb.irp.domain.testpackage.Identifier;
import org.cresst.sb.irp.domain.testpackage.Itempool;
import org.cresst.sb.irp.domain.testpackage.Poolproperty;
import org.cresst.sb.irp.domain.testpackage.Property;
import org.cresst.sb.irp.domain.testpackage.Testblueprint;
import org.cresst.sb.irp.domain.testpackage.Testform;
import org.cresst.sb.irp.domain.testpackage.Testpackage;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.cresst.sb.irp.service.TestPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@Controller
public class TestPackageController {
	private static Logger logger = Logger.getLogger(TestPackageController.class);
	
	@Autowired
	public TestPackageService testPackageService;
	
	@RequestMapping(value="/testpackage", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Testpackage getTestpackage(){
		logger.info("getTestpackage() method");
		return testPackageService.getTestpackage();
	}
	
	@RequestMapping(value="/testpackage/identifier", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Identifier getIdentifier(){
		logger.info("getIdentifier() method");
		return testPackageService.getIdentifier();
	}
	
	@RequestMapping(value="/testpackage/properties", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<Property> getListProperty(){
		logger.info("getListProperty() method");
		return testPackageService.getListProperty();
	}
	
	@RequestMapping(value="/testpackage/administration", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Administration getAdministration(){
		logger.info("getAdministration() method");
		return testPackageService.getAdministration();
	}
	
	@RequestMapping(value="/testpackage/publisher", method = RequestMethod.GET, produces="text/plain")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public String getPublisher(){
		logger.info("getPublisher() method");
		return testPackageService.getPublisher();
	}
	
	@RequestMapping(value="/testpackage/publishdate", method = RequestMethod.GET, produces="text/plain")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public String getPublishdate(){
		logger.info("getPublishdate() method");
		return testPackageService.getPublishdate();
	}
	
	@RequestMapping(value="/testpackage/version", method = RequestMethod.GET, produces="text/plain")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public String getVersion(){
		logger.info("getVersion() method");
		return testPackageService.getVersion();
	}
	
	@RequestMapping(value="/testpackage/purpose", method = RequestMethod.GET, produces="text/plain")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public String getPurpose(){
		logger.info("getPurpose() method");
		return testPackageService.getPurpose();
	}
	
	@RequestMapping(value="/testpackage/testblueprint", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Testblueprint getTestblueprint(){
		logger.info("getTestblueprint() method");
		return testPackageService.getTestblueprint();
	}
	
	@RequestMapping(value="/testpackage/poolproperties", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<Poolproperty> getListPoolproperty(){
		logger.info("getListPoolproperty() method");
		return testPackageService.getListPoolproperty();
	}
	
	@RequestMapping(value="/testpackage/itempool", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Itempool getItempool(){
		logger.info("getItempool() method");
		return testPackageService.getItempool();
	}
	
	@RequestMapping(value="/testpackage/testform", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<Testform> getTestform(){
		logger.info("getTestform() method");
		return testPackageService.getTestform();
	}
	
	@RequestMapping(value="/testpackage/adminsegment", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<Adminsegment> getAdminsegment(){
		logger.info("getAdminsegment() method");
		return testPackageService.getAdminsegment();
	}
	
	
	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
	/* . . . . . . . . . . . . . EXCEPTION HANDLERS . . . . . . . . . . . . .. */
	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	@ExceptionHandler(NotFoundException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Not Found")
	public String handleNotFoundException(NotFoundException ex) {
		logger.info("NotFoundException: " + ex.getMessage());
		return ex.getMessage();
	}

}
