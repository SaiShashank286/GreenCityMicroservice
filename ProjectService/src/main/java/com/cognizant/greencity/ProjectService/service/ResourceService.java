package com.cognizant.greencity.ProjectService.service;

import com.cognizant.greencity.ProjectService.dto.ResourceCreateRequestDto;
import com.cognizant.greencity.ProjectService.dto.ResourceResponseDto;
import com.cognizant.greencity.ProjectService.dto.ResourceUpdateRequestDto;
import com.cognizant.greencity.ProjectService.dto.ResourceUsageCreateRequestDto;
import com.cognizant.greencity.ProjectService.dto.ResourceUsageResponseDto;
import com.cognizant.greencity.ProjectService.dto.ResourceUsageUpdateRequestDto;
import com.cognizant.greencity.ProjectService.entity.Project;
import com.cognizant.greencity.ProjectService.entity.Resource;
import com.cognizant.greencity.ProjectService.entity.ResourceUsage;
import com.cognizant.greencity.ProjectService.exception.NotFoundException;
import com.cognizant.greencity.ProjectService.repository.ProjectRepository;
import com.cognizant.greencity.ProjectService.repository.ResourceRepository;
import com.cognizant.greencity.ProjectService.repository.ResourceUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ResourceService {

	private final ResourceRepository resourceRepository;
	private final ResourceUsageRepository resourceUsageRepository;
	private final ProjectRepository projectRepository;

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
		return toResourceResponse(resourceRepository.save(resource));
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
		return toUsageResponse(resourceUsageRepository.save(usage));
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
