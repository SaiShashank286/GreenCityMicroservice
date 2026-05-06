package com.cognizant.complianceservice.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "project-service", contextId = "projectClient",url = "http://localhost:8089")
public interface ProjectClient {
    @GetMapping("/api/projects/{id}/exists")
    Boolean existsById(@PathVariable("id") Integer id);
}