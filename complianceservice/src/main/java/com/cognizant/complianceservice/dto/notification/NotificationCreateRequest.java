package com.cognizant.complianceservice.dto.notification;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationCreateRequest {
    private Integer userId;
    private Integer projectId;
    private Integer entityId;
    private String entityType;
    private String category;
}
