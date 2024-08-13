package org.orderservice.mapper;

import org.orderservice.dto.user.UserDTO;
import org.orderservice.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO getDTOFromUser(User user)
    {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }

    public User getUserFromDTO(UserDTO dto)
    {
        return User.builder().
                id(dto.getId())
                .email(dto.getEmail())
                .username(dto.getUsername())
                .build();
    }
}
