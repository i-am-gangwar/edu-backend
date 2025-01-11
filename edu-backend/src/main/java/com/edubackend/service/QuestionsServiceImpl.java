package com.edubackend.service;

import com.edubackend.model.Questions;
import com.edubackend.repository.QuestionsRepository;
import com.edubackend.service.interfaces.QuestionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionsServiceImpl implements QuestionsService {

    @Autowired
    QuestionsRepository questionsRepository;


//    public QuestionsServiceImpl(QuestionsRepository questionsRepository) {
//        this.questionsRepository = questionsRepository;
//    }


    @Override
    public List<Questions> getAllQuestions(){
        return questionsRepository.findAll();

    }
}

