package com.cognizant.greencity.ProjectService.dto;

import com.cognizant.greencity.ProjectService.entity.enums.ImpactStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImpactResponseDto {

	private Long impactId;
	private Long projectId;
	private Map<String, Object> metricsJson;
	private LocalDate recordedDate;
	private ImpactStatus status;
}
