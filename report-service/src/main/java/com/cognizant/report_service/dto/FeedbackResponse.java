package com.cognizant.report_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponse {
    private Integer feedbackId;
    private Integer citizenId;
    private String category;
    private String comments;
    private LocalDate date;
    private String status;
}
