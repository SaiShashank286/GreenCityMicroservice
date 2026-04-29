package com.cognizant.report_service.service;

import com.cognizant.report_service.dto.CitizenReportResponse;
import com.cognizant.report_service.dto.ComplianceRecordResponse;
import com.cognizant.report_service.dto.FeedbackResponse;
import com.cognizant.report_service.dto.ProjectResponseDto;
import com.cognizant.report_service.dto.ResourceResponseDto;
import com.cognizant.report_service.feign.CitizenClient;
import com.cognizant.report_service.feign.ComplianceClient;
import com.cognizant.report_service.feign.ProjectClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportAggregationService {

    private final ComplianceClient complianceClient;
    private final ProjectClient projectClient;
    private final CitizenClient citizenClient;

    public List<ComplianceRecordResponse> getAllComplianceRecords() {
        return complianceClient.getAllComplianceRecords();
    }

    public List<ProjectResponseDto> getAllProjects() {
        return projectClient.getAllProjects();
    }

    public List<ResourceResponseDto> getResourcesByProject(Long projectId) {
        return projectClient.getResourcesByProject(projectId);
    }

    public List<CitizenReportResponse> getAllReports() {
        return citizenClient.getAllCitizenReports();
    }

    public List<FeedbackResponse> getAllFeedbacks() {
        return citizenClient.getAllFeedbacks();
    }
}
