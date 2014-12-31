package org.cresst.sb.irp.domain.analysis;

public class ToolPageCategory {

	private Long page;
	private String groupId;
	private Long count;
	
	private FieldCheckType pageFieldCheckType;
	private FieldCheckType groupIdFieldCheckType;
	private FieldCheckType countFieldCheckType;
	
	public Long getPage() {
		return page;
	}

	public void setPage(Long page) {
		this.page = page;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public FieldCheckType getPageFieldCheckType() {
		return pageFieldCheckType;
	}

	public void setPageFieldCheckType(FieldCheckType pageFieldCheckType) {
		this.pageFieldCheckType = pageFieldCheckType;
	}

	public FieldCheckType getGroupIdFieldCheckType() {
		return groupIdFieldCheckType;
	}

	public void setGroupIdFieldCheckType(FieldCheckType groupIdFieldCheckType) {
		this.groupIdFieldCheckType = groupIdFieldCheckType;
	}

	public FieldCheckType getCountFieldCheckType() {
		return countFieldCheckType;
	}

	public void setCountFieldCheckType(FieldCheckType countFieldCheckType) {
		this.countFieldCheckType = countFieldCheckType;
	}

	@Override
	public String toString() {
		return "ToolPageCategory [page=" + page + ", groupId=" + groupId + ", count=" + count + ", pageFieldCheckType="
				+ pageFieldCheckType + ", groupIdFieldCheckType=" + groupIdFieldCheckType + ", countFieldCheckType="
				+ countFieldCheckType + "]";
	}
}
