package com.edu_backend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.*;

@Document(collection = "quizAttempt")
@Getter
@Setter
@ToString
public class QuizAttempt {

    @Id
    private String resultId;
    @NonNull
    private String userId;
    private List<QuizSet> quizSet = new ArrayList<>();

    @Data
    public static class QuizSet {
        @NonNull
        private String quizSetId;
        private List<QuizSetAttempt> quizSetAttempts = new ArrayList<>();
    }

    @Data
    public static class QuizSetAttempt {
        private String date;
        private String totalTimeTookToAttempt;
       private  Map<String,List<String>> quizSetAttempt = new HashMap<>();
    }

}

