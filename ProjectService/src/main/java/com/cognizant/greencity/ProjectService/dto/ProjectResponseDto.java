package com.cognizant.greencity.ProjectService.dto;

import com.cognizant.greencity.ProjectService.entity.enums.ProjectStatus;
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
public class ProjectResponseDto {

	private Long projectId;
	private Integer createdBy;
	private String title;
	private String description;
	private LocalDate startDate;
	private LocalDate endDate;
	private BigDecimal budget;
	private ProjectStatus status;
}
