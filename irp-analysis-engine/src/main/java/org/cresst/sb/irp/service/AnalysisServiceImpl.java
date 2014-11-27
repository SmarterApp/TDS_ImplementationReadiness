package org.cresst.sb.irp.service;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.dao.AnalysisDao;
import org.cresst.sb.irp.domain.analysis.AnalysisResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnalysisServiceImpl implements AnalysisService {
	private static Logger logger = Logger.getLogger(AnalysisServiceImpl.class);
	
	@Autowired
	private AnalysisDao analysisDao;
	
	public AnalysisServiceImpl(){
		logger.info("initializing");
	}

	@Override
	public void analysisProcess(AnalysisResponse analysisResponse) {
		analysisDao.analysisProcess(analysisResponse);
		
	}

	/*@Override
	public void analysisProcess(TDSReport tdsReport, List analysisResponseList) {
		analysisDao.analysisProcess(tdsReport, analysisResponseList);
		//return analysisDao.
	}*/
	
	
	
	
}
