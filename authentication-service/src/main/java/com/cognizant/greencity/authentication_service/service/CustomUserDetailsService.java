package com.cognizant.greencity.authentication_service.service;

import com.cognizant.greencity.authentication_service.dto.UserDetailsDTO;
import com.cognizant.greencity.authentication_service.feignclient.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserClient userFeignClient;
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {


        UserDetailsDTO user =userFeignClient.getUserByUsername(username);
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                List.of(
                        new SimpleGrantedAuthority(
                               "ROLE_"+user.getRole()))
        );

    }
}
