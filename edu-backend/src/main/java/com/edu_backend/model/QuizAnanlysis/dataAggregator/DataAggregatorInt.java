package com.edu_backend.model.QuizAnanlysis.dataAggregator;

import lombok.Getter;

@Getter
public class DataAggregatorInt {
    
    private int total=0;

    public void add(int value) {
        total += value;
    }
    public void sub(int value) {
        total += value;
    }
    public void max(){
        total= Integer.MAX_VALUE;
    }
}