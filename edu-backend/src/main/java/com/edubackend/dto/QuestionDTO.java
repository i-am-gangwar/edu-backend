package com.edu_backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.Date;

@Data
@JsonIgnoreProperties
public class QuestionDTO {
    private String id;
    private String questionText;
    private String subjectId;
    private String examId;
    private String category;
    private List<Option> options;
    private String difficulty;
    private String fact;
    private Date createdAt;

    @Data
    public static class Option {
        private String id;
        private String text;
        private boolean isCorrect;
    }
}