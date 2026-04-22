package com.cognizant.greencity.user_service.service;

import com.cognizant.greencity.user_service.entity.AuditLog;
import com.cognizant.greencity.user_service.entity.User;
import com.cognizant.greencity.user_service.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public void record(User user, String action, String resource) {
        if (user == null) {
            return;
        }
        AuditLog log = new AuditLog();
        log.setUser(user);
        log.setAction(action);
        log.setResources(resource);
        log.setTimestamp(LocalDateTime.now());
        auditLogRepository.save(log);

    }
}
