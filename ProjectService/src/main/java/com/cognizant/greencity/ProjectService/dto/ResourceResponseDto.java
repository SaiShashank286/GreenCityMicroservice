package com.cognizant.greencity.ProjectService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceResponseDto {

	private Long resourceId;
	private Long projectId;
	private String type;
	private String location;
	private Double capacity;
	private String status;
}
