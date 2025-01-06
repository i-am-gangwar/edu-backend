package com.edu_backend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Document(collection = "quizAttemptResults")
@Getter
@Setter
@ToString
public class QuizAttemptResult {
    @Id
    private String id; // Unique identifier for the analysis
    @NonNull
    private String userId; // User ID for easy querying
    private List<QuizSetResult> quizSetResult = new ArrayList<>();   // list of all quizSets attempted by user

    @Data
    public static class QuizSetResult {
        @NonNull
        private String quizSetId;  // quizSet id attempted by user
        private List<QuizSetAttemptResult> quizSetAttemptResults = new ArrayList<>();  // list of all  attempt of that quizSet id
    }

    @Data
    public static class QuizSetAttemptResult {
        @NonNull
        private String quizSetAttemptId; // Reference to the raw result
        private int totalAttemptedQuestions; // Total questions attempted
        private int totalNotAttemptedQuestions; // Total questions attempted
        private int correctAnswers; // Total correct answers
        private int inCorrectAnswers; // Total correct answers
        private double accuracy; // Correct answers percentage
        private Map<String, Integer> subjectScoresForCorrectAnswer = new HashMap<>(); // Subject -> Score
        private Map<String, Integer> subjectCategoryScoresForCorrectAnswer = new HashMap<>(); // Subject category-> Score
        private Map<String, Integer> subjectScoresForInCorrectAnswer = new HashMap<>(); // Subject -> Score
        private Map<String, Integer> subjectCategoryScoresForInCorrectAnswer = new HashMap<>(); // Subject category-> Score
        private Map<String, Integer> subjectScoresForNotAttemptedQ = new HashMap<>();
        private Map<String, Integer> subjectCategoryScoresNotAttemptedQ = new HashMap<>();
        private String timeSpent; // Time spent on the quiz
        private Date analyzedAt; // Timestamp of analysis creation

    }

}