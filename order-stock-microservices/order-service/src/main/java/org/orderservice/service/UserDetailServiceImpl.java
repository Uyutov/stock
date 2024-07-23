package org.orderservice.service;

import jakarta.persistence.EntityNotFoundException;
import org.orderservice.entity.User;
import org.orderservice.mapper.UserMapper;
import org.orderservice.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDetailServiceImpl(UserRepository userRepository,
                                 UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> {
            return new EntityNotFoundException("User with username " + username + " not found");
        });

        return userMapper.getDTOFromUser(user);
    }
}
