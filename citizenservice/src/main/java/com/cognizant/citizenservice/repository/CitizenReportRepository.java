package com.cognizant.citizenservice.repository;

import com.cognizant.citizenservice.entity.CitizenReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CitizenReportRepository extends JpaRepository<CitizenReport, Integer> {
    // REMOVE: List<CitizenReport> findByCitizen_UserId(Integer userId);
    List<CitizenReport> findByCitizen(Integer citizen);
}