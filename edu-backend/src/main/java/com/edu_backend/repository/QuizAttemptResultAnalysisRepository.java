package com.edu_backend.repository;

import com.edu_backend.model.AllResultsAnalysis;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizAttemptResultAnalysisRepository extends MongoRepository<AllResultsAnalysis,String> {
}