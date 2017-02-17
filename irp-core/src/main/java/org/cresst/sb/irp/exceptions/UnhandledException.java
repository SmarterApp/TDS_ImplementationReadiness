package org.cresst.sb.irp.exceptions;

public class UnhandledException extends RuntimeException{

	private static final long serialVersionUID = 4305069299833143587L;

	public UnhandledException(String msg) {
		super(msg);
	}

	public UnhandledException(String msg, Throwable t) {
		super(msg, t);
	}
	
}
