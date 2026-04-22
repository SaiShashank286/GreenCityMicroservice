package com.cognizant.greencity.authentication_service.dto;

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
