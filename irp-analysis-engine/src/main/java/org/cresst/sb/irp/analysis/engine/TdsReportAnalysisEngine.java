package org.cresst.sb.irp.analysis.engine;

import org.cresst.sb.irp.domain.analysis.AnalysisResponse;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.utils.XMLValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;

import javax.xml.transform.stream.StreamSource;

import java.io.IOException;
import java.nio.file.Path;

@Service
public class TdsReportAnalysisEngine implements AnalysisEngine {
	private final static Logger logger = LoggerFactory.getLogger(TdsReportAnalysisEngine.class);

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
	public TestScoreAnalysisAction testScoreAnalysisAction;

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

	// http://www.smarterapp.org/specs/TestResultsTransmissionFormat.html Date 2015-06-17
	@Value("classpath:TestResultsTransmissionFormat_Schema.xsd")
	private Resource TDSReportXSDResource;

	@Autowired
	private XMLValidate xmlValidate;

	@Autowired
	private Unmarshaller unmarshaller;

	@Override
	public AnalysisResponse analyze(Iterable<Path> tdsReportPaths) {
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

					testAnalysisAction.analyze(individualResponse);
					if (individualResponse.isValidTestName()) {
						examineeAnalysisAction.analyze(individualResponse);
						if (individualResponse.isValidExaminee()) {
							examineeAttributeAnalysisAction.analyze(individualResponse);
							examineeRelationshipAnalysisAction.analyze(individualResponse);
							opportunityAnalysisAction.analyze(individualResponse);
							segmentAnalysisAction.analyze(individualResponse);
							accommodationAnalysisAction.analyze(individualResponse);
							testScoreAnalysisAction.analyze(individualResponse);
							itemAttributesAnalysisAction.analyze(individualResponse);
							itemResponseAnalysisAction.analyze(individualResponse);
							itemScoreInfoAnalysisAction.analyze(individualResponse);
							commentAnalysisAction.analyze(individualResponse);
							toolUsageAnalysisAction.analyze(individualResponse);
						}
					}
				}
			} catch (IOException e) {
				logger.error("analyze exception: ", e);
			}
		}
		return analysisResponse;
	}

}
