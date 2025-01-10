package com.edubackend.mongo;

import com.edubackend.model.quizattempts.QuizSet;
import com.edubackend.model.quizattempts.QuizSetAttempt;
import com.edubackend.model.quizattempts.QuizAttempts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class MongoService implements MongoQueryUtil{


   private final  MongoTemplate mongoTemplate;

    @Autowired
    public MongoService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Query createQuery(String userId, String quizSetId) {
        Criteria criteria = new Criteria();   // Build the query using $elemMatch to ensure both conditions in the array match
        criteria.andOperator(
                Criteria.where("userId").is(userId),
                Criteria.where("quizSets")
                        .elemMatch(
                                Criteria.where("quizSetId").is(quizSetId)));
        return new Query(criteria);
    }

    @Override
    public Query createQuery(String userId, String quizSetId, String quizSetAttemptId) {
        Criteria criteria = new Criteria();
        // Build the query using $elemMatch to ensure both conditions in the array match
        criteria.andOperator(
                Criteria.where("userId").is(userId),
                Criteria.where("quizSets")
                        .elemMatch(
                                Criteria.where("quizSetId").is(quizSetId)
                                        .and("quizSetAttempts")
                                        .elemMatch(Criteria.where("quizSetAttemptId").is(quizSetAttemptId)))
        );

        return new Query(criteria);
    }



    // Method to find a QuizSetAttempt by userId, quizSetId, and quizSetAttemptId
    public QuizSetAttempt findQuizSetAttempt(String userId, String quizSetId, String quizSetAttemptId) {
        Query q = createQuery(userId,quizSetId,quizSetAttemptId);
        QuizAttempts quizAttempts = mongoTemplate.findOne(q, QuizAttempts.class);
        if (quizAttempts != null) {
            for (QuizSet quizSet : quizAttempts.getQuizSets()) {
                for (QuizSetAttempt quizSetAttempt : quizSet.getQuizSetAttempts()) {
                    if (quizSetAttempt.getQuizSetAttemptId().equals(quizSetAttemptId)) {
                        return quizSetAttempt; // Return found quizSetAttempt
                    }
                }
            }
        }
        return null;
    }


    // Method to find a QuizSet by userId and quizSetId
    public QuizSet findQuizSet(String userId, String quizSetId) {
        Query q = createQuery(userId,quizSetId);
        QuizAttempts quizAttempts = mongoTemplate.findOne(q, QuizAttempts.class);
        // If the quizAttempt is found, loop through quizSets to find the quizSetAttempt
        if (quizAttempts != null) {
            for (QuizSet quizSet : quizAttempts.getQuizSets()) {
                    if (quizSet.getQuizSetId().equals(quizSetId))
                        return quizSet; // Return found quizSetAttempt
            }
        }
        return null; // Return empty if not found
    }


}