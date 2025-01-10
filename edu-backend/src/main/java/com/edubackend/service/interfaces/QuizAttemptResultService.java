package com.edubackend.service.Interface;

import com.edubackend.model.QuizResults.QuizResults;
import com.edubackend.model.QuizResults.QuizSetAttemptResult;

public interface QuizAttemptResultService {
    QuizResults saveQuizAttemptResult(String userId, String quizSetId, QuizSetAttemptResult quizResultCalculated);

}