package com.cognizant.greencity.authentication_service.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String name;
    private String email;
    private String password;
    private String phone;
}
