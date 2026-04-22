package com.example.microservice_notify.repository;

import com.example.microservice_notify.entity.ComplianceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplianceRecordRepository extends JpaRepository<ComplianceRecord, Integer> {

}

