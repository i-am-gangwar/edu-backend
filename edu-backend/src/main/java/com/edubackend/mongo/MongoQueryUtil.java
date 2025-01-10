package com.edubackend.mongo;

import org.springframework.data.mongodb.core.query.Query;

public interface MongoQueryUtil {
    public Query createQuery(String userId,String quizSetId);
     public Query createQuery(String userId,String quizSetId,String quizSetAttemptId);

}