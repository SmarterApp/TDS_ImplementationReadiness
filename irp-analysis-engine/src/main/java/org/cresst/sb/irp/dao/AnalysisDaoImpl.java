package org.cresst.sb.irp.dao;

import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Test;
import org.springframework.stereotype.Repository;

@Repository
public class AnalysisDaoImpl implements AnalysisDao {

	@Override
	public void analysisProcess(TDSReport tdsReport) {
		// TODO Auto-generated method stub
		Test test = tdsReport.getTest();
		String name = test.getName();
		String subject = test.getSubject();
		System.out.println("name --------->" + name);
		System.out.println("subject --------->" + subject);
	}

}
