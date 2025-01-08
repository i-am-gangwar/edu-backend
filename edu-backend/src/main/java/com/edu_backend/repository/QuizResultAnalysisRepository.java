package com.edu_backend.repository;

import com.edu_backend.model.QuizResultsAnalysis;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizResultAnalysisRepository extends MongoRepository<QuizResultsAnalysis,String> {
    QuizResultsAnalysis findByUserId(String userId);
}