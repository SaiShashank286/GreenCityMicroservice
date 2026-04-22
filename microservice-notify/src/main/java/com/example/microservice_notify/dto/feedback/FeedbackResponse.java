package com.example.microservice_notify.dto.feedback;

import com.example.microservice_notify.entity.Feedback;
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
    private Feedback.Category category;
    private String comments;
    private LocalDate date;
    private String status;
}
