package com.edu_backend.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Document(collection = "quizAttemptResults")
@Getter
@Setter
@ToString
public class QuizAttemptResult {
    @Id
    private String quizAttemptResultId; // Unique identifier for the analysis
    @NonNull
    private String quizAttemptId; // Reference to the raw result
    @NonNull
    private String userId; // User ID for easy querying
    @NonNull
    private String quizSetId; // Reference to the quiz set
    private int totalAttemptedQuestions; // Total questions attempted
    private int totalNotAttemptedQuestions; // Total questions attempted
    private int correctAnswers; // Total correct answers
    private int inCorrectAnswers; // Total correct answers
    private double accuracy; // Correct answers percentage
    private Map<String, Integer> subjectWiseScores = new HashMap<>(); // Subject -> Score
    private Map<String, Integer> subjectWiseCategoryScores = new HashMap<>(); // Subject category-> Score
    private String timeSpent; // Time spent on the quiz
    private Date analyzedAt; // Timestamp of analysis creation
}