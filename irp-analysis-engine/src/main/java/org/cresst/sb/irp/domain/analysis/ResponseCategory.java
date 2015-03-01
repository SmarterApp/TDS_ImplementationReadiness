package org.cresst.sb.irp.domain.analysis;

import org.cresst.sb.irp.domain.studentresponse.StudentResponse;


public class ResponseCategory {

    private String date;
    private String type;
    private String content;
    private FieldCheckType dateFieldCheckType;
    private FieldCheckType typeFieldCheckType;
    private FieldCheckType contentFieldCheckType;
    
    public boolean isResponseValid;
    
    public boolean isResponseValid() {
		return isResponseValid;
	}

	public void setIsResponseValid(boolean isResponseValid) {
		this.isResponseValid = isResponseValid;
	}

	private StudentResponse studentResponse; 

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public StudentResponse getStudentResponse() {
		return studentResponse;
	}

	public void setStudentResponse(StudentResponse studentResponse) {
		this.studentResponse = studentResponse;
	}

	public FieldCheckType getDateFieldCheckType() {
        return dateFieldCheckType;
    }

    public void setDateFieldCheckType(FieldCheckType dateFieldCheckType) {
        this.dateFieldCheckType = dateFieldCheckType;
    }

    public FieldCheckType getTypeFieldCheckType() {
        return typeFieldCheckType;
    }

    public void setTypeFieldCheckType(FieldCheckType typeFieldCheckType) {
        this.typeFieldCheckType = typeFieldCheckType;
    }

    public FieldCheckType getContentFieldCheckType() {
        return contentFieldCheckType;
    }

    public void setContentFieldCheckType(FieldCheckType contentFieldCheckType) {
        this.contentFieldCheckType = contentFieldCheckType;
    }

	@Override
	public String toString() {
		return "ResponseCategory [date=" + date + ", type=" + type + ", content=" + content + ", dateFieldCheckType="
				+ dateFieldCheckType + ", typeFieldCheckType=" + typeFieldCheckType + ", contentFieldCheckType="
				+ contentFieldCheckType + ", studentResponse=" + studentResponse + "]";
	}

 
}
