package org.cresst.sb.irp.domain.analysis;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class AnalysisResponse {
	private static Logger logger = Logger.getLogger(AnalysisResponse.class);

	//each IndividualResponse represents each TDSReport xml file uploaded 
	//and it includes all analysis results  
	private List<IndividualResponse> listIndividualResponse;

	public AnalysisResponse() {
		logger.info("initializing");
		listIndividualResponse = new ArrayList<IndividualResponse>();
	}

	public List<IndividualResponse> getListIndividualResponse() {
		return listIndividualResponse;
	}

	public void setListIndividualResponse(List<IndividualResponse> listIndividualResponse) {
		this.listIndividualResponse = listIndividualResponse;
	}
	
	public void addListIndividualResponse(IndividualResponse individualResponse){
		this.listIndividualResponse.add(individualResponse);
	}
}
