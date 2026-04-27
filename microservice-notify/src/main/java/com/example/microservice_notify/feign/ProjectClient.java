package com.example.microservice_notify.feign;

import com.example.microservice_notify.dto.external.ProjectResponse;
import com.example.microservice_notify.dto.external.ResourceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "project-service")
public interface ProjectClient {
    @GetMapping("/api/projects/{projectId}")
    ProjectResponse getProject(@PathVariable("projectId") Integer projectId);

    @GetMapping("/api/resources/{resourceId}")
    ResourceResponse getResource(@PathVariable("resourceId") Integer resourceId);
}
