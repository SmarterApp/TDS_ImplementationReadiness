package org.cresst.sb.irp.domain.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;

public class IndividualResponse {
	private static Logger logger = Logger.getLogger(IndividualResponse.class);

	private String fileName; // id
	private boolean isValidXMLfile;
	private TDSReport tdsReport; //

	private List<CellCategory> listTestCategory;
	private ExamineeCategory examineeCategory;
	private List<ExamineeAttributeCategory> listExamineeAttributeCategory;

	// Test, Examinee, ExamineeAttribute, Opportunity
	//private HashMap<String, Hashtable<String, FieldCheckType>> mapCategoryField;

	public IndividualResponse() {
		logger.info("initializing");
		setListTestCategory(new ArrayList<CellCategory>());
		setListExamineeAttributeCategory(new ArrayList<ExamineeAttributeCategory>());
		//setMapCategoryField(new HashMap<String, Hashtable<String, FieldCheckType>>());
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

	/*
	public HashMap<String, Hashtable<String, FieldCheckType>> getMapCategoryField() {
		return mapCategoryField;
	}

	public void setMapCategoryField(HashMap<String, Hashtable<String, FieldCheckType>> mapCategoryField) {
		this.mapCategoryField = mapCategoryField;
	}*/

	public boolean isValidXMLfile() {
		return isValidXMLfile;
	}

	public void setValidXMLfile(boolean isValidXMLfile) {
		this.isValidXMLfile = isValidXMLfile;
	}

	public List<CellCategory> getListTestCategory() {
		return listTestCategory;
	}

	public void setListTestCategory(List<CellCategory> listTestCategory) {
		this.listTestCategory = listTestCategory;
	}

	public ExamineeCategory getExamineeCategory() {
		return examineeCategory;
	}

	public void setExamineeCategory(ExamineeCategory examineeCategory) {
		this.examineeCategory = examineeCategory;
	}

	public List<ExamineeAttributeCategory> getListExamineeAttributeCategory() {
		return listExamineeAttributeCategory;
	}

	public void setListExamineeAttributeCategory(List<ExamineeAttributeCategory> listExamineeAttributeCategory) {
		this.listExamineeAttributeCategory = listExamineeAttributeCategory;
	}

	@Override
	public String toString() {
		return "IndividualResponse [fileName=" + fileName + ", isValidXMLfile=" + isValidXMLfile + ", tdsReport=" + tdsReport
				+ ", listTestCategory=" + listTestCategory + ", examineeCategory=" + examineeCategory
				+ ", listExamineeAttributeCategory=" + listExamineeAttributeCategory + "]";
	}

	

}
