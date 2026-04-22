package com.cognizant.complianceservice.repository;

import com.cognizant.complianceservice.entity.ComplianceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplianceRecordRepository extends JpaRepository<ComplianceRecord, Integer> {
}

