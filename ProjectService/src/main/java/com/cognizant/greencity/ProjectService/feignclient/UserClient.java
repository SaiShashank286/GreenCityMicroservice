package com.cognizant.greencity.ProjectService.feignclient;

import com.cognizant.greencity.ProjectService.dto.UserDetailsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {

	@GetMapping("/api/users/getid/{email}")
	UserDetailsDto getById(@PathVariable("email") String email);


}
