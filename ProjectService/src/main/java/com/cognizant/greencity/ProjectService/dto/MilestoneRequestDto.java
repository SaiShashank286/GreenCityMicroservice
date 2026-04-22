package com.cognizant.greencity.ProjectService.dto;

import com.cognizant.greencity.ProjectService.entity.enums.MilestoneStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MilestoneRequestDto {

	@NotNull
	private Long projectId;

	@NotBlank
	private String title;

	@NotNull
	private LocalDate milestoneDate;

	@NotNull
	private MilestoneStatus status;
}
