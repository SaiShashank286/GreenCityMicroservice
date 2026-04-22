package com.example.microservice_notify.repository;

import com.example.microservice_notify.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    List<Project> findByCreatedBy_UserId(Integer userId);
}
