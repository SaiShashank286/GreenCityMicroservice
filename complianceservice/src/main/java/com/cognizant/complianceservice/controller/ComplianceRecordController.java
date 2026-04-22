package com.cognizant.complianceservice.controller;

import com.cognizant.complianceservice.dto.compliance.ComplianceRecordCreateRequest;
import com.cognizant.complianceservice.dto.compliance.ComplianceRecordResponse;
import com.cognizant.complianceservice.dto.compliance.ComplianceRecordUpdateRequest;
import com.cognizant.complianceservice.service.ComplianceRecordService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compliance")
@Slf4j
public class ComplianceRecordController {

    private final ComplianceRecordService complianceRecordService;

    public ComplianceRecordController(ComplianceRecordService complianceRecordService) {
        this.complianceRecordService = complianceRecordService;
    }

    @GetMapping
    public List<ComplianceRecordResponse> list() {
        log.info("Received request to list compliance records");
        List<ComplianceRecordResponse> response = complianceRecordService.list();
        log.info("Successfully fetched compliance records");
        return response;
    }

    @GetMapping("/{complianceId}")
    public ComplianceRecordResponse get(@PathVariable Integer complianceId) {
        log.info("Received request to get compliance record id: {}", complianceId);
        ComplianceRecordResponse response = complianceRecordService.get(complianceId);
        log.info("Successfully fetched compliance record id: {}", complianceId);
        return response;
    }

    @PostMapping
    public ComplianceRecordResponse create(@Valid @RequestBody ComplianceRecordCreateRequest request
                                           ) {
        log.info("Received request to create a compliance record");
        ComplianceRecordResponse response = complianceRecordService.create(request);
        log.info("Successfully created compliance record");
        return response;
    }

    @PutMapping("/{complianceId}")
    public ComplianceRecordResponse update(@PathVariable Integer complianceId,
                                           @Valid @RequestBody ComplianceRecordUpdateRequest request
                                          ) {
        log.info("Received request to update compliance record id: {}", complianceId);
        ComplianceRecordResponse response = complianceRecordService.update(complianceId, request);
        log.info("Successfully updated compliance record id: {}", complianceId);
        return response;
    }

    @DeleteMapping("/{complianceId}")
    public void delete(@PathVariable Integer complianceId) {
        log.info("Received request to delete compliance record id: {}", complianceId);
        complianceRecordService.delete(complianceId);
        log.info("Successfully deleted compliance record id: {}", complianceId);
    }
}