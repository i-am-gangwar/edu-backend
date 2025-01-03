package com.edu_backend.controller;



import com.edu_backend.service.QuestionsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/questions")
@Slf4j
public class QuestionsController {

    @Autowired
    QuestionsServiceImpl questionsServiceimpl;

    @GetMapping
    public ResponseEntity<?> getAllQuestions(){
        List<?> allQuestions =  questionsServiceimpl.getAllQuestions();
        System.out.println(allQuestions);
        System.out.println(allQuestions.toString());
        if(!allQuestions.isEmpty()){
            return new ResponseEntity<>(allQuestions.toString(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Unable to fetch the data",HttpStatus.NOT_FOUND);

    }



}
