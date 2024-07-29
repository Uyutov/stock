package org.orderservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orderservice.dto.user.UserDTO;
import org.orderservice.entity.User;
import org.orderservice.mapper.UserMapper;
import org.orderservice.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserDetailsServiceImpl userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    public void setUp(){
        user = User.builder()
                .id("1")
                .email("Vladimir@gmail.com")
                .username("Vladimir")
                .build();

        userDTO = UserDTO.builder()
                .id("1")
                .email("Vladimir@gmail.com")
                .username("Vladimir")
                .build();
    }

    @Test
    void loadUserByUsername() {
        String username = "user";

        Mockito.when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(userMapper.getDTOFromUser(user)).thenReturn(userDTO);

        UserDTO response = (UserDTO) userService.loadUserByUsername(username);

        assertThat(response).isEqualTo(userDTO);
    }
}