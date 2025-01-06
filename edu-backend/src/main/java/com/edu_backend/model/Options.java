package com.edu_backend.model;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Data
@ToString
public class Options {

    @Id
    private String ObjectId;
    private String text;
    private boolean isCorrect;



}
