package com.edu_backend.repository;

import com.edu_backend.model.QuizAttempt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizAttemptRepository extends MongoRepository<QuizAttempt, String> {
      Optional<QuizAttempt> findByUserId(String userId);
}