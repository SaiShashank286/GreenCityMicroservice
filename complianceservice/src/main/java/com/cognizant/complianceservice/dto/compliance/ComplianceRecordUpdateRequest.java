package com.cognizant.complianceservice.dto.compliance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Size;

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
