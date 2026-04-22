package com.cognizant.citizenservice.controller;

import com.cognizant.citizenservice.dto.feedback.FeedbackCreateRequest;
import com.cognizant.citizenservice.dto.feedback.FeedbackResponse;
import com.cognizant.citizenservice.dto.feedback.FeedbackUpdateRequest;
import com.cognizant.citizenservice.service.FeedbackService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackController.class);
    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping
    public List<FeedbackResponse> listMine(Authentication authentication) {
        logger.info("Received request to list user's feedback");
        List<FeedbackResponse> response = feedbackService.listMine(authentication);
        logger.info("Successfully fetched user's feedback");
        return response;
    }

    @GetMapping("/{id}")
    public FeedbackResponse getMine(@PathVariable Integer id, Authentication authentication) {
        logger.info("Received request to get feedback id: {}", id);
        FeedbackResponse response = feedbackService.getMine(id, authentication);
        logger.info("Successfully fetched feedback id: {}", id);
        return response;
    }

    @PostMapping
    public FeedbackResponse create(@Valid @RequestBody FeedbackCreateRequest request, Authentication authentication) {
        logger.info("Received request to create feedback");
        FeedbackResponse response = feedbackService.create(request, authentication);
        logger.info("Successfully created feedback");
        return response;
    }

    @PutMapping("/{id}")
    public FeedbackResponse update(@PathVariable Integer id, @Valid @RequestBody FeedbackUpdateRequest request, Authentication authentication) {
        logger.info("Received request to update feedback id: {}", id);
        FeedbackResponse response = feedbackService.update(id, request, authentication);
        logger.info("Successfully updated feedback id: {}", id);
        return response;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id, Authentication authentication) {
        logger.info("Received request to delete feedback id: {}", id);
        feedbackService.delete(id, authentication);
        logger.info("Successfully deleted feedback id: {}", id);
    }
}