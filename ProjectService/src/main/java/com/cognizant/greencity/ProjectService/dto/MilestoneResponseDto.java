package com.cognizant.greencity.ProjectService.dto;

import com.cognizant.greencity.ProjectService.entity.enums.MilestoneStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MilestoneResponseDto {

	private Long milestoneId;
	private Long projectId;
	private String title;
	private LocalDate milestoneDate;
	private MilestoneStatus status;
}
