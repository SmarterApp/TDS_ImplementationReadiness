package org.cresst.sb.irp.service;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.dao.AnalysisDao;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
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
	public void analysisProcess(TDSReport tdsReport) {
		analysisDao.analysisProcess(tdsReport);
		//return analysisDao.
	}
	
	
	
	
}
