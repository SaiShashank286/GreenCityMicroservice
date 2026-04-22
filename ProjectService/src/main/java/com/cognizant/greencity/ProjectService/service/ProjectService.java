package com.cognizant.greencity.ProjectService.service;

import com.cognizant.greencity.ProjectService.dto.ImpactRequestDto;
import com.cognizant.greencity.ProjectService.dto.ImpactResponseDto;
import com.cognizant.greencity.ProjectService.dto.MilestoneRequestDto;
import com.cognizant.greencity.ProjectService.dto.MilestoneResponseDto;
import com.cognizant.greencity.ProjectService.dto.ProjectRequestDto;
import com.cognizant.greencity.ProjectService.dto.ProjectResponseDto;
import com.cognizant.greencity.ProjectService.dto.UserDetailsDto;
import com.cognizant.greencity.ProjectService.entity.Impact;
import com.cognizant.greencity.ProjectService.entity.Milestone;
import com.cognizant.greencity.ProjectService.entity.Project;
import com.cognizant.greencity.ProjectService.exception.BadRequestException;
import com.cognizant.greencity.ProjectService.exception.NotFoundException;
import com.cognizant.greencity.ProjectService.feignclient.UserClient;
import com.cognizant.greencity.ProjectService.repository.ImpactRepository;
import com.cognizant.greencity.ProjectService.repository.MilestoneRepository;
import com.cognizant.greencity.ProjectService.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {

	private final ProjectRepository projectRepository;
	private final MilestoneRepository milestoneRepository;
	private final ImpactRepository impactRepository;
	private final UserClient userClient;

	@Transactional(readOnly = true)
	public List<ProjectResponseDto> getAllProjects() {
		return projectRepository.findAll().stream().map(this::toProjectResponse).toList();
	}

	@Transactional(readOnly = true)
	public ProjectResponseDto getProject(Long projectId) {
		return toProjectResponse(getProjectEntity(projectId));
	}

	public ProjectResponseDto createProject(ProjectRequestDto request) {
		validateDateRange(request.getStartDate(), request.getEndDate());
		validateAuthenticatedUser();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		log.info(authentication.getName());
      UserDetailsDto user=userClient.getById(authentication.getName());
       log.info(user.getUserId()+" "+user.getEmail());
		Project project = Project.builder()
				.title(request.getTitle())
				.createdBy(user.getUserId())
				.description(request.getDescription())
				.startDate(request.getStartDate())
				.endDate(request.getEndDate())
				.budget(request.getBudget())
				.status(request.getStatus())
				.build();
		return toProjectResponse(projectRepository.save(project));
	}

	public ProjectResponseDto updateProject(Long projectId, ProjectRequestDto request) {
		Project project = getProjectEntity(projectId);
		validateDateRange(request.getStartDate(), request.getEndDate());

		project.setTitle(request.getTitle());
		project.setDescription(request.getDescription());
		project.setStartDate(request.getStartDate());
		project.setEndDate(request.getEndDate());
		project.setBudget(request.getBudget());
		project.setStatus(request.getStatus());
		return toProjectResponse(projectRepository.save(project));
	}

	public void deleteProject(Long projectId) {
		Project project = getProjectEntity(projectId);
		projectRepository.delete(project);
	}

	@Transactional(readOnly = true)
	public boolean projectExists(Long projectId) {
		return projectRepository.existsById(projectId);
	}

	public MilestoneResponseDto createMilestone(MilestoneRequestDto request) {
		Project project = getProjectEntity(request.getProjectId());
		Milestone milestone = Milestone.builder()
				.project(project)
				.title(request.getTitle())
				.milestoneDate(request.getMilestoneDate())
				.status(request.getStatus())
				.build();
		return toMilestoneResponse(milestoneRepository.save(milestone));
	}

	@Transactional(readOnly = true)
	public List<MilestoneResponseDto> getMilestonesByProject(Long projectId) {
		validateProjectExists(projectId);
		return milestoneRepository.findByProject_ProjectId(projectId).stream()
				.map(this::toMilestoneResponse)
				.toList();
	}

	public ImpactResponseDto createImpact(ImpactRequestDto request) {
		Project project = getProjectEntity(request.getProjectId());
		Impact impact = Impact.builder()
				.project(project)
				.metricsJson(request.getMetricsJson())
				.recordedDate(request.getRecordedDate())
				.status(request.getStatus())
				.build();
		return toImpactResponse(impactRepository.save(impact));
	}

	@Transactional(readOnly = true)
	public List<ImpactResponseDto> getImpactsByProject(Long projectId) {
		validateProjectExists(projectId);
		return impactRepository.findByProject_ProjectId(projectId).stream()
				.map(this::toImpactResponse)
				.toList();
	}

	private void validateAuthenticatedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication.getName() == null) {
			throw new BadRequestException("Authenticated user details are missing");
		}
		UserDetailsDto user = userClient.getById(authentication.getName());
		if (user == null || user.getUserId() == null) {
			throw new BadRequestException("Authenticated user is not found in user-service");
		}
	}

	private void validateDateRange(java.time.LocalDate startDate, java.time.LocalDate endDate) {
		if (endDate != null && endDate.isBefore(startDate)) {
			throw new BadRequestException("endDate cannot be before startDate");
		}
	}

	private Project getProjectEntity(Long projectId) {
		return projectRepository.findById(projectId)
				.orElseThrow(() -> new NotFoundException("Project with id " + projectId + " not found"));
	}

	private void validateProjectExists(Long projectId) {
		if (!projectRepository.existsById(projectId)) {
			throw new NotFoundException("Project with id " + projectId + " not found");
		}
	}

	private ProjectResponseDto toProjectResponse(Project project) {
		return ProjectResponseDto.builder()
				.projectId(project.getProjectId())
				.createdBy(project.getCreatedBy())
				.title(project.getTitle())
				.description(project.getDescription())
				.startDate(project.getStartDate())
				.endDate(project.getEndDate())
				.budget(project.getBudget())
				.status(project.getStatus())
				.build();
	}

	private MilestoneResponseDto toMilestoneResponse(Milestone milestone) {
		return MilestoneResponseDto.builder()
				.milestoneId(milestone.getMilestoneId())
				.projectId(milestone.getProject().getProjectId())
				.title(milestone.getTitle())
				.milestoneDate(milestone.getMilestoneDate())
				.status(milestone.getStatus())
				.build();
	}

	private ImpactResponseDto toImpactResponse(Impact impact) {
		return ImpactResponseDto.builder()
				.impactId(impact.getImpactId())
				.projectId(impact.getProject().getProjectId())
				.metricsJson(impact.getMetricsJson())
				.recordedDate(impact.getRecordedDate())
				.status(impact.getStatus())
				.build();
	}
}
