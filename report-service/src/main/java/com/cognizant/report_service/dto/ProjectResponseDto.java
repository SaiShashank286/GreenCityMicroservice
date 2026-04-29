package com.cognizant.report_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponseDto {

    private Long projectId;
    private Integer createdBy;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal budget;
    private String status;
}
