package com.edu_backend.service.Interface;

import com.edu_backend.model.QuizAttemptResult;
import com.edu_backend.model.QuizResultsAnalysis;

public interface QuizAttemptResultAnalysisService {
    QuizResultsAnalysis calculateResultAnalysis(QuizAttemptResult userResult,QuizResultsAnalysis quizResultsAnalysis);
}