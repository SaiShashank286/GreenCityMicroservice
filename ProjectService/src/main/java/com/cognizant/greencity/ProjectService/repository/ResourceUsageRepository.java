package com.cognizant.greencity.ProjectService.repository;

import com.cognizant.greencity.ProjectService.entity.ResourceUsage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceUsageRepository extends JpaRepository<ResourceUsage, Long> {

	List<ResourceUsage> findByResource_ResourceId(Long resourceId);
}
