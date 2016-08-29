package org.cresst.sb.irp.auto.art;

public class ArtStudentUploaderResult {
    private int numberOfStudentsUploaded;
    private String studentUploadId;
    private boolean uploadSuccessful;
    private boolean validateSuccessful;
    private boolean saveSuccessful;
    private StringBuilder message = new StringBuilder("ArtStudentUploaderResult Messages");

    public int getNumberOfStudentsUploaded() {
        return numberOfStudentsUploaded;
    }

    public void setNumberOfStudentsUploaded(int numberOfStudentsUploaded) {
        this.numberOfStudentsUploaded = numberOfStudentsUploaded;
    }

    public String getStudentUploadId() {
        return studentUploadId;
    }

    public void setStudentUploadId(String studentUploadId) {
        this.studentUploadId = studentUploadId;
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
