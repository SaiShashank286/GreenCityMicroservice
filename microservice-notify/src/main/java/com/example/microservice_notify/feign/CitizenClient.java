package com.example.microservice_notify.feign;

import com.example.microservice_notify.dto.external.CitizenReportResponse;
import com.example.microservice_notify.dto.external.FeedbackResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "citizenservice")
public interface CitizenClient {
    @GetMapping("/api/citizen/{id}")
    CitizenReportResponse getReport(@PathVariable("id") Integer id);

    @GetMapping("/api/feedback/{id}")
    FeedbackResponse getFeedback(@PathVariable("id") Integer id);
}
