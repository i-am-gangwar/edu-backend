package com.edubackend.controller;

import com.edubackend.model.quizattempts.QuizSetAttempt;
import com.edubackend.model.quizresults.QuizResults;
import com.edubackend.model.quizresults.QuizSetAttemptResult;
import com.edubackend.mongo.MongoService;
import com.edubackend.service.QuizAttemptResultServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quizAttemptResult")
public class QuizAttemptResultController {


    QuizAttemptResultServiceImpl quizAttemptResultService;
    MongoService mongoService;

    @Autowired
    public QuizAttemptResultController(QuizAttemptResultServiceImpl quizAttemptResultService, MongoService mongoService) {
        this.quizAttemptResultService = quizAttemptResultService;
        this.mongoService = mongoService;
    }

    @PostMapping("/{userId}/{quizSetId}/{quizSetAttemptId}")
    public ResponseEntity<?> saveQuizAttemptResult(
            @PathVariable String userId,
            @PathVariable String quizSetId,
            @PathVariable String quizSetAttemptId) {

       try{
           QuizSetAttempt quizAttempt = mongoService.findQuizSetAttempt(userId, quizSetId, quizSetAttemptId);
           QuizSetAttemptResult quizResultCalculated = quizAttemptResultService.calculateResultOfQuizAttempt(quizSetAttemptId, quizAttempt); // calculating result of attempted quiz
           QuizResults result =  quizAttemptResultService.saveQuizAttemptResult(userId,quizSetId,quizResultCalculated);
           if (result!=null)
               return  ResponseEntity.status(HttpStatus.CREATED).body("Quiz Attempt Result saved");
           else
               return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quiz Attempt Result not saved\n"+"UserId: "+userId+ "\nQuizSetId: "+quizSetId+"\nQuizAttemptId: "+quizSetAttemptId);
       }
       catch (Exception e){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to get details, try again.");
       }
    }


    // get result by user id
    // get result by user id and quiz set id
    // get result by user id and quiz set id and quiz attempt id

    // delete by user id
    // delete by by user id and quiz set id and quiz attempt id




}