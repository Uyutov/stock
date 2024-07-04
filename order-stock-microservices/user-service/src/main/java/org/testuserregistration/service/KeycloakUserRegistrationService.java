package org.testuserregistration.service;

import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.testuserregistration.dto.LoginDTO;
import org.testuserregistration.dto.RefreshTokenDTO;
import org.testuserregistration.dto.UserRegistrationDTO;

public interface KeycloakUserRegistrationService {
    UserRegistrationDTO createUser(UserRegistrationDTO user);
    ResponseEntity<AccessTokenResponse> loginUser(LoginDTO loginDTO);
    ResponseEntity<AccessTokenResponse> refreshToken(RefreshTokenDTO refreshTokenDTO);
}
