package com.cognizant.greencity.ProjectService.service;

import com.cognizant.greencity.ProjectService.dto.MilestoneRequestDto;
import com.cognizant.greencity.ProjectService.dto.NotificationCreateRequestDto;
import com.cognizant.greencity.ProjectService.dto.ProjectRequestDto;
import com.cognizant.greencity.ProjectService.dto.UserDetailsDto;
import com.cognizant.greencity.ProjectService.entity.Milestone;
import com.cognizant.greencity.ProjectService.entity.Project;
import com.cognizant.greencity.ProjectService.entity.enums.MilestoneStatus;
import com.cognizant.greencity.ProjectService.entity.enums.ProjectStatus;
import com.cognizant.greencity.ProjectService.feignclient.NotificationClient;
import com.cognizant.greencity.ProjectService.feignclient.UserClient;
import com.cognizant.greencity.ProjectService.repository.ImpactRepository;
import com.cognizant.greencity.ProjectService.repository.MilestoneRepository;
import com.cognizant.greencity.ProjectService.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private MilestoneRepository milestoneRepository;
    @Mock
    private ImpactRepository impactRepository;
    @Mock
    private UserClient userClient;
    @Mock
    private NotificationClient notificationClient;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void createProject_savesProject_andSendsNotification() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("planner@demo.com", null)
        );
        UserDetailsDto user = new UserDetailsDto();
        user.setUserId(7);
        when(userClient.getById("planner@demo.com")).thenReturn(user);
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> {
            Project p = invocation.getArgument(0);
            p.setProjectId(101L);
            return p;
        });

        ProjectRequestDto request = ProjectRequestDto.builder()
                .title("Solar Grid")
                .description("Test")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(5))
                .status(ProjectStatus.PLANNED)
                .build();

        var response = projectService.createProject(request);

        assertEquals(101L, response.getProjectId());
        ArgumentCaptor<NotificationCreateRequestDto> captor = ArgumentCaptor.forClass(NotificationCreateRequestDto.class);
        verify(notificationClient).createNotification(captor.capture());
        assertEquals(7, captor.getValue().getUserId());
        assertEquals("PROJECT", captor.getValue().getEntityType());
    }

    @Test
    void createMilestone_savesMilestone_andSendsNotification() {
        Project project = Project.builder()
                .projectId(11L)
                .createdBy(3)
                .title("P")
                .startDate(LocalDate.now())
                .status(ProjectStatus.PLANNED)
                .build();
        when(projectRepository.findById(11L)).thenReturn(Optional.of(project));
        when(milestoneRepository.save(any(Milestone.class))).thenAnswer(invocation -> {
            Milestone m = invocation.getArgument(0);
            m.setMilestoneId(25L);
            return m;
        });

        MilestoneRequestDto request = MilestoneRequestDto.builder()
                .projectId(11L)
                .title("Phase 1")
                .milestoneDate(LocalDate.now().plusDays(2))
                .status(MilestoneStatus.PENDING)
                .build();

        var response = projectService.createMilestone(request);

        assertEquals(25L, response.getMilestoneId());
        verify(notificationClient).createNotification(any(NotificationCreateRequestDto.class));
    }
}
