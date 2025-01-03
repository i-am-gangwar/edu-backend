package com.edu_backend.service;

import com.edu_backend.model.Questions;
import com.edu_backend.repository.QuestionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionsServiceImpl implements QuestionsService{

    @Autowired
    QuestionsRepository questionsRepository;


    @Override
    public List<Questions> getAllQuestions(){
        Questions q = new Questions();
        return questionsRepository.findAll();

    }

}

