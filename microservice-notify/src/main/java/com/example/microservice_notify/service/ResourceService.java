package com.example.microservice_notify.service;

import com.example.microservice_notify.dto.resource.ResourceCreateRequest;
import com.example.microservice_notify.dto.resource.ResourceResponse;
import com.example.microservice_notify.dto.resource.ResourceUpdateRequest;
import com.example.microservice_notify.entity.Project;
import com.example.microservice_notify.entity.Resource;
import com.example.microservice_notify.entity.User;
import com.example.microservice_notify.exception.NotFoundException;
import com.example.microservice_notify.repository.ProjectRepository;
import com.example.microservice_notify.repository.ResourceRepository;
import com.example.microservice_notify.repository.UserRepository;
import com.example.microservice_notify.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;
    private final ModelMapper modelMapper;

    public List<ResourceResponse> list(Integer projectId) {
        if (projectId != null) {
            return resourceRepository.findByProject_ProjectId(projectId).stream().map(this::toResponse).toList();
        }
        return resourceRepository.findAll().stream().map(this::toResponse).toList();
    }

    public ResourceResponse get(Integer id) {
        return toResponse(getEntity(id));
    }

    public ResourceResponse create(ResourceCreateRequest request, Authentication authentication) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new NotFoundException("Project not found"));

        Resource resource = new Resource();
        resource.setProject(project);
        resource.setType(request.getType());
        resource.setLocation(request.getLocation());
        resource.setCapacity(request.getCapacity());
        resource.setStatus(request.getStatus() != null ? request.getStatus() : "ACTIVE");

        Resource saved = resourceRepository.save(resource);
        auditLogService.record(currentUser(authentication), "RESOURCE_CREATE", "resources/" + saved.getResourceId());
        return toResponse(saved);
    }

    public ResourceResponse update(Integer id, ResourceUpdateRequest request, Authentication authentication) {
        Resource resource = getEntity(id);

        if (request.getType() != null) resource.setType(request.getType());
        if (request.getLocation() != null) resource.setLocation(request.getLocation());
        if (request.getCapacity() != null) resource.setCapacity(request.getCapacity());
        if (request.getStatus() != null) resource.setStatus(request.getStatus());

        Resource saved = resourceRepository.save(resource);
        auditLogService.record(currentUser(authentication), "RESOURCE_UPDATE", "resources/" + id);
        return toResponse(saved);
    }

    public void delete(Integer id, Authentication authentication) {
        Resource resource = getEntity(id);
        resourceRepository.delete(resource);
        auditLogService.record(currentUser(authentication), "RESOURCE_DELETE", "resources/" + id);
    }

    private Resource getEntity(Integer id) {
        return resourceRepository.findById(id).orElseThrow(() -> new NotFoundException("Resource not found"));
    }

    private ResourceResponse toResponse(Resource resource) {
        ResourceResponse response = modelMapper.map(resource, ResourceResponse.class);
        response.setProjectId(resource.getProject() != null ? resource.getProject().getProjectId() : null);
        return response;
    }

    private User currentUser(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            return null;
        }
        return userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}

