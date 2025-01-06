package com.edu_backend.mongo;

import com.edu_backend.model.QuizAttempt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class MongoService {

     @Autowired
     MongoQueryUtil mongoQueryUtil;

    // Method to find a QuizSetAttempt by userId, quizSetId, and quizSetAttemptId
    public Optional<QuizAttempt.QuizSetAttempt> findQuizSetAttempt(String userId, String quizSetId, String quizSetAttemptId) {
        Criteria criteria = new Criteria();
        // Build the query using $elemMatch to ensure both conditions in the array match
        criteria.andOperator(
                Criteria.where("userId").is(userId),
                Criteria.where("quizSet")
                        .elemMatch(
                                Criteria.where("quizSetId").is(quizSetId)
                                        .and("quizSetAttempts")
                                        .elemMatch(Criteria.where("quizSetAttemptId").is(quizSetAttemptId)))
        );

        // Use dynamic query utility method
        QuizAttempt quizAttempt = mongoQueryUtil.findOneByCriteria(QuizAttempt.class, criteria);
        // If the quizAttempt is found, loop through quizSets to find the quizSetAttempt
        if (quizAttempt != null) {
            for (QuizAttempt.QuizSet quizSet : quizAttempt.getQuizSet()) {
                for (QuizAttempt.QuizSetAttempt quizSetAttempt : quizSet.getQuizSetAttempts()) {
                    if (quizSetAttempt.getQuizSetAttemptId().equals(quizSetAttemptId)) {
                        return Optional.of(quizSetAttempt); // Return found quizSetAttempt
                    }
                }
            }
        }
        return Optional.empty(); // Return empty if not found
    }

    // Method to find a QuizSet by userId and quizSetId
    public Optional<QuizAttempt.QuizSet> findQuizSet(String userId, String quizSetId) {
        Criteria criteria = new Criteria();
        // Build the query using $elemMatch to ensure both conditions in the array match
        criteria.andOperator(
                Criteria.where("userId").is(userId),
                Criteria.where("quizSet")
                        .elemMatch(
                                Criteria.where("quizSetId").is(quizSetId)));

        // Use dynamic query utility method
        QuizAttempt quizAttempt = mongoQueryUtil.findOneByCriteria(QuizAttempt.class, criteria);
        // If the quizAttempt is found, loop through quizSets to find the quizSetAttempt
        if (quizAttempt != null) {
            for (QuizAttempt.QuizSet quizSet : quizAttempt.getQuizSet()) {
                    if (quizSet.getQuizSetId().equals(quizSetId))
                        return Optional.of(quizSet); // Return found quizSetAttempt
            }
        }
        return Optional.empty(); // Return empty if not found
    }



}