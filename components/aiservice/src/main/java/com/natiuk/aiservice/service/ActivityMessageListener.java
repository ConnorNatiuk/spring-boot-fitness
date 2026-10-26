package com.natiuk.aiservice.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.natiuk.aiservice.model.Activity;
import com.natiuk.aiservice.model.Recommendation;
import com.natiuk.aiservice.repository.RecommendationRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor    
public class ActivityMessageListener {

    private final ActivityAIService aiService;
    private final RecommendationRepository recommendationRepository;

    @RabbitListener(queues = "activity.queue") 
    public void processActivity(Activity activity) {
        try {
            log.info("Received activity for processing: {}", activity.getId());
            Recommendation recommendation = aiService.generateRecommendation(activity);
            log.info("Generated: {}", recommendation);
            Recommendation saved = recommendationRepository.save(recommendation);
            log.info("Saved with id: {}", saved.getId());
        } catch (Exception e) {
            log.error("FAILED PROCESSING ACTIVITY {}", activity.getId(), e);
            throw e;
        }
    }
}
