package org.cresst.sb.irp.domain.analysis;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class AnalysisResponse {
	private static Logger logger = Logger.getLogger(AnalysisResponse.class);

	//each IndividualResponse represents each TDSReport xml file uploaded 
	//and it includes all analyze results
	private List<IndividualResponse> individualResponses;

	public AnalysisResponse() {
		logger.info("initializing");
		individualResponses = new ArrayList<IndividualResponse>();
	}

	public List<IndividualResponse> getIndividualResponses() {
		return individualResponses;
	}

	public void setIndividualResponses(List<IndividualResponse> individualResponses) {
		this.individualResponses = individualResponses;
	}
	
	public void addListIndividualResponse(IndividualResponse individualResponse){
		this.individualResponses.add(individualResponse);
	}
}
