package com.cognizant.complianceservice.service;

import com.cognizant.complianceservice.dto.audit.AuditCreateRequest;
import com.cognizant.complianceservice.dto.audit.AuditResponse;
import com.cognizant.complianceservice.dto.audit.AuditUpdateRequest;
import com.cognizant.complianceservice.dto.user.UserDTO;
import com.cognizant.complianceservice.entity.Audit;
import com.cognizant.complianceservice.entity.ComplianceRecord;
import com.cognizant.complianceservice.exception.NotFoundException;
import com.cognizant.complianceservice.feignClient.UserClient;
import com.cognizant.complianceservice.repository.AuditRepository;
import com.cognizant.complianceservice.repository.ComplianceRecordRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditRepository auditRepository;
    private final ComplianceRecordRepository complianceRecordRepository;
    private final UserClient userClient;
    private final ModelMapper modelMapper;

    public List<AuditResponse> listByCompliance(Integer complianceId) {
        return auditRepository.findByComplianceRecord_ComplianceId(complianceId).stream()
                .map(this::toResponse)
                .toList();
    }

    public AuditResponse getByCompliance(Integer complianceId, Integer auditId) {
        return toResponse(getEntityByCompliance(complianceId, auditId));
    }

    public AuditResponse create(Integer complianceId, AuditCreateRequest request) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
         UserDTO user=userClient.getuserId(authentication.getName());
        Integer officer = user.getUserId();
        ComplianceRecord complianceRecord = complianceRecordRepository.findById(complianceId)
                .orElseThrow(() -> new NotFoundException("ComplianceRecord not found"));

        Audit audit = modelMapper.map(request, Audit.class);
        audit.setOfficer(officer);
        audit.setComplianceRecord(complianceRecord);
        if (audit.getDate() == null) {
            audit.setDate(LocalDateTime.now());
        }

        Audit saved = auditRepository.save(audit);
               return toResponse(saved);
    }

    public AuditResponse update(Integer complianceId, Integer auditId, AuditUpdateRequest request) {

        Audit audit = getEntityByCompliance(complianceId, auditId);

        if (request.getScope() != null) audit.setScope(request.getScope());
        if (request.getFindings() != null) audit.setFindings(request.getFindings());
        if (request.getDate() != null) audit.setDate(request.getDate());
        if (request.getStatus() != null) audit.setStatus(request.getStatus());

        Audit saved = auditRepository.save(audit);
                return toResponse(saved);
    }

    public void delete(Integer complianceId, Integer auditId) {

        Audit audit = getEntityByCompliance(complianceId, auditId);
        auditRepository.delete(audit);
           }

    private Audit getEntityByCompliance(Integer complianceId, Integer auditId) {
        return auditRepository.findByAuditIdAndComplianceRecord_ComplianceId(auditId, complianceId)
                .orElseThrow(() -> new NotFoundException("Audit not found"));
    }

    private AuditResponse toResponse(Audit audit) {
        AuditResponse response = modelMapper.map(audit, AuditResponse.class);
        response.setComplianceId(audit.getComplianceRecord() != null ? audit.getComplianceRecord().getComplianceId() : null);
        response.setOfficerId(audit.getOfficer() != null ? audit.getOfficer() : null);
        return response;
    }


}

