package com.cognizant.report_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplianceRecordResponse {
    private Integer complianceId;
    private Integer entityId;
    private String entityType;
    private String result;
    private LocalDateTime date;
    private String notes;
}
