package com.example.microservice_notify.service;

import com.example.microservice_notify.dto.compliance.ComplianceRecordCreateRequest;
import com.example.microservice_notify.dto.compliance.ComplianceRecordResponse;
import com.example.microservice_notify.dto.compliance.ComplianceRecordUpdateRequest;
import com.example.microservice_notify.entity.Audit;
import com.example.microservice_notify.entity.ComplianceRecord;
import com.example.microservice_notify.entity.User;
import com.example.microservice_notify.exception.BadRequestException;
import com.example.microservice_notify.exception.NotFoundException;
import com.example.microservice_notify.exception.UnauthorizedException;
import com.example.microservice_notify.repository.AuditRepository;
import com.example.microservice_notify.repository.ComplianceRecordRepository;
import com.example.microservice_notify.repository.UserRepository;
import com.example.microservice_notify.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplianceRecordService {

    private final ComplianceRecordRepository complianceRecordRepository;
    private final AuditRepository auditRepository;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;
    private final ModelMapper modelMapper;

    public List<ComplianceRecordResponse> list() {
        return complianceRecordRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public ComplianceRecordResponse get(Integer complianceId) {
        return toResponse(getEntity(complianceId));
    }

    public ComplianceRecordResponse create(ComplianceRecordCreateRequest request, Authentication authentication) {
        User officer = currentUser(authentication);

        ComplianceRecord record = new ComplianceRecord();
        record.setEntityId(request.getEntityId());
        record.setEntityType(request.getEntityType());
        record.setResult(request.getResult());
        record.setNotes(request.getNotes());
        record.setDate(request.getDate());
        if (record.getDate() == null) {
            record.setDate(LocalDateTime.now());
        }

        ComplianceRecord saved = complianceRecordRepository.save(record);
        auditLogService.record(officer, "COMPLIANCE_CREATE", "compliance_records/" + saved.getComplianceId());
        return toResponse(saved);
    }

    public ComplianceRecordResponse update(Integer complianceId, ComplianceRecordUpdateRequest request, Authentication authentication) {
        User officer = currentUser(authentication);
        ComplianceRecord record = getEntity(complianceId);

        if (request.getResult() != null) record.setResult(request.getResult());
        if (request.getDate() != null) record.setDate(request.getDate());
        if (request.getNotes() != null) record.setNotes(request.getNotes());

        ComplianceRecord saved = complianceRecordRepository.save(record);
        auditLogService.record(officer, "COMPLIANCE_UPDATE", "compliance_records/" + complianceId);
        return toResponse(saved);
    }

    public void delete(Integer complianceId, Authentication authentication) {
        User officer = currentUser(authentication);
        ComplianceRecord record = getEntity(complianceId);

        List<Audit> audits = auditRepository.findByComplianceRecord_ComplianceId(complianceId);
        if (audits != null && !audits.isEmpty()) {
            throw new BadRequestException("Cannot delete compliance record with existing audits");
        }

        complianceRecordRepository.delete(record);
        auditLogService.record(officer, "COMPLIANCE_DELETE", "compliance_records/" + complianceId);
    }

    private ComplianceRecord getEntity(Integer complianceId) {
        return complianceRecordRepository.findById(complianceId)
                .orElseThrow(() -> new NotFoundException("ComplianceRecord not found"));
    }

    private ComplianceRecordResponse toResponse(ComplianceRecord record) {
        return modelMapper.map(record, ComplianceRecordResponse.class);
    }

    private User currentUser(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            throw new UnauthorizedException("Unauthorized");
        }
        return userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}

