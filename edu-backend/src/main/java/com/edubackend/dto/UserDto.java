package com.edubackend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@JsonIgnoreProperties
@Document(collection = "users")
public class UserDto {

    @Id
    private String id;
    @JsonProperty("username")
    private String username;
    @JsonProperty("contact")
    private String contact;
    private String password;
    private String role;


}
