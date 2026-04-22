package com.example.microservice_notify.repository;

import com.example.microservice_notify.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Integer> {
    List<Resource> findByProject_ProjectId(Integer projectId);
//    @Query("SELECT r.capacity, ru.quantity FROM Resource r JOIN ResourceUsage ru ON r.id = ru.resourceId WHERE ru.id = :usageId")
//    Object[] findUsageAndCapacity(@Param("usageId") Integer usageId);
    @Query("SELECT r.type FROM Resource r WHERE r.resourceId = :id")
    String findTypeById(@Param("id") Integer id);
    @Query("SELECT ru.capacity FROM Resource ru WHERE ru.resourceId = :id")
    Double findCurrentUsageByResourceId(@Param("id") Integer id);

}

