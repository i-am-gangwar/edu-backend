package com.edu_backend.repository;

import com.edu_backend.model.QuizAnanlysis.ResultsAnalysis;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizResultAnalysisRepository extends MongoRepository<ResultsAnalysis,String> {
    ResultsAnalysis findByUserId(String userId);
}