package com.cognizant.greencity.user_service.dto.user;

import lombok.Data;

@Data
public class UserDTO {
    private String name;
    private String email;
    private String password;
    private String phone;
}
