package com.example.microservice_notify.controller;

import com.example.microservice_notify.dto.NotificationCreateRequest;
import com.example.microservice_notify.entity.Notification;
import com.example.microservice_notify.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Endpoint to create notification directly
    @PostMapping("/create")
    public Notification createNotification(@RequestBody NotificationCreateRequest request) {
        return notificationService.createNotification(
                request.getUserId(),
                request.getProjectId(),
                request.getEntityId(),
                request.getEntityType(),
                request.getCategory()
        );
    }

    // Endpoint to fetch notifications for a user
    @GetMapping("/user/{userId}")
    public List<Notification> getNotificationsByUser(@PathVariable Integer userId) {
        return notificationService.getNotificationsByUser(userId);
    }
}
