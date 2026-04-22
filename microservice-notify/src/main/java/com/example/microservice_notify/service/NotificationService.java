package com.example.microservice_notify.service;

import com.example.microservice_notify.entity.ComplianceRecord;
import com.example.microservice_notify.entity.Notification;
import com.example.microservice_notify.entity.User;
import com.example.microservice_notify.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ResourceRepository resourceRepository; // Add this
//    private final ResourceUsageRepository resourceUsageRepository;
    private final ComplianceRecordRepository complianceRecordRepository;
    private final FeedbackRepository feedbackRepository;// Add this

    public NotificationService(NotificationRepository notificationRepository,
                               UserRepository userRepository,
                               ResourceRepository resourceRepository,
//                               ResourceUsageRepository resourceUsageRepository,
                               ComplianceRecordRepository complianceRecordRepository,
                               FeedbackRepository feedbackRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.resourceRepository=resourceRepository;
//        this.resourceUsageRepository=resourceUsageRepository;
        this.feedbackRepository=feedbackRepository;
        this.complianceRecordRepository=complianceRecordRepository;
    }

    public Notification createNotification(Integer userId, Integer projectId, Integer milestoneId,
                                           String entityType, String category) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Notification notification = new Notification();
        notification.setUserId(user.getUserId());
        notification.setEntityId(milestoneId);
        notification.setCategory(category);
        notification.setEntityType(entityType);
        notification.setStatus("PENDING");

        // Normalize strings for comparison
        String type = entityType != null ? entityType.trim().toUpperCase() : "";
        String cat = category != null ? category.trim().toUpperCase() : "";

        // 1. Check for Milestone/Project Logic
        if ("MILESTONE".equals(type) && "PROJECT".equals(cat)) {
            LocalDate deadline = notificationRepository.findDeadlineByMilestoneAndProject(milestoneId, projectId);

            if (deadline == null) {
                notification.setMessage("Milestone " + milestoneId + " found, but no deadline is set.");
            } else if (deadline.isBefore(LocalDate.now())) {
                notification.setMessage("CRITICAL: Milestone " + milestoneId + " passed its deadline on " + deadline);
            } else if (deadline.isBefore(LocalDate.now().plusDays(3))) {
                notification.setMessage("URGENT: Milestone " + milestoneId + " is due in less than 3 days (" + deadline + ")");
            } else {
                notification.setMessage("Reminder: Milestone " + milestoneId + " is scheduled for " + deadline);
            }
        }
        // 2. Add your other categories here (RESOURCE, COMPLIANCE, etc.)
        if ("RESOURCE_USAGE".equals(type) && "RESOURCE".equals(cat)) {

            // 1. You still need the 'currentUsage' from the database to compare against
            Double currentUsage = resourceRepository.findCurrentUsageByResourceId(milestoneId);

            // 2. Get the resource type (e.g., "ENERGY") from the DB based on the ID
            String resourceType = resourceRepository.findTypeById(milestoneId);

            // 3. Get the Max Capacity from our internal function logic
            Double limit = getMaxCapacityByType(resourceType);

            // 4. The 120% Rule check
            if (currentUsage > (limit * 1.2)) {
                notification.setMessage(String.format("%s usage exceeded safe limit! Current: %.1f, Limit: %.1f",
                        resourceType, currentUsage, limit));
            } else {
                notification.setMessage(resourceType + " usage is within acceptable parameters.");
            }
        }
        // --- Logic Branch: COMPLIANCE ---
        else if ("COMPLIANCE_RECORD".equals(type) && "COMPLIANCE".equals(cat)) {

            // Fetch the record using milestoneId (passed as complianceId from controller)
            ComplianceRecord record = complianceRecordRepository.findById(milestoneId).orElse(null);

            if (record == null) {
                notification.setMessage("Compliance record " + milestoneId + " not found.");
            }
            else {
                LocalDateTime deadline = record.getDate(); // 'date' column is the deadline
                String result = record.getResult();

                // RULE 1: If result is FAIL, notify immediately (Violation)
                if ("FAIL".equalsIgnoreCase(result)) {
                    notification.setMessage("Non-compliance detected in Waste Disposal Project");
                    notification.setCategory("COMPLIANCE");
                }
                // RULE 2: If result isn't FAIL, check if the DEADLINE has passed
                else if (deadline != null && deadline.isBefore(LocalDateTime.now())) {
                    notification.setMessage("CRITICAL: Compliance deadline passed on " + deadline.toLocalDate());
                    notification.setStatus("OVERDUE");
                }
                // RULE 3: Check if the DEADLINE is approaching (within 3 days)
                else if (deadline != null && deadline.isBefore(LocalDateTime.now().plusDays(3))) {
                    notification.setMessage("URGENT: Compliance deadline for record " + milestoneId + " is approaching on " + deadline.toLocalDate());
                }
                else {
                    notification.setMessage("Compliance record " + milestoneId + " is currently " + (result != null ? result : "PENDING"));
                }
            }
        }
        // --- Logic Branch: FEEDBACK ---
        else if ("FEEDBACK".equals(type) && "FEEDBACK".equals(cat)) {

            // Fetch current status from DB
            String feedbackStatus = feedbackRepository.findStatusById(milestoneId);

            if (feedbackStatus == null) {
                notification.setMessage("Feedback record " + milestoneId + " not found.");
            }
            // Rule: If status = RESOLVED
            else if ("RESOLVED".equalsIgnoreCase(feedbackStatus)) {
                notification.setMessage("Your feedback on pollution has been reviewed and resolved");
                notification.setStatus("SENT");
            }
            // Rule: If status = OPEN
            else if ("OPEN".equalsIgnoreCase(feedbackStatus)) {
                notification.setMessage("Your feedback is considered and will be implemented");
                notification.setStatus("PENDING");
            }
            else {
                notification.setMessage("Feedback status updated: " + feedbackStatus);
                notification.setStatus("PENDING");
            }
        }
        // 3. The Fallback (only for truly unknown data)
        else {
            notification.setMessage(String.format("New %s notification received (Ref ID: %d)", type, milestoneId));
        }

        return notificationRepository.save(notification);
    }
    public List<Notification> getNotificationsByUser(Integer userId) {
        return notificationRepository.findByUserId(userId);
    }

    private Double getMaxCapacityByType(String resourceType) {
        if (resourceType == null) return 0.0;

        return switch (resourceType.toUpperCase()) {
            case "ENERGY" -> 500.0;   // Based on your screenshot for 'Solar Plant'
            case "WATER"  -> 1000.0;
            case "WASTE"  -> 300.0;
            default       -> 100.0;
        };
    }

}
