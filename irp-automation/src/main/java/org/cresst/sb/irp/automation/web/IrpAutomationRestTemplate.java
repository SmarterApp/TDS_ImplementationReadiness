package org.cresst.sb.irp.automation.web;

import org.cresst.sb.irp.automation.accesstoken.AccessToken;
import org.cresst.sb.irp.common.web.LoggingRequestInterceptor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class IrpAutomationRestTemplate extends RestTemplate implements AutomationRestTemplate {

    public IrpAutomationRestTemplate() {
        super();

        ClientHttpRequestInterceptor loggingRequestInterceptor = new LoggingRequestInterceptor();

        List<ClientHttpRequestInterceptor> clientHttpRequestInterceptors = new ArrayList<>();
        clientHttpRequestInterceptors.add(loggingRequestInterceptor);

        setInterceptors(clientHttpRequestInterceptors);
        setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
    }

    @Override
    public void addAccessToken(AccessToken accessToken) {
        AccessTokenRequestInterceptor accessTokenRequestInterceptor = new AccessTokenRequestInterceptor(accessToken);

        List<ClientHttpRequestInterceptor> interceptors = getInterceptors();
        interceptors.add(0, accessTokenRequestInterceptor);
    }

    @Override
    public void setCookies(List<String> cookies) {
        CookieRequestInterceptor cookieRequestInterceptor = new CookieRequestInterceptor(cookies);

        List<ClientHttpRequestInterceptor> interceptors = getInterceptors();
        interceptors.add(1, cookieRequestInterceptor);
    }

    public static <T> HttpEntity<T> constructHttpEntity(T requestBody) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Accept", "application/json");
        httpHeaders.add("Content-Type", "application/json");

        return new HttpEntity<>(requestBody, httpHeaders);
    }

}
