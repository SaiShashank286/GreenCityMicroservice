package com.example.microservice_notify.dto.external;

import lombok.Data;

@Data
public class UserResponse {
    private Integer userId;
    private String username;
    private String email;
}
