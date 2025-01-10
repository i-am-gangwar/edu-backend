package com.edubackend.model.quizattempts;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizSet {
    @NonNull
    @NotBlank(message = "Quiz Set ID cannot be blank")
    private String quizSetId;

    @NotNull(message = "Quiz set attempts cannot be null")
    private List<QuizSetAttempt> quizSetAttempts = new ArrayList<>();

}