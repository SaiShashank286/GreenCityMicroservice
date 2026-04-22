package com.cognizant.complianceservice.feignClient;

import com.cognizant.complianceservice.dto.user.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="user-service",url = "http://localhost:8081")
public interface UserClient {
    @GetMapping("/api/users/getid/{email}")
    UserDTO getuserId(@PathVariable String email);
}
