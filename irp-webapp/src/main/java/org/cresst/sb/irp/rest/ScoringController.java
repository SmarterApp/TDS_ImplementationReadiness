package org.cresst.sb.irp.rest;

import java.util.List;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.scoring.TDSReport;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.cresst.sb.irp.service.ScoringService;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.jsondoc.core.pojo.ApiVerb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Api(name = "Scoring API", description = "REST API for Scoring objects")
@Controller
@RequestMapping(value = "/scoring")
public class ScoringController {
	private static Logger logger = Logger.getLogger(ScoringController.class);

	@Autowired
	private ScoringService scoringService;

	@ApiMethod(path = "scoring/test", description = "Returns the test", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value="/test", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject TDSReport.Test getTest(){
		return scoringService.getTest();
	}

	@ApiMethod(path = "scoring/examinee", description = "Returns the examinee", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value="/examinee", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject TDSReport.Examinee getExaminee(){
		return scoringService.getExaminee();
	}

	@ApiMethod(path = "scoring/opportunity", description = "Returns the scoring opportunity", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value="/opportunity", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject TDSReport.Opportunity getOpportunity(){
		return scoringService.getOpportunity();
	}

	@ApiMethod(path = "scoring/opportunity/scores", description = "Returns the opportunity's scores", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value="/opportunity/scores", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject List<TDSReport.Opportunity.Score> getOpportunityScores(){
		return scoringService.getOpportunityScores();
	}

	@ApiMethod(path = "scoring/opportunity/items", description = "Returns the opportunity's items", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value="/opportunity/items", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject List<TDSReport.Opportunity.Item> getOpportunityItems(){
		return scoringService.getOpportunityItems();
	}

	@ApiMethod(path = "scoring/comments", description = "Returns the scoring comments", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value="/comments", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject List<TDSReport.Comment> getComments(){
		return scoringService.getComments();
	}

	@ApiMethod(path = "scoring/toolusage", description = "Returns the scoring tool usage", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value="/toolusage", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject List<TDSReport.ToolUsage> getToolUsage(){
		return scoringService.getToolUsage();
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
