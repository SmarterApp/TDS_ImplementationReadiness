package org.cresst.sb.irp.domain.analysis;

public class ScoreRationaleCategory {

    private String message;

    private FieldCheckType messageFieldCheckType;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public FieldCheckType getMessageFieldCheckType() {
        return messageFieldCheckType;
    }

    public void setMessageFieldCheckType(FieldCheckType messageFieldCheckType) {
        this.messageFieldCheckType = messageFieldCheckType;
    }

    @Override
    public String toString() {
        return "ScoreRationaleCategory [message=" + message + ", messageFieldCheckType=" + messageFieldCheckType + "]";
    }
}
