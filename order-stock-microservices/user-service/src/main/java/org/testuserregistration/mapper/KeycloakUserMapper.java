package org.testuserregistration.mapper;

import org.keycloak.representations.idm.UserRepresentation;
import org.testuserregistration.dto.UserRegistrationDTO;

import java.util.List;

public class KeycloakUserMapper{
    public static UserRepresentation mapUser(UserRegistrationDTO userDTO)
    {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(userDTO.username());
        user.setEmail(userDTO.email());
        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());
        user.setEmailVerified(true);
        user.setGroups(List.of("user"));
        return user;
    }
}
