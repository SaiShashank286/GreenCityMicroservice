package com.cognizant.greencity.ProjectService.dto;

import com.cognizant.greencity.ProjectService.entity.enums.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectRequestDto {

	@NotBlank
	private String title;

	private String description;

	@NotNull
	private LocalDate startDate;

	@NotNull
	private LocalDate endDate;

	private BigDecimal budget;

	@NotNull
	private ProjectStatus status;
}
