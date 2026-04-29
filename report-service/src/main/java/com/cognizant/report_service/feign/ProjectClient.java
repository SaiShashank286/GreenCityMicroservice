package com.cognizant.report_service.feign;

import com.cognizant.report_service.dto.ProjectResponseDto;
import com.cognizant.report_service.dto.ResourceResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "project-service")
public interface ProjectClient {

    @GetMapping("/api/projects")
    List<ProjectResponseDto> getAllProjects();

    @GetMapping("/api/resources")
    List<ResourceResponseDto> getResourcesByProject(@RequestParam("projectId") Long projectId);
}
