package org.cresst.sb.irp.auto.art;

import org.cresst.sb.irp.auto.accesstoken.AccessToken;
import org.cresst.sb.irp.auto.tsb.LoggingRequestInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ArtTestSelector {
    private final static Logger logger = LoggerFactory.getLogger(ArtTestSelector.class);

    public void selectTests(URL artUrl, AccessToken accessToken, List<String> tsbAssessmentIds) {

        URL artAssessmentURL;
        try {
            artAssessmentURL = new URL(artUrl, "/rest/assessment");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken.getAccessTokenString());
        httpHeaders.add("Accept", "application/json");
        httpHeaders.add("Content-Type", "application/json");

        HttpEntity<TestSpecBankData> request = new HttpEntity<>(testSpecBankData, httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ClientHttpRequestInterceptor ri = new LoggingRequestInterceptor();
        List<ClientHttpRequestInterceptor> ris = new ArrayList<>();
        ris.add(ri);
        restTemplate.setInterceptors(ris);
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));

        ResponseEntity<TestSpecBankData> response = restTemplate.exchange(artAssessmentURL, HttpMethod.POST, request, TestSpecBankData.class);

    }
}
