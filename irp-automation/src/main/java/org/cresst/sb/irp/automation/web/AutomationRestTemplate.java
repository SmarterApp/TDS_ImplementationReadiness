package org.cresst.sb.irp.automation.web;

import org.cresst.sb.irp.automation.accesstoken.AccessToken;
import org.springframework.web.client.RestOperations;

import java.util.List;

public interface AutomationRestTemplate extends RestOperations {
    void addAccessToken(AccessToken accessToken);

    void setCookies(List<String> cookies);
}
