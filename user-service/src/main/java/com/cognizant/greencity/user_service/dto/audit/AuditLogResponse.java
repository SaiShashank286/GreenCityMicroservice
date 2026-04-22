package com.cognizant.greencity.user_service.dto.audit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogResponse {
    private Integer auditId;
    private Integer userId;
    private String action;
    private String resources;
    private LocalDateTime timestamp;
}
