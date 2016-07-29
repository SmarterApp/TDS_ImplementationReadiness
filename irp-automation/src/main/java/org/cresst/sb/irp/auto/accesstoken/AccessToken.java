package org.cresst.sb.irp.auto.accesstoken;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AccessToken {
    private final static Logger logger = LoggerFactory.getLogger(AccessToken.class);

    final static Pattern p = Pattern.compile("\"access_token\":\"(.*)\"");

    private String strAccessToken = null;

    public AccessToken() { }

    public AccessToken getAccessToken(URL oamUrl, String clientId, String clientSecret, String username, String password) {
        if (strAccessToken != null) {
            return this;
        }

        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
        form.add("grant_type", "password");
        form.add("username", username);
        form.add("password", password);
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        String response = restTemplate.postForObject(oamUrl.toString(), form, String.class);

        logger.info(response);

        String accessToken = null;

        Matcher m = p.matcher(response);
        if (m.find()) {
            accessToken = m.group(1);
        }

        strAccessToken = accessToken;

        return this;
    }


    @Override
    public String toString() {
        return strAccessToken;
    }
}
