package com.edubackend.repository;

import com.edubackend.dto.UserDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepo extends MongoRepository<UserDto,String> {
       Optional<UserDto> findById(String id);
}