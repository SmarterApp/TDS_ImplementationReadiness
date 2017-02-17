package org.cresst.sb.irp.exceptions;

/**
 * Exception for Validation errors
 */
public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = -7526215504496550950L;
	
	public ValidationException(String message) {
		super(message);
	}

}
