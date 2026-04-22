package com.example.microservice_notify.dto.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MilestoneResponse {
    private Integer milestoneId;
    private Integer projectId;
    private String title;
    private LocalDate date;
    private String status;
}
