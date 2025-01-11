package com.edubackend.repository;

import com.edubackend.model.Questions;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionsRepository extends MongoRepository<Questions, String>{

}
