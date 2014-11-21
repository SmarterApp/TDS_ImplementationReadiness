package org.cresst.sb.irp.rest;

import java.util.List;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.cresst.sb.irp.service.TDSReportService;
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

@Api(name = "TDSReport API", description = "REST API for TDSReport objects")
@Controller
@RequestMapping(value = "/tdsreport")
public class TDSReportController {
	private static Logger logger = Logger.getLogger(TDSReportController.class);

	@Autowired
	private TDSReportService tDSReportService;

	@ApiMethod(path = "tdsreport/test", description = "Returns the test", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value="/test", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject TDSReport.Test getTest(){
		return tDSReportService.getTest();
	}

	@ApiMethod(path = "tdsreport/examinee", description = "Returns the examinee", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value="/examinee", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject TDSReport.Examinee getExaminee(){
		return tDSReportService.getExaminee();
	}

	@ApiMethod(path = "tdsreport/opportunity", description = "Returns the tdsreport opportunity", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value="/opportunity", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject TDSReport.Opportunity getOpportunity(){
		return tDSReportService.getOpportunity();
	}

	@ApiMethod(path = "tdsreport/opportunity/scores", description = "Returns the opportunity's scores", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value="/opportunity/scores", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject List<TDSReport.Opportunity.Score> getOpportunityScores(){
		return tDSReportService.getOpportunityScores();
	}

	@ApiMethod(path = "tdsreport/opportunity/items", description = "Returns the opportunity's items", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value="/opportunity/items", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject List<TDSReport.Opportunity.Item> getOpportunityItems(){
		return tDSReportService.getOpportunityItems();
	}

	@ApiMethod(path = "tdsreport/comments", description = "Returns the tdsreport comments", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value="/comments", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject List<TDSReport.Comment> getComments(){
		return tDSReportService.getComments();
	}

	@ApiMethod(path = "tdsreport/toolusage", description = "Returns the tdsreport tool usage", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value="/toolusage", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject List<TDSReport.ToolUsage> getToolUsage(){
		return tDSReportService.getToolUsage();
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
