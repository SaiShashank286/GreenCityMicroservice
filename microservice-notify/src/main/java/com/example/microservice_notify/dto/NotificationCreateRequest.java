package com.example.microservice_notify.dto;

import lombok.Data;

@Data
public class NotificationCreateRequest {
    private Integer userId;
    private Integer projectId;
    private Integer entityId;
    private String entityType;
    private String category;
}
