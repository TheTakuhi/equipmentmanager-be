package com.interstellar.equipmentmanager.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class RequestHelper {
    
    @Value("${keycloak-admin-cli.client-id}")
    private String clientId;
    
    @Value("${keycloak-admin-cli.client-secret}")
    private String clientSecret;
    
    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String keycloakUrl;
    
    public HttpEntity<?> getSecuredRequest() {
        return new HttpEntity<>(getHeaders());
    }
    public HttpEntity<?> getSecuredRequest(JSONObject requestBody) {
        return new HttpEntity<>(requestBody.toString(), getHeaders());
    }
    public String createRequestURL(String uri, int port) {
        return "http://127.0.0.1:%s/v1/%s".formatted(port, uri);
    }
    
    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        httpHeaders.set("Authorization", "Bearer " + obtainAccessToken());
        return httpHeaders;
    }
    
    private String extractAccessToken(String responseBody) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(responseBody);
            return rootNode.path("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("Cannot extract access token", e);
        }
    }
    
    private String obtainAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", this.clientId);
        requestBody.add("client_secret", this.clientSecret);
        requestBody.add("grant_type", "client_credentials");
        
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
        
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                keycloakUrl,
                HttpMethod.POST,
                requestEntity,
                String.class);
        
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String responseBody = responseEntity.getBody();
            return extractAccessToken(responseBody);
        }
        throw new RuntimeException("Cannot obtain access token");
    }
}
