package com.edubackend.service.interfaces;

import com.edubackend.model.quizresults.QuizResults;
import com.edubackend.model.quizresults.QuizSetAttemptResult;

public interface QuizAttemptResultService {
    QuizResults saveQuizAttemptResult(String userId, String quizSetId, QuizSetAttemptResult quizResultCalculated);

}