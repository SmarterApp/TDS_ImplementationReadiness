package org.cresst.sb.irp.rest;

import java.util.List;

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
import org.cresst.sb.irp.service.TestPackageService;
import org.jsondoc.core.annotation.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Api(name = "Test Package API", description = "REST API for Test Packages")
@Controller
public class TestPackageController {
	private final static Logger logger = LoggerFactory.getLogger(TestPackageController.class);
	
	@Autowired
	public TestPackageService testPackageService;
	
	@RequestMapping(value="/testpackage/{uniqueid}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Testspecification getTestpackage(@PathVariable("uniqueid") String uniqueid){
		logger.info("getTestpackage() method");
		return testPackageService.getTestpackage(uniqueid);
	}
	
	@RequestMapping(value="/testpackage/{uniqueid}/identifier", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Identifier getIdentifier(@PathVariable("uniqueid") String uniqueid){
		logger.info("getIdentifier() method");
		return testPackageService.getIdentifier(uniqueid);
	}
	
	@RequestMapping(value="/testpackage/{uniqueid}/properties", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<Property> getListProperty(@PathVariable("uniqueid") String uniqueid){
		logger.info("getListProperty() method");
		return testPackageService.getListProperty(uniqueid);
	}
	
	@RequestMapping(value="/testpackage/{uniqueid}/administration", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Administration getAdministration(@PathVariable("uniqueid") String uniqueid){
		logger.info("getAdministration() method");
		return testPackageService.getAdministration(uniqueid);
	}
	
	@RequestMapping(value="/testpackage/{uniqueid}/publisher", method = RequestMethod.GET, produces="text/plain")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public String getPublisher(@PathVariable("uniqueid") String uniqueid){
		logger.info("getPublisher() method");
		return testPackageService.getPublisher(uniqueid);
	}
	
	@RequestMapping(value="/testpackage/{uniqueid}/publishdate", method = RequestMethod.GET, produces="text/plain")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public String getPublishdate(@PathVariable("uniqueid") String uniqueid){
		logger.info("getPublishdate() method");
		return testPackageService.getPublishdate(uniqueid);
	}
	
	@RequestMapping(value="/testpackage/{uniqueid}/version", method = RequestMethod.GET, produces="text/plain")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public String getVersion(@PathVariable("uniqueid") String uniqueid){
		logger.info("getVersion() method");
		return testPackageService.getVersion(uniqueid);
	}
	
	@RequestMapping(value="/testpackage/{uniqueid}/purpose", method = RequestMethod.GET, produces="text/plain")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public String getPurpose(@PathVariable("uniqueid") String uniqueid){
		logger.info("getPurpose() method");
		return testPackageService.getPurpose(uniqueid);
	}
	
	@RequestMapping(value="/testpackage/{uniqueid}/administration/testblueprint", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Testblueprint getTestblueprint(@PathVariable("uniqueid") String uniqueid){
		logger.info("getTestblueprint() method");
		return testPackageService.getTestblueprint(uniqueid);
	}
	
	@RequestMapping(value="/testpackage/{uniqueid}/administration/poolproperties", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<Poolproperty> getListPoolproperty(@PathVariable("uniqueid") String uniqueid){
		logger.info("getListPoolproperty() method");
		return testPackageService.getListPoolproperty(uniqueid);
	}
	
	@RequestMapping(value="/testpackage/{uniqueid}/administration/itempool", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Itempool getItempool(@PathVariable("uniqueid") String uniqueid){
		logger.info("getItempool() method");
		return testPackageService.getItempool(uniqueid);
	}
	
	@RequestMapping(value="/testpackage/{uniqueid}/administration/testform", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<Testform> getTestform(@PathVariable("uniqueid") String uniqueid){
		logger.info("getTestform() method");
		return testPackageService.getTestform(uniqueid);
	}
	
	@RequestMapping(value="/testpackage/{uniqueid}/administration/adminsegment", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<Adminsegment> getAdminsegment(@PathVariable("uniqueid") String uniqueid){
		logger.info("getAdminsegment() method");
		return testPackageService.getAdminsegment(uniqueid);
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
