package com.edu_backend.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Document(collection = "quizAnalysis")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllResultsAnalysis {

    @Id
    @NotBlank(message = "Result Analysis ID cannot be blank")
    private String id;

    @NonNull
    @NotBlank(message = "User ID cannot be blank")
    private String userId; // Links to the userId in the QuizSetAttempt schema

    private OverallPerformance overallPerformance;


    @Data
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class OverallPerformance {

        @Id
        @NotBlank(message = "Overall Performance Analysis ID cannot be blank")
        private String id= UUID.randomUUID().toString();

        @Builder.Default
        private int totalQuizSets = 0; // Total number of quiz sets attempted

        @Builder.Default
        private int totalQ = 0; // Total questions across all quiz sets

        @Builder.Default
        private int totalAttemptedQ = 0; // Total attempted questions

        @Builder.Default
        private int totalCorrectScore = 0; // Total correct answers

        @Builder.Default
        private int totalIncorrectScore = 0; // Total incorrect answers

        @Builder.Default
        private int highestScore = 0;

        @Builder.Default
        private int lowestScore = Integer.MAX_VALUE; // Use MAX_VALUE for meaningful comparison

        @Builder.Default
        private double averageScore = 0.0;

        @Builder.Default
        private double overallAccuracy = 0.0; // Overall accuracy percentage

        @Builder.Default
        private Map<String, Integer> subjectScoresForCorrectAnswer = new HashMap<>(); // Subject -> Score

        @Builder.Default()
        private Map<String, Integer> subjectCategoryScoresForCorrectAnswer = new HashMap<>(); // Subject category -> Score

        @Builder.Default()
        private Map<String, Integer> subjectScoresForInCorrectAnswer = new HashMap<>(); // Subject -> Score

        @Builder.Default()
        private Map<String, Integer> subjectCategoryScoresForInCorrectAnswer = new HashMap<>(); // Subject category -> Score

        @Builder.Default
        private Map<String, Integer> subjectScoresForNotAttemptedQ = new HashMap<>();

        @Builder.Default
        private Map<String, Integer> subjectCategoryScoresNotAttemptedQ = new HashMap<>();

        @Builder.Default
        private Date analyzedAt = new Date(); // Timestamp of analysis creation

    }
}