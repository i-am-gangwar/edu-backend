package com.edu_backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;


@Document(collection = "questions")
@Data
public class Questions {

    @Id
    private String ObjectId ;
    private String questionText;
    private String subjectId;
    private String examId;
    private String category;
    private List<Options> options;
    private String difficulty;
    private String fact;
    private long createdAt;
    private int version;

    @Override
    public String toString() {
        return "Questions{" +
                "ObjectId='" + ObjectId + '\'' +
                ", questionText='" + questionText + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", examId='" + examId + '\'' +
                ", category='" + category + '\'' +
                ", options=" + options +
                ", difficulty='" + difficulty + '\'' +
                ", fact='" + fact + '\'' +
                ", createdAt=" + createdAt +
                ", version=" + version +
                '}';
    }
}
