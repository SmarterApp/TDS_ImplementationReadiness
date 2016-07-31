package org.cresst.sb.irp.auto.engine;

import org.cresst.sb.irp.auto.accesstoken.AccessToken;
import org.cresst.sb.irp.auto.progman.ProgMan;
import org.cresst.sb.irp.domain.automation.AutomationRequest;
import org.cresst.sb.irp.domain.automation.AutomationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class IrpAutomationEngine implements AutomationEngine {
    private final static Logger logger = LoggerFactory.getLogger(IrpAutomationEngine.class);

    @Override
    public AutomationResponse automate(AutomationRequest automationRequest) throws Exception {
        AutomationResponse automationResponse = new AutomationResponse();

        AccessToken accessToken = AccessToken.buildAccessToken(automationRequest.getoAuthUrl(),
                automationRequest.getProgramManagementClientId(),
                automationRequest.getProgramManagementClientSecret(),
                automationRequest.getProgramManagementUserId(),
                automationRequest.getProgramManagementUserPassword());

        ProgMan progMan = new ProgMan();
        String tenantId = progMan.getTenantId(accessToken,
                automationRequest.getProgramManagementUrl(),
                automationRequest.getStateAbbreviation());

        return automationResponse;
    }
}
