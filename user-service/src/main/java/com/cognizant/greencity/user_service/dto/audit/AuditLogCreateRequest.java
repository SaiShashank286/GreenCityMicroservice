package com.cognizant.greencity.user_service.dto.audit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogCreateRequest {
    @NotNull
    private Integer userId;

    @NotBlank
    @Size(max = 255)
    private String action;

    @Size(max = 255)
    private String resources;
}
