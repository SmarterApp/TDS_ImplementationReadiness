package org.cresst.sb.irp.automation.service.engine.adapterclient;

public interface AdapterClient extends AdapterClientView {
    AdapterClient get();
    <T> AdapterClient post(T postObject);
    AdapterClient location();
    AdapterClient rel(String rel);
    AdapterClient target(String href);
}
