package com.edubackend.repository;

import com.edubackend.model.quizattempts.QuizAttempts;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizAttemptsRepository extends MongoRepository<QuizAttempts, String> {
      QuizAttempts findByUserId(String userId);
}