package com.edubackend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@JsonIgnoreProperties
@Document(collection = "users")
public class UserDto {

    @Id
    private String id;
    private String username;
    private String contact;
    private String password;
    private String role;


}
