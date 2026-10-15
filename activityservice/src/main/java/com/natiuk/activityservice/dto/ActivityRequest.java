package com.natiuk.activityservice.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.natiuk.activityservice.model.ActivityType;

import lombok.Data;

@Data
public class ActivityRequest {
    private String userId;
    private ActivityType type;
    private Integer durationMinutes;
    private Integer caloriesBurned;
    private LocalDateTime startTime;
    private Map<String, Object> additionalMetrics;
}
