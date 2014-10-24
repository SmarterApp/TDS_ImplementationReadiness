package org.readiness.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/pages")
public class ApiPagesController {
	
	@RequestMapping(params = "fdAction=manifestapi", method = RequestMethod.GET)
	public ModelAndView getManifestAPI() {
		return new ModelAndView("manifestAPI");
	}

	@RequestMapping(params = "fdAction=resourcesIdentifier", method = RequestMethod.GET)
	public ModelAndView getResourcesIdentifierPage() {
		return new ModelAndView("resourcesIdentifier");
	}
	
	@RequestMapping(params = "fdAction=studentsapi", method = RequestMethod.GET)
	public ModelAndView getStudentsAPI() {
		return new ModelAndView("studentsAPI");
	}
	
	@RequestMapping(params = "fdAction=scoringapi", method = RequestMethod.GET)
	public ModelAndView getScoringAPI() {
		return new ModelAndView("scoringAPI");
	}
	
	@RequestMapping(params = "fdAction=itemsapi", method = RequestMethod.GET)
	public ModelAndView getItemAPI() {
		return new ModelAndView("itemAPI");
	}

	@RequestMapping(params = "fdAction=itemIDs", method = RequestMethod.GET)
	public ModelAndView getItemIDsPage() {
		return new ModelAndView("itemIDs");
	}
	
	@RequestMapping(params = "fdAction=attidIDs", method = RequestMethod.GET)
	public ModelAndView getAttidIDsPage() {
		return new ModelAndView("attidIDs");
	}
	
	@RequestMapping(params = "fdAction=studentIdentifier", method = RequestMethod.GET)
	public ModelAndView getStudentIdentifierPage() {
		return new ModelAndView("studentIdentifier");
	}

}
