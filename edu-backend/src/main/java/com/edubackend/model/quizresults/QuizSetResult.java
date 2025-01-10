package com.edubackend.model.quizresults;

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
public class QuizSetResult {

    @NonNull
    @NotBlank(message = "Quiz Set Result ID cannot be blank")
    private String quizSetId;

    @NotNull(message = "Quiz set attempts result cannot be null")
    private List<QuizSetAttemptResult> quizSetAttemptResults = new ArrayList<>();

}