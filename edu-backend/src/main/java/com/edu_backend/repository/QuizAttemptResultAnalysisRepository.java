package com.edu_backend.repository;

import com.edu_backend.model.QuizAttemptResultAnalysis;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuizAttemptResultAnalysisRepository extends MongoRepository<QuizAttemptResultAnalysis,String> {
}