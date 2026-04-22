package com.cognizant.complianceservice.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "project-service", contextId = "resourceClient")
public interface ResourceClient {
    @GetMapping("/api/resources/{id}/exists")
    Boolean existsById(@PathVariable("id") Integer id);
}
