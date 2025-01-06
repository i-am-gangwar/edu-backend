package com.edu_backend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.*;

@Document(collection = "quizAttempts")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class QuizAttempt {

    @Id
    @NonNull
    private String resultId;  // Unique identifier for the QuizAttempt
    @NonNull
    private String userId;// user Id
    private List<QuizSet> quizSet = new ArrayList<>();   // list of all quizSets attempted by user

    @Data
    public static class QuizSet {
        @NonNull
        private String quizSetId;  // quizSet id attempted by user
        private List<QuizSetAttempt> quizSetAttempts = new ArrayList<>();  // list of all  attempt of that quizSet id
    }

    @Data
    public static class QuizSetAttempt {
        @NonNull
        private String quizSetAttemptId;//  Unique identifier for the QuizSetAttempt
        @NonNull
        private String date;  // date when QuizSetAttempt attempted by user
        private String totalTimeTakenToAttempt = "null"; // time taken by user for QuizSetAttempt
        private  Map<String,List<String>> quizSetAttempt = new HashMap<>();
       // map pf question id and their options id selected by user for QuizSetAttempt

       // Constructor to auto-generate quizAttemptId if not provided
       public QuizSetAttempt() {
           if (this.quizSetAttemptId == null) {
               this.quizSetAttemptId = UUID.randomUUID().toString();  // Auto-generate quizAttemptId
           }
       }
    }

}

