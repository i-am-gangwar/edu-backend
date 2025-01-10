package com.edubackend.model;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Data
@ToString
public class Options {

    @Id
    private String objectId;
    private String text;
    private boolean isCorrect;



}
