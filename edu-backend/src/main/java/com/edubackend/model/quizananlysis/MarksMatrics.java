package com.edubackend.model.quizananlysis;

import com.edubackend.model.quizananlysis.dataaggregator.DataAggregatorInt;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarksMatrics {

    DataAggregatorInt totalQuizSets= new DataAggregatorInt(); // Total number of quiz sets attempted
    DataAggregatorInt  totalQ =  new DataAggregatorInt(); // Total questions across all quiz sets
    DataAggregatorInt  totalAttemptedQ =  new DataAggregatorInt(); // Total attempted questions
    DataAggregatorInt  totalCorrectScore =  new DataAggregatorInt(); // Total correct answers
    DataAggregatorInt  totalIncorrectScore =  new DataAggregatorInt(); // Total incorrect answers
    private int  highestScore =  0;
    private int  lowestScore = Integer.MAX_VALUE;  // Use MAX_VALUE for meaningful comparison
    private double averageScore = 0.0;
    private double overallAccuracy = 0.0; // Overall accuracy percentage
}