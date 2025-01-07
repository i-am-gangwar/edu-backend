package com.edu_backend.repository;

import com.edu_backend.model.QuizAttemptResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface QuizAttemptResultRepository extends MongoRepository<QuizAttemptResult,String> {
    Optional<QuizAttemptResult> findByUserId(String userId);
}