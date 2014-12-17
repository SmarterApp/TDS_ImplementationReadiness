package org.cresst.sb.irp.domain.analysis;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class ToolUsageCategory {
	private static Logger logger = Logger.getLogger(ToolUsageCategory.class);
	
	private String type;
	private String code;
	private FieldCheckType typeFieldCheckType;
	private FieldCheckType codeFieldCheckType;
	
	private List<ToolPageCategory> toolPageCategories;
	
	public ToolUsageCategory() {
		logger.info("initializing");
		setToolPageCategories(new ArrayList<ToolPageCategory>()); 
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public FieldCheckType getTypeFieldCheckType() {
		return typeFieldCheckType;
	}

	public void setTypeFieldCheckType(FieldCheckType typeFieldCheckType) {
		this.typeFieldCheckType = typeFieldCheckType;
	}

	public FieldCheckType getCodeFieldCheckType() {
		return codeFieldCheckType;
	}

	public void setCodeFieldCheckType(FieldCheckType codeFieldCheckType) {
		this.codeFieldCheckType = codeFieldCheckType;
	}

	public List<ToolPageCategory> getToolPageCategories() {
		return toolPageCategories;
	}

	public void setToolPageCategories(List<ToolPageCategory> toolPageCategories) {
		this.toolPageCategories = toolPageCategories;
	}

	@Override
	public String toString() {
		return "ToolUsageCategory [type=" + type + ", code=" + code + ", typeFieldCheckType=" + typeFieldCheckType
				+ ", codeFieldCheckType=" + codeFieldCheckType + ", toolPageCategories=" + toolPageCategories + "]";
	}

	
	
}
