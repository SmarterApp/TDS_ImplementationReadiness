package org.cresst.sb.irp.rest;

import java.util.List;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.scoring.TDSReport;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.cresst.sb.irp.service.ScoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(value = "/scoring")
public class ScoringController {
	private static Logger logger = Logger.getLogger(ScoringController.class);

	@Autowired
	private ScoringService scoringService;
	
	@RequestMapping(value="/test", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public TDSReport.Test getTest(){
		return scoringService.getTest();
	}
	
	@RequestMapping(value="/examinee", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public TDSReport.Examinee getExaminee(){
		return scoringService.getExaminee();
	}
	
	@RequestMapping(value="/opportunity", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public TDSReport.Opportunity getOpportunity(){
		return scoringService.getOpportunity();
	}
	
	@RequestMapping(value="/opportunity/scores", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<TDSReport.Opportunity.Score> getOpportunityScores(){
		return scoringService.getOpportunityScores();
	}
	
	@RequestMapping(value="/opportunity/items", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<TDSReport.Opportunity.Item> getOpportunityItems(){
		return scoringService.getOpportunityItems();
	}
	
	@RequestMapping(value="/comments", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<TDSReport.Comment> getComments(){
		return scoringService.getComments();
	}
	
	@RequestMapping(value="/toolusage", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<TDSReport.ToolUsage> getToolUsage(){
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
