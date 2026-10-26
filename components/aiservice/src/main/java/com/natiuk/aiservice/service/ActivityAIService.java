package com.natiuk.aiservice.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.natiuk.aiservice.model.Activity;
import com.natiuk.aiservice.model.Recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
@Slf4j 
@RequiredArgsConstructor  
public class ActivityAIService {

    private final GeminiService geminiService;

    public Recommendation generateRecommendation(Activity activity) {
        String prompt = createPromptForActivity(activity);
        String aiResponse = geminiService.getAnswer(prompt);
        log.info("RESPONSE FROM AI: {}", aiResponse);
        return processAiResponse(activity, aiResponse);
    }



    private Recommendation processAiResponse(Activity activity, String aiResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(aiResponse);
            JsonNode textNode = rootNode.path("candidates")
                    .path(0)
                    .path("content")
                    .path("parts")
                    .path(0)
                    .path("text");
            
            String jsonContent = textNode.asString()
                    .replaceAll("```json\\n", "")
                    .replaceAll("\\n```", "")
                    .trim();
                
            log.info("PARSED RESPONSE FROM AI: {} ", jsonContent);

            JsonNode analysisJson = mapper.readTree(jsonContent);
            JsonNode analysisNode = analysisJson.path("analysis");
            StringBuilder fullAnalysis = new StringBuilder();
            addAnalysisSection(fullAnalysis, analysisNode, "overall", "Overall:");
            addAnalysisSection(fullAnalysis, analysisNode, "pace", "Pace:");
            addAnalysisSection(fullAnalysis, analysisNode, "heartRate", "HeartRate:");
            addAnalysisSection(fullAnalysis, analysisNode, "caloriesBurned", "Calories Burned:");
            
            List<String> improvements = extractImprovements(analysisJson.path("improvements"));
            List<String> suggestions = extractSuggestions(analysisJson.path("suggestions"));
            List<String> safety = extractSafetyGuidelines(analysisJson.path("safety"));
            
            log.info("Finished the building! Returning...");

            return Recommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .activityType(activity.getType())
                    .recommendation(fullAnalysis.toString().trim())
                    .improvements(improvements)
                    .suggestions(suggestions)
                    .safetyMeasures(safety)
                    .createdAt(LocalDateTime.now())
                    .build();
        } catch (Exception e) {
            log.error("ERROR PARSING RESPONSE: ", e);
            return createDefaultRecommendation(activity);
        }
    }

    private Recommendation createDefaultRecommendation(Activity activity) {
        return Recommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .activityType(activity.getType())
                    .recommendation("Unable to generate recommendation")
                    .improvements(Collections.singletonList("Work hard, play hard"))
                    .suggestions(Collections.singletonList("Eat a burrito, that might help possibly indirectly"))
                    .safetyMeasures(Arrays.asList(
                        "Don't die",
                        "Stay hydrated",
                        "Kiss yo mamma"
                    ))
                    .createdAt(LocalDateTime.now())
                    .build();
    }

    private void addAnalysisSection(StringBuilder fullAnalysis, JsonNode analysisNode, String key, String prefix) {
        if (!analysisNode.path(key).isMissingNode()) {
            fullAnalysis.append(prefix)
                    .append(analysisNode.path(key).asString())
                    .append("\n\n");
        }
    }



    private List<String> extractImprovements(JsonNode improvementsNode) {
        List<String> improvements = new ArrayList<>();
        if (improvementsNode.isArray()) {
            improvementsNode.forEach(improvement -> {
                String area = improvement.path("area").asString();
                String detail = improvement.path("recommendation").asString();

                improvements.add(String.format("%s: %s", area, detail));
            });

        }
        return improvements.isEmpty() ?
            Collections.singletonList("No specific improvements are provided") :
            improvements;
    }



    private List<String> extractSuggestions(JsonNode suggestionsNode) {
        List<String> suggestions = new ArrayList<>();
        if (suggestionsNode.isArray()) {
            suggestionsNode.forEach(suggestion -> {
                String workout = suggestion.path("improvement").asString();
                String description = suggestion.path("description").asString();
                suggestions.add(String.format("%s: %s", workout, description));
            });
        }
        return suggestions.isEmpty() ?
                Collections.singletonList("No specific suggestions are provided") :
                suggestions;
    }
    
    private List<String> extractSafetyGuidelines(JsonNode safetyNode) {
        List<String> safetyList = new ArrayList<>();
        if (safetyNode.isArray()) {
            safetyNode.forEach(item -> safetyList.add(item.asString()));
        }
        return safetyList.isEmpty() ?
                Collections.singletonList("Follow general guidelines") :
                safetyList;
    }



    private String createPromptForActivity(Activity activity) {
        return String.format("""
            Analyze this fitness activity and provide detailed recommendations
            {
                "analysis" : {
                    "overall": "Overall analysis here",
                    "pace": "Pace analysis here",
                    "heartRate": "Heart rate analysis here",
                    "caloriesBurned": "Calories analysis here"
                },
                "improvements": [
                    {
                        "area": "Area name",
                        "recommendation": "Detailed recommendation"
                    }
                ],
                "suggestions": [
                    {
                        "workout": "Workout name",
                        "description": "Workout description"
                    }
                ],

                "safety": [
                    "Safety point 1",
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
