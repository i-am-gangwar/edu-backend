package com.edubackend.model;


import lombok.Data;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;


@Document(collection = "questions")
@Data
@EntityScan
public class Questions {

    @Id
    private String objectId ;
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
                "ObjectId='" + objectId + '\'' +
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
