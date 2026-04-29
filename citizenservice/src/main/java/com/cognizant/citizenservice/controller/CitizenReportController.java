package com.cognizant.citizenservice.controller;

import com.cognizant.citizenservice.dto.report.CitizenReportCreateRequest;
import com.cognizant.citizenservice.dto.report.CitizenReportResponse;
import com.cognizant.citizenservice.dto.report.CitizenReportUpdateRequest;
import com.cognizant.citizenservice.service.CitizenReportService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citizen")
public class CitizenReportController {

    private static final Logger logger = LoggerFactory.getLogger(CitizenReportController.class);
    private final CitizenReportService citizenReportService;

    public CitizenReportController(CitizenReportService citizenReportService) {
        this.citizenReportService = citizenReportService;
    }

    @GetMapping
    public List<CitizenReportResponse> listMine(Authentication authentication) {
        logger.info("Received request to list user's citizen reports");
        List<CitizenReportResponse> response = citizenReportService.listMine(authentication);
        logger.info("Successfully fetched user's citizen reports");
        return response;
    }

    @GetMapping("/all")
    public List<CitizenReportResponse> listAll() {
        logger.info("Received request to list all citizen reports");
        List<CitizenReportResponse> response = citizenReportService.listAll();
        logger.info("Successfully fetched all citizen reports");
        return response;
    }

    @GetMapping("/{id}")
    public CitizenReportResponse getMine(@PathVariable Integer id, Authentication authentication) {
        logger.info("Received request to get citizen report id: {}", id);
        CitizenReportResponse response = citizenReportService.getMine(id, authentication);
        logger.info("Successfully fetched citizen report id: {}", id);
        return response;
    }

    @PostMapping
    public CitizenReportResponse create(@Valid @RequestBody CitizenReportCreateRequest request, Authentication authentication) {
        logger.info("Received request to create a new citizen report");
        CitizenReportResponse response = citizenReportService.create(request, authentication);
        logger.info("Successfully created a new citizen report");
        return response;
    }

    @PutMapping("/{id}")
    public CitizenReportResponse update(@PathVariable Integer id, @Valid @RequestBody CitizenReportUpdateRequest request, Authentication authentication) {
        logger.info("Received request to update citizen report id: {}", id);
        CitizenReportResponse response = citizenReportService.update(id, request, authentication);
        logger.info("Successfully updated citizen report id: {}", id);
        return response;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id, Authentication authentication) {
        logger.info("Received request to delete citizen report id: {}", id);
        citizenReportService.delete(id, authentication);
        logger.info("Successfully deleted citizen report id: {}", id);
    }
}