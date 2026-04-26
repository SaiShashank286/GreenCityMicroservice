package com.cognizant.greencity.ProjectService.service;

import com.cognizant.greencity.ProjectService.dto.NotificationCreateRequestDto;
import com.cognizant.greencity.ProjectService.dto.ResourceCreateRequestDto;
import com.cognizant.greencity.ProjectService.dto.ResourceUsageCreateRequestDto;
import com.cognizant.greencity.ProjectService.entity.Project;
import com.cognizant.greencity.ProjectService.entity.Resource;
import com.cognizant.greencity.ProjectService.entity.ResourceUsage;
import com.cognizant.greencity.ProjectService.entity.enums.ProjectStatus;
import com.cognizant.greencity.ProjectService.feignclient.NotificationClient;
import com.cognizant.greencity.ProjectService.repository.ProjectRepository;
import com.cognizant.greencity.ProjectService.repository.ResourceRepository;
import com.cognizant.greencity.ProjectService.repository.ResourceUsageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResourceServiceTest {

    @Mock
    private ResourceRepository resourceRepository;
    @Mock
    private ResourceUsageRepository resourceUsageRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private NotificationClient notificationClient;

    @InjectMocks
    private ResourceService resourceService;

    @Test
    void createResource_savesResource_andSendsNotification() {
        Project project = Project.builder()
                .projectId(12L)
                .createdBy(9)
                .title("Smart Water")
                .startDate(LocalDate.now())
                .status(ProjectStatus.PLANNED)
                .build();
        when(projectRepository.findById(12L)).thenReturn(Optional.of(project));
        when(resourceRepository.save(any(Resource.class))).thenAnswer(invocation -> {
            Resource r = invocation.getArgument(0);
            r.setResourceId(44L);
            return r;
        });

        ResourceCreateRequestDto request = ResourceCreateRequestDto.builder()
                .projectId(12L)
                .type("WATER")
                .location("Zone A")
                .capacity(1000.0)
                .status("ACTIVE")
                .build();

        var response = resourceService.createResource(request);

        assertEquals(44L, response.getResourceId());
        verify(notificationClient).createNotification(any(NotificationCreateRequestDto.class));
    }

    @Test
    void createUsage_savesUsage_andSendsNotification() {
        Project project = Project.builder()
                .projectId(12L)
                .createdBy(9)
                .title("Smart Water")
                .startDate(LocalDate.now())
                .status(ProjectStatus.PLANNED)
                .build();
        Resource resource = Resource.builder()
                .resourceId(44L)
                .project(project)
                .type("WATER")
                .location("Zone A")
                .capacity(1000.0)
                .status("ACTIVE")
                .build();

        when(resourceRepository.findById(44L)).thenReturn(Optional.of(resource));
        when(resourceUsageRepository.save(any(ResourceUsage.class))).thenAnswer(invocation -> {
            ResourceUsage usage = invocation.getArgument(0);
            usage.setUsageId(77L);
            return usage;
        });

        ResourceUsageCreateRequestDto request = ResourceUsageCreateRequestDto.builder()
                .quantity(300.0)
                .status("OK")
                .build();

        var response = resourceService.createUsage(44L, request);

        assertEquals(77L, response.getUsageId());
        verify(notificationClient).createNotification(any(NotificationCreateRequestDto.class));
    }
}
