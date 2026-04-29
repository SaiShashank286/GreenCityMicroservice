package com.cognizant.report_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceResponseDto {
    private Long resourceId;
    private Long projectId;
    private String type;
    private String location;
    private Double capacity;
    private String status;
}
