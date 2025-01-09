package com.edu_backend.service.Interface;

import com.edu_backend.model.QuizResults.QuizResults;
import com.edu_backend.model.QuizAnanlysis.ResultsAnalysis;
import com.edu_backend.model.QuizResults.QuizSetAttemptResult;
import com.edu_backend.model.QuizResults.QuizSetResult;

public interface QuizAttemptResultAnalysisService {
    ResultsAnalysis calculateAnalysis(String userId);
    ResultsAnalysis calculateAnalysis(String userId, QuizSetResult quizSetResults);
  //  ResultsAnalysis calculateResultAnalysis(QuizResults userResult, ResultsAnalysis resultsAnalysis);
}