package com.cognizant.complianceservice.dto.audit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditUpdateRequest {
    @Size(max = 255)
    private String scope;

    private LocalDateTime date;

    @Size(max = 255)
    private String status;

    private String findings;
}
