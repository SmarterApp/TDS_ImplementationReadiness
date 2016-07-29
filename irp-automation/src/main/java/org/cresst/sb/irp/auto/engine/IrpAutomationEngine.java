package org.cresst.sb.irp.auto.engine;

import org.cresst.sb.irp.auto.accesstoken.AccessToken;
import org.cresst.sb.irp.domain.analysis.AutomationRequest;
import org.cresst.sb.irp.domain.analysis.AutomationResponse;

public class IrpAutomationEngine implements AutomationEngine {

    @Override
    public AutomationResponse automate(AutomationRequest automationRequest) {
        AutomationResponse automationResponse = new AutomationResponse();

        AccessToken accessToken = new AccessToken();
        accessToken = accessToken.getAccessToken(automationRequest.getOauthUrl(),
                automationRequest.getProgramManagementClientId(),
                automationRequest.getProgramManagementClientSecret(),
                automationRequest.getProgramManagementUserId(),
                automationRequest.getProgramManagementUserPassword());



        return automationResponse;
    }
}
