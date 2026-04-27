package com.example.microservice_notify.service;

import com.example.microservice_notify.entity.Notification;
import com.example.microservice_notify.feign.CitizenClient;
import com.example.microservice_notify.feign.ComplianceClient;
import com.example.microservice_notify.feign.ProjectClient;
import com.example.microservice_notify.feign.UserClient;
import com.example.microservice_notify.repository.NotificationRepository;
import feign.FeignException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserClient userClient;
    private final ProjectClient projectClient;
    private final CitizenClient citizenClient;
    private final ComplianceClient complianceClient;

    public NotificationService(
            NotificationRepository notificationRepository,
            UserClient userClient,
            ProjectClient projectClient,
            CitizenClient citizenClient,
            ComplianceClient complianceClient
    ) {
        this.notificationRepository = notificationRepository;
        this.userClient = userClient;
        this.projectClient = projectClient;
        this.citizenClient = citizenClient;
        this.complianceClient = complianceClient;
    }

    public Notification createNotification(Integer userId, Integer projectId, Integer entityId,
                                           String entityType, String category) {
        validateUser(userId);

        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setEntityId(entityId);
        notification.setCategory(category);
        notification.setEntityType(entityType);
        notification.setStatus("PENDING");

        String type = entityType != null ? entityType.trim().toUpperCase() : "";
        String cat = category != null ? category.trim().toUpperCase() : "";

        notification.setMessage(buildMessage(userId, projectId, entityId, type, cat));

        return notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsByUser(Integer userId) {
        return notificationRepository.findByUserId(userId);
    }

    private void validateUser(Integer userId) {
        try {
            userClient.getById(userId);
        } catch (FeignException.NotFound ex) {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }

    private String buildMessage(Integer userId, Integer projectId, Integer entityId, String type, String category) {
        try {
            return switch (type) {
                case "PROJECT" -> {
                    var project = projectClient.getProject(projectId != null ? projectId : entityId);
                    String projectTitle = project != null && project.getTitle() != null
                            ? project.getTitle()
                            : "project";
                    yield "Project created: " + projectTitle + " by user " + userId;
                }
                case "RESOURCE" -> {
                    var resource = projectClient.getResource(entityId);
                    String resourceType = resource != null && resource.getType() != null
                            ? resource.getType()
                            : "resource";
                    yield "Resource created: " + resourceType + " (id " + entityId + ")";
                }
                case "REPORT" -> {
                    var report = citizenClient.getReport(entityId);
                    String reportType = report != null && report.getType() != null
                            ? report.getType()
                            : "citizen report";
                    yield "Citizen report created: " + reportType + " (id " + entityId + ")";
                }
                case "FEEDBACK" -> {
                    var feedback = citizenClient.getFeedback(entityId);
                    String feedbackCategory = feedback != null && feedback.getCategory() != null
                            ? feedback.getCategory()
                            : "feedback";
                    yield "Feedback created in category " + feedbackCategory + " (id " + entityId + ")";
                }
                case "COMPLIANCE_RECORD" -> {
                    var record = complianceClient.getComplianceRecord(entityId);
                    String result = record != null && record.getResult() != null
                            ? record.getResult()
                            : "PENDING";
                    yield "Compliance record created with result " + result + " (id " + entityId + ")";
                }
                default -> "New " + category + " notification received (Ref ID: " + entityId + ")";
            };
        } catch (FeignException ex) {
            return "New " + category + " notification received (Ref ID: " + entityId + ")";
        }
    }

}
