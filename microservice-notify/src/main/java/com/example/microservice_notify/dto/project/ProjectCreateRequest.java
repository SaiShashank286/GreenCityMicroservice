package com.example.microservice_notify.dto.project;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectCreateRequest {

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255)
    private String title;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 100000)
    private String description;

    @NotNull(message = "Start date cannot be null")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    private LocalDate endDate;

    @NotNull(message = "Budget cannot be null")
    @PositiveOrZero(message = "Budget cannot be negative")
    private BigDecimal budget;

    @NotBlank(message = "Status cannot be blank")
    @Size(max = 255)
    private String status;


    @AssertTrue(message = "End date must be strictly after the start date")
    private boolean isEndDateValid() {

        if (startDate == null || endDate == null) {
            return true;
        }
        return endDate.isAfter(startDate);
    }
}