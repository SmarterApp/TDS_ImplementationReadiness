package org.cresst.sb.irp.auto.progman;

import org.cresst.sb.irp.auto.accesstoken.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;

public class ProgMan {
    private final static Logger logger = LoggerFactory.getLogger(ProgMan.class);

    private static String TENANT_TYPE = "STATE";

    public String getTenantId(AccessToken accessToken, URL progManUrl, String stateCode) throws Exception {

        // From load_reg_package.pl
        // /rest/tenant?name=$Configuration{ 'ProgramMgmtTenant' }&type=$Configuration{ 'ProgramMgmtTenantLevel' }
        String tenantPath = String.format("/rest/tenant?type=%s&name=%s", TENANT_TYPE, stateCode);
        URL tenantIdUrl;
        try {
             tenantIdUrl = new URL(progManUrl, tenantPath);
        } catch (MalformedURLException ex) {
            // TODO: Define own exception
            throw new Exception(ex);
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken.getAccessTokenString());

        HttpEntity<String> request = new HttpEntity<>(httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ProgManTenantResponse> response = restTemplate.exchange(
                tenantIdUrl.toString(),
                HttpMethod.GET,
                request,
                ProgManTenantResponse.class);
        ProgManTenantResponse progManTenantResponse = response.getBody();

        String tenantId = null;
        ProgManTenantSearchResult[] progManTenantSearchResults = progManTenantResponse.getSearchResults();
        for (int index = 0; index < progManTenantResponse.getReturnCount(); index++) {

            ProgManTenantSearchResult progManTenantSearchResult = progManTenantSearchResults[index];

            if (progManTenantSearchResult.getName().equals(stateCode) &&
                    progManTenantSearchResult.getType().equals(TENANT_TYPE)) {
                tenantId = progManTenantSearchResult.getId();
            }
        }

        logger.debug("Tenant ID: " + tenantId);

        return tenantId;
    }
}
