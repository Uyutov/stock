package org.testuserregistration.service;

import jakarta.ws.rs.core.Response;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.testuserregistration.dto.LoginDTO;
import org.testuserregistration.dto.RefreshTokenDTO;
import org.testuserregistration.dto.UserRegistrationDTO;

public interface KeycloakUserRegistrationService {
    ResponseEntity createUser(UserRegistrationDTO user);

    AccessTokenResponse loginUser(LoginDTO loginDTO);

    AccessTokenResponse refreshToken(RefreshTokenDTO refreshTokenDTO);
}
