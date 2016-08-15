package org.cresst.sb.irp.auto.web;

import org.cresst.sb.irp.auto.accesstoken.AccessToken;
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
public class AutomationRestTemplate extends RestTemplate {

    public AutomationRestTemplate() {
        super();

        ClientHttpRequestInterceptor loggingRequestInterceptor = new LoggingRequestInterceptor();

        List<ClientHttpRequestInterceptor> clientHttpRequestInterceptors = new ArrayList<>();
        clientHttpRequestInterceptors.add(loggingRequestInterceptor);

        setInterceptors(clientHttpRequestInterceptors);
        setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
    }

    public void addAccessToken(AccessToken accessToken) {
        AccessTokenRequestInterceptor accessTokenRequestInterceptor = new AccessTokenRequestInterceptor(accessToken);

        List<ClientHttpRequestInterceptor> interceptors = getInterceptors();
        interceptors.add(0, accessTokenRequestInterceptor);
    }

    public static <T> HttpEntity<T> constructHttpEntity(T requestBody) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Accept", "application/json");
        httpHeaders.add("Content-Type", "application/json");

        return new HttpEntity<>(requestBody, httpHeaders);
    }

}
