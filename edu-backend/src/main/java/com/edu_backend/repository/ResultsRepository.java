package com.edu_backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.edu_backend.model.Results;

import java.util.Optional;

public interface ResultsRepository extends MongoRepository<Results, String> {

      Optional<Results> findByUserId(String userId);
      Optional<Results> findByUserIdAndQuizSetQuizSetId(String userId, String quizSetId);
}