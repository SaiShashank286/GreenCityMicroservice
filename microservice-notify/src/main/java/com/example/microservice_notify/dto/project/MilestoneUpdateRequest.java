package com.example.microservice_notify.dto.project;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MilestoneUpdateRequest {

    @Size(max = 255)
    private String title;

    private LocalDate date;

    @Size(max = 255)
    private String status;
}
