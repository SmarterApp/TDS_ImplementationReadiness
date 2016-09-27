package org.cresst.sb.irp.automation.service.engine.adapterclient;

public interface AdapterStateTrigger {
    AdapterState transition(final AdapterClientView adapterClient);
}