package com.cognizant.greencity.ProjectService.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationCreateRequestDto {
    private Integer userId;
    private Integer projectId;
    private Integer entityId;
    private String entityType;
    private String category;
}
