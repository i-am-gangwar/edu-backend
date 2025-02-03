package com.edubackend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "otps")
@Data
public class Otp {
    @Id
    private String id;
    private String email;
    private String otp;
    private LocalDateTime expirationTime;
    private boolean isVerified;


}

