package com.cognizant.greencity.ProjectService.repository;

import com.cognizant.greencity.ProjectService.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
