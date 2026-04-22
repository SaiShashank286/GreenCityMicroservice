package com.example.microservice_notify.dto.compliance;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplianceRecordCreateRequest {

    @NotNull
    private Integer entityId;

    @NotBlank
    @Size(max = 255)
    private String entityType; // "Project" or "Resource"

    @NotBlank
    @Size(max = 255)
    private String result;

    private LocalDateTime date;

    @NotBlank
    @Size(max = 255)
    private String notes;
}
