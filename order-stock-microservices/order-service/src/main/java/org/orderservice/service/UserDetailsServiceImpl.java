package org.orderservice.service;

import jakarta.persistence.EntityNotFoundException;
import org.orderservice.entity.User;
import org.orderservice.mapper.UserMapper;
import org.orderservice.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final String USER_NOT_FOUND_EXC = "User with username %s not found";
    public UserDetailsServiceImpl(UserRepository userRepository,
                                  UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> {
            return new EntityNotFoundException(String.format(USER_NOT_FOUND_EXC, username));
        });

        return userMapper.getDTOFromUser(user);
    }
}
