package com.example.microservice_notify.service;

import com.example.microservice_notify.dto.feedback.FeedbackCreateRequest;
import com.example.microservice_notify.dto.feedback.FeedbackResponse;
import com.example.microservice_notify.dto.feedback.FeedbackUpdateRequest;
import com.example.microservice_notify.entity.Feedback;
import com.example.microservice_notify.entity.User;
import com.example.microservice_notify.exception.NotFoundException;
import com.example.microservice_notify.exception.UnauthorizedException;
import com.example.microservice_notify.repository.FeedbackRepository;
import com.example.microservice_notify.repository.UserRepository;
import com.example.microservice_notify.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;
    private final ModelMapper modelMapper;

    public List<FeedbackResponse> listMine(Authentication authentication) {
        Integer userId = principal(authentication).getUserId();
        return feedbackRepository.findByCitizen_UserId(userId).stream().map(this::toResponse).toList();
    }

    public FeedbackResponse getMine(Integer id, Authentication authentication) {
        Feedback feedback = getEntity(id);
        enforceOwner(feedback, authentication);
        return toResponse(feedback);
    }

    public FeedbackResponse create(FeedbackCreateRequest request, Authentication authentication) {
        User user = currentUser(authentication);

        Feedback feedback = new Feedback();
        feedback.setCitizen(user);
        feedback.setCategory(request.getCategory());
        feedback.setComments(request.getComments());
        feedback.setStatus(request.getStatus() != null ? request.getStatus() : "OPEN");
        feedback.setDate(LocalDate.now());

        Feedback saved = feedbackRepository.save(feedback);
        auditLogService.record(user, "FEEDBACK_CREATE", "feedback/" + saved.getFeedbackId());
        return toResponse(saved);
    }

    public FeedbackResponse update(Integer id, FeedbackUpdateRequest request, Authentication authentication) {
        Feedback feedback = getEntity(id);
        enforceOwner(feedback, authentication);

        if (request.getCategory() != null) feedback.setCategory(request.getCategory());
        if (request.getComments() != null) feedback.setComments(request.getComments());
        if (request.getStatus() != null) feedback.setStatus(request.getStatus());

        Feedback saved = feedbackRepository.save(feedback);
        auditLogService.record(saved.getCitizen(), "FEEDBACK_UPDATE", "feedback/" + id);
        return toResponse(saved);
    }

    public void delete(Integer id, Authentication authentication) {
        Feedback feedback = getEntity(id);
        enforceOwner(feedback, authentication);
        feedbackRepository.delete(feedback);
        auditLogService.record(feedback.getCitizen(), "FEEDBACK_DELETE", "feedback/" + id);
    }

    private Feedback getEntity(Integer id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Feedback not found"));
    }

    private FeedbackResponse toResponse(Feedback feedback) {
        FeedbackResponse response = modelMapper.map(feedback, FeedbackResponse.class);
        response.setCitizenId(feedback.getCitizen() != null ? feedback.getCitizen().getUserId() : null);
        return response;
    }

    private UserPrincipal principal(Authentication authentication) {
        return (UserPrincipal) authentication.getPrincipal();
    }

    private User currentUser(Authentication authentication) {
        Integer userId = principal(authentication).getUserId();
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
    }

    private void enforceOwner(Feedback feedback, Authentication authentication) {
        Integer userId = principal(authentication).getUserId();
        if (feedback.getCitizen() == null || feedback.getCitizen().getUserId() == null || !userId.equals(feedback.getCitizen().getUserId())) {
            throw new UnauthorizedException("Not allowed");
        }
    }
}

