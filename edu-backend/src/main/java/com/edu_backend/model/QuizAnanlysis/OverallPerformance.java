package com.edu_backend.model.QuizAnanlysis;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OverallPerformance {

    @Id
    @NotBlank(message = "Overall Performance Analysis ID cannot be blank")
    private String id=UUID.randomUUID().toString();

    private  MarksMatrics marksMatrics = new MarksMatrics();

    private  SubjectMatrics subjectMatrics= new SubjectMatrics();

    private Date analyzedAt = new Date();
}