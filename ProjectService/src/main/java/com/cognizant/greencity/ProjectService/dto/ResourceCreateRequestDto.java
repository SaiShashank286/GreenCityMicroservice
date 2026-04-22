package com.cognizant.greencity.ProjectService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ResourceCreateRequestDto {

	@NotNull
	private Long projectId;

	@NotBlank
	@Size(max = 50)
	private String type;

	@NotBlank
	@Size(max = 255)
	private String location;

	@NotNull
	@PositiveOrZero
	private Double capacity;

	@NotBlank
	@Size(max = 50)
	private String status;
}
