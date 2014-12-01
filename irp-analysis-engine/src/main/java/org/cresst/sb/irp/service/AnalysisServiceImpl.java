package org.cresst.sb.irp.service;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.dao.AnalysisDao;
import org.cresst.sb.irp.domain.analysis.AnalysisResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class AnalysisServiceImpl implements AnalysisService {
	private static Logger logger = Logger.getLogger(AnalysisServiceImpl.class);
	
	@Autowired
	private AnalysisDao analysisDao;
	
	public AnalysisServiceImpl(){
		logger.info("initializing");
	}

	@Override
	public AnalysisResponse analysisProcess(Iterable<Path> tdsReportPaths) {
		return new AnalysisResponse();
	}
}
