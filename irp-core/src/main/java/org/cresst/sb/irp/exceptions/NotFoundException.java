package org.cresst.sb.irp.exceptions;

/**
 * Exception for when a resource or property cannot be found.
 */
public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 8093235623928808300L;

	public NotFoundException(String msg) {
		super(msg);
	}
	
}
