package com.example.microservice_notify.service;

import com.example.microservice_notify.dto.external.CitizenReportResponse;
import com.example.microservice_notify.dto.external.ProjectResponse;
import com.example.microservice_notify.dto.external.UserResponse;
import com.example.microservice_notify.entity.Notification;
import com.example.microservice_notify.feign.CitizenClient;
import com.example.microservice_notify.feign.ComplianceClient;
import com.example.microservice_notify.feign.ProjectClient;
import com.example.microservice_notify.feign.UserClient;
import com.example.microservice_notify.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private UserClient userClient;
    @Mock
    private ProjectClient projectClient;
    @Mock
    private CitizenClient citizenClient;
    @Mock
    private ComplianceClient complianceClient;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void createNotification_forProject_buildsProjectMessage() {
        UserResponse user = new UserResponse();
        user.setUserId(4);
        when(userClient.getById(4)).thenReturn(user);
        ProjectResponse project = new ProjectResponse();
        project.setProjectId(12L);
        project.setTitle("Green Roof");
        when(projectClient.getProject(12)).thenReturn(project);
        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Notification saved = notificationService.createNotification(4, 12, 12, "PROJECT", "PROJECT");

        assertTrue(saved.getMessage().contains("Project created"));
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void createNotification_forReport_buildsReportMessage() {
        UserResponse user = new UserResponse();
        user.setUserId(8);
        when(userClient.getById(8)).thenReturn(user);
        CitizenReportResponse report = new CitizenReportResponse();
        report.setReportId(90);
        report.setType("POLLUTION");
        when(citizenClient.getReport(90)).thenReturn(report);
        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Notification saved = notificationService.createNotification(8, null, 90, "REPORT", "REPORT");

        assertTrue(saved.getMessage().contains("Citizen report created"));
        verify(notificationRepository).save(any(Notification.class));
    }
}
