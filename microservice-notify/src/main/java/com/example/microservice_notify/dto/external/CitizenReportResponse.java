package com.example.microservice_notify.dto.external;

import lombok.Data;

@Data
public class CitizenReportResponse {
    private Integer reportId;
    private Integer citizenId;
    private String type;
    private String status;
}
