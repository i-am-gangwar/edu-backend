package com.edubackend.repository;

import com.edubackend.model.quizananlysis.ResultsAnalysis;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizResultAnalysisRepository extends MongoRepository<ResultsAnalysis,String> {
    ResultsAnalysis findByUserId(String userId);
}