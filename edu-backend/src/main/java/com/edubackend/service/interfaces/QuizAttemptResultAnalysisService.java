package com.edubackend.service.interfaces;

import com.edubackend.model.quizananlysis.ResultsAnalysis;
import com.edubackend.model.quizresults.QuizSetResult;

public interface QuizAttemptResultAnalysisService {
    ResultsAnalysis calculateAnalysis(String userId);
    ResultsAnalysis calculateAnalysis(String userId, QuizSetResult quizSetResults);
}