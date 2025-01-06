package com.edu_backend.service.Interface;

import com.edu_backend.model.QuizAttemptResult;

public interface QuizAttemptResultService {

    QuizAttemptResult saveQuizAttemptResult(String userId,String quizSetId,QuizAttemptResult.QuizSetAttemptResult quizResultCalculated);

}