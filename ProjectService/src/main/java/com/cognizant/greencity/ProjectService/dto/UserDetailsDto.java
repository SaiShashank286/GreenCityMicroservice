package com.cognizant.greencity.ProjectService.dto;

import lombok.Data;

@Data
public class UserDetailsDto {
	private Integer userId;
	private String name;
	private String email;
	private String phone;
	private String role;
	private String status;
}
