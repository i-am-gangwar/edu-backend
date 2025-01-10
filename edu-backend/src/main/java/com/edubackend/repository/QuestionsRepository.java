package com.edu_backend.repository;

import com.edu_backend.model.Questions;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionsRepository extends MongoRepository<Questions, String>{

}
