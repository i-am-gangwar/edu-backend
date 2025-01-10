package com.edubackend.repository;

import com.edubackend.model.Questions;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionsRepository extends MongoRepository<Questions, String>{

}
