package com.cognizant.complianceservice.service;

import com.cognizant.complianceservice.dto.compliance.ComplianceRecordCreateRequest;
import com.cognizant.complianceservice.dto.compliance.ComplianceRecordResponse;
import com.cognizant.complianceservice.dto.compliance.ComplianceRecordUpdateRequest;
import com.cognizant.complianceservice.dto.notification.NotificationCreateRequest;
import com.cognizant.complianceservice.entity.Audit;
import com.cognizant.complianceservice.entity.ComplianceRecord;
import com.cognizant.complianceservice.exception.BadRequestException;
import com.cognizant.complianceservice.exception.NotFoundException;
import com.cognizant.complianceservice.feignClient.NotificationClient;
import com.cognizant.complianceservice.feignClient.ProjectClient;
import com.cognizant.complianceservice.feignClient.ResourceClient;
import com.cognizant.complianceservice.feignClient.UserClient;
import com.cognizant.complianceservice.repository.AuditRepository;
import com.cognizant.complianceservice.repository.ComplianceRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ComplianceRecordService {

    private final ComplianceRecordRepository complianceRecordRepository;
    private final AuditRepository auditRepository;
    private final ModelMapper modelMapper;
    private final ProjectClient projectClient;
    private final ResourceClient resourceClient;
    private final UserClient userClient;
    private final NotificationClient notificationClient;



    public List<ComplianceRecordResponse> list() {
        return complianceRecordRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public ComplianceRecordResponse get(Integer complianceId) {
        return toResponse(getEntity(complianceId));
    }

    public ComplianceRecordResponse create(ComplianceRecordCreateRequest request) {
        validateEntityExists(request.getEntityType(), request.getEntityId());

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
        Integer userId = resolveAuthenticatedUserId();
        sendNotification(userId, saved.getComplianceId(), "COMPLIANCE_RECORD", "COMPLIANCE");

        return toResponse(saved);
    }

    public ComplianceRecordResponse update(Integer complianceId, ComplianceRecordUpdateRequest request) {

        ComplianceRecord record = getEntity(complianceId);

        if (request.getResult() != null) record.setResult(request.getResult());
        if (request.getDate() != null) record.setDate(request.getDate());
        if (request.getNotes() != null) record.setNotes(request.getNotes());

        ComplianceRecord saved = complianceRecordRepository.save(record);

        return toResponse(saved);
    }

    public void delete(Integer complianceId) {

        ComplianceRecord record = getEntity(complianceId);

        List<Audit> audits = auditRepository.findByComplianceRecord_ComplianceId(complianceId);
        if (audits != null && !audits.isEmpty()) {
            throw new BadRequestException("Cannot delete compliance record with existing audits");
        }

        complianceRecordRepository.delete(record);
            }

    private ComplianceRecord getEntity(Integer complianceId) {
        return complianceRecordRepository.findById(complianceId)
                .orElseThrow(() -> new NotFoundException("ComplianceRecord not found"));
    }

    private ComplianceRecordResponse toResponse(ComplianceRecord record) {
        return modelMapper.map(record, ComplianceRecordResponse.class);
    }
    private void validateEntityExists(String type, Integer id) {
        boolean exists = false;

        if ("Project".equalsIgnoreCase(type)) {
            exists = Boolean.TRUE.equals(projectClient.existsById(id));
        } else if ("Resource".equalsIgnoreCase(type)) {
            exists = Boolean.TRUE.equals(resourceClient.existsById(id));
        } else {
            throw new BadRequestException("Invalid entity type: " + type);
        }

        if (!exists) {
            throw new NotFoundException(type + " with ID " + id + " does not exist.");
        }
    }

    private Integer resolveAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            return null;
        }
        return userClient.getuserId(authentication.getName()).getUserId();
    }

    private void sendNotification(Integer userId, Integer entityId, String entityType, String category) {
        if (userId == null) {
            return;
        }
        try {
            notificationClient.createNotification(NotificationCreateRequest.builder()
                    .userId(userId)
                    .projectId(null)
                    .entityId(entityId)
                    .entityType(entityType)
                    .category(category)
                    .build());
        } catch (Exception ex) {
            log.warn("Notification call failed for {} {}", entityType, entityId, ex);
        }
    }

}

