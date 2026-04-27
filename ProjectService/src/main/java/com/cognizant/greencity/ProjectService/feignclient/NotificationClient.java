package com.cognizant.greencity.ProjectService.feignclient;

import com.cognizant.greencity.ProjectService.dto.NotificationCreateRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service")
public interface NotificationClient {
    @PostMapping("/api/notifications/create")
    void createNotification(@RequestBody NotificationCreateRequestDto request);
}
