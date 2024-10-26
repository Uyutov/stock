package org.testuserregistration.config.security;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class KeycloakConfig {
    @Value("${keycloak.serverUrl:}")
    private String serverUrl;
    @Value("${keycloak.realm:}")
    private String realm;
    @Value("${keycloak.adminClientId:}")
    private String adminClientId;
    @Value("${keycloak.adminClientSecret:}")
    private String adminClientSecret;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(adminClientId)
                .clientSecret(adminClientSecret)
                .build();
    }
}
