package com.example.microservice_notify.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MilestoneCreateRequest {

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotNull
    private LocalDate date;

    @NotBlank
    @Size(max = 255)
    private String status;
}
