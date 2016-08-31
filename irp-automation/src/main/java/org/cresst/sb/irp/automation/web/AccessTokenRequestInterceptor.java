package org.cresst.sb.irp.automation.web;

import org.cresst.sb.irp.automation.accesstoken.AccessToken;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

class AccessTokenRequestInterceptor implements ClientHttpRequestInterceptor {

    private final AccessToken accessToken;

    public AccessTokenRequestInterceptor(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes,
                                        ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {

        HttpHeaders httpHeaders = httpRequest.getHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        return clientHttpRequestExecution.execute(httpRequest, bytes);
    }
}
