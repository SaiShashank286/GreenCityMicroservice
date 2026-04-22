package com.cognizant.greencity.user_service.dto.user;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Integer userId;
    private String name;
    private String email;
    private String phone;
    private String role;
    private String status;
}
