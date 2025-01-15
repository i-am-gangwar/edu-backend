package com.edubackend.service.interfaces;

import com.edubackend.model.quizresults.QuizResults;
import com.edubackend.model.quizresults.QuizSetAttemptResult;
import com.edubackend.model.quizresults.QuizSetResult;

public interface QuizAttemptResultService {
    QuizSetAttemptResult saveQuizAttemptResult(String userId, String quizSetId, String quizSetAttemptId);
    QuizResults createQuizResult(String userId, String quizSetId, QuizSetAttemptResult quizResult);
    QuizSetResult createQuizSetResult(String quizSetId, QuizSetAttemptResult quizSetAttemptResult);

}