package com.cognizant.greencity.ProjectService.repository;

import com.cognizant.greencity.ProjectService.entity.Impact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImpactRepository extends JpaRepository<Impact, Long> {

	List<Impact> findByProject_ProjectId(Long projectId);
}
