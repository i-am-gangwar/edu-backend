package com.edu_backend.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class MongoQueryUtil {

    @Autowired
    MongoTemplate mongoTemplate;

    // Method to find one document based on criteria
    public <T> T findOneByCriteria(Class<T> entityClass, Criteria criteria) {
        Query query = new Query(criteria);
        return mongoTemplate.findOne(query, entityClass);
    }
}