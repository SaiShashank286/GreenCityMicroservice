package com.cognizant.citizenservice.dto.report;

import com.cognizant.citizenservice.entity.CitizenReport;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitizenReportUpdateRequest {
    private CitizenReport.ReportType type;

    @Size(max = 255)
    private String location;

    @Size(max = 255)
    private String status;
}
