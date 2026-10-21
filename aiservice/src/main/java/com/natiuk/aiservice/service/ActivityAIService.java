package com.natiuk.aiservice.service;

import org.springframework.stereotype.Service;

import com.natiuk.aiservice.model.Activity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j 
@RequiredArgsConstructor  
public class ActivityAIService {

    private final GeminiService geminiService;

    public String generateRecommendation(Activity activity) {
        String prompt = createPromptForActivity(activity);
        String aiResponse = geminiService.getAnswer(prompt);
        log.info("RESPONSE FROM AI: {}", aiResponse);
        return aiResponse;
    }

    private String createPromptForActivity(Activity activity) {
        return String.format("""
            Analyze this fitness activity and provide detailed recommendations
            {
                "analysis" : {
                    "overall": "Overall analysis here",
                    "pact": "Pace analysis here",
                    "heartRate": "Heart rate analysis here",
                    "caloriesBurned": "Calories analysis here"
                },
                "improvements": [
                    {
                        "area": "Area name",
                        "recommendation": "Detailed recommendation"
                    }
                ],
                "safety": [
                    "Safety point 1"
                    "Safety point 2"
                ]   
            }

            Analyze this activity:
            Activity type: %s
            Duration: %d minutes
            Calories burned: %d
            Additional Metrics: %s

            Provide detailed analysis focusing on performance, improvements, next workout suggestions, and safety guidelines.
            Make sure to follow the EXACT JSON format as shown above.
            """,
            activity.getType(),
            activity.getDurationMinutes(),
            activity.getCaloriesBurned(),
            activity.getAdditionalMetrics()
        );
    }
}
