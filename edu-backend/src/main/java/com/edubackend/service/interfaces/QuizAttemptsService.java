package com.edubackend.service.interfaces;

import com.edubackend.model.quizattempts.QuizAttempts;
import com.edubackend.model.quizattempts.QuizSet;
import com.edubackend.model.quizattempts.QuizSetAttempt;

public interface QuizAttemptsService {
     QuizAttempts createQuizAttempt(String userId, String quizSetId, QuizSetAttempt newAttempt);
     QuizSet createQuizSetAttempt(String quizSetId, QuizSetAttempt newAttempt );
     QuizSetAttempt createSetAttempt(QuizSetAttempt newAttempt);

}