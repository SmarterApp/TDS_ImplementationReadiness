package org.cresst.sb.irp.auto.accesstoken;

import org.codehaus.janino.Access;
import org.joda.time.Duration;
import org.joda.time.Instant;
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

    final static Pattern accessTokenPattern = Pattern.compile("\"access_token\":\"(.*)\"");
    final static Pattern expiresInPattern = Pattern.compile("\"expires_in\":([0-9]+),");

    private final URL oamUrl;
    private final String clientId;
    private final String clientSecret;
    private final String username;
    private final String password;

    private String strAccessToken = null;
    private Instant expiration;

    private AccessToken(URL oamUrl, String clientId, String clientSecret, String username, String password) {
        this.oamUrl = oamUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.username = username;
        this.password = password;
    }

    public static AccessToken buildAccessToken(URL oamUrl, String clientId, String clientSecret, String username, String password) {
        AccessToken accessToken = new AccessToken(oamUrl, clientId, clientSecret, username, password);
        return accessToken;
    }

    public String getAccessTokenString() {
        if (tokenIsValid()) {
            return strAccessToken;
        }

        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("username", username);
        form.add("password", password);
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        String response = restTemplate.postForObject(oamUrl.toString(), form, String.class);

        logger.info(response);

        String accessToken = null;
        String expiresIn = null;

        Matcher matcher = accessTokenPattern.matcher(response);
        if (matcher.find()) {
            accessToken = matcher.group(1);
        }

        matcher = expiresInPattern.matcher(response);
        if (matcher.find()) {
            expiresIn = matcher.group(1);
        }

        strAccessToken = accessToken;
        expiration = Instant.now().plus(Duration.standardSeconds(Long.parseLong(expiresIn)));

        return strAccessToken;
    }

    private boolean tokenIsValid() {
        return strAccessToken != null && Instant.now().isBefore(expiration);
    }

    @Override
    public String toString() {
        return getAccessTokenString();
    }
}
