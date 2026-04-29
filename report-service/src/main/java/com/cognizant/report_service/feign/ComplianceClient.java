package com.cognizant.report_service.feign;

import com.cognizant.report_service.dto.ComplianceRecordResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "complianceservice")
public interface ComplianceClient {

    @GetMapping("/api/compliance")
    List<ComplianceRecordResponse> getAllComplianceRecords();
}
