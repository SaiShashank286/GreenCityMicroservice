package com.cognizant.citizenservice.service;

import com.cognizant.citizenservice.dto.notification.NotificationCreateRequest;
import com.cognizant.citizenservice.dto.report.CitizenReportCreateRequest;
import com.cognizant.citizenservice.dto.user.UserDTO;
import com.cognizant.citizenservice.entity.CitizenReport;
import com.cognizant.citizenservice.feignclient.NotificationClient;
import com.cognizant.citizenservice.feignclient.UserClient;
import com.cognizant.citizenservice.repository.CitizenReportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CitizenReportServiceTest {

    @Mock
    private CitizenReportRepository citizenReportRepository;
    @Mock
    private UserClient userClient;
    @Mock
    private NotificationClient notificationClient;

    private final ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    private CitizenReportService citizenReportService;

    @Test
    void create_savesReport_andSendsNotification() {
        citizenReportService = new CitizenReportService(citizenReportRepository, userClient, notificationClient, modelMapper);
        Authentication auth = new UsernamePasswordAuthenticationToken("citizen@demo.com", null);
        UserDTO user = new UserDTO();
        user.setUserId(4);
        when(userClient.getuserId("citizen@demo.com")).thenReturn(user);
        when(citizenReportRepository.save(any(CitizenReport.class))).thenAnswer(invocation -> {
            CitizenReport report = invocation.getArgument(0);
            report.setReportId(99);
            return report;
        });

        CitizenReportCreateRequest request = new CitizenReportCreateRequest(
                CitizenReport.ReportType.POLLUTION,
                "Lake Road",
                "OPEN"
        );

        var response = citizenReportService.create(request, auth);

        assertEquals(99, response.getReportId());
        verify(notificationClient).createNotification(any(NotificationCreateRequest.class));
    }
}
