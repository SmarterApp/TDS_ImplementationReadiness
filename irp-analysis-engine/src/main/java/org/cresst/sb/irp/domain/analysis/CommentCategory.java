package org.cresst.sb.irp.domain.analysis;

import org.apache.log4j.Logger;

public class CommentCategory {
	private static Logger logger = Logger.getLogger(CommentCategory.class);
	
	private String context;
	private String itemPosition;
	private String date;
	private String content;
	
	private FieldCheckType contextFieldCheckType;
	private FieldCheckType itemPositionFieldCheckType;
	private FieldCheckType dateFieldCheckType;
	private FieldCheckType contentFieldCheckType;
	
	public CommentCategory() {
		logger.info("initializing");
	}
	
	public FieldCheckType getContextFieldCheckType() {
		return contextFieldCheckType;
	}

	public void setContextFieldCheckType(FieldCheckType contextFieldCheckType) {
		this.contextFieldCheckType = contextFieldCheckType;
	}

	public FieldCheckType getItemPositionFieldCheckType() {
		return itemPositionFieldCheckType;
	}

	public void setItemPositionFieldCheckType(FieldCheckType itemPositionFieldCheckType) {
		this.itemPositionFieldCheckType = itemPositionFieldCheckType;
	}

	public FieldCheckType getDateFieldCheckType() {
		return dateFieldCheckType;
	}

	public void setDateFieldCheckType(FieldCheckType dateFieldCheckType) {
		this.dateFieldCheckType = dateFieldCheckType;
	}

	public FieldCheckType getContentFieldCheckType() {
		return contentFieldCheckType;
	}

	public void setContentFieldCheckType(FieldCheckType contentFieldCheckType) {
		this.contentFieldCheckType = contentFieldCheckType;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getItemPosition() {
		return itemPosition;
	}

	public void setItemPosition(String itemPosition) {
		this.itemPosition = itemPosition;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "CommentCategory [context=" + context + ", itemPosition=" + itemPosition + ", date=" + date + ", "
				+ " contextFieldCheckType=" + contextFieldCheckType + ", itemPositionFieldCheckType="
				+ itemPositionFieldCheckType + ", dateFieldCheckType=" + dateFieldCheckType + ", contentFieldCheckType="
				+ contentFieldCheckType + "]";
	}
	
	
	
}
