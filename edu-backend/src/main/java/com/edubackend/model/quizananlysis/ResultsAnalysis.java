package com.edubackend.model.quizananlysis;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "quizAnalysis")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultsAnalysis {

    @Id
    @NotBlank(message = "Result Analysis ID cannot be blank")
    private String id;

    @NonNull
    @NotBlank(message = "User ID cannot be blank")
    private String userId;

    private  OverallPerformance overallPerformance = new OverallPerformance();

}