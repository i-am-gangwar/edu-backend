package com.edubackend.repository;

import com.edubackend.model.quizattempts.QuizAttempts;
import com.edubackend.model.quizattempts.QuizSet;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizAttemptsRepository extends MongoRepository<QuizAttempts, String> {
      QuizAttempts findByUserId(String userId);
      @Aggregation(pipeline = {
              "{ '$match': { 'userId': ?0 } }",
              "{ '$project': { 'quizSets': { '$filter': { 'input': '$quizSets', 'as': 'quizSet', 'cond': { '$eq': ['$$quizSet.quizSetId', ?1] } } } } }",
              "{ '$project': { 'quizSetAttempts': { '$arrayElemAt': ['$quizSets.quizSetAttempts', 0] }, '_id': 0 } }"
      })
      QuizSet findQuizSetAttemptsByUserIdAndQuizSetId(String userId, String quizSetId);
}