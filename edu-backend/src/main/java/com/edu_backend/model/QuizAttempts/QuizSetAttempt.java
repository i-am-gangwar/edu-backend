package com.edu_backend.model.QuizAttempts;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Data
@AllArgsConstructor
public class QuizSetAttempt {

    @Id @NonNull @NotBlank(message = "ID cannot be blank")
    private String quizSetAttemptId;

    private String date;

    @NotBlank(message = "Total time taken to attempt cannot be blank.")
    private String totalTimeTakenToAttempt;

    @NotEmpty(message = "Quiz Set Attempt Map cannot be empty")
    private Map<String, List<String>> quizSetAttempt = new HashMap<>();

    // Constructor to auto-generate quizAttemptId if not provided
    public QuizSetAttempt() {
        if (this.quizSetAttemptId == null) {
            this.quizSetAttemptId = UUID.randomUUID().toString();  // Auto-generate quizAttemptId
        }
    }
}