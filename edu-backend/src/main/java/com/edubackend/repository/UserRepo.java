package com.edubackend.repository;

import com.edubackend.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

@JsonIgnoreProperties
public interface UserRepo extends MongoRepository<UserDto,String> {
       Optional<UserDto> findById(String id);
}