package com.cognizant.greencity.ProjectService.controller;

import com.cognizant.greencity.ProjectService.dto.ResourceCreateRequestDto;
import com.cognizant.greencity.ProjectService.dto.ResourceResponseDto;
import com.cognizant.greencity.ProjectService.dto.ResourceUpdateRequestDto;
import com.cognizant.greencity.ProjectService.dto.ResourceUsageCreateRequestDto;
import com.cognizant.greencity.ProjectService.dto.ResourceUsageResponseDto;
import com.cognizant.greencity.ProjectService.dto.ResourceUsageUpdateRequestDto;
import com.cognizant.greencity.ProjectService.service.ResourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
@RequiredArgsConstructor
public class ResourceController {

	private final ResourceService resourceService;

	@GetMapping
	public List<ResourceResponseDto> listResources(@RequestParam(required = false) Long projectId) {
		return resourceService.listResources(projectId);
	}

	@GetMapping("/{resourceId}")
	public ResourceResponseDto getResource(@PathVariable Long resourceId) {
		return resourceService.getResource(resourceId);
	}

	@GetMapping("/{resourceId}/exists")
	public Boolean resourceExists(@PathVariable Long resourceId) {
		return resourceService.resourceExists(resourceId);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResourceResponseDto createResource(@Valid @RequestBody ResourceCreateRequestDto request) {
		return resourceService.createResource(request);
	}

	@PutMapping("/{resourceId}")
	public ResourceResponseDto updateResource(@PathVariable Long resourceId,
			@Valid @RequestBody ResourceUpdateRequestDto request) {
		return resourceService.updateResource(resourceId, request);
	}

	@DeleteMapping("/{resourceId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteResource(@PathVariable Long resourceId) {
		resourceService.deleteResource(resourceId);
	}

	@GetMapping("/{resourceId}/usages")
	public List<ResourceUsageResponseDto> listUsages(@PathVariable Long resourceId) {
		return resourceService.listUsages(resourceId);
	}

	@PostMapping("/{resourceId}/usages")
	@ResponseStatus(HttpStatus.CREATED)
	public ResourceUsageResponseDto createUsage(@PathVariable Long resourceId,
			@Valid @RequestBody ResourceUsageCreateRequestDto request) {
		return resourceService.createUsage(resourceId, request);
	}

	@GetMapping("/usages/{usageId}")
	public ResourceUsageResponseDto getUsage(@PathVariable Long usageId) {
		return resourceService.getUsage(usageId);
	}

	@PutMapping("/usages/{usageId}")
	public ResourceUsageResponseDto updateUsage(@PathVariable Long usageId,
			@Valid @RequestBody ResourceUsageUpdateRequestDto request) {
		return resourceService.updateUsage(usageId, request);
	}

	@DeleteMapping("/usages/{usageId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUsage(@PathVariable Long usageId) {
		resourceService.deleteUsage(usageId);
	}
}
