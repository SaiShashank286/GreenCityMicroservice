package com.cognizant.greencity.ProjectService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceUsageResponseDto {

	private Long usageId;
	private Long resourceId;
	private Double quantity;
	private LocalDateTime date;
	private String status;
}
