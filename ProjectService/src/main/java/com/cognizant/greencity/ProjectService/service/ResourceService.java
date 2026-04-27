package com.cognizant.greencity.ProjectService.service;

import com.cognizant.greencity.ProjectService.dto.ResourceCreateRequestDto;
import com.cognizant.greencity.ProjectService.dto.ResourceResponseDto;
import com.cognizant.greencity.ProjectService.dto.ResourceUpdateRequestDto;
import com.cognizant.greencity.ProjectService.dto.ResourceUsageCreateRequestDto;
import com.cognizant.greencity.ProjectService.dto.ResourceUsageResponseDto;
import com.cognizant.greencity.ProjectService.dto.ResourceUsageUpdateRequestDto;
import com.cognizant.greencity.ProjectService.dto.NotificationCreateRequestDto;
import com.cognizant.greencity.ProjectService.entity.Project;
import com.cognizant.greencity.ProjectService.entity.Resource;
import com.cognizant.greencity.ProjectService.entity.ResourceUsage;
import com.cognizant.greencity.ProjectService.exception.NotFoundException;
import com.cognizant.greencity.ProjectService.feignclient.NotificationClient;
import com.cognizant.greencity.ProjectService.repository.ProjectRepository;
import com.cognizant.greencity.ProjectService.repository.ResourceRepository;
import com.cognizant.greencity.ProjectService.repository.ResourceUsageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ResourceService {

	private final ResourceRepository resourceRepository;
	private final ResourceUsageRepository resourceUsageRepository;
	private final ProjectRepository projectRepository;
	private final NotificationClient notificationClient;

	@Transactional(readOnly = true)
	public List<ResourceResponseDto> listResources(Long projectId) {
		List<Resource> resources = projectId == null
				? resourceRepository.findAll()
				: resourceRepository.findByProject_ProjectId(projectId);
		return resources.stream().map(this::toResourceResponse).toList();
	}

	@Transactional(readOnly = true)
	public ResourceResponseDto getResource(Long resourceId) {
		return toResourceResponse(getResourceEntity(resourceId));
	}

	@Transactional(readOnly = true)
	public boolean resourceExists(Long resourceId) {
		return resourceRepository.existsById(resourceId);
	}

	public ResourceResponseDto createResource(ResourceCreateRequestDto request) {
		Project project = getProjectEntity(request.getProjectId());
		Resource resource = Resource.builder()
				.project(project)
				.type(request.getType())
				.location(request.getLocation())
				.capacity(request.getCapacity())
				.status(request.getStatus())
				.build();
		Resource saved = resourceRepository.save(resource);
		sendNotification(project.getCreatedBy(), project.getProjectId(), saved.getResourceId(), "RESOURCE", "PROJECT");
		return toResourceResponse(saved);
	}

	public ResourceResponseDto updateResource(Long resourceId, ResourceUpdateRequestDto request) {
		Resource resource = getResourceEntity(resourceId);

		if (request.getType() != null) {
			resource.setType(request.getType());
		}
		if (request.getLocation() != null) {
			resource.setLocation(request.getLocation());
		}
		if (request.getCapacity() != null) {
			resource.setCapacity(request.getCapacity());
		}
		if (request.getStatus() != null) {
			resource.setStatus(request.getStatus());
		}

		return toResourceResponse(resourceRepository.save(resource));
	}

	public void deleteResource(Long resourceId) {
		Resource resource = getResourceEntity(resourceId);
		resourceRepository.delete(resource);
	}

	@Transactional(readOnly = true)
	public List<ResourceUsageResponseDto> listUsages(Long resourceId) {
		validateResourceExists(resourceId);
		return resourceUsageRepository.findByResource_ResourceId(resourceId)
				.stream()
				.map(this::toUsageResponse)
				.toList();
	}

	@Transactional(readOnly = true)
	public ResourceUsageResponseDto getUsage(Long usageId) {
		return toUsageResponse(getUsageEntity(usageId));
	}

	public ResourceUsageResponseDto createUsage(Long resourceId, ResourceUsageCreateRequestDto request) {
		Resource resource = getResourceEntity(resourceId);
		ResourceUsage usage = ResourceUsage.builder()
				.resource(resource)
				.quantity(request.getQuantity())
				.status(request.getStatus())
				.date(LocalDateTime.now())
				.build();
		ResourceUsage saved = resourceUsageRepository.save(usage);
		sendNotification(
				resource.getProject().getCreatedBy(),
				resource.getProject().getProjectId(),
				saved.getUsageId(),
				"RESOURCE_USAGE",
				"RESOURCE"
		);
		return toUsageResponse(saved);
	}

	private void sendNotification(Integer userId, Long projectId, Long entityId, String entityType, String category) {
		if (userId == null || entityId == null) {
			return;
		}
		try {
			notificationClient.createNotification(NotificationCreateRequestDto.builder()
					.userId(userId)
					.projectId(projectId != null ? projectId.intValue() : null)
					.entityId(entityId.intValue())
					.entityType(entityType)
					.category(category)
					.build());
		} catch (Exception ex) {
			log.warn("Notification call failed for {} {}", entityType, entityId, ex);
		}
	}

	public ResourceUsageResponseDto updateUsage(Long usageId, ResourceUsageUpdateRequestDto request) {
		ResourceUsage usage = getUsageEntity(usageId);

		if (request.getQuantity() != null) {
			usage.setQuantity(request.getQuantity());
		}
		if (request.getStatus() != null) {
			usage.setStatus(request.getStatus());
		}

		return toUsageResponse(resourceUsageRepository.save(usage));
	}

	public void deleteUsage(Long usageId) {
		ResourceUsage usage = getUsageEntity(usageId);
		resourceUsageRepository.delete(usage);
	}

	private Resource getResourceEntity(Long resourceId) {
		return resourceRepository.findById(resourceId)
				.orElseThrow(() -> new NotFoundException("Resource with id " + resourceId + " not found"));
	}

	private ResourceUsage getUsageEntity(Long usageId) {
		return resourceUsageRepository.findById(usageId)
				.orElseThrow(() -> new NotFoundException("Resource usage with id " + usageId + " not found"));
	}

	private Project getProjectEntity(Long projectId) {
		return projectRepository.findById(projectId)
				.orElseThrow(() -> new NotFoundException("Project with id " + projectId + " not found"));
	}

	private void validateResourceExists(Long resourceId) {
		if (!resourceRepository.existsById(resourceId)) {
			throw new NotFoundException("Resource with id " + resourceId + " not found");
		}
	}

	private ResourceResponseDto toResourceResponse(Resource resource) {
		return ResourceResponseDto.builder()
				.resourceId(resource.getResourceId())
				.projectId(resource.getProject().getProjectId())
				.type(resource.getType())
				.location(resource.getLocation())
				.capacity(resource.getCapacity())
				.status(resource.getStatus())
				.build();
	}

	private ResourceUsageResponseDto toUsageResponse(ResourceUsage usage) {
		return ResourceUsageResponseDto.builder()
				.usageId(usage.getUsageId())
				.resourceId(usage.getResource().getResourceId())
				.quantity(usage.getQuantity())
				.date(usage.getDate())
				.status(usage.getStatus())
				.build();
	}
}
