package com.edu_backend.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

public interface MongoQueryUtil {
    public Query createQuery(String userId,String quizSetId);
     public Query createQuery(String userId,String quizSetId,String quizSetAttemptId);

}