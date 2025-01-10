package com.edu_backend.service;

import com.edu_backend.dto.QuestionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Optional<QuestionDTO> getQuestionById(String questionId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(questionId));

        // Use the QuestionDTO class for mapping
        QuestionDTO question = mongoTemplate.findOne(query, QuestionDTO.class, "questions");
        return Optional.ofNullable(question);
    }
}