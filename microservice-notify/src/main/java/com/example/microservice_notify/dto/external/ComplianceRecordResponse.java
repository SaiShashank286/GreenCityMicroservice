package com.example.microservice_notify.dto.external;

import lombok.Data;

@Data
public class ComplianceRecordResponse {
    private Integer complianceId;
    private String result;
    private String entityType;
}
