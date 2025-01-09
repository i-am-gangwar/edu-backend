package com.edu_backend.repository;

import com.edu_backend.model.QuizAttempts.QuizAttempts;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizAttemptsRepository extends MongoRepository<QuizAttempts, String> {
      QuizAttempts findByUserId(String userId);
}