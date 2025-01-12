package com.edubackend.repository;

import com.edubackend.model.quizresults.QuizResults;
import com.edubackend.model.quizresults.QuizSetResult;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface QuizAttemptResultRepository extends MongoRepository<QuizResults,String> {
    QuizResults findByUserId(String userId);
    @Aggregation(pipeline = {
            "{ '$match': { 'userId': ?0 } }",
            "{ '$project': { 'quizSetResult': { '$filter': { 'input': '$quizSetResult', 'as': 'quizSetResult', 'cond': { '$eq': ['$$quizSetResult.quizSetId', ?1] } } } } }",
            "{ '$project': { 'quizSetAttemptResults': { '$arrayElemAt': ['$quizSetResult.quizSetAttemptResults', 0] }, '_id': 0 } }"
    })
    QuizSetResult findQuizSetAttemptResultsByUserIdAndQuizSetId(String userId, String quizSetId);
}