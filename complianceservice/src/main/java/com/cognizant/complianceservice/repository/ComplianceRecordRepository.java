package com.cognizant.complianceservice.repository;

import com.cognizant.complianceservice.entity.ComplianceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplianceRecordRepository extends JpaRepository<ComplianceRecord, Integer> {
    List<ComplianceRecord> findByOfficerId(Integer officerId);
}

