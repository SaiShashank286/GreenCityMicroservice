package com.cognizant.complianceservice.controller;

import com.cognizant.complianceservice.dto.audit.AuditCreateRequest;
import com.cognizant.complianceservice.dto.audit.AuditResponse;
import com.cognizant.complianceservice.dto.audit.AuditUpdateRequest;
import com.cognizant.complianceservice.service.AuditService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit/{complianceId}")
@Slf4j
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping
    public List<AuditResponse> list(@PathVariable Integer complianceId) {
        log.info("Received request to list audits for complianceId: {}", complianceId);
        List<AuditResponse> response = auditService.listByCompliance(complianceId);
        log.info("Successfully fetched audits for complianceId: {}", complianceId);
        return response;
    }

    @GetMapping("/{auditId}")
    public AuditResponse get(@PathVariable Integer complianceId, @PathVariable Integer auditId) {
        log.info("Received request to get auditId: {} for complianceId: {}", auditId, complianceId);
        AuditResponse response = auditService.getByCompliance(complianceId, auditId);
        log.info("Successfully fetched auditId: {} for complianceId: {}", auditId, complianceId);
        return response;
    }

    @PostMapping
    public AuditResponse create(@PathVariable Integer complianceId,
                                @Valid @RequestBody AuditCreateRequest request
                               ) {
        log.info("Received request to create audit for complianceId: {}", complianceId);
        AuditResponse response = auditService.create(complianceId, request);
        log.info("Successfully created audit for complianceId: {}", complianceId);
        return response;
    }

    @PutMapping("/{auditId}")
    public AuditResponse update(@PathVariable Integer complianceId,
                                @PathVariable Integer auditId,
                                @Valid @RequestBody AuditUpdateRequest request
                               ) {
        log.info("Received request to update auditId: {} for complianceId: {}", auditId, complianceId);
        AuditResponse response = auditService.update(complianceId, auditId, request);
        log.info("Successfully updated auditId: {} for complianceId: {}", auditId, complianceId);
        return response;
    }

    @DeleteMapping("/{auditId}")
    public void delete(@PathVariable Integer complianceId,
                       @PathVariable Integer auditId
                     ) {
        log.info("Received request to delete auditId: {} for complianceId: {}", auditId, complianceId);
        auditService.delete(complianceId, auditId);
        log.info("Successfully deleted auditId: {} for complianceId: {}", auditId, complianceId);
    }
}