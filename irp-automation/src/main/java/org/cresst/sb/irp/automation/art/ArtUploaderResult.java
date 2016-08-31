package org.cresst.sb.irp.automation.art;

public class ArtUploaderResult {
    private int numberOfRecordsUploaded;
    private String dataUploadId;
    private boolean uploadSuccessful;
    private boolean validateSuccessful;
    private boolean saveSuccessful;
    private StringBuilder message = new StringBuilder("ArtUploaderResult Messages");

    public int getNumberOfRecordsUploaded() {
        return numberOfRecordsUploaded;
    }

    public void setNumberOfRecordsUploaded(int numberOfRecordsUploaded) {
        this.numberOfRecordsUploaded = numberOfRecordsUploaded;
    }

    public String getDataUploadId() {
        return dataUploadId;
    }

    public void setDataUploadId(String dataUploadId) {
        this.dataUploadId = dataUploadId;
    }

    public String getMessage() {
        return message.toString();
    }

    public void appendMessage(String message) {
        this.message.append(" | ").append(message);
    }

    public boolean isSuccessful() {
        return uploadSuccessful && validateSuccessful && saveSuccessful;
    }

    public void setUploadSuccessful(boolean uploadSuccessful) {
        this.uploadSuccessful = uploadSuccessful;
    }

    public void setValidateSuccessful(boolean verifySuccessful) {
        this.validateSuccessful = verifySuccessful;
    }

    public void setSaveSuccessful(boolean saveSuccessful) {
        this.saveSuccessful = saveSuccessful;
    }
}
