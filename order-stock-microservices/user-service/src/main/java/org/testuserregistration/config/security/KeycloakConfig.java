package org.testuserregistration.config.security;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class KeycloakConfig {
    public final static String serverUrl = "http://localhost:9090";
    public final static String realm = "innowise";
    public final static String adminClientId = "admin-cli";
    public final static String authClientId = "innowise-task3-auth";
    public final static String adminClientSecret = "MmMRS9DKN2whMqYjw82eAkIpXAKhw71o";
    final static String userName = "admin";
    final static String password = "admin";

    @Bean
    public Keycloak keycloak(){
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(adminClientId)
                .clientSecret(adminClientSecret)
                .build();
    }
}
