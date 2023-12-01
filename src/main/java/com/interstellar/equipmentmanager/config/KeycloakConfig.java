package com.interstellar.equipmentmanager.config;

import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {
    @Value("${keycloak.auth-server-url}")
    private String keycloakUrl;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.resource}")
    private String clientId;
    @Value("${keycloak.secret}")
    private String clientSecret;

    @Bean
    public Keycloak keycloak() {
        return Keycloak.getInstance(
                keycloakUrl,
                realm,
                clientId,
                clientSecret);
    }
}
