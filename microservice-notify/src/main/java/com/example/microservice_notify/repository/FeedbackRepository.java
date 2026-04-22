package com.example.microservice_notify.repository;

import com.example.microservice_notify.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    List<Feedback> findByCitizen_UserId(Integer userId);
    @Query("SELECT f.status FROM Feedback f WHERE f.feedbackId = :id")
    String findStatusById(@Param("id") Integer id);
}

