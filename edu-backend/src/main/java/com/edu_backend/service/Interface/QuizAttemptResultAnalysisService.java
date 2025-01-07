package com.edu_backend.service.Interface;

import com.edu_backend.model.AllResultsAnalysis;
import com.edu_backend.model.QuizAttemptResult;

public interface QuizAttemptResultAnalysisService {
    AllResultsAnalysis ResultAnalysis(QuizAttemptResult userResult);
}