package com.edu_backend.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Document(collection = "QuizAnalysis")
@Getter
@Setter
@ToString
public class QuizAttemptResultAnalysis {
    @Id
    private String id;
    private String userId; // Links to the userId in the QuizSetAttempt schema

    @Data
    private static class QuizResultAnanlysis {
        private int totalQuizes;
        private int totalMarks;
        private int totalScore;
        private double averageScore;
        private int highestScore;
        private int lowestScore;
        private Map<String, Integer> subjectWiseScores = new HashMap<>(); // Key: Subject, Value: Score
        private Date lastUpdatedAt;
    }
}