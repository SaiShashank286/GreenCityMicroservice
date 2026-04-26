package com.cognizant.citizenservice.service;

import com.cognizant.citizenservice.dto.feedback.FeedbackCreateRequest;
import com.cognizant.citizenservice.dto.notification.NotificationCreateRequest;
import com.cognizant.citizenservice.dto.user.UserDTO;
import com.cognizant.citizenservice.entity.Feedback;
import com.cognizant.citizenservice.feignclient.NotificationClient;
import com.cognizant.citizenservice.feignclient.UserClient;
import com.cognizant.citizenservice.repository.FeedbackRepository;
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
class FeedbackServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;
    @Mock
    private UserClient userClient;
    @Mock
    private NotificationClient notificationClient;

    private final ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    private FeedbackService feedbackService;

    @Test
    void create_savesFeedback_andSendsNotification() {
        feedbackService = new FeedbackService(feedbackRepository, userClient, notificationClient, modelMapper);
        Authentication auth = new UsernamePasswordAuthenticationToken("citizen@demo.com", null);
        UserDTO user = new UserDTO();
        user.setUserId(4);
        when(userClient.getuserId("citizen@demo.com")).thenReturn(user);
        when(feedbackRepository.save(any(Feedback.class))).thenAnswer(invocation -> {
            Feedback feedback = invocation.getArgument(0);
            feedback.setFeedbackId(33);
            return feedback;
        });

        FeedbackCreateRequest request = new FeedbackCreateRequest(
                Feedback.Category.Waste,
                "Need more bins",
                "OPEN"
        );

        var response = feedbackService.create(request, auth);

        assertEquals(33, response.getFeedbackId());
        verify(notificationClient).createNotification(any(NotificationCreateRequest.class));
    }
}
