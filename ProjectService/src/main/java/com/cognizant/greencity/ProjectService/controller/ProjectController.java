package com.cognizant.greencity.ProjectService.controller;

import com.cognizant.greencity.ProjectService.dto.ImpactRequestDto;
import com.cognizant.greencity.ProjectService.dto.ImpactResponseDto;
import com.cognizant.greencity.ProjectService.dto.MilestoneRequestDto;
import com.cognizant.greencity.ProjectService.dto.MilestoneResponseDto;
import com.cognizant.greencity.ProjectService.dto.ProjectRequestDto;
import com.cognizant.greencity.ProjectService.dto.ProjectResponseDto;
import com.cognizant.greencity.ProjectService.service.ProjectService;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

	private final ProjectService projectService;

	@GetMapping
	public List<ProjectResponseDto> listProjects() {
		return projectService.getAllProjects();
	}

	@GetMapping("/{projectId:\\d+}")
	public ProjectResponseDto getProject(@PathVariable Long projectId) {
		return projectService.getProject(projectId);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProjectResponseDto createProject(@Valid @RequestBody ProjectRequestDto request) {
		return projectService.createProject(request);
	}

	@PutMapping("/{projectId}")
	public ProjectResponseDto updateProject(@PathVariable Long projectId, @Valid @RequestBody ProjectRequestDto request) {
		return projectService.updateProject(projectId, request);
	}

	@DeleteMapping("/{projectId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteProject(@PathVariable Long projectId) {
		projectService.deleteProject(projectId);
	}

	@GetMapping("/{projectId}/exists")
	public Boolean projectExists(@PathVariable Long projectId) {
		return projectService.projectExists(projectId);
	}

	@GetMapping("/{projectId}/milestones")
	public List<MilestoneResponseDto> listMilestonesByProject(@PathVariable Long projectId) {
		return projectService.getMilestonesByProject(projectId);
	}

	@PostMapping("/milestones")
	@ResponseStatus(HttpStatus.CREATED)
	public MilestoneResponseDto createMilestone(@Valid @RequestBody MilestoneRequestDto request) {
		return projectService.createMilestone(request);
	}

	@GetMapping("/{projectId}/impacts")
	public List<ImpactResponseDto> listImpactsByProject(@PathVariable Long projectId) {
		return projectService.getImpactsByProject(projectId);
	}

	@PostMapping("/impacts")
	@ResponseStatus(HttpStatus.CREATED)
	public ImpactResponseDto createImpact(@Valid @RequestBody ImpactRequestDto request) {
		return projectService.createImpact(request);
	}
}
