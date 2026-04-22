package com.cognizant.greencity.authentication_service.feignclient;

import com.cognizant.greencity.authentication_service.dto.RegisterRequest;
import com.cognizant.greencity.authentication_service.dto.UserDTO;
import com.cognizant.greencity.authentication_service.dto.UserDetailsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="user-service",url = "http://localhost:8081")
public interface UserClient {
    @PostMapping("/api/users/register")
    UserDTO registerUser(@RequestBody RegisterRequest registerRequest);

    @GetMapping("/api/users/username/{username}")
    UserDetailsDTO getUserByUsername(@PathVariable String username);
}
