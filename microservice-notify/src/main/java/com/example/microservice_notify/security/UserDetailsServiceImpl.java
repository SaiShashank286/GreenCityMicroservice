package com.example.microservice_notify.security;

import com.example.microservice_notify.entity.User;
import com.example.microservice_notify.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));

        boolean enabled = user.getStatus() == null || "ACTIVE".equalsIgnoreCase(user.getStatus());
        return new UserPrincipal(user.getUserId(), user.getEmail(), user.getPasswordHash(), user.getRole(), enabled);
    }
}

