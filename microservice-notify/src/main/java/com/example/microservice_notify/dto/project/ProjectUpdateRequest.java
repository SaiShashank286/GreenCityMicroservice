package com.example.microservice_notify.dto.project;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUpdateRequest {


    @Size(max = 255)
    private String title;


    @Size(max = 100000)
    private String description;


    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;


    private LocalDate endDate;


    @PositiveOrZero(message = "Budget cannot be negative")
    private BigDecimal budget;


    @Size(max = 255)
    private String status;


    @AssertTrue(message = "End date must be strictly after the start date")
    private boolean isEndDateValid() {
        if (startDate == null || endDate == null) {
            return true; // Let @NotNull handle the null cases
        }
        return endDate.isAfter(startDate);
    }
}