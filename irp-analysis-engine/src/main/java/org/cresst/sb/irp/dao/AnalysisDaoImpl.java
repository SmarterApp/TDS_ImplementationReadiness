package org.cresst.sb.irp.dao;

import java.io.IOException;
import java.nio.file.Path;

import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.analysis.AnalysisResponse;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.utils.XMLValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Repository;

@Repository
public class AnalysisDaoImpl implements AnalysisDao {
	private static Logger logger = Logger.getLogger(AnalysisDaoImpl.class);
	
	@Autowired
	public TestAnalysisAction testAnalysisAction;
	
	@Autowired
	public ExamineeAnalysisAction examineeAnalysisAction;
	
	@Autowired
	public ExamineeAttributeAnalysisAction examineeAttributeAnalysisAction;
	
	@Autowired
	public ExamineeRelationshipAnalysisAction examineeRelationshipAnalysisAction;

	@Autowired
	public OpportunityAnalysisAction opportunityAnalysisAction;
	
	@Autowired
	public SegmentAnalysisAction segmentAnalysisAction;
	
	@Autowired
	public AccommodationAnalysisAction accommodationAnalysisAction;
	
	@Autowired
	public ItemAttributesAnalysisAction itemAttributesAnalysisAction;
	
	@Autowired
	public ItemResponseAnalysisAction itemResponseAnalysisAction;
	
	@Autowired
	public ItemScoreInfoAnalysisAction itemScoreInfoAnalysisAction;
	
	@Autowired
	public CommentAnalysisAction commentAnalysisAction;
	
	@Autowired
	public ToolUsageAnalysisAction toolUsageAnalysisAction;
	
	//tdsreport_12_8_14.xsd from AIR David original file name tdsreport.xsd
	@Value("classpath:irp-package/tdsreport_12_8_14.xsd") //reportxml_oss.xsd") from Rami on 12/4/14 vs sample_oss_report2.xml
	private Resource TDSReportXSDResource;

	@Autowired
	private XMLValidate xmlValidate;

	@Autowired
	private Unmarshaller unmarshaller;

	public AnalysisDaoImpl() {
		logger.info("initializing");
	}

	@Override
	public AnalysisResponse analysisProcess(Iterable<Path> tdsReportPaths) {
		AnalysisResponse analysisResponse = new AnalysisResponse();
		for (Path tmpPath : tdsReportPaths) {
			IndividualResponse individualResponse = new IndividualResponse();
			individualResponse.setFileName(tmpPath.getFileName().toString());
			analysisResponse.addListIndividualResponse(individualResponse);
			try {
				if (xmlValidate.validateXMLSchema(TDSReportXSDResource, tmpPath.toString())) {
					individualResponse.setValidXMLfile(true);
					TDSReport tdsReport = (TDSReport) unmarshaller.unmarshal(new StreamSource(tmpPath.toString()));
					individualResponse.setTDSReport(tdsReport);
					
					testAnalysisAction.setIndividualResponse(individualResponse);
					testAnalysisAction.analysis();
					
					examineeAnalysisAction.setIndividualResponse(individualResponse);
					examineeAnalysisAction.analysis();
					
					examineeAttributeAnalysisAction.setIndividualResponse(individualResponse);
					examineeAttributeAnalysisAction.analysis();
					
					examineeRelationshipAnalysisAction.setIndividualResponse(individualResponse);
					examineeRelationshipAnalysisAction.analysis();
					
					opportunityAnalysisAction.setIndividualResponse(individualResponse);
					opportunityAnalysisAction.analysis();
					
					segmentAnalysisAction.setIndividualResponse(individualResponse);
					segmentAnalysisAction.analysis();
					
					accommodationAnalysisAction.setIndividualResponse(individualResponse);
					accommodationAnalysisAction.analysis();
					
					itemAttributesAnalysisAction.setIndividualResponse(individualResponse);
					itemAttributesAnalysisAction.analysis();
					
					itemResponseAnalysisAction.setIndividualResponse(individualResponse);
					itemResponseAnalysisAction.analysis();
					
					itemScoreInfoAnalysisAction.setIndividualResponse(individualResponse);
					itemScoreInfoAnalysisAction.analysis();
					
					commentAnalysisAction.setIndividualResponse(individualResponse);
					commentAnalysisAction.analysis();
					
					toolUsageAnalysisAction.setIndividualResponse(individualResponse);
					toolUsageAnalysisAction.analysis();
				}
			} catch (IOException e) {
				logger.error("analysisProcess exception: ", e);
			}

		}
		return analysisResponse;
	}
	

}
