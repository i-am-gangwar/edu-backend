package com.edubackend.service.Interface;

import com.edubackend.model.QuizAnanlysis.ResultsAnalysis;
import com.edubackend.model.QuizResults.QuizSetResult;

public interface QuizAttemptResultAnalysisService {
    ResultsAnalysis calculateAnalysis(String userId);
    ResultsAnalysis calculateAnalysis(String userId, QuizSetResult quizSetResults);
  //  ResultsAnalysis calculateResultAnalysis(QuizResults userResult, ResultsAnalysis resultsAnalysis);
}