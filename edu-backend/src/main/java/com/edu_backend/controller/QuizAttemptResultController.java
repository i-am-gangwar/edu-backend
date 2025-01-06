package com.edu_backend.controller;

import com.edu_backend.model.QuizAttempt;
import com.edu_backend.model.QuizAttemptResult;
import com.edu_backend.mongo.MongoService;
import com.edu_backend.service.QuizAttemptResultServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/quizAttemptResult")
public class QuizAttemptResultController {

    @Autowired
    QuizAttemptResultServiceImpl quizAttemptResultService;

    @Autowired
    MongoService mongoService;

    @PostMapping("/{userId}/{quizSetId}/{quizSetAttemptId}")
    public ResponseEntity<?> saveQuizAttemptResult(@PathVariable String userId, @PathVariable String quizSetId, @PathVariable String quizSetAttemptId) {
        Optional<QuizAttempt.QuizSetAttempt> quizAttempt = mongoService.findQuizSetAttempt(userId, quizSetId, quizSetAttemptId);  // fetch quizSetAttempt from database
        QuizAttemptResult.QuizSetAttemptResult quizResultCalculated = quizAttemptResultService.calculateResultOfQuizAttempt(quizSetAttemptId, quizAttempt); // calculating result of attempted quiz

        QuizAttemptResult result =  quizAttemptResultService.saveQuizAttemptResult(userId,quizSetId,quizResultCalculated);

      if (result !=null){
          return  new ResponseEntity<>("Result Saved",HttpStatus.OK);
      }
      else{
          return  new ResponseEntity<>("Result not Saved",HttpStatus.NOT_FOUND);
      }

      }
}