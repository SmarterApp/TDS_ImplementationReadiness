package org.readiness.rest;

import java.util.List;

import org.apache.log4j.Logger;
import org.readiness.exceptions.NotFoundException;
import org.readiness.scoring.domain.TDSReport.Comment;
import org.readiness.scoring.domain.TDSReport.Examinee;
import org.readiness.scoring.domain.TDSReport.Opportunity;
import org.readiness.scoring.domain.TDSReport.Opportunity.Item;
import org.readiness.scoring.domain.TDSReport.Opportunity.Score;
import org.readiness.scoring.domain.TDSReport.Test;
import org.readiness.scoring.domain.TDSReport.ToolUsage;
import org.readiness.service.ScoringService;
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
	public Test getTest(){
		logger.info("getTest() method");
		return scoringService.getTest();
	}
	
	@RequestMapping(value="/examinee", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Examinee getExaminee(){
		logger.info("getExaminee() method");
		return scoringService.getExaminee();
	}
	
	@RequestMapping(value="/opportunity", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Opportunity getOpportunity(){
		logger.info("getOpportunity() method");
		return scoringService.getOpportunity();
	}
	
	@RequestMapping(value="/opportunity/scores", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<Score> getOpportunityScores(){
		logger.info("getOpportunityScores() method");
		return scoringService.getOpportunityScores();
	}
	
	@RequestMapping(value="/opportunity/items", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<Item> getOpportunityItems(){
		logger.info("getOpportunityItems() method");
		return scoringService.getOpportunityItems();
	}
	
	@RequestMapping(value="/comments", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<Comment> getComments(){
		logger.info("getComments() method");
		return scoringService.getComments();
	}
	
	@RequestMapping(value="/toolusage", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<ToolUsage> getToolUsage(){
		logger.info("getToolUsage() method");
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
