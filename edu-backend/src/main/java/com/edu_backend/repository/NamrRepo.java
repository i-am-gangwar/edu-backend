package com.edu_backend.repository;

import com.edu_backend.model.Name;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface NamrRepo  extends MongoRepository<Name,String> {

}
