package org.cresst.sb.irp.upload;

/**
 * An Exception class to represent File upload errors.
 */
public class FileUploadException extends Exception {

    public FileUploadException(Throwable e) {
        super(e);
    }

    public FileUploadException(String message) {
        super(message);
    }
}
