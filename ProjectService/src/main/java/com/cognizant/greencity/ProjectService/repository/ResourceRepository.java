package com.cognizant.greencity.ProjectService.repository;

import com.cognizant.greencity.ProjectService.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

	List<Resource> findByProject_ProjectId(Long projectId);
}
