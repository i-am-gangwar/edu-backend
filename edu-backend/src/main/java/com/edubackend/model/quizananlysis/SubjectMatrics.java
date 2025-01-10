package com.edu_backend.model.QuizAnanlysis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashMap;
import java.util.Map;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectMatrics {

    @Builder.Default()
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
}