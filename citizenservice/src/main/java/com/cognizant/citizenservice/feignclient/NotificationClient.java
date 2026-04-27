package com.cognizant.citizenservice.feignclient;

import com.cognizant.citizenservice.dto.notification.NotificationCreateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service")
public interface NotificationClient {
    @PostMapping("/api/notifications/create")
    void createNotification(@RequestBody NotificationCreateRequest request);
}
