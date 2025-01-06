package com.edu_backend.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Document(collection = "QuizAttemptAnalysis")
@Getter
@Setter
@ToString
public class QuizAttemptResultAnalysis {
    @Id
    private String userId; // Links to the userId in the QuizSetAttempt schema
    private int totalScore;
    private double averageScore;
    private int highScore;
    private Map<String, Integer> subjectWiseScores = new HashMap<>(); // Key: Subject, Value: Score
    private int quizzesTaken;
    private Date lastUpdated;
}