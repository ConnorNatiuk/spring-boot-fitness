package com.natiuk.aiservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.natiuk.aiservice.model.Recommendation;
import com.natiuk.aiservice.repository.RecommendationRepository;

@Service 
public class RecommendationService {
    
    private final RecommendationRepository repository;

    public RecommendationService(RecommendationRepository repository) {
        this.repository = repository;
    }

    public List<Recommendation> getUserRecommendations(String userId) {
        return repository.findByUserId(userId);
    }

    public Recommendation getActivityRecommendations(String activityId) {
        return repository.findByActivityId(activityId)
                .orElseThrow(() -> new RuntimeException("No Recommendation found for: " + activityId));
    }
}
