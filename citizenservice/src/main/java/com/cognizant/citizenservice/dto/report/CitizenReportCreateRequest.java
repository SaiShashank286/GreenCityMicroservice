package com.cognizant.citizenservice.dto.report;

import com.cognizant.citizenservice.entity.CitizenReport;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitizenReportCreateRequest {
    @NotNull
    private CitizenReport.ReportType type;

    @NotBlank
    @Size(max = 255)
    private String location;

    @NotBlank
    @Size(max = 255)
    private String status;
}
