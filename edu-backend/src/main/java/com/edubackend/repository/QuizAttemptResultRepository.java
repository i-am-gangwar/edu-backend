package com.edubackend.repository;

import com.edubackend.model.quizresults.QuizResults;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizAttemptResultRepository extends MongoRepository<QuizResults,String> {
    QuizResults findByUserId(String userId);
}