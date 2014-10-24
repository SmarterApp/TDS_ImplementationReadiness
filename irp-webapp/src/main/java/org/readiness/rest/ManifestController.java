package org.readiness.rest;

import java.util.List;

import org.apache.log4j.Logger;
import org.readiness.exceptions.NotFoundException;
import org.readiness.manifest.domain.Manifest;
import org.readiness.manifest.domain.Manifest.Metadata;
import org.readiness.manifest.domain.Manifest.Resources;
import org.readiness.manifest.domain.Manifest.Resources.Resource;
import org.readiness.service.ManifestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@Controller
public class ManifestController {
	private static Logger logger = Logger.getLogger(ManifestController.class);
	
	@Autowired
	private ManifestService manifestService;
	
	@RequestMapping(value="/manifest", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<Manifest> getManifest() {
		logger.info("getManifest() method");
		return new ResponseEntity<Manifest>(
				manifestService.getManifest(), HttpStatus.OK);
	}
	
	@RequestMapping(value="/manifest/resources", method = RequestMethod.GET, produces="application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public HttpEntity<List<Resources>> getResources() {
		logger.info("getResources() method");
		return new HttpEntity<List<Resources>>(manifestService.getResources());
	}
	
	@RequestMapping(value="/manifest/resources/{identifier}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Resource getResource(@PathVariable("identifier") String identifier) {
		logger.info("getResource() method with identifier " + identifier);
		return manifestService.getResource(identifier);
	}
	
	@RequestMapping(value="/manifest/metadata", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Metadata getMetadata() {
		logger.info("getMetadata() method");
		return manifestService.getMetadata();
	}
	
	@RequestMapping(value="/manifest/organizations", method = RequestMethod.GET, produces="text/plain")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public String getOrganizations(){
		logger.info("getOrganizations() method");
		return manifestService.getOrganizations();
	}
	
	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
	/* . . . . . . . . . . . . . EXCEPTION HANDLERS . . . . . . . . . . . . .. */
	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
	
	@ExceptionHandler
	@ResponseBody 
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public String handleException(Exception ex) {
		logger.info("handleException: " + ex.getMessage());
		return ex.getMessage();
	}
	
	@ExceptionHandler(NotFoundException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Not Found")
	public String handleNotFoundException(NotFoundException ex) {
		logger.info("NotFoundException: " + ex.getMessage());
		return ex.getMessage();
	}
	
}
