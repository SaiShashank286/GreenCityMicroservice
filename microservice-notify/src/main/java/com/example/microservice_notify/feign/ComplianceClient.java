package com.example.microservice_notify.feign;

import com.example.microservice_notify.dto.external.ComplianceRecordResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "complianceservice")
public interface ComplianceClient {
    @GetMapping("/api/compliance/{complianceId}")
    ComplianceRecordResponse getComplianceRecord(@PathVariable("complianceId") Integer complianceId);
}
