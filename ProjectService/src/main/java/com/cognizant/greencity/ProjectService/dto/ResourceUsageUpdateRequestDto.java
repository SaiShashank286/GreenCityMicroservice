package com.cognizant.greencity.ProjectService.dto;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceUsageUpdateRequestDto {

	@PositiveOrZero
	private Double quantity;

	@Size(max = 50)
	private String status;
}
