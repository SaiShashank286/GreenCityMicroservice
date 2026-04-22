package com.example.microservice_notify.dto.compliance;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplianceRecordUpdateRequest {

    @Size(max = 255)
    private String result;

    private LocalDateTime date;

    @Size(max = 255)
    private String notes;
}
