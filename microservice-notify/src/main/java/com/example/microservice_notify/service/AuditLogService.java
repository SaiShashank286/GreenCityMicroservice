package com.example.microservice_notify.service;

import com.example.microservice_notify.entity.AuditLog;
import com.example.microservice_notify.entity.User;
import com.example.microservice_notify.repository.AuditLogRepository;
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

