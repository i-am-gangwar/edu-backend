package com.edu_backend.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.*;

@Document(collection = "results")
@Getter
@Setter
@ToString
public class Results {

    @Id
    private String resultId;
    private String userId;
    private List<QuizSet> quizSet = new ArrayList<>();

    @Data
    public static class QuizSet {
        private String quizSetId;
        private List<QuizAttempt> quizSetAttempts = new ArrayList<>();
    }

    @Data
    public static class QuizAttempt {
        private String date;
       private  Map<String,List<String>> quizSetAttempt = new HashMap<>();
    }

}

