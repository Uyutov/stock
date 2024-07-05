package org.testuserregistration.service.impl;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.testuserregistration.dto.LoginDTO;
import org.testuserregistration.dto.RefreshTokenDTO;
import org.testuserregistration.dto.UserRegistrationDTO;
import org.testuserregistration.mapper.KeycloakUserMapper;
import org.testuserregistration.service.KeycloakUserRegistrationService;

import java.util.List;

@Service
public class KeycloakUserRegistrationServiceImpl implements KeycloakUserRegistrationService {
    @Value("${keycloak.authClientId}")
    private String authClientId;
    private final Keycloak adminKeycloak;
    private final KeycloakUserMapper keycloakUserMapper;
    private final String SUCCESSFUL_USER_REGISTRATION = "User successfully registered";
    private final String FAILED_USER_REGISTRATION = "User successfully registered";
    private final String TOKEN_URL = "http://localhost:9090/realms/innowise/protocol/openid-connect/token";

    public KeycloakUserRegistrationServiceImpl(Keycloak adminKeycloak, KeycloakUserMapper keycloakUserMapper) {
        this.adminKeycloak = adminKeycloak;
        this.keycloakUserMapper = keycloakUserMapper;
    }

    @Override
    public ResponseEntity createUser(UserRegistrationDTO userDTO) {
        UserRepresentation user = keycloakUserMapper.mapUser(userDTO);

        CredentialRepresentation credentials = new CredentialRepresentation();
        credentials.setValue(userDTO.password());
        credentials.setTemporary(false);
        credentials.setType(CredentialRepresentation.PASSWORD);

        user.setCredentials(List.of(credentials));

        RealmResource realm = adminKeycloak.realm("innowise");
        UsersResource usersResource = realm.users();

        Response response = usersResource.create(user);
        if (response.getStatus() == 201) {
            return ResponseEntity.status(201).body(SUCCESSFUL_USER_REGISTRATION);
        }
        return ResponseEntity.status(response.getStatus()).body(FAILED_USER_REGISTRATION);
    }

    @Override
    public ResponseEntity<AccessTokenResponse> loginUser(LoginDTO loginDTO) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        map.add("grant_type", "password");
        map.add("client_id", authClientId);
        map.add("username", loginDTO.username());
        map.add("password", loginDTO.password());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<AccessTokenResponse> response =
                restTemplate.exchange(
                        TOKEN_URL,
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
        map.add("client_id", authClientId);
        map.add("refresh_token", refreshTokenDTO.refreshToken());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<AccessTokenResponse> response =
                restTemplate.exchange(
                        TOKEN_URL,
                        HttpMethod.POST,
                        entity,
                        AccessTokenResponse.class
                );
        return response;
    }

}
