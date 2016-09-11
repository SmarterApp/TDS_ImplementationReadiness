package org.cresst.sb.irp.automation.proctor;

import java.util.Set;

public interface Proctor {
    boolean login();
    boolean startTestSession(Set<String> irpTestKeys);
    String getSessionId();
}
