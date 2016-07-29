package org.cresst.sb.irp.auto.progman;

import org.cresst.sb.irp.auto.accesstoken.AccessToken;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URL;

public class ProgMan {
    public String getTenantId(AccessToken accessToken, URL progManUrl, String stateCode) {

        // From load_reg_package.pl
        // /rest/tenant?name=$Configuration{ 'ProgramMgmtTenant' }&type=$Configuration{ 'ProgramMgmtTenantLevel' }
        String tenantPath = String.format("/rest/tenant?name=%s&type=STATE", stateCode.toUpperCase());
        URL tenantIdUrl = new URL(progManUrl, tenantPath);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        HttpEntity<String> request = new HttpEntity<String>(httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                tenantIdUrl.toString(),
                HttpMethod.GET,
                request,
                String.class);
        String tenant = response.getBody();

        return null;

    }
}
