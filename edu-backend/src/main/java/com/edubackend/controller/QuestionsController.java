package com.edubackend.controller;

import com.edubackend.model.Questions;
import com.edubackend.service.QuestionsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionsController {

  private final  QuestionsServiceImpl questionsServiceimpl;

    @Autowired
    public QuestionsController(QuestionsServiceImpl questionsServiceimpl) {
        this.questionsServiceimpl = questionsServiceimpl;
    }

    @GetMapping
    public ResponseEntity<String> getAllQuestions(){
        List<Questions> allQuestions =  questionsServiceimpl.getAllQuestions();
        if(!allQuestions.isEmpty()){
            return new ResponseEntity<>(allQuestions.toString(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Unable to fetch the data",HttpStatus.NOT_FOUND);

    }
}
