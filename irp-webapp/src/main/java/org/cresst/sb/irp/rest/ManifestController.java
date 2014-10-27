package org.cresst.sb.irp.rest;

import java.util.List;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.manifest.Manifest;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.cresst.sb.irp.service.ManifestService;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiParam;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.jsondoc.core.pojo.ApiParamType;
import org.jsondoc.core.pojo.ApiVerb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@Api(name = "Manifest API", description = "REST API for the Manifest")
@Controller
public class ManifestController {
	private static Logger logger = Logger.getLogger(ManifestController.class);
	
	@Autowired
	private ManifestService manifestService;

	@ApiMethod(path = "manifest", description = "Returns a list of metadata, organizations, resources", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value="/manifest", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject Manifest getManifest() {
		return manifestService.getManifest();
	}

	@ApiMethod(path = "manifest/resources", description = "Returns a list of Resources", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value="/manifest/resources", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject List<Manifest.Resources> getResources() {
		return manifestService.getResources();
	}

	@ApiMethod(path = "manifest/resources/{identifier}", description = "Returns a Resources", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value="/manifest/resources/{identifier}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject Manifest.Resources.Resource getResource(
			@ApiParam(name = "identifier", description = "Manifest resource's ID", paramType = ApiParamType.PATH)
			@PathVariable("identifier") String identifier) {
		return manifestService.getResource(identifier);
	}

	@ApiMethod(path = "manifest/metadata", description = "Returns a manifest metadata", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value="/manifest/metadata", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject Manifest.Metadata getMetadata() {
		return manifestService.getMetadata();
	}

	@ApiMethod(path = "manifest/organizations", description = "Returns the organizations", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value="/manifest/organizations", method = RequestMethod.GET, produces="text/plain")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject String getOrganizations(){
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
