package org.cresst.sb.irp.utils;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.analysis.ExamineeRelationshipCategory;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.student.Student;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee.ExamineeRelationship;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class ExamineeRelationshipAnalysisAction extends AnalysisAction {
	private static Logger logger = Logger.getLogger(ExamineeRelationshipAnalysisAction.class);

	@Override
	public void analysis() throws IOException {
		try {
			IndividualResponse individualResponse = getIndividualResponse();
			List<ExamineeRelationshipCategory> listExamineeRelationshipCategory = individualResponse.getListExamineeRelationshipCategory();
			ExamineeRelationshipCategory examineeRelationshipCategory;
			TDSReport tdsReport = getTdsReport();
			Examinee examinee = tdsReport.getExaminee();
			Student student = getStudent(examinee.getKey());
			System.out.println("student first name in relationship ->" + student.getFirstName());
			List<ExamineeRelationship> listExamineeRelationship = getExamineeRelationships(examinee);
			if (listExamineeRelationship != null) {
				for (ExamineeRelationship er : listExamineeRelationship) {
					System.out.println("ddddd...." + er.getName());
					examineeRelationshipCategory = new ExamineeRelationshipCategory();
					listExamineeRelationshipCategory.add(examineeRelationshipCategory);
					analysisEachExamineeRelationship(examineeRelationshipCategory, er, student);

				}
			}
			
			
		} catch (Exception e) {
			logger.error("analysis exception: ", e);
		}
	}
	
	private void analysisEachExamineeRelationship(ExamineeRelationshipCategory examineeRelationshipCategory,
			ExamineeRelationship examineeRelationship, Student student) {
		try {
			
		} catch (Exception e) {
			logger.error("analysisEachExamineeRelationship exception: ", e);
		}
	}

}
