package com.edu_backend.model.QuizAttempts;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.*;

@Document(collection = "quizAttempts")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuizAttempts {

    @Id @NonNull @NotBlank(message = "ID cannot be blank")
    private String id;

    @NonNull @NotBlank(message = "User ID cannot be blank")
    private String userId;

    @NotNull(message = "Quiz sets cannot be null") @Size(min = 1, message = "There must be at least one quiz set")
    private List<QuizSet> quizSets = new ArrayList<>();
}