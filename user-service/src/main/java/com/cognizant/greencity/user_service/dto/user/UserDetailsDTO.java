package com.cognizant.greencity.user_service.dto.user;

import lombok.Data;

@Data
public class UserDetailsDTO {
    private Integer userId;
    private String name;
    private String email;
    private String phone;
    private String role;
    private String status;
    private String password;
}
