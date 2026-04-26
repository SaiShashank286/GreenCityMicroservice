package com.cognizant.citizenservice.service;

import com.cognizant.citizenservice.dto.feedback.FeedbackCreateRequest;
import com.cognizant.citizenservice.dto.feedback.FeedbackResponse;
import com.cognizant.citizenservice.dto.feedback.FeedbackUpdateRequest;
import com.cognizant.citizenservice.dto.notification.NotificationCreateRequest;
import com.cognizant.citizenservice.dto.user.UserDTO;
import com.cognizant.citizenservice.entity.Feedback;
import com.cognizant.citizenservice.exception.NotFoundException;
import com.cognizant.citizenservice.exception.UnauthorizedException;
import com.cognizant.citizenservice.feignclient.NotificationClient;
import com.cognizant.citizenservice.feignclient.UserClient;
import com.cognizant.citizenservice.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserClient userClient;
    private final NotificationClient notificationClient;
    private final ModelMapper modelMapper;

    public List<FeedbackResponse> listMine(Authentication authentication) {
        UserDTO user = userClient.getuserId(authentication.getName());
        Integer userId = user.getUserId();
        // Change findByCitizen_UserId to findByCitizen
        return feedbackRepository.findByCitizen(userId).stream()
                .map(this::toResponse).toList();
    }

    public FeedbackResponse getMine(Integer id, Authentication authentication) {
        Feedback feedback = getEntity(id);
        enforceOwner(feedback, authentication);
        return toResponse(feedback);
    }

    public FeedbackResponse create(FeedbackCreateRequest request, Authentication authentication) {
        UserDTO user=userClient.getuserId(authentication.getName());
        Integer userId = user.getUserId();

        Feedback feedback = new Feedback();
        feedback.setCitizen(userId);
        feedback.setCategory(request.getCategory());
        feedback.setComments(request.getComments());
        feedback.setStatus(request.getStatus() != null ? request.getStatus() : "OPEN");
        feedback.setDate(LocalDate.now());

        Feedback saved = feedbackRepository.save(feedback);
        sendNotification(userId, saved.getFeedbackId(), "FEEDBACK", "FEEDBACK");

        return toResponse(saved);
    }

    public FeedbackResponse update(Integer id, FeedbackUpdateRequest request, Authentication authentication) {
        Feedback feedback = getEntity(id);
        enforceOwner(feedback, authentication);

        if (request.getCategory() != null) feedback.setCategory(request.getCategory());
        if (request.getComments() != null) feedback.setComments(request.getComments());
        if (request.getStatus() != null) feedback.setStatus(request.getStatus());

        Feedback saved = feedbackRepository.save(feedback);

        return toResponse(saved);
    }

    public void delete(Integer id, Authentication authentication) {
        Feedback feedback = getEntity(id);
        enforceOwner(feedback, authentication);
        feedbackRepository.delete(feedback);

    }

    private Feedback getEntity(Integer id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Feedback not found"));
    }

    private FeedbackResponse toResponse(Feedback feedback) {
        FeedbackResponse response = modelMapper.map(feedback, FeedbackResponse.class);
        response.setCitizenId(feedback.getCitizen() != null ? feedback.getCitizen() : null);
        return response;
    }

    private void enforceOwner(Feedback feedback, Authentication authentication) {
        UserDTO user=userClient.getuserId(authentication.getName());
        Integer userId = user.getUserId();
        if (feedback.getCitizen() == null  || !userId.equals(feedback.getCitizen())) {
            throw new UnauthorizedException("Not allowed");
        }
    }

    private void sendNotification(Integer userId, Integer entityId, String entityType, String category) {
        try {
            notificationClient.createNotification(NotificationCreateRequest.builder()
                    .userId(userId)
                    .projectId(null)
                    .entityId(entityId)
                    .entityType(entityType)
                    .category(category)
                    .build());
        } catch (Exception ex) {
            log.warn("Notification call failed for {} {}", entityType, entityId, ex);
        }
    }
}

