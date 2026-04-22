package com.cognizant.citizenservice.dto.user;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO
{
    private Integer userId;
    private String name;
    private String email;
    private String phone;
    private String role;
    private String status;
    private String password;
}
