package com.edubackend.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Document(collection = "leaderboard")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Leaderboard {

    private Map<String,Integer> totalQuizAttempted = new HashMap<>();
    private Map<String,Double> overallAccuracy = new HashMap<>();


}

