package com.cognizant.greencity.user_service.controller;

import com.cognizant.greencity.user_service.dto.user.UserDTO;
import com.cognizant.greencity.user_service.dto.user.UserDetailsDTO;
import com.cognizant.greencity.user_service.dto.user.UserResponse;
import com.cognizant.greencity.user_service.dto.user.UserUpdateRequest;
import com.cognizant.greencity.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> list() {
        log.info("Received request to list users");
        List<UserResponse> response = userService.list();
        log.info("Successfully fetched users");
        return response;
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable Integer id) {
        log.info("Received request to get user id: {}", id);
        UserResponse response = userService.get(id);
        log.info("Successfully fetched user id: {}", id);
        return response;
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Integer id, @Valid @RequestBody UserUpdateRequest request) {
        log.info("Received request to update user id: {}", id);
        UserResponse response = userService.update(id, request);
        log.info("Successfully updated user id: {}", id);
        return response;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        log.info("Received request to delete user id: {}", id);
        userService.delete(id);
        log.info("Successfully deleted user id: {}", id);
    }
    @GetMapping("/username/{username}")
    public ResponseEntity<UserDetailsDTO> getUserByUsername(@PathVariable String username) {


        UserDetailsDTO user=userService.findByUsername(username);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public UserDTO registerUser(@RequestBody UserDTO userDto)
    {
        return userService.registerUser(userDto);
    }
    @GetMapping("/getid/{email}")
    public ResponseEntity<UserDetailsDTO> getById(@PathVariable String email) {


        UserDetailsDTO user=userService.getById(email);

        return ResponseEntity.ok(user);
    }
}
