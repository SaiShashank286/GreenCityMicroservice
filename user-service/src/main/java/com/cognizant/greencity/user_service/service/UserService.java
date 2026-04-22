package com.cognizant.greencity.user_service.service;

import com.cognizant.greencity.user_service.dto.user.UserDTO;
import com.cognizant.greencity.user_service.dto.user.UserDetailsDTO;
import com.cognizant.greencity.user_service.dto.user.UserResponse;
import com.cognizant.greencity.user_service.dto.user.UserUpdateRequest;
import com.cognizant.greencity.user_service.entity.User;
import com.cognizant.greencity.user_service.exception.NotFoundException;
import com.cognizant.greencity.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    public List<UserResponse> list() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    public UserResponse get(Integer id) {
        log.info("get method");
        return toResponse(getEntity(id));

    }

    public UserResponse update(Integer id, UserUpdateRequest request) {
        User user = getEntity(id);

        if (request.getName() != null) user.setName(request.getName().trim());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getStatus() != null) user.setStatus(request.getStatus());

        User saved = userRepository.save(user);
        auditLogService.record(saved, "USER_UPDATE", "users/" + id);
        return toResponse(saved);
    }

    public void delete(Integer id) {
        User user = getEntity(id);
        userRepository.delete(user);
        auditLogService.record(user, "USER_DELETE", "users/" + id);
    }

    private User getEntity(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    private UserResponse toResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }
    public UserDTO registerUser(UserDTO userDto) {

        User user = modelMapper.map(userDto,User.class);

        // Encrypt password
        String encryptedPassword = passwordEncoder.encode(userDto.getPassword());
        user.setPasswordHash(encryptedPassword);
        user.setRole("CITIZEN");
        user.setStatus("ACTIVE");
        User savedUser=userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);

    }

    public UserDetailsDTO findByUsername(String username) {
        User user = userRepository
                .findByEmail(username)
                .orElseThrow(() ->
                        new NotFoundException("User not found"));
        return modelMapper.map(user, UserDetailsDTO.class);
    }
    public UserDetailsDTO getById(String email){
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new NotFoundException("User not found"));
        return modelMapper.map(user, UserDetailsDTO.class);
    }
}
