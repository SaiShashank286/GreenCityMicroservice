package com.cognizant.greencity.authentication_service.controller;

import com.cognizant.greencity.authentication_service.dto.AuthResponse;
import com.cognizant.greencity.authentication_service.dto.LoginRequest;
import com.cognizant.greencity.authentication_service.dto.RegisterRequest;
import com.cognizant.greencity.authentication_service.dto.UserDTO;
import com.cognizant.greencity.authentication_service.feignclient.UserClient;
import com.cognizant.greencity.authentication_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    private final UserClient userClient;
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request) {

        Authentication authentication =
                authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()));


        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String role = userDetails.getAuthorities()
                .iterator()
                .next()
                .getAuthority();
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", role);
        String token = jwtUtil.generateToken(userDetails.getUsername(), extraClaims);

        return ResponseEntity.ok(new AuthResponse(token));
    }
        @PostMapping("/register")
        public UserDTO registerUser(@RequestBody RegisterRequest registerRequest)
        {
            System.out.println(registerRequest);
            return userClient.registerUser(registerRequest);
        }


}
