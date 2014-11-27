package org.cresst.sb.irp.domain.analysis;

import java.util.HashMap;
import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.springframework.web.multipart.MultipartFile;

public class AnalysisResponse {
	private static Logger logger = Logger.getLogger(AnalysisResponse.class);

	private String fileName; // id
	private TDSReport tdsReport; //
	private MultipartFile multipartFile; // upload file
	private HashMap<String, Hashtable<String, FieldCheckType>> mapCategoryField;

	public AnalysisResponse() {
		logger.info("initializing");
		setMapCategoryField(new HashMap<String, Hashtable<String, FieldCheckType>>());
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public TDSReport getTDSReport() {
		return tdsReport;
	}

	public void setTDSReport(TDSReport tdsReport) {
		this.tdsReport = tdsReport;
	}

	public MultipartFile getMultipartFile() {
		return multipartFile;
	}

	public void setMultipartFile(MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
	}

	public HashMap<String, Hashtable<String, FieldCheckType>> getMapCategoryField() {
		return mapCategoryField;
	}

	public void setMapCategoryField(HashMap<String, Hashtable<String, FieldCheckType>> mapCategoryField) {
		this.mapCategoryField = mapCategoryField;
	}
}
