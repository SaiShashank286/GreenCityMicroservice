package com.cognizant.report_service.feign;

import com.cognizant.report_service.dto.CitizenReportResponse;
import com.cognizant.report_service.dto.FeedbackResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "citizenservice")
public interface CitizenClient {

    @GetMapping("/api/citizen/all")
    List<CitizenReportResponse> getAllCitizenReports();

    @GetMapping("/api/feedback/all")
    List<FeedbackResponse> getAllFeedbacks();
}
