package com.example.microservice_notify.dto.external;

import lombok.Data;

@Data
public class FeedbackResponse {
    private Integer feedbackId;
    private Integer citizenId;
    private String category;
    private String status;
}
