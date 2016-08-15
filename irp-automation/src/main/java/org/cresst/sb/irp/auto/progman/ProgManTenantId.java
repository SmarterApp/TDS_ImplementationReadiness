package org.cresst.sb.irp.auto.progman;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URL;

/**
 * Gets the Tenant ID from the vendor's Program Management implementation
 */
public class ProgManTenantId {
    private final static Logger logger = LoggerFactory.getLogger(ProgManTenantId.class);

    private static String TENANT_TYPE = "STATE";

    private final RestOperations automationRestTemplate;
    private final URI progManUri;
    private final String stateCode;

    public ProgManTenantId(RestOperations automationRestTemplate, URL progManUrl, String stateCode) {

        this.automationRestTemplate = automationRestTemplate;

        // From load_reg_package.pl
        // /rest/tenant?name=$Configuration{ 'ProgramMgmtTenant' }&type=$Configuration{ 'ProgramMgmtTenantLevel' }
        this.progManUri = UriComponentsBuilder.fromHttpUrl(progManUrl.toString())
                .pathSegment("rest", "tenant")
                .queryParam("type", TENANT_TYPE)
                .queryParam("name", stateCode)
                .build(true)
                .toUri();

        this.stateCode = stateCode;
    }

    public String getTenantId() {

        ProgManTenantResponse progManTenantResponse = automationRestTemplate.getForObject(progManUri,
                ProgManTenantResponse.class);

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
