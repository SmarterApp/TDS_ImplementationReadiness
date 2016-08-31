package org.cresst.sb.irp.automation.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

public class CookieRequestInterceptor implements ClientHttpRequestInterceptor {

    private List<String> cookies;

    public CookieRequestInterceptor(List<String> cookies) {
        this.cookies = cookies;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes,
                                        ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {

        if (cookies != null && !cookies.isEmpty()) {
            HttpHeaders httpHeaders = httpRequest.getHeaders();
            httpHeaders.add("Cookie", StringUtils.collectionToDelimitedString(cookies, "; "));
        }

        return clientHttpRequestExecution.execute(httpRequest, bytes);

    }
}
