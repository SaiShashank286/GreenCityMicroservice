package com.cognizant.report_service.controller;

import com.cognizant.report_service.dto.CitizenReportResponse;
import com.cognizant.report_service.dto.ComplianceRecordResponse;
import com.cognizant.report_service.dto.FeedbackResponse;
import com.cognizant.report_service.dto.ProjectResponseDto;
import com.cognizant.report_service.dto.ResourceResponseDto;
import com.cognizant.report_service.service.ReportAggregationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportAggregationService reportAggregationService;

    @GetMapping("/compliance-records")
    public List<ComplianceRecordResponse> listComplianceRecords() {
        return reportAggregationService.getAllComplianceRecords();
    }

    @GetMapping("/projects")
    public List<ProjectResponseDto> listProjects() {
        return reportAggregationService.getAllProjects();
    }

    @GetMapping("/projects/{projectId}/resources")
    public List<ResourceResponseDto> listResourcesByProject(@PathVariable Long projectId) {
        return reportAggregationService.getResourcesByProject(projectId);
    }

    @GetMapping("/all-reports")
    public List<CitizenReportResponse> listAllReports() {
        return reportAggregationService.getAllReports();
    }

    @GetMapping("/feedbacks")
    public List<FeedbackResponse> listAllFeedbacks() {
        return reportAggregationService.getAllFeedbacks();
    }
}
