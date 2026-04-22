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
public class ResourceUpdateRequestDto {

	@Size(max = 50)
	private String type;

	@Size(max = 255)
	private String location;

	@PositiveOrZero
	private Double capacity;

	@Size(max = 50)
	private String status;
}
