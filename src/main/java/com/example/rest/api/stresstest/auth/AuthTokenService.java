package com.example.rest.api.stresstest.auth;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthTokenService {

    private final static Logger log = LoggerFactory.getLogger(AuthTokenService.class);

    @Value("${auth0.clientId}")
    private String clientId;

    @Value("${auth0.clientSecret}")
    private String clientSecret;

    @Value("${auth0.audience}")
    private String audience;

    @Value("${auth0.tokenUrl}")
    private String tokenUrl;

    private AuthToken token;

    @PostConstruct
    public void setup() {
        retrieveAccessToken();
    }

    public String retrieveAccessToken() {
        try {
            // Check if token already exist & expired.
            if (this.token != null) {
                if (isTokenActive()) {
                    return this.token.getAccess_token();
                }
            }

            // http POST to obtain token
            RestTemplate template = new RestTemplate();

            Map<String, String> params = new HashMap<String, String>();
            params.put("grant_type", "client_credentials");
            params.put("client_id", this.clientId);
            params.put("client_secret", this.clientSecret);
            params.put("audience", this.audience);

            ResponseEntity<AuthToken> result = template.postForEntity(this.tokenUrl, params, AuthToken.class);

            this.token = result.getBody();

            return this.token.getAccess_token();
        } catch (Exception e) {
            log.error("Error retrieving token", e);
            return "";
        }
    }

    public boolean isTokenActive() {

        if (this.token != null) {

            DecodedJWT jwt = JWT.decode(this.token.getAccess_token());

            LocalDateTime now = LocalDateTime.now();

            LocalDateTime issueAt = LocalDateTime.ofInstant(jwt.getIssuedAt().toInstant(),
                    TimeZone.getTimeZone("America/New_York").toZoneId());
            LocalDateTime expiresAt = LocalDateTime.ofInstant(jwt.getExpiresAt().toInstant(),
                    TimeZone.getTimeZone("America/New_York").toZoneId());

            if (now.isAfter(issueAt) && now.isBefore(expiresAt)) {
                return true;
            }
        }
        return false;
    }
}

