package com.edu_backend.model;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.*;

@Document(collection = "quizAttempts")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuizAttempt {

    @Id @NonNull @NotBlank(message = "Result ID cannot be blank")
    private String resultId;  // Unique identifier for the QuizAttempt

    @NonNull @NotBlank(message = "User ID cannot be blank")
    private String userId;// user Id

    @NotNull(message = "Quiz sets cannot be null") @Size(min = 1, message = "There must be at least one quiz set")
    private List<QuizSet> quizSet = new ArrayList<>();   // list of all quizSets attempted by user

    @Data
    public static class QuizSet {

        @NonNull @NotBlank(message = "Quiz Set ID cannot be blank")
        private String quizSetId;  // quizSet id attempted by user

        @NotNull(message = "Quiz set attempts cannot be null")
        private List<QuizSetAttempt> quizSetAttempts = new ArrayList<>();  // list of all  attempt of that quizSet id
    }

    @Data
    public static class QuizSetAttempt {

        @NonNull @NotBlank(message = "Quiz Set Attempt ID cannot be blank")
        private String quizSetAttemptId;//  Unique identifier for the QuizSetAttempt

        private String date;  // date when QuizSetAttempt attempted by user

        @NotBlank(message = "Total time taken to attempt cannot be blank.")
        private String totalTimeTakenToAttempt; // time taken by user for QuizSetAttempt

        @NotEmpty(message = "Quiz Set Attempt Map cannot be empty")
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