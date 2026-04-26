package com.cognizant.complianceservice.service;

import com.cognizant.complianceservice.dto.compliance.ComplianceRecordCreateRequest;
import com.cognizant.complianceservice.dto.notification.NotificationCreateRequest;
import com.cognizant.complianceservice.dto.user.UserDTO;
import com.cognizant.complianceservice.entity.ComplianceRecord;
import com.cognizant.complianceservice.feignClient.NotificationClient;
import com.cognizant.complianceservice.feignClient.ProjectClient;
import com.cognizant.complianceservice.feignClient.ResourceClient;
import com.cognizant.complianceservice.feignClient.UserClient;
import com.cognizant.complianceservice.repository.AuditRepository;
import com.cognizant.complianceservice.repository.ComplianceRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComplianceRecordServiceTest {

    @Mock
    private ComplianceRecordRepository complianceRecordRepository;
    @Mock
    private AuditRepository auditRepository;
    @Mock
    private ProjectClient projectClient;
    @Mock
    private ResourceClient resourceClient;
    @Mock
    private UserClient userClient;
    @Mock
    private NotificationClient notificationClient;

    private final ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    private ComplianceRecordService complianceRecordService;

    @Test
    void create_savesRecord_andSendsNotification() {
        complianceRecordService = new ComplianceRecordService(
                complianceRecordRepository,
                auditRepository,
                modelMapper,
                projectClient,
                resourceClient,
                userClient,
                notificationClient
        );
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("compliance@demo.com", null)
        );
        when(projectClient.existsById(88)).thenReturn(true);
        UserDTO user = new UserDTO();
        user.setUserId(6);
        when(userClient.getuserId("compliance@demo.com")).thenReturn(user);
        when(complianceRecordRepository.save(any(ComplianceRecord.class))).thenAnswer(invocation -> {
            ComplianceRecord record = invocation.getArgument(0);
            record.setComplianceId(201);
            return record;
        });

        ComplianceRecordCreateRequest request = new ComplianceRecordCreateRequest();
        request.setEntityId(88);
        request.setEntityType("Project");
        request.setResult("PASS");
        request.setNotes("ok");

        var response = complianceRecordService.create(request);

        assertEquals(201, response.getComplianceId());
        verify(notificationClient).createNotification(any(NotificationCreateRequest.class));
    }
}
