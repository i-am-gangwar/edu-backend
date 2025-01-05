package com.edu_backend.service.Interface;

import com.edu_backend.model.QuizAttemptResult;

public interface QuizAttemptResultService {

    boolean calculateQuizAttemptResult(String userId,String quizSetId,String quizAttemptId);

}