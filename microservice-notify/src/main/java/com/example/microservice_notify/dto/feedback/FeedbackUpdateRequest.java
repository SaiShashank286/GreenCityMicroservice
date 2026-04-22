package com.example.microservice_notify.dto.feedback;

import com.example.microservice_notify.entity.Feedback;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackUpdateRequest {
    private Feedback.Category category;

    @Size(max = 255)
    private String comments;

    @Size(max = 255)
    private String status;
}
