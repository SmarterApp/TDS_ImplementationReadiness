package org.cresst.sb.irp.dao;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.analysis.AccommodationCategory;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.OpportunityCategory;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Accommodation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class AccommodationAnalysisAction extends AnalysisAction {
	private static Logger logger = Logger.getLogger(AccommodationAnalysisAction.class);

	@Override
	public void analysis() throws IOException {
		try {
			IndividualResponse individualResponse = getIndividualResponse();
			TDSReport tdsReport = individualResponse.getTDSReport();
			OpportunityCategory opportunityCategory = individualResponse.getOpportunityCategory();
			List<AccommodationCategory> listAccommodationCategory = opportunityCategory.getListAccommodationCategory();
			
			AccommodationCategory accommodationCategory;
			Opportunity opportunity = getOpportunity(tdsReport);
			List<Accommodation> listAccommodation = opportunity.getAccommodation();
			for (Accommodation a : listAccommodation) {
				accommodationCategory = new AccommodationCategory();
				listAccommodationCategory.add(accommodationCategory);
				analysisEachAccommodation(accommodationCategory, a);
			}
			
		} catch (Exception e) {
			logger.error("analysis exception: ", e);
		}
	}

	private void analysisEachAccommodation(AccommodationCategory accommodationCategory, Accommodation accommodation){
		try {
			
		} catch (Exception e) {
			logger.error("analysisEachSegment exception: ", e);
		}
	}
	
}
