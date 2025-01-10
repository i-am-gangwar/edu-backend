package com.edu_backend.model.QuizResults;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizSetAttemptResult {

    @NonNull @NotBlank(message = "Quiz Set Attempt Result ID cannot be blank")
    private String quizSetAttemptId;
    private int totalAttemptedQuestions;
    private int totalNotAttemptedQuestions;
    private int correctAnswers;
    private int inCorrectAnswers;
    private double accuracy;
    private Map<String, Integer> subjectScoresForCorrectAnswer = new HashMap<>(); // Subject -> Score
    private Map<String, Integer> subjectCategoryScoresForCorrectAnswer = new HashMap<>(); // Subject category-> Score
    private Map<String, Integer> subjectScoresForInCorrectAnswer = new HashMap<>(); // Subject -> Score
    private Map<String, Integer> subjectCategoryScoresForInCorrectAnswer = new HashMap<>(); // Subject category-> Score
    private Map<String, Integer> subjectScoresForNotAttemptedQ = new HashMap<>();
    private Map<String, Integer> subjectCategoryScoresNotAttemptedQ = new HashMap<>();
    private String timeSpent;
    private Date analyzedAt;
}
