package com.edu_backend.service.Interface;

import com.edu_backend.model.QuizResults.QuizResults;
import com.edu_backend.model.QuizResults.QuizSetAttemptResult;

public interface QuizAttemptResultService {
    QuizResults saveQuizAttemptResult(String userId, String quizSetId, QuizSetAttemptResult quizResultCalculated);

}