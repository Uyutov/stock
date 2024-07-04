package org.testuserregistration.controller;

import jakarta.ws.rs.core.Response;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.testuserregistration.dto.LoginDTO;
import org.testuserregistration.dto.RefreshTokenDTO;
import org.testuserregistration.dto.UserRegistrationDTO;
import org.testuserregistration.service.KeycloakUserRegistrationService;

@RestController
@RequestMapping("/users")
public class UserController {
    private final KeycloakUserRegistrationService keycloakUserRegistrationService;

    public UserController(KeycloakUserRegistrationService keycloakUserRegistrationService) {
        this.keycloakUserRegistrationService = keycloakUserRegistrationService;
    }

    @PostMapping("/register")
    public ResponseEntity createUser (@RequestBody UserRegistrationDTO userDTO)
    {
        return keycloakUserRegistrationService.createUser(userDTO);
    }
    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login (@RequestBody LoginDTO loginDTO)
    {
        return keycloakUserRegistrationService.loginUser(loginDTO);
    }
    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponse> refreshToken (@RequestBody RefreshTokenDTO refreshTokenDTO)
    {
        return keycloakUserRegistrationService.refreshToken(refreshTokenDTO);
    }
}
