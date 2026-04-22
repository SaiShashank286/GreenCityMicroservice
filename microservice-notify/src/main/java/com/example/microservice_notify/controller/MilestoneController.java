package com.example.microservice_notify.controller;

import com.example.microservice_notify.dto.project.MilestoneCreateRequest;
import com.example.microservice_notify.dto.project.MilestoneResponse;
import com.example.microservice_notify.dto.project.MilestoneUpdateRequest;
import com.example.microservice_notify.service.MilestoneService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/milestones")
public class MilestoneController {

    private static final Logger logger = LoggerFactory.getLogger(MilestoneController.class);
    private final MilestoneService milestoneService;

    public MilestoneController(MilestoneService milestoneService) {
        this.milestoneService = milestoneService;
    }

    @GetMapping
    public List<MilestoneResponse> list(@PathVariable Integer projectId, Authentication authentication) {
        logger.info("Received request to list milestones for projectId: {}", projectId);
        List<MilestoneResponse> response = milestoneService.list(projectId, authentication);
        logger.info("Successfully fetched milestones for projectId: {}", projectId);
        return response;
    }

    @GetMapping("/{milestoneId}")
    public MilestoneResponse get(@PathVariable Integer projectId,
                                 @PathVariable Integer milestoneId,
                                 Authentication authentication) {
        logger.info("Received request to get milestoneId: {} for projectId: {}", milestoneId, projectId);
        MilestoneResponse response = milestoneService.get(projectId, milestoneId, authentication);
        logger.info("Successfully fetched milestoneId: {} for projectId: {}", milestoneId, projectId);
        return response;
    }

    @PostMapping
    public MilestoneResponse create(@PathVariable Integer projectId,
                                    @Valid @RequestBody MilestoneCreateRequest request,
                                    Authentication authentication) {
        logger.info("Received request to create milestone for projectId: {}", projectId);
        MilestoneResponse response = milestoneService.create(projectId, request, authentication);
        logger.info("Successfully created milestone for projectId: {}", projectId);
        return response;
    }

    @PutMapping("/{milestoneId}")
    public MilestoneResponse update(@PathVariable Integer projectId,
                                    @PathVariable Integer milestoneId,
                                    @Valid @RequestBody MilestoneUpdateRequest request,
                                    Authentication authentication) {
        logger.info("Received request to update milestoneId: {} for projectId: {}", milestoneId, projectId);
        MilestoneResponse response = milestoneService.update(projectId, milestoneId, request, authentication);
        logger.info("Successfully updated milestoneId: {} for projectId: {}", milestoneId, projectId);
        return response;
    }

    @DeleteMapping("/{milestoneId}")
    public void delete(@PathVariable Integer projectId,
                       @PathVariable Integer milestoneId,
                       Authentication authentication) {
        logger.info("Received request to delete milestoneId: {} for projectId: {}", milestoneId, projectId);
        milestoneService.delete(projectId, milestoneId, authentication);
        logger.info("Successfully deleted milestoneId: {} for projectId: {}", milestoneId, projectId);
    }
}