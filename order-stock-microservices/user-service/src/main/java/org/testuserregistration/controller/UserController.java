package org.testuserregistration.controller;

import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity createUser(@RequestBody UserRegistrationDTO userDTO) {
        return keycloakUserRegistrationService.createUser(userDTO);
    }

    @PostMapping("/login")
    public AccessTokenResponse login(@RequestBody LoginDTO loginDTO) {
        return keycloakUserRegistrationService.loginUser(loginDTO);
    }

    @PostMapping("/refresh")
    public AccessTokenResponse refreshToken(@RequestBody RefreshTokenDTO refreshTokenDTO) {
        return keycloakUserRegistrationService.refreshToken(refreshTokenDTO);
    }
}
