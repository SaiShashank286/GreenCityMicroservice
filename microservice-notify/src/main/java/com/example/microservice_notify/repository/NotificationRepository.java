package com.example.microservice_notify.repository;

import com.example.microservice_notify.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    // Fetch all notifications for a user
    List<Notification> findByUserId(Integer userId);

    // Correct query: use entity field names, not DB column names
    @Query("SELECT m.date FROM Milestone m WHERE m.milestoneId = :milestoneId AND m.project.projectId = :projectId")
    LocalDate findDeadlineByMilestoneAndProject(@Param("milestoneId") Integer milestoneId,
                                                @Param("projectId") Integer projectId);
}
