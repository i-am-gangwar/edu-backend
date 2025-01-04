package com.edu_backend.repository;

import com.edu_backend.model.QuizAttemptResult;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuizAttemptResultRepository extends MongoRepository<QuizAttemptResult,String> {
}