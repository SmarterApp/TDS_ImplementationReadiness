package org.cresst.sb.irp.automation.service.engine.adapterclient;

public class AdapterStateMachine {

    private final AdapterClient adapterClient;

    public AdapterStateMachine(final AdapterClient adapterClient) {
        this.adapterClient = adapterClient;
    }

    public void start() {
        AdapterState currentState = AdapterState.DISCOVERY;
        try {
            while (currentState.action(adapterClient)) {
                currentState = currentState.transition(adapterClient);
            }
        } catch (AdapterStateMachineException ex) {
            ex.getExceptionState();
        }
    }
}