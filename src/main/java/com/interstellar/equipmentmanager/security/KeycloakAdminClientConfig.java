package com.interstellar.equipmentmanager.security;

import jakarta.ws.rs.client.ClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakAdminClientConfig {

    @Value("${keycloak.auth-server-url}")
    private String authUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak-admin-cli.client-id}")
    private String clientId;

    @Value("${keycloak-admin-cli.client-secret}")
    private String clientSecret;

    @Bean
    public Keycloak getKeycloak() {
        return KeycloakBuilder.builder()
                .realm(realm)
                .serverUrl(authUrl)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .resteasyClient(ClientBuilder.newBuilder().build())
                .build();
    }
}
