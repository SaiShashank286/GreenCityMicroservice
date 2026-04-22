package com.example.microservice_notify.service;

import com.example.microservice_notify.dto.project.MilestoneCreateRequest;
import com.example.microservice_notify.dto.project.MilestoneResponse;
import com.example.microservice_notify.dto.project.MilestoneUpdateRequest;
import com.example.microservice_notify.entity.Milestone;
import com.example.microservice_notify.entity.Project;
import com.example.microservice_notify.entity.User;
import com.example.microservice_notify.exception.NotFoundException;
import com.example.microservice_notify.exception.UnauthorizedException;
import com.example.microservice_notify.repository.MilestoneRepository;
import com.example.microservice_notify.repository.ProjectRepository;
import com.example.microservice_notify.repository.UserRepository;
import com.example.microservice_notify.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MilestoneService {

    private final MilestoneRepository milestoneRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;
    private final ModelMapper modelMapper;

    public List<MilestoneResponse> list(Integer projectId, Authentication authentication) {
        enforceProjectOwner(projectId, authentication);
        return milestoneRepository.findByProject_ProjectId(projectId).stream().map(this::toResponse).toList();
    }

    public MilestoneResponse get(Integer projectId, Integer milestoneId, Authentication authentication) {
        enforceProjectOwner(projectId, authentication);
        return toResponse(getEntity(projectId, milestoneId));
    }

    public MilestoneResponse create(Integer projectId, MilestoneCreateRequest request, Authentication authentication) {
        User user = enforceProjectOwner(projectId, authentication);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));

        Milestone milestone = new Milestone();
        milestone.setProject(project);
        milestone.setTitle(request.getTitle());
        milestone.setDate(request.getDate() != null ? request.getDate() : LocalDate.now());
        milestone.setStatus(request.getStatus());

        Milestone saved = milestoneRepository.save(milestone);
        auditLogService.record(user, "MILESTONE_CREATE", "projects/" + projectId + "/milestones/" + saved.getMilestoneId());
        return toResponse(saved);
    }

    public MilestoneResponse update(Integer projectId, Integer milestoneId, MilestoneUpdateRequest request, Authentication authentication) {
        User user = enforceProjectOwner(projectId, authentication);
        Milestone milestone = getEntity(projectId, milestoneId);

        if (request.getTitle() != null) milestone.setTitle(request.getTitle());
        if (request.getDate() != null) milestone.setDate(request.getDate());
        if (request.getStatus() != null) milestone.setStatus(request.getStatus());

        Milestone saved = milestoneRepository.save(milestone);
        auditLogService.record(user, "MILESTONE_UPDATE", "projects/" + projectId + "/milestones/" + milestoneId);
        return toResponse(saved);
    }

    public void delete(Integer projectId, Integer milestoneId, Authentication authentication) {
        User user = enforceProjectOwner(projectId, authentication);
        Milestone milestone = getEntity(projectId, milestoneId);
        milestoneRepository.delete(milestone);
        auditLogService.record(user, "MILESTONE_DELETE", "projects/" + projectId + "/milestones/" + milestoneId);
    }

    private Milestone getEntity(Integer projectId, Integer milestoneId) {
        return milestoneRepository.findByMilestoneIdAndProject_ProjectId(milestoneId, projectId)
                .orElseThrow(() -> new NotFoundException("Milestone not found"));
    }

    private MilestoneResponse toResponse(Milestone milestone) {
        MilestoneResponse response = modelMapper.map(milestone, MilestoneResponse.class);
        response.setProjectId(milestone.getProject() != null ? milestone.getProject().getProjectId() : null);
        return response;
    }

    private User enforceProjectOwner(Integer projectId, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            throw new UnauthorizedException("Unauthorized");
        }

        User user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));

        if (project.getCreatedBy() == null || project.getCreatedBy().getUserId() == null || !user.getUserId().equals(project.getCreatedBy().getUserId())) {
            throw new UnauthorizedException("Not allowed");
        }
        return user;
    }
}

