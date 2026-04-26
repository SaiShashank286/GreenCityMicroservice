package com.cognizant.citizenservice.dto.feedback;

import com.cognizant.citizenservice.entity.Feedback;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    @Size(max = 255)
    private String status;
}
