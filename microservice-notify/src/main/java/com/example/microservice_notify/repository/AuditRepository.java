package com.example.microservice_notify.repository;

import com.example.microservice_notify.entity.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuditRepository extends JpaRepository<Audit, Integer> {
    List<Audit> findByComplianceRecord_ComplianceId(Integer complianceId);

    Optional<Audit> findByAuditIdAndComplianceRecord_ComplianceId(Integer auditId, Integer complianceId);
}

