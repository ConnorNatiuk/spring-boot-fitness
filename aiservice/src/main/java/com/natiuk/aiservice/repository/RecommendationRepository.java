package com.natiuk.aiservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.natiuk.aiservice.model.Recommendation;

public interface RecommendationRepository extends MongoRepository<Recommendation, String>{

    List<Recommendation> findByUserId(String userId);
    Optional<Recommendation> findByActivityId(String activityId);
}
