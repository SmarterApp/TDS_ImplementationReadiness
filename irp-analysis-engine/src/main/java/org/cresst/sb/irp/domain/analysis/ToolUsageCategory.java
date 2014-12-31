package org.cresst.sb.irp.domain.analysis;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class ToolUsageCategory {

	private String type;
	private String code;
	private FieldCheckType typeFieldCheckType;
	private FieldCheckType codeFieldCheckType;
	
	private List<ToolPageCategory> toolPageCategories = new ArrayList<>();

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
		return ImmutableList.copyOf(toolPageCategories);
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
