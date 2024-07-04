package org.testuserregistration.service.impl;

import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.testuserregistration.config.security.KeycloakConfig;
import org.testuserregistration.dto.LoginDTO;
import org.testuserregistration.dto.RefreshTokenDTO;
import org.testuserregistration.dto.UserRegistrationDTO;
import org.testuserregistration.service.KeycloakUserRegistrationService;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class KeycloakUserRegistrationServiceImpl implements KeycloakUserRegistrationService {
    private Keycloak adminKeycloak;

    public KeycloakUserRegistrationServiceImpl(Keycloak adminKeycloak) {
        this.adminKeycloak = adminKeycloak;
    }

    @Override
    public UserRegistrationDTO createUser(UserRegistrationDTO userDTO) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(userDTO.username());
        user.setEmail(userDTO.email());
        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());
        user.setEmailVerified(true);
        user.setGroups(List.of("user"));

        CredentialRepresentation credentials = new CredentialRepresentation();
        credentials.setValue(userDTO.password());
        credentials.setTemporary(false);
        credentials.setType(CredentialRepresentation.PASSWORD);

        user.setCredentials(List.of(credentials));

        RealmResource realm = adminKeycloak.realm("innowise");
        UsersResource usersResource = realm.users();

        Response response = usersResource.create(user);

        if(response.getStatus() == 201)
        {
            return userDTO;
        }

        return null;
    }

    @Override
    public ResponseEntity<AccessTokenResponse> loginUser(LoginDTO loginDTO) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        map.add("grant_type", "password");
        map.add("client_id", "innowise-task3-auth");
        map.add("username", loginDTO.username());
        map.add("password", loginDTO.password());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<AccessTokenResponse> response =
                restTemplate.exchange(
                        "http://localhost:9090/realms/innowise/protocol/openid-connect/token",
                        HttpMethod.POST,
                        entity,
                        AccessTokenResponse.class
                );
        return response;
    }

    @Override
    public ResponseEntity<AccessTokenResponse> refreshToken(RefreshTokenDTO refreshTokenDTO) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        map.add("grant_type", "refresh_token");
        map.add("client_id", "innowise-task3-auth");
        map.add("refresh_token", refreshTokenDTO.refreshToken());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<AccessTokenResponse> response =
                restTemplate.exchange(
                        "http://localhost:9090/realms/innowise/protocol/openid-connect/token",
                        HttpMethod.POST,
                        entity,
                        AccessTokenResponse.class
                );
        return response;
    }

}
