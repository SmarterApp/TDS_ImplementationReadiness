package org.cresst.sb.irp.automation.service.engine.adapterclient;

public class AdapterStateMachineException extends RuntimeException {
    private AdapterState exceptionState;

    public AdapterStateMachineException(AdapterState exceptionState, String message) {
        super(message);
        this.exceptionState = exceptionState;
    }

    public AdapterState getExceptionState() {
        return exceptionState;
    }
}
