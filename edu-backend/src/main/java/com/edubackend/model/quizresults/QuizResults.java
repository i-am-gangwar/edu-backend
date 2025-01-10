package com.edubackend.model.quizresults;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.*;

@Document(collection = "quizAttemptResults")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizResults {

    @Id @NotBlank(message = "Result ID cannot be blank")
    private String id;

    @NonNull @NotBlank(message = "User ID cannot be blank")
    private String userId;

    @NotNull(message = "Quiz sets Result cannot be null")
    @Size(min = 1, message = "There must be at least one quiz set Result")
    private List<QuizSetResult> quizSetResult = new ArrayList<>();


}