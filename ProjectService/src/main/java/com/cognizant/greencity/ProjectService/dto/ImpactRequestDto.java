package com.cognizant.greencity.ProjectService.dto;

import com.cognizant.greencity.ProjectService.entity.enums.ImpactStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImpactRequestDto {

	@NotNull
	private Long projectId;

	@NotNull
	@Builder.Default
	private Map<String, Object> metricsJson = new HashMap<>();

	@NotNull
	private LocalDate recordedDate;

	@NotNull
	private ImpactStatus status;
}
