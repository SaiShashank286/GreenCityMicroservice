package com.cognizant.report_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitizenReportResponse {
    private Integer reportId;
    private Integer citizenId;
    private String type;
    private String location;
    private LocalDateTime date;
    private String status;
}
