package com.edubackend.repository;
import com.edubackend.model.Otp;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface OtpRepository extends MongoRepository<Otp, String> {
    Otp findByEmail(String email);
}
