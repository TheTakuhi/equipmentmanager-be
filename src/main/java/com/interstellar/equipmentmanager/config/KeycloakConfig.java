package com.interstellar.equipmentmanager.config;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
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
    public KeycloakConfigResolver keycloak() {
        return new KeycloakSpringBootConfigResolver();
//        return Keycloak.getInstance(
//                keycloakUrl,
//                realm,
//                clientId,
//                clientSecret);
    }
}
